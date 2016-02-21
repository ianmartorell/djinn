package com.djinnapp.djinn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by nacho on 20/02/2016.
 */
public class GalleryActivity extends AppCompatActivity {
    private static String LOG_TAG = "GalleryActivity";
    private RecyclerView galleryRecyclerView;
    private RecyclerView.Adapter thumbAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton button;
    ArrayList<Thumbnail> newThumbnails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryRecyclerView = (RecyclerView) findViewById(R.id.thumbnails_recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.gallery_swipe_refresh);
        button = (FloatingActionButton) findViewById(R.id.take_photo);

        galleryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        galleryRecyclerView.setLayoutManager(layoutManager);

        thumbAdapter = new ThumbnailAdapter(new ArrayList<Thumbnail>());
        this.updateDataSet();
        galleryRecyclerView.setAdapter(thumbAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDataSet();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.djinapp.djinn.CAPTURE"));
                updateDataSet();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateDataSet();
        ((ThumbnailAdapter) thumbAdapter).setOnItemClickListener(new ThumbnailAdapter.ThumbnailClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_joinevent) {
            startActivity(new Intent("com.djinapp.djinn.CREATE"));
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateDataSet() {

        String eventId = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                .getString("currentEventId", null);
        Ion.with(getApplicationContext())
                .load(getResources().getString(R.string.url)+"/api/photos?eventId="+eventId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                                 @Override
                                 public void onCompleted(Exception e, JsonObject result) {
                     newThumbnails.clear();
                     JsonArray photos = result.getAsJsonArray("photos");
                     for (int i = 0; i < photos.size(); ++i) {
                         JsonObject obj = (JsonObject) (photos.get(i));
                         final String id = obj.get("_id").getAsString();
                         Ion.with(getApplicationContext())
                                 .load(getResources().getString(R.string.url) + "/api/photos/thumb/" + id + ".jpg")
                                 .asBitmap()
                                 .setCallback(new FutureCallback<Bitmap>() {
                                     @Override
                                     public void onCompleted(Exception e, Bitmap result) {
                                         if (e != null)
                                             Log.i("updateDataSet", "fail getting bitmap");
                                         else {
                                             Thumbnail thumbnail = new Thumbnail(result, id);
                                             newThumbnails.add(thumbnail);
                                         }
                                     }
                                 });
                     }
                     ((ThumbnailAdapter) thumbAdapter).animateTo(newThumbnails);
                     }
                 });
    }
}

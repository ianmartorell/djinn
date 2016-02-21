package com.djinnapp.djinn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by nacho on 20/02/2016.
 */
public class GalleryActivity extends Activity {
    private static String LOG_TAG = "GalleryActivity";
    private RecyclerView galleryRecyclerView;
    private RecyclerView.Adapter thumbAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryRecyclerView = (RecyclerView) findViewById(R.id.thumbnails_recyclerView);
        galleryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        galleryRecyclerView.setLayoutManager(layoutManager);
        thumbAdapter = new ThumbnailAdapter(new ArrayList<Thumbnail>());
        updateDataSet();
        galleryRecyclerView.setAdapter(thumbAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ThumbnailAdapter) thumbAdapter).setOnItemClickListener(new ThumbnailAdapter.ThumbnailClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
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
                                                             ((ThumbnailAdapter) thumbAdapter).addItem(thumbnail, 0);
                                                         }
                                                     }
                                                 });
                                     }
                                 }
                             });
    }
}

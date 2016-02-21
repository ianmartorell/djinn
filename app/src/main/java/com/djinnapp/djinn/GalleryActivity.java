package com.djinnapp.djinn;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

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
        layoutManager = new LinearLayoutManager(this);
        galleryRecyclerView.setLayoutManager(layoutManager);
        thumbAdapter = new ThumbnailAdapter(new ArrayList<Thumbnail>());
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


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Button gen = (Button) findViewById(R.id.generate);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GridLayout ll = (GridLayout) findViewById(R.id.gridlay);
                //---------
                int id = 0;

                String s= "@Strings/eventID";
                Ion.with(getApplicationContext())
                    .load(getResources().getString(R.string.url)+"/api/photos?eventId="+s)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            JsonArray photos = result.getAsJsonArray("photos");
                            for (int i = 0; i < photos.size(); ++i){
                                ImageView img = new ImageView(getApplicationContext());
                                img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                JsonObject obj = (JsonObject) (photos.get(i));
                                String z = obj.get("_id").getAsString();
                                Ion.with(img).load(getResources().getString(R.string.url)+"/api/photos/thumb/"+z+".jpg");
                                GridLayout.LayoutParams lparams = new GridLayout.LayoutParams(

                                );
                                img.setLayoutParams(lparams);
                                ll.addView(img);
                            }
                        }
                    });
                }
        });
    }*/
}

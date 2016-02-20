package com.djinnapp.djinn;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nacho on 20/02/2016.
 */
public class layouttest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridnew);
        Button gen = (Button) findViewById(R.id.generate);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GridLayout ll = (GridLayout) findViewById(R.id.gridlay);
                //---------
                int id = 0;

                String s= "@Strings/eventID";
                Ion.with(getApplicationContext())
                        .load("http://5c4ad84b.ngrok.com/api/photos?eventId="+s)
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
                                    Ion.with(img).load("http://5c4ad84b.ngrok.com/api/photos/thumb/"+z+".jpg");
                                    GridLayout.LayoutParams lparams = new GridLayout.LayoutParams(

                                    );
                                    img.setLayoutParams(lparams);
                                    ll.addView(img);
                                }
                            }
                        });
                {

                }



                }



        });
    }
}

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
public class JoinEventActivity extends Activity {

    private static String LOG_TAG = "JoinEventActivity";
    private RecyclerView eventRecyclerView;
    private RecyclerView.Adapter eventAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinevent);
        final EditText et = (EditText) findViewById(R.id.eventName);

        eventRecyclerView = (RecyclerView) findViewById(R.id.events_recyclerView);
        eventRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(new ArrayList<Event>());
        updateDataSet();
        eventRecyclerView.setAdapter(eventAdapter);

        Button createEventButton = (Button) findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et.getText().toString();
                // Send create event POST
                JsonObject json = new JsonObject();
                json.addProperty("name", name);
                Ion.with(getApplicationContext())
                        .load(getResources().getString(R.string.url) + "/api/events/create")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                String eventId = result.get("eventId").getAsString();
                            }
                        });
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ((EventAdapter) eventAdapter).setOnItemClickListener(new
        EventAdapter.EventClickListener() {
          @Override
          public void onItemClick(int position, View v) {
              Log.i(LOG_TAG, " Clicked on Item " + position);
          }
        });
    }

    private void updateDataSet() {
        Ion.with(getApplicationContext())
            .load(getResources().getString(R.string.url)+"/api/events")
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    JsonArray events = result.getAsJsonArray("events");
                    for (int i = 0; i < events.size(); ++i) {
                        JsonObject obj = (JsonObject) (events.get(i));
                        String id = obj.get("_id").getAsString();
                        String name = obj.get("name").getAsString();
//                        String date = obj.get("date").getAsString();
                        Event event = new Event(id, name, "date");
                        ((EventAdapter) eventAdapter).addItem(event, 0);
                    }
                }
            });
    }
}

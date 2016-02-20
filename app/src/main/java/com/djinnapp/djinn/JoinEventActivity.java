package com.djinnapp.djinn;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        eventAdapter = new EventAdapter(getDataSet());
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

    private ArrayList<Event> getDataSet() {
        ArrayList results = new ArrayList<Event>();
        for (int index = 0; index < 6; index++) {
            Event obj = new Event("Some Primary Text " + index, "Secondary " + index);
            results.add(index, obj);
        }
        return results;
    }
}

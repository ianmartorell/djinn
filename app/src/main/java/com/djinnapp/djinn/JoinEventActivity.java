package com.djinnapp.djinn;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nacho on 20/02/2016.
 */
public class JoinEventActivity extends Activity implements SearchView.OnQueryTextListener {

    private static String LOG_TAG = "JoinEventActivity";
    private RecyclerView eventRecyclerView;
    private RecyclerView.Adapter eventAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText editText;
    private ArrayList<Event> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinevent);

        mDataset = new ArrayList<>();
        editText = (EditText) findViewById(R.id.eventName);
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
                // Send create event POST
                JsonObject json = new JsonObject();
                json.addProperty("name", editText.getText().toString());
                Ion.with(getApplicationContext())
                        .load(getResources().getString(R.string.url) + "/api/events/create")
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                String eventId = result.get("eventId").getAsString();
                                getApplicationContext().getSharedPreferences(
                                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                                        .edit().putString("currentEventId", eventId).commit();
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
                getApplicationContext().getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                        .edit().putString("currentEventId", ((EventAdapter) eventAdapter).getItemMongoId(position)).commit();
                finish();
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_joinevent, menu);
//        final MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(this);
//        return true;
//    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Event> filteredModelList = filter(mDataset, query);
        ((EventAdapter) eventAdapter).animateTo(filteredModelList);
        eventRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Event> filter(List<Event> events, String query) {
        query = query.toLowerCase();
        final List<Event> filteredEventList = new ArrayList<>();
        for (Event event : events) {
            final String text = event.getName().toLowerCase();
            if (text.contains(query)) {
                filteredEventList.add(event);
            }
        }
        return filteredEventList;
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
                        mDataset.add(0, event);
                    }
                }
            });
    }
}

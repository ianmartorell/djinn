package com.djinnapp.djinn;

/**
 * Created by ian on 20/02/16.
 */
public class Event {
    private String name;
    private String date;

    Event (String name, String date){
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
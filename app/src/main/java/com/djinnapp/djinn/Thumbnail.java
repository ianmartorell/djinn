package com.djinnapp.djinn;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by nacho on 20/02/2016.
 * Extended by louis on 21/02/2016.
 */

public class Thumbnail {
    private Bitmap thumbnail;
    private String id;

    Thumbnail(Bitmap thumbnail, String id) {
        this.thumbnail = thumbnail;
        this.id = id;
    }

    public Bitmap getThumbnail() {
        return this.thumbnail;
    }

    public String getId() {
        return this.id;
    }
}

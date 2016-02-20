package com.djinnapp.djinn;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by nacho on 20/02/2016.
 */
public class IPic {
    Bitmap thumbnail;
    String id;
    IPic (Bitmap thumbnail, String id) {
        this.thumbnail = thumbnail;
        this.id = id;
    }
}

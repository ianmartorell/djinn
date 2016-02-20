package com.djinnapp.djinn;

/**
 * Created by nacho on 20/02/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Capture extends Activity implements OnClickListener {

    ImageView mImageView;
    Button cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_layout);
        mImageView = (ImageView) findViewById(R.id.img1);
        cam = (Button) findViewById(R.id.camera);
        cam.setOnClickListener(this);

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // TODO Upload

            mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.camera:
                Toast t = Toast.makeText(getApplicationContext(), "starting camera", Toast.LENGTH_LONG);
                t.show();
                dispatchTakePictureIntent();
                break;
        }
    }
}
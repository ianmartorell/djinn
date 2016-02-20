package com.djinnapp.djinn;

/**
 * Created by nacho on 20/02/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.concurrent.Future;

public class CaptureActivity extends Activity implements OnClickListener {

    ImageView mImageView;
    ImageButton cam;
    String path;
    Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        mImageView = (ImageView) findViewById(R.id.img1);
        cam = (ImageButton) findViewById(R.id.camera);
        cam.setOnClickListener(this);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA = 137;
    static final int CAPTURE_IMAGE_FULLSIZE = 12476;

    private void dispatchTakePictureIntent2() {
        // Simple version of the take Picture intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePictureIntent() {
        // Simple version of the take Picture intent
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

    }

    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data"); // Thumbnail
            mImageView.setImageBitmap(imageBitmap);
        }

        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE  && resultCode == RESULT_OK)
        {
            //Get our saved file into a bitmap object:
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");

            Bitmap imageHD = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 1000);
//            Bitmap imageLD = decodeSampledBitmapFromFile(file.getAbsolutePath(), 100, 100);

            String imageHDpath = SaveImage(imageHD);
//            String imageLDpath = SaveImage(imageLD);
            mImageView.setImageBitmap(imageHD);

            File f = new File(imageHDpath);
            Future uploading = Ion.with(CaptureActivity.this)
                .load(getResources().getString(R.string.url) + "/api/upload")
                .setMultipartParameter("eventId", getResources().getString(R.string.eventID))
                .setMultipartParameter("title", "title")
                .setMultipartParameter("description", "description")
                .setMultipartFile("image", f)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        try {
                            JsonObject jobj = result.getResult();
                            Toast.makeText(getApplicationContext(), jobj.get("response").getAsString(), Toast.LENGTH_SHORT).show();
                        } catch (JsonIOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
        }
    }

    private String SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myDir + "/" + fname;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
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
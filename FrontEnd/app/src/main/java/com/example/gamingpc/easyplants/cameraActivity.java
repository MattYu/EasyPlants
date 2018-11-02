package com.example.gamingpc.easyplants;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class cameraActivity extends AppCompatActivity {

    private static final String TAG = "Camera Activity";
    // The request code used to ask for permissions
    private static final int REQUEST_CODE_WC_PERM = 1;
    // The request code use to open the camera
    private static final int REQUEST_CODE_C_OPEN = 2;

    // UI elements
    Button camera;
    ImageView cameraImage;

    // Sets up the UI elements and the listener
    private void setup() {

        ImageView cameraImage = findViewById(R.id.view_camera);
        Button camera = findViewById(R.id.button_camera);

        // Opens the camera on click
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_C_OPEN);
            }
        });
    }

    // Stores the result from startActivityForResult that came from opening the camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Grabs the produced bitmap and sets it to the imageView
        Bitmap imageMap = (Bitmap)data.getExtras().get("data");
        cameraImage.setImageBitmap(imageMap);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkPermissions();
    }

    // Makes sure that the camera and write permissions have been granted
    private void checkPermissions() {
        Log.d(TAG, "checkPermissions: Asking user for camera and write permissions");
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        // Check the WRITE permission and CAMERA permission
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {

                // TODO - Add code that opens the camera once permissions are checked
        } else {
            // Get permissions if they are not granted
            ActivityCompat.requestPermissions(cameraActivity.this, permissions, REQUEST_CODE_WC_PERM);
        }

    }
}

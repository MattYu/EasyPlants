package com.example.gamingpc.easyplants;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class cameraActivity extends AppCompatActivity {

    private static final String TAG = "Camera Activity";
    // The request code used to ask for permissions
    private static final int REQUEST_CODE_WC = 1;

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
            ActivityCompat.requestPermissions(cameraActivity.this, permissions, REQUEST_CODE_WC);
        }

    }
}

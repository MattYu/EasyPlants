package com.example.gamingpc.easyplants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamingpc.easyplants.Database.SharedPreferenceHelper;
import com.example.gamingpc.easyplants.Models.UserThreshold;

public class setThresholdActivity extends AppCompatActivity {

    private static final String TAG = "setThresholdActivity";

    // Set up UI elements
    Button save;
    EditText lowerThresh;   // User input of lower humidity bound
    EditText upperThresh;   // User input of upper humidity bound
    TextView currentMinMax; // Displays the current set threshold

    // Initializes the UI elements
    private void setup() {
        // The text fields


        lowerThresh = findViewById(R.id.text_newMin);
        upperThresh = findViewById(R.id.text_newMax);
        currentMinMax = findViewById(R.id.text_currentMinMax);
        // Save button and its listener
        save = findViewById(R.id.button_save);

        SharedPreferenceHelper helperLoad = new SharedPreferenceHelper(setThresholdActivity.this);
        UserThreshold temp = helperLoad.getUserthreshold();
        String message = temp.getThresholdMin() + " - " + temp.getThresholdMax() + "%";
        currentMinMax.setText(message);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid()){
                    SharedPreferenceHelper helperSave = new SharedPreferenceHelper(setThresholdActivity.this);
                    helperSave.saveFromUserThreshold(new UserThreshold(Integer.valueOf(upperThresh.getText().toString()), Integer.valueOf(lowerThresh.getText().toString())));
                    String message = lowerThresh.getText().toString() + " - " + upperThresh.getText().toString() + "%";
                    currentMinMax.setText(message);
                    Toast.makeText(getApplicationContext(), "New Threshold Set", Toast.LENGTH_SHORT).show();
                }


                //startActivity(new Intent(setThresholdActivity.this, MainActivity.class));
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_threshold);

        // Allow the action bar back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setup();
    }

    private boolean isValid(){
        String lower = lowerThresh.getText().toString();
        String upper = upperThresh.getText().toString();

        if(lower.isEmpty() || upper.isEmpty()){
            Toast.makeText(this, "Invalid Input - One or more of the fields is empty", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(Integer.valueOf(lower) < 0 || Integer.valueOf(lower) > 100 || Integer.valueOf(lower) > Integer.valueOf(upper)) {
            Toast.makeText(this, "Invalid Input - Min input is incorrect", Toast.LENGTH_LONG).show();
            lowerThresh.setText("", TextView.BufferType.EDITABLE);
            return false;
        }
        else if(Integer.valueOf(upper) < 0 || Integer.valueOf(upper) > 100 || Integer.valueOf(upper) < Integer.valueOf(lower)){
            Toast.makeText(this, "Invalid Input - Max input is incorrect", Toast.LENGTH_LONG).show();
            lowerThresh.setText("",TextView.BufferType.EDITABLE);
            return false;
        }
        else{
            return true;
        }
    }
}

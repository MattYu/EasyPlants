package com.example.gamingpc.easyplants;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class sensorListActivity extends AppCompatActivity {

    private static final String TAG = "sensorListActivity";

    // UI elements
    private ListView sensorList;

    // Test arrays for UI
    String[] PLANTS = {"Aloe Vera", "Rose Bush", "Poppy"};
    String[] SENSOR_ID = {"1", "2", "3"};

    // Called to set up all the UI elements
    private void setup() {

        sensorList = findViewById(R.id.list_sensors);
        CustomAdapter customAdapter = new CustomAdapter();
        sensorList.setAdapter(customAdapter);

        sensorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setup();
    }

    // Class used to populate the custom adapter - with the methods that java requires for some reason
    class CustomAdapter extends BaseAdapter {

        // Temporary until Firebase data is accessed
        @Override
        public int getCount() {
            return PLANTS.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.sensor_list_view_custom, null);

            TextView names = view.findViewById(R.id.text_plantName);
            TextView id = view.findViewById(R.id.text_sensorID);
            ImageView image = view.findViewById(R.id.view_plantImage);

            names.setText(PLANTS[i]);
            id.setText("Sensor: " + SENSOR_ID[i]);
            image.setImageResource(R.drawable.plant_test);


            return view;
        }
    }
}

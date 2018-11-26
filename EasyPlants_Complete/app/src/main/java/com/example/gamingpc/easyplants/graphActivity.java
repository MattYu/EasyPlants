package com.example.gamingpc.easyplants;

import android.app.Fragment;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gamingpc.easyplants.Models.SensorData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

public class graphActivity extends AppCompatActivity {

    final String TAG = "GraphActivity";
    List<SensorData> totalSensorData = new ArrayList<>();
    List<SensorData> graphData = new ArrayList<>();

    List<List<SensorData>> GraphDataYear = new ArrayList<List<SensorData>>();

    String[] displayedYears;
    String[] displayedMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] displayedDays;

    int currentYear = 2018;
    int currentMonth = 11;
    int currentDay = 25;

    String sensorID;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    Spinner yearSelector = (Spinner) findViewById(R.id.yearSelector);
    Spinner monthSelector = (Spinner) findViewById(R.id.monthSelector);
    Spinner daySelector = (Spinner) findViewById(R.id.daySelector);

    Button refreshGraphButton = (Button) findViewById(R.id.refreshGraphButton);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        onSetup();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.graphView, new PlaceholderFragment()).commit();
        }

        // Enable the back button to the mainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class PlaceholderFragment extends Fragment {

        private LineChartView chart;
        private PreviewLineChartView previewChart;
        private LineChartData data;
        /**
         * Deep copy of data.
         */
        private LineChartData previewData;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.activity_graph, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            previewChart = (PreviewLineChartView) rootView.findViewById(R.id.chart_preview);

            // Generate data for previewed chart and copy of that data for preview chart.
            setDataToGraph();

            chart.setLineChartData(data);
            // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
            // zoom/scroll is unnecessary.
            chart.setZoomEnabled(false);
            chart.setScrollEnabled(false);

            previewChart.setLineChartData(previewData);
            previewChart.setViewportChangeListener(new ViewportListener());

            previewX(false);

            return rootView;
        }

        public void onClickRefreshButton() {
                setDataToGraph();
                chart.setLineChartData(data);
                previewChart.setLineChartData(previewData);
                previewX(true);
        }

        private void setDataToGraph() {

            List<PointValue> values = new ArrayList<PointValue>();

            List<SensorData> dataSet = getDataFromDate(currentYear, currentMonth, currentDay);

            for (int i = 0; i < dataSet.size(); ++i) {
                values.add(new PointValue(i, (float) Math.random() * 100f)); //(x, y)
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_BLUE);
            line.setHasPoints(false);// too many values so don't draw points.

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            data = new LineChartData(lines);
            data.setAxisXBottom(new Axis());
            data.setAxisYLeft(new Axis().setHasLines(true));

            // prepare preview data, is better to use separate deep copy for preview chart.
            // Set color to grey to make preview area more visible.
            previewData = new LineChartData(data);
            previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);

        }

        private void previewY() {
            Viewport tempViewport = new Viewport(chart.getMaximumViewport());
            float dy = tempViewport.height() / 4;
            tempViewport.inset(0, dy);
            previewChart.setCurrentViewportWithAnimation(tempViewport);
            previewChart.setZoomType(ZoomType.VERTICAL);
        }

        private void previewX(boolean animate) {
            Viewport tempViewport = new Viewport(chart.getMaximumViewport());
            float dx = tempViewport.width() / 4;
            tempViewport.inset(dx, 0);
            if (animate) {
                previewChart.setCurrentViewportWithAnimation(tempViewport);
            } else {
                previewChart.setCurrentViewport(tempViewport);
            }
            previewChart.setZoomType(ZoomType.HORIZONTAL);
        }

        private void previewXY() {
            // Better to not modify viewport of any chart directly so create a copy.
            Viewport tempViewport = new Viewport(chart.getMaximumViewport());
            // Make temp viewport smaller.
            float dx = tempViewport.width() / 4;
            float dy = tempViewport.height() / 4;
            tempViewport.inset(dx, dy);
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        }

        /**
         * Viewport listener for preview chart(lower one). in {@link #onViewportChanged(Viewport)} method change
         * viewport of upper chart.
         */
        private class ViewportListener implements ViewportChangeListener {

            @Override
            public void onViewportChanged(Viewport newViewport) {
                // don't use animation, it is unnecessary when using preview chart.
                chart.setCurrentViewport(newViewport);
            }

        }

    }


    private static List<SensorData> getDataFromDate(int currentYear, int currentMonth, int currentDay){
        //TODO : Grab the data related to the current Date from the full list

        List<SensorData> result = new ArrayList<>();

        for(int i = 0; i < totalSensorData.size(); i++){
            SensorData currentData = totalSensorData.get(i);
            if(currentData.isSameDate(currentData, currentYear, currentMonth, currentDay)){
                result.add(currentData);
            }
        }

        return result;
    }

    private void getPassedData() {
        Intent received = getIntent();
        sensorID = received.getStringExtra("sensorID");

    }

    protected void onSetup(){

        getPassedData();

        String path = "UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/" + sensorID + "/SensorData";

        DatabaseReference dbRef = database.getReference(path);


        yearSelector = (Spinner) findViewById(R.id.yearSelector);
        monthSelector = (Spinner) findViewById(R.id.monthSelector);
        daySelector = (Spinner) findViewById(R.id.daySelector);

        refreshGraphButton = (Button) findViewById(R.id.refreshGraphButton);

        daySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentDay = Integer.valueOf(displayedDays[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch(displayedMonths[i]){

                    case "January":
                        currentMonth = 1;
                        break;

                    case "February":
                        currentMonth = 2;
                        break;

                    case "March":
                        currentMonth = 3;
                        break;

                    case "April":
                        currentMonth = 4;
                        break;

                    case "May":
                        currentMonth = 5;
                        break;

                    case "June":
                        currentMonth = 6;
                        break;

                    case "July":
                        currentMonth = 7;
                        break;

                    case "August":
                        currentMonth = 8;
                        break;

                    case "September":
                        currentMonth = 9;
                        break;

                    case "October":
                        currentMonth = 10;
                        break;

                    case "November":
                        currentMonth = 11;
                        break;

                    default:
                        currentMonth = 12;
                }

                changeDaysList(currentYear, currentMonth);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentYear = Integer.valueOf(displayedYears[i]);
                changeDaysList(currentYear, currentMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                fillTotalData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void changeDaysList(int year, int month){
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int numOfDays = yearMonthObject.lengthOfMonth();

        setupDay(numOfDays);
    }

    protected void setupYear() {


        displayedYears = getYearsOfData();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, displayedYears);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelector.setAdapter(adapter);

    }

    private String[] getYearsOfData() {

        List<Integer> years = new ArrayList<Integer>();

        for(SensorData data : totalSensorData){
            boolean isThere = false;
            for(int year: years){
                if(data.getYear() == year){
                    isThere = true;
                    break;
                }
            }
            if(isThere)
                continue;
            else
                years.add(data.getYear());

        }

        String result[] = new String[years.size()];
        for(int i = 0; i < result.length; i++){
            result[i] = Integer.toBinaryString(years.get(i));
        }

        return result;
    }

    protected void setupDay(int maxDay){

        if(currentDay > maxDay){
            currentDay = maxDay;

            displayedDays = new String[maxDay];
            for(int i = 1; i <= maxDay; i++){
                displayedDays[i] = Integer.toString(i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, displayedDays);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            daySelector.setAdapter(adapter);

        }

    }

    protected void fillTotalData(DataSnapshot sensorData){

        //TODO : Gather all data from the Sensor from Firebase

        totalSensorData = new ArrayList<SensorData>();

    for(DataSnapshot entry : sensorData.getChildren()){

        Integer humidity;
        String time;

        if(entry.child("humidity_value").exists()){
            humidity = entry.child("humidity_value").getValue(Integer.class);
            Log.d(TAG, Integer.toString(humidity));
        }
        else{
            Toast.makeText(this, "Oh no! We couldn't fetch the current humidity", Toast.LENGTH_LONG).show();
            continue;
        }

        if(entry.child("Time").exists()){
            time = entry.child("humidity_value").getValue(String.class);
            Log.d(TAG, time);
        }
        else{
            Toast.makeText(this, "Oh no! We couldn't fetch the current humidity", Toast.LENGTH_LONG).show();
            continue;
        }

        totalSensorData.add(new SensorData(humidity, time));
    }

    }

}

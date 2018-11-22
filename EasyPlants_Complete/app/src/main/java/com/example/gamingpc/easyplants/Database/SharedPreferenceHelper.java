package com.example.gamingpc.easyplants.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gamingpc.easyplants.Models.UserThreshold;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SharedPreferenceHelper {

    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context){
        sharedPreferences = context.getSharedPreferences("ThresholdPreference", Context.MODE_PRIVATE);
    }

    // Saves the threshold the user has input
    public void saveFromUserThreshold(UserThreshold userThreshold){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ThresholdMax", userThreshold.getThresholdMax());
        editor.putInt("ThresholdMin", userThreshold.getThresholdMin());
        editor.commit();

        // Send the stored data to Firebase
        toFirebase(Integer.toString(userThreshold.getThresholdMin()),Integer.toString(userThreshold.getThresholdMax()));
    }

    public String getHumidity(){
        DatabaseReference humidityValue = FirebaseDatabase.getInstance().getReference().child("HumidityTest").child("humidity_value");
        String humidity = humidityValue.getKey();
        return  humidity;
    }

    public UserThreshold getUserthreshold(){
        UserThreshold userThreshold = new UserThreshold(
                -1,
                sharedPreferences.getInt("ThresholdMax", 100),
                sharedPreferences.getInt("ThresholdMin", 0)
        );

        return userThreshold;
    }

    public int getLowerThresh() {
        return sharedPreferences.getInt("ThresholdMin", 0);
    }

    public int getUpperThresh() {
        return sharedPreferences.getInt("ThresholdMax", 100);
    }


    // Sends input threshold data to firebase
    private void toFirebase(String lower, String upper) {

        DatabaseReference refMin = FirebaseDatabase.getInstance().getReference().child("ThresholdValues").child("min");
        DatabaseReference refMax = FirebaseDatabase.getInstance().getReference().child("ThresholdValues").child("max");
        refMin.setValue(lower);
        refMax.setValue(upper);
    }

}


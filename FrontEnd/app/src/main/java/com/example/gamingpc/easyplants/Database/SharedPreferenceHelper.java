package com.example.gamingpc.easyplants.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gamingpc.easyplants.Models.UserThreshold;

public class SharedPreferenceHelper {

    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context){
        sharedPreferences = context.getSharedPreferences("ThresholdPreference", Context.MODE_PRIVATE);
    }

    public void saveFromUserThreshold(UserThreshold userThreshold){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ThresholdMax", userThreshold.getThresholdMax());
        editor.putInt("ThresholdMin", userThreshold.getThresholdMin());
        editor.commit();
    }

    public UserThreshold getUserthreshold(){
        UserThreshold userThreshold = new UserThreshold(
                -1,
                sharedPreferences.getInt("ThresholdMax", 100),
                sharedPreferences.getInt("ThresholdMin", 0)
        );

        return userThreshold;
    }

}


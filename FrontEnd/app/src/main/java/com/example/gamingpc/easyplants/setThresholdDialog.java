package com.example.gamingpc.easyplants;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class setThresholdDialog extends DialogFragment {

    private static final String TAG = "setThresholdDialog";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_set_threshold, container, false);

        Log.d(TAG, "Dialog Opened");



        return view;
    }
}

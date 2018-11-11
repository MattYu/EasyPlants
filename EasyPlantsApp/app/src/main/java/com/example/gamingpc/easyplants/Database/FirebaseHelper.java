package com.example.gamingpc.easyplants.Database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    private static final String TAG = "Firebase Helper";


    public void exampleWrite(String user_id, String sensor_id){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("sensor_user_pairing");

        Map<String, Object> mp = new HashMap<>();

        mp.put("user_id", user_id);
        mp.put("sensor_id", sensor_id);

        myRef.setValue(mp);
    }

    public void exampleQuery(String user_id){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("sensor_user_pairing").orderByChild("user_id").equalTo(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot pairing : dataSnapshot.getChildren()) {
                        // do something with the individual "pairing"
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}

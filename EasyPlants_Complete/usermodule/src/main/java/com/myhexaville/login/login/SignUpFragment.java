/*
Imported library from myhaxaville.login
Used in accordance to Apache License with modifications
 */

package com.myhexaville.login.login;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myhexaville.login.R;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment implements OnSignUpListener{
    private FirebaseAuth mAuth;
    EditText SignUpEmail,SignUpPassword, SignUpConfirmPass;


    private static final String TAG = "SignUpFragment";
    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_signup, container, false);
        SignUpEmail = (EditText)inflate.findViewById(R.id.EmailField2);
        SignUpPassword = (EditText)inflate.findViewById(R.id.SignUpPass);
        SignUpConfirmPass = (EditText)inflate.findViewById(R.id.SignUpPassConfirm);
        //SignUpEmail = (EditText) findViewById(R.id.EmailField2);
        mAuth = FirebaseAuth.getInstance();
        return inflate;
    }

    @Override
    public void signUp() {
        String message =  SignUpEmail.getText().toString();

        if (SignUpPassword.getText().toString().length() == 0 || SignUpConfirmPass.getText().toString().length() == 0 || message.length() ==0){
            Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (SignUpPassword.getText().toString().equals(SignUpConfirmPass.getText().toString())){
            mAuth.createUserWithEmailAndPassword(message, SignUpConfirmPass.getText().toString());
            Toast.makeText(getContext(), "Signing up...", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (mAuth.getCurrentUser() == null){
                        Toast.makeText(getContext(), "Oh no! Please verify your info", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Signing in!", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"SensorFolder");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (!snapshot.hasChild("DemoSensor")) {
                                    DatabaseReference myRef8 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/Preference1");
                                    myRef8.setValue("Alarm On");

                                    DatabaseReference myRef2 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/DemoSensor/MinThreshold");
                                    myRef2.setValue(0);

                                    DatabaseReference myRef3 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/DemoSensor/MaxThreshold");
                                    myRef3.setValue(100);

                                    DatabaseReference myRef0 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/DemoSensor/AutoWaterOn");
                                    myRef0.setValue(1);

                                    DatabaseReference myRef5 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/DemoSensor/EnableReading");
                                    myRef5.setValue(1);

                                    DatabaseReference myRef6 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/DemoSensor/Deleted");
                                    myRef6.setValue(0);

                                    DatabaseReference myRef7 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/DemoSensor/PlantName");
                                    myRef7.setValue("Demo Plant");

                                    DatabaseReference myRef4 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() +"/SensorFolder/DemoSensor/SensorData/-LQv5Qq2f0pUQ2K9TC26/");
                                    Map<String, Object> mp = new HashMap<>();
                                    mp.put("humidity_value", 60);
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/YYYY");
                                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                    mp.put("Time", (sdf.format(timestamp)));
                                    myRef4.setValue(mp);

                                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

                                    DatabaseReference myRef9 = database.getReference("UserFolder/" + mAuth.getCurrentUser().getUid() + "/SensorFolder/DemoSensor/AlertTime");
                                    myRef9.setValue(sdf1.format(timestamp));

                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });

                        Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage("com.example.gamingpc.easyplants");
                        if (launchIntent != null) {
                            startActivity(launchIntent);//null pointer check in case package name was not found
                        }
                    }
                }
            }, 3500);

        }
        else {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }
}

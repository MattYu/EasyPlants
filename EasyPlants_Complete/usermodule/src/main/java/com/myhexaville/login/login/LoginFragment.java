package com.myhexaville.login.login;


import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.myhexaville.login.R;

public class LoginFragment extends Fragment implements OnLoginListener{
    private static final String TAG = "LoginFragment";

    FirebaseAuth mAuth;

    EditText emailField;
    EditText LoginPassField;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);
        emailField = (EditText)inflate.findViewById(R.id.emailField);
        LoginPassField = (EditText)inflate.findViewById(R.id.LoginPassField);
        mAuth = FirebaseAuth.getInstance();
        inflate.findViewById(R.id.forgot_password).setOnClickListener(v ->
                resetPassword());
        return inflate;
    }

    public void resetPassword(){
        String email = emailField.getText().toString();
        mAuth.sendPasswordResetEmail(email);
        Toast.makeText(getContext(), "Reset link sent to the email entered above", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void login() {
        String email = emailField.getText().toString();
        String password = LoginPassField.getText().toString();

        if (email.length() == 0 || password.length()==0){
            Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {

            mAuth.signInWithEmailAndPassword(email, password);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (mAuth.getCurrentUser() == null){
                        Toast.makeText(getContext(), "Oh no! Please try again and very your network", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Signing in!", Toast.LENGTH_SHORT).show();
                        Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage("com.example.gamingpc.easyplants");
                        if (launchIntent != null) {
                            startActivity(launchIntent);//null pointer check in case package name was not found
                        }
                    }
                }
            }, 3500);

            Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();


        }
    }
}

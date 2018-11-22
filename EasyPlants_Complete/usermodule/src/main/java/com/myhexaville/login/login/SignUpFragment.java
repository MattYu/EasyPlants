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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myhexaville.login.R;

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
                        Toast.makeText(getContext(), "Oh no! Please very your info", Toast.LENGTH_SHORT).show();
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

        }
        else {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }
}

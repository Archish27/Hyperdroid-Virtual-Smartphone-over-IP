package com.hyperdroidclient.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hyperdroidclient.R;
import com.hyperdroidclient.common.BaseActivity;
import com.hyperdroidclient.dashboard.MainActivity;
import com.hyperdroidclient.data.local.SharedPreferenceManager;
import com.hyperdroidclient.widgets.BaseButton;
import com.hyperdroidclient.widgets.BaseTextView;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Archish on 10/16/2017.
 */

public class RegisterActivity extends BaseActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    int Temp;
    EditText etName, etEmailID, etPassword, etCPassword;
    BaseButton bRegister;
    BaseTextView tvLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void initViews() {

        mAuth = FirebaseAuth.getInstance();

        etName = (EditText) findViewById(R.id.etName);
        etEmailID = (EditText) findViewById(R.id.etEmailId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etCPassword = (EditText) findViewById(R.id.etCPassword);
        bRegister = (BaseButton) findViewById(R.id.bRegister);
        tvLogin = (BaseTextView) findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int check = validation();
                if (check == 0) {
                    String Display_Name = etName.getText().toString();
                    String Email = etEmailID.getText().toString();
                    String Password = etPassword.getText().toString();
                    if (!TextUtils.isEmpty(Display_Name) || !TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Password)) {
                        reg_user(Display_Name, Email, Password);
                    } else
                        Toast.makeText(RegisterActivity.this, "Details missing!!", Toast.LENGTH_SHORT).show();

/*
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    */
                    //TODO Network call to register user
                } else if (check == 1) {
                    Toast.makeText(RegisterActivity.this, "Some fields are empty.", Toast.LENGTH_SHORT).show();
                } else if (check == 2) {
                    etCPassword.setFocusable(true);
                    etCPassword.setError("Password does not match!");
                } else if (check == 3) {
                    etEmailID.setFocusable(true);
                    etEmailID.setError("Invalid Email ID");
                }


            }
        });
    }

    private int validation() {
        String email = etEmailID.getText().toString();
        String name = etName.getText().toString();
        String password = etPassword.getText().toString();
        String cpassword = etCPassword.getText().toString();
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || cpassword.isEmpty())
            return 1;
        else if (!matcher.find())
            return 3;
        else if (!password.equals(cpassword))
            return 2;

        return 0;

    }

    private void reg_user(final String display_name, String email, String password) {
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Setting Up DataBase
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String UUID = current_user.getUid();
                    new SharedPreferenceManager(getApplicationContext()).saveAccessToken(UUID);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UUID);
                    HashMap<String, String> userMap = new HashMap<String, String>();
                    userMap.put("Name", display_name);
                    userMap.put("Machine", "Non-Privileged");
                    userMap.put("vmid", "");
                    userMap.put("TimeStamp","");
                    databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {

                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                new SharedPreferenceManager(getApplicationContext()).saveMainPage(1);
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Cannot Register Details\nCheck Details and Try Again", Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }
        });
    }

}
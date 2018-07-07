package com.hyperdroidclient.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
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
import com.hyperdroidclient.R;
import com.hyperdroidclient.common.BaseActivity;
import com.hyperdroidclient.dashboard.MainActivity;
import com.hyperdroidclient.data.local.SharedPreferenceManager;
import com.hyperdroidclient.widgets.BaseButton;
import com.hyperdroidclient.widgets.BaseTextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Archish on 10/16/2017.
 */

public class LoginActivity extends BaseActivity {

    FirebaseAuth mAuth;

    EditText etEmailID;
    EditText etPassword;
    BaseButton bLogin;
    BaseTextView tvRegister;
    DatabaseReference databaseReference;
    // changes from here


    private boolean isNetworkConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // till here


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();

        if (isNetworkConnected()) {
            ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            if (new SharedPreferenceManager(getApplicationContext()).getMainPage() != 0) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            setContentView(R.layout.activity_login);
            initViews();
            mProgressDialog.dismiss();
        }
        else {
            setContentView(R.layout.activity_login);
            new AlertDialog.Builder(this)
                    .setTitle("No Active Internet Connection")
                    .setMessage("It looks like your internet connection is off. Please turn it " +
                            "on and try again")
                    .setPositiveButton  (android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setIcon(R.drawable.icon_wifinotworking).show();
        }
    }

    private void initViews() {


        etEmailID = (EditText) findViewById(R.id.etEmailId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (BaseTextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        bLogin = (BaseButton) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = validation();
                if (check == 0) {
                    showProgressDialog();
                    mAuth = FirebaseAuth.getInstance();
                    String login_Email = etEmailID.getText().toString();
                    String login_Password = etPassword.getText().toString();
                    login_User(login_Email, login_Password);
                } else if (check == 1) {
                    etEmailID.setError("Email ID cannot be empty");
                    etEmailID.setFocusable(true);
                } else if (check == 2) {
                    etPassword.setError("Password cannot be empty");
                    etPassword.setFocusable(true);
                } else if (check == 3) {
                    etEmailID.setError("Invalid EmailID");
                    etEmailID.setFocusable(true);
                }
            }
        });
    }

    private int validation() {
        String email = etEmailID.getText().toString();
        String password = etPassword.getText().toString();
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (email.isEmpty())
            return 1;
        else if (password.isEmpty())
            return 2;
        else if (!matcher.find())
            return 3;
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS}, 1001);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access all the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS},
                                                        1001);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void login_User(String login_email, String login_password) {
        mAuth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    new SharedPreferenceManager(getApplicationContext()).saveAccessToken(user.getUid());
                    new SharedPreferenceManager(getApplicationContext()).saveMainPage(1);
                    new SharedPreferenceManager(getApplicationContext()).saveEmail(user.getEmail());
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").child(user.getUid()).child("Machine")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String machine = dataSnapshot.getValue().toString();
                                    new SharedPreferenceManager(getApplicationContext()).saveAdmin(machine);
                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Email/Password Combination!!", Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();
            }
        });
    }
}
package com.hyperdroidclient.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hyperdroidclient.R;
import com.hyperdroidclient.auth.LoginActivity;
import com.hyperdroidclient.common.BaseActivity;
import com.hyperdroidclient.common.Config;
import com.hyperdroidclient.common.CustomFontLoader;
import com.hyperdroidclient.connection.AdminFragment;
import com.hyperdroidclient.connection.HomeFragment;
import com.hyperdroidclient.data.local.SharedPreferenceManager;
import com.hyperdroidclient.data.local.remote.PublicIPResponse;
import com.hyperdroidclient.data.local.remote.api.RestService;
import com.hyperdroidclient.dispatcher.RetrofitObj;
import com.hyperdroidclient.widgets.BaseTextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;


/***
 * MainActivity
 * includes
 * NavigationDrawer.
 */

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.MainView {
    public static String PIP = "";
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    String Name;


    @Override
    public void onNetworkException(Throwable e) {
        super.onNetworkException(e);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        RepairRequestsListActivity fragment = new RepairRequestsListActivity();
//        android.support.v4.app.FragmentTransaction fragmentTransaction =
//                getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment);
//        fragmentTransaction.commit();

//        PIP = getPublicIPAddress(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Config.changeFontInViewGroup(drawer, CustomFontLoader.MONTSERRAT_BOLD);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        BaseTextView role = (BaseTextView) headerView.findViewById(R.id.tvEmail);
        final BaseTextView username = (BaseTextView) headerView.findViewById(R.id.tvName);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        RestService restService = RetrofitObj.getInstance().create(RestService.class);
//        mainPresenter = new MainPresenter(restService, this);
//        mainPresenter.getPublicIp("json");
        //username.setText(databaseReference.child("Users").child(new SharedPreferenceManager(getApplicationContext()).getAccessToken()).child("Name"));
        databaseReference.child("Users").child(new SharedPreferenceManager(getApplicationContext()).getAccessToken()).child("Name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Name = dataSnapshot.getValue().toString();
                        username.setText(Name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        role.setText(firebaseUser.getEmail());
        navigationView.setNavigationItemSelectedListener(this);
        if (!new SharedPreferenceManager(getApplicationContext()).getEmailId().equalsIgnoreCase("admin@gmail.com")) {
            HomeFragment fragment = new HomeFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else {
            AdminFragment fragment = new AdminFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }

        Config.changeFontInViewGroup(drawer, CustomFontLoader.MONTSERRAT);
    }

    public static String getPublicIPAddress(Context context) {
        //final NetworkInfo info = NetworkUtils.getNetworkInfo(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();

        RunnableFuture<String> futureRun = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if ((info != null && info.isAvailable()) && (info.isConnected())) {
                    StringBuilder response = new StringBuilder();

                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) (
                                new URL("http://checkip.amazonaws.com/").openConnection());
                        urlConnection.setRequestProperty("User-Agent", "Android-device");
                        //urlConnection.setRequestProperty("Connection", "close");
                        urlConnection.setReadTimeout(15000);
                        urlConnection.setConnectTimeout(15000);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Content-type", "application/json");
                        urlConnection.connect();

                        int responseCode = urlConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }

                        }
                        urlConnection.disconnect();
                        Log.i("HyperDroid-Assignment", "public Ip :- " + response.toString());
                        return response.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //Log.w(TAG, "No network available INTERNET OFF!");
                    return null;
                }
                return null;
            }
        });

        new Thread(futureRun).start();

        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                if (!new SharedPreferenceManager(getApplicationContext()).getEmailId().equalsIgnoreCase("admin@gmail.com")) {
                    HomeFragment fragment = new HomeFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                } else {
                    AdminFragment fragment = new AdminFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.nav_logout:
                showLogoutDialog();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(MainActivity.this).setTitle("Confirm Logout?")
                .setMessage("Are your sure you want to Logout?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        new SharedPreferenceManager(getApplicationContext()).removeAllToken();
                        Intent i1 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i1);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(R.drawable.icon_logout)
                .show();
    }

    @Override
    public void onPublicIp(PublicIPResponse publicIPResponse) {
        PIP = publicIPResponse.getIp();

    }
}

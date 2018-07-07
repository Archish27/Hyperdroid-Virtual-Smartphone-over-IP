package com.example.nikhil.hyperdroid_backgroundservices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    // Using Firebase Real Time Database
    public static FirebaseDatabase database;
    public static DatabaseReference mDatabase;
    public static Context ctx;
    // Refress interval
    public static int minterval = 1000;
    public static SharedPreferences sharedPreferences;
    // Authentication using Firebase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Servive Start And End
    private Button startService;
    private Button stopService;
    MainPresenter mainPresenter;
    String PIP;

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs)
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else if (!isIPv4) {
                            int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                            return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                        }
                    }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    private static String readContentsOfHash() {

        File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
        File file = new File(sdcard, "temphyper/storehash.txt");

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();

    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        String s = sharedPreferences.getString("name@VM", "");
        if (s.isEmpty()) {
            s = getSaltString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name@VM", s);
            editor.apply();
        }
        ctx = getApplicationContext();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("admin@gmail.com", "admin@123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d("Hyperdroid", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Hyperdroid", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "auth_failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        FirebaseDatabase database;
        DatabaseReference mDatabase;

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        String vmname = sharedPreferences.getString("name@VM", "");
        HashMap<String, String> map = new HashMap<>();
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wm.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        map.put("Address", getIPAddress(true));
        map.put("Port", "5901");
        map.put("TimeStamp", "");
        map.put("VMID", vmname);
        //map.put("Status","Idle");
        map.put("SSID", ssid);

        // Type can Have Privileged / Non-Privileged
        map.put("Type", "Non-Privileged");
//        RestService restService = RetrofitObj.getInstance().create(RestService.class);
//        mainPresenter = new MainPresenter(restService,this);
//        mainPresenter.getPublicIp("json");
        PIP = getPublicIPAddress(getApplicationContext());
        Log.i("HyperDroid-Assignment", "Public IP is :-" + PIP);
        map.put("PublicIP", PIP);
        map.put("Hash", readContentsOfHash());
        //map.put("RefInterval", String.valueOf(MainActivity.minterval));
        Log.i("#Hyperdroid", ssid);
        mDatabase.child("VirtualMachine").child(vmname).setValue(map);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainActivity.minterval = ((Long) dataSnapshot.child("Refresh_Interval").getValue()).intValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        startService = (Button) findViewById(R.id.startService);
        stopService = (Button) findViewById(R.id.stopService);
        // Updating the time on firebase to Address the problem of Status.
        // Starting the Service
        startService(new Intent(getApplicationContext(), MyService.class));
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), MyService.class));
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), MyService.class));
            }
        });

    }

    private String getSaltString() {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(androidId.getBytes("UTF-8"));
            androidId = bytesToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return androidId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                        Log.i("HyperDroid-Assignment" , "public Ip :- " + response.toString()   );
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
    public void onNetworkException(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onPublicIp(PublicIPResponse publicIPResponse) {
        PIP = publicIPResponse.getIp();
    }
}

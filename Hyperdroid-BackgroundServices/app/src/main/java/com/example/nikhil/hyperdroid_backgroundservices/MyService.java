package com.example.nikhil.hyperdroid_backgroundservices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by nikhil on 10/10/17.
 */

public class MyService extends Service {

    private Thread thread;
    private Handler mhandler = null;
    private DatabaseReference mDatabase;

    private  String vmname;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mhandler = new Handler();
        startRepeatingTask();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();

    Runnable m_statusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                repeatingTask(); //this function can change value of m_interval.
            } catch (Exception e) {
                e.printStackTrace();
            }
            mhandler.postDelayed(m_statusChecker, MainActivity.minterval);
        }
    };
    Future longRunningTaskFuture = threadPoolExecutor.submit(m_statusChecker);
    public void startRepeatingTask() {
        m_statusChecker.run();

    }

    public void stopRepeatingTask() {

        longRunningTaskFuture.cancel(true);
        mhandler.removeCallbacks(m_statusChecker);


    }

    public void repeatingTask() throws Exception {

        final FirebaseDatabase database;
        DatabaseReference mDatabase;
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        vmname = sharedPreferences.getString("name@VM", "");
            String currentDateandTime = sdf.format(new Date());
        mDatabase.child("VirtualMachine").child(vmname).child("TimeStamp").setValue(currentDateandTime);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

    }
}

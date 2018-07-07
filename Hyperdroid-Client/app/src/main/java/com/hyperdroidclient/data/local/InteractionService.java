package com.hyperdroidclient.data.local;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hyperdroidclient.androidVNC.VncCanvasActivity;
import com.hyperdroidclient.dashboard.MainActivity;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by nikhi on 05-03-2018.
 */

public class InteractionService extends Service {

    private static Date lastInteraction;

    private Handler mhandler = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static void UpdateInteraction(){
        lastInteraction = new Date();
    }

    int timeleft=0;
    boolean loggingoff = false;
    public void repeatingTask() throws Exception {

        Date now = new Date();
        long timenow = now.getTime();
        long lasttime = lastInteraction.getTime();
        Log.i("Preempting-User" , "Comparing "+(timenow-lasttime) + " with " + new SharedPreferenceManager(getApplicationContext()).getInteractionTimeout() );
        if((timenow-lasttime) > new SharedPreferenceManager(getApplicationContext()).getInteractionTimeout())
        {
            if(!loggingoff)
            {
                loggingoff = true;
                timeleft = 10 ;
                Toast.makeText(this, "Logging off in 10 Seconds of inactivity", Toast.LENGTH_SHORT).show();
            }
            else{
                if ( timeleft != 0 ){
                    //Toast.makeText(this, "Loggin Off... " + timeleft + " Seconds", Toast.LENGTH_SHORT).show();
                    timeleft--;
                    Thread.sleep(1000);
                }
                else
                {
                    VncCanvasActivity.AbortSession();
                    loggingoff=false;
                }
            }
        }
        else {
            timeleft = 0;
            loggingoff = false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mhandler = new Handler();
        startRepeatingTaskInteractionCheck();
        UpdateInteraction();
        Log.i("Preempting-User" , "Starting Service");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Preempting-User" , "Stoping Service");
        stopRepeatingTaskInteractionCheck();
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
            mhandler.postDelayed(m_statusChecker,1000);
        }
    };
    Future longRunningTaskFuture = threadPoolExecutor.submit(m_statusChecker);

    private void startRepeatingTaskInteractionCheck()
    {
        m_statusChecker.run();
    }
    private void stopRepeatingTaskInteractionCheck()
    {
        longRunningTaskFuture.cancel(true);
        mhandler.removeCallbacks(m_statusChecker);
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

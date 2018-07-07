package com.archish.androidscriptapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by archish on 26/11/17.
 */

public class BootService extends Service {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    String username = "admin@gmail.com";
    String password = "admin@123";
    String SALT2 = "deliciously salty";
    private SharedPreferences sharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        final String pText = sharedPreferences.getString("plainText", "");
        final int firstid = sharedPreferences.getInt("firstid", 0);
        try {
            if (firstid == 0 && pText.equals("")) {
                byte[] key = Base64.encode((SALT2 + username + password).getBytes(), Base64.DEFAULT);
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16); // use only first 128 bit

                SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
                // Instantiate the cipher
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                String secKey = Base64.encodeToString(secretKeySpec.getEncoded(), Base64.DEFAULT);
                Log.d("Server Key :", secKey);

                String secretID = getSaltString();
                byte[] encrypted = cipher.doFinal((secretID).getBytes());
                String encryptedValue = new String(Base64.encode(encrypted, Base64.DEFAULT));
                Log.d("Hash :", encryptedValue);
                storeHash("storehash.txt", encryptedValue);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("plainText", secretID);
                editor.putInt("firstid", 1);
                editor.apply();
            }
            String SharedText = sharedPreferences.getString("plainText", "");
            Log.d("PlainText", SharedText);
            Log.d("Command : ", "droidvnc_x86 -pass " + SharedText);
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream dOut = new DataOutputStream(su.getOutputStream());
            dOut.writeBytes("droidvnc_x86 -pass " + SharedText);
            dOut.writeBytes("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public void storeHash(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "temphyper");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}

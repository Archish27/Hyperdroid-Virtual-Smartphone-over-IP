package com.hyperdroidclient.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;

public class SharedPreferenceManager {
    private SharedPreferences settings;

    private static final String PREFS_NAME = "HyperdroidPrefs";
    private static final String PREFS_FULLNAME = "name";
    private static final String PREFS_EMAILID = "emailid";

    private static final String PREFS_ACCESS_TOKEN = "accessToken";
    private static final String PREFS_MAINPAGE = "mainpage";
    private static final String PREFS_VM_KEY = "vmkey";
    private static final String PREFS_VM_STATUS = "vmstatus";
    private static String PREFS_DEVICE_TOKEN = "devicetoken";
    private static String PREFS_ADMIN = "admin";
    private static String PREFS_INTERACTION_TIMEOUT = "interaction_timeout";



    public SharedPreferenceManager(Context mContext) {
        settings = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveName(String first_name) {
        settings.edit().putString(PREFS_FULLNAME, first_name).apply();
    }
    public void saveAdmin(String admin) {
        settings.edit().putString(PREFS_ADMIN, admin).apply();
    }

    public void saveEmail(String last_name) {
        settings.edit().putString(PREFS_EMAILID, last_name).apply();
    }


    public void saveMainPage(int page) {
        settings.edit().putInt(PREFS_MAINPAGE, page).apply();
    }
    public void saveInteractionTimeout(int interaction_timeout) {
        settings.edit().putInt(PREFS_INTERACTION_TIMEOUT, interaction_timeout).apply();
    }

    public void saveAccessToken(String accessToken) {
        settings.edit().putString(PREFS_ACCESS_TOKEN, accessToken).apply();
    }


    public void saveVMKey(String vmKey) {
        settings.edit().putString(PREFS_VM_KEY, vmKey).apply();
    }
    public String getVMKey(){
        return settings.getString(PREFS_VM_KEY,null);
    }
    public String getAdmin(){
        return settings.getString(PREFS_ADMIN,null);
    }
    public void saveVMStatus(boolean vmStatus) {
        settings.edit().putBoolean(PREFS_VM_STATUS, vmStatus).apply();
    }
    public boolean getVMStatus(){
        return settings.getBoolean(PREFS_VM_STATUS,true);
    }

    public String getAccessToken() {
        return settings.getString(PREFS_ACCESS_TOKEN, null);
    }

    public String getName() {
        return settings.getString(PREFS_FULLNAME, "");
    }

    public String getEmailId() {
        return settings.getString(PREFS_EMAILID, "");
    }


    public int getMainPage() {
        return settings.getInt(PREFS_MAINPAGE, 0);
    }
    public int getInteractionTimeout() {
        return settings.getInt(PREFS_INTERACTION_TIMEOUT, 0);
    }

    public void saveDeviceToken(String token) {
        settings.edit().putString(PREFS_DEVICE_TOKEN, token).apply();
    }

    public String getDeviceToken() {
        return settings.getString(PREFS_DEVICE_TOKEN, null);
    }

    public void removeAccessToken() {
        settings.edit().remove(PREFS_ACCESS_TOKEN).apply();
    }

    public void removeMainPage() {
        settings.edit().remove(PREFS_MAINPAGE).apply();
    }

    public void removeAllToken() {
        settings.edit().clear().apply();
    }


}

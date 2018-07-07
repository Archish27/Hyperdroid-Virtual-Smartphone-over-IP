package com.hyperdroidclient.data.local.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.hyperdroidclient.dashboard.MainActivity;

/**
 * Created by nikhi on 31-01-2018.
 */

public class User implements Parcelable {

    private String key;
    private String Machine;
    private String Name;
    private String VMID;
    private String TimeStamp;


    public User(String key, String machine, String name, String VMID, String timeStamp) {
        this.key = key;
        Machine = machine;
        Name = name;
        this.VMID = VMID;
        TimeStamp = timeStamp;
    }

    public User() {
    }

    protected User(Parcel in) {
        key = in.readString();
        Machine = in.readString();
        Name = in.readString();
        VMID = in.readString();
        TimeStamp = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMachine() {
        return Machine;
    }

    public void setMachine(String machine) {
        Machine = machine;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVMID() {
        return VMID;
    }

    public void setVMID(String VMID) {
        this.VMID = VMID;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(Machine);
        parcel.writeString(Name);
        parcel.writeString(VMID);
        parcel.writeString(TimeStamp);
    }
}

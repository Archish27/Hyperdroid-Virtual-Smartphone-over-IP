package com.hyperdroidclient.data.local.remote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by archish on 27/11/17.
 */

public class VirtualMachine implements Parcelable {
    private String Address;
    private String Hash;
    private String Port;
    private String SSID;
    private String TimeStamp;
    private String Type;
    private String VMID;
    private String PublicIP;

    public VirtualMachine() {
    }

    public VirtualMachine(String Address, String Hash, String Port, String SSID, String TimeStamp, String Type , String VMID , String PublicIP) {
        this.Address = Address;
        this.Hash = Hash;
        this.Port = Port;
        this.SSID = SSID;
        this.TimeStamp = TimeStamp;
        this.Type = Type;
        this.VMID = VMID;
        this.PublicIP = PublicIP;
    }


    protected VirtualMachine(Parcel in) {
        Address = in.readString();
        Hash = in.readString();
        Port = in.readString();
        SSID = in.readString();
        TimeStamp = in.readString();
        Type = in.readString();
        VMID = in.readString();
        PublicIP = in.readString();
    }

    public static final Creator<VirtualMachine> CREATOR = new Creator<VirtualMachine>() {
        @Override
        public VirtualMachine createFromParcel(Parcel in) {
            return new VirtualMachine(in);
        }

        @Override
        public VirtualMachine[] newArray(int size) {
            return new VirtualMachine[size];
        }
    };

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String Hash) {
        this.Hash = Hash;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String Port) {
        this.Port = Port;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public String getVMID() {
        return VMID;
    }

    public void setVMID(String VMID) {
        this.VMID = VMID;
    }

    public String getPublicIP() {
        return PublicIP;
    }

    public void setPublicIP(String publicIP) {
        this.PublicIP = PublicIP;
    }

    public String getType(){    return Type;    }

    public void setType(String Type){  this.Type = Type;    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Address);
        parcel.writeString(Hash);
        parcel.writeString(Port);
        parcel.writeString(SSID);
        parcel.writeString(TimeStamp);
        parcel.writeString(Type);
        parcel.writeString(VMID);
        parcel.writeString(PublicIP);
    }

    @Override
    public String toString() {
        return Address;
    }
}

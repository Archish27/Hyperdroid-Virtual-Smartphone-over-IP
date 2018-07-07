package com.hyperdroidclient.data.local.remote;

/**
 * Created by archish on 8/3/18.
 */

public class PublicIPResponse {
    String ip;

    public PublicIPResponse(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

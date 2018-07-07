package com.hyperdroidclient.data.local.remote;

/**
 * Created by archish on 8/3/18.
 */

public class PublicIPRequest {
    String format;

    public PublicIPRequest(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}

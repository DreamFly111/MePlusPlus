package com.meplus.events;

/**
 * Created by dandanba on 3/11/16.
 */
public class BaseEvent {

    public final static String STATUS_OK = "OK";
    public final static String STATUS_FAIL = "FAIL";

    private String status = STATUS_OK;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BaseEvent() {
    }

    public boolean ok() {
        return this.status.equals(STATUS_OK);
    }
}

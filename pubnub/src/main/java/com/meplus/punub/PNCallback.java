package com.meplus.punub;

import android.content.Context;
import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

/**
 * Created by dandanba on 3/3/16.
 */
public class PNCallback extends Callback {
    private final static String TAG = "PUBNUB";
    private final Context mContext;

    public PNCallback(Context context) {
        mContext = context;
    }

    @Override
    public void successCallback(String channel, Object response) {
        final String message = "successCallback: " + response;
        log(message);
    }

    @Override
    public void connectCallback(String channel, Object response) {
        final String message = "connectCallback: " + response;
        log(message);
    }

    @Override
    public void errorCallback(String channel, PubnubError error) {
        final String message = "errorCallback: " + error.errorCode + " " + error.getErrorString();
        log(message);
    }

    private void log(String message) {
        Log.d(TAG, message);
    }
}

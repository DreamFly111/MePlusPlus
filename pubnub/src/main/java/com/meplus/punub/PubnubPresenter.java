package com.meplus.punub;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.meplus.events.EventUtils;
import com.meplus.punub.utils.JsonUtils;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;


/**
 * PNPresenter
 * https://www.pubnub.com/docs/tutorials/pubnub-publish-subscribe
 * https://www.pubnub.com/docs/android-java/api-reference
 */
public class PubnubPresenter {
    private final static String TAG = "PUBNUB";
    private Pubnub mPubnub;
    private String mChannel;

    public void initPubnub(String uuId) {
        mPubnub = new Pubnub(Constants.PN_PUB_KEY, Constants.PN_SUB_KEY);
        mPubnub.setUUID(uuId);
    }

    public void destroy() {
        unsubscribe();
        if (mPubnub != null) {
            mPubnub.shutdown();
            mPubnub = null;
        }
    }

    public void subscribe(Context context, String channel) {
        Log.i(TAG, "subscribe :" + "channel :" + channel);
        if (TextUtils.isEmpty(channel)) {
            return;
        }
        mChannel = channel;
        try {
            mPubnub.subscribe(channel, new PNCallback(context) {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);
                    if (channel.equals(channel)) {
                        Command command = JsonUtils.readValue(message.toString(), Command.class);
                        if (!mPubnub.getUUID().equals(command.getSender())) {
                            Log.i(TAG, "delay :" + (System.currentTimeMillis() - command.getTimeStamp()));
                            final CommandEvent event = new CommandEvent();
                            event.setCommand(command);
                            EventUtils.postEvent(event);
                        }
                    }
                }

                @Override
                public void connectCallback(String channel, Object message) {
                    super.connectCallback(channel, message);
                }

            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe() {
        Log.i(TAG, "unsubscribe :" + ",channel :" + mChannel);
        if (TextUtils.isEmpty(mChannel)) {
            return;
        }
        if (mPubnub != null) {
            mPubnub.unsubscribeAll();
        }
    }

    public void publish(Context context, String message) {
        Log.i(TAG, "publish :" + ",message :" + message);
        if (TextUtils.isEmpty(mChannel)) return; // Return if empty
        if (TextUtils.isEmpty(message)) return; // Return if empty

        final Command command = new Command(mPubnub.getUUID(), message);
        message = JsonUtils.writeValueAsString(command);
        mPubnub.publish(mChannel, message, new PNCallback(context) {
            @Override
            public void successCallback(String channel, Object response) {
                super.successCallback(channel, response);
                final StateEvent event = new StateEvent();
                event.setCommand(command);
                EventUtils.postEvent(event);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);
                final ErrorEvent event = new ErrorEvent();
                event.setCommand(command);
                event.setErrorString(error.getErrorString());
                EventUtils.postEvent(event);
            }
        });
    }
}

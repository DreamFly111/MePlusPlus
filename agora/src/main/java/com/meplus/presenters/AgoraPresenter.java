package com.meplus.presenters;


import io.agora.rtc.RtcEngine;
import io.agora.sample.agora.AgoraApplication;
import io.agora.sample.agora.BaseEngineHandlerActivity;
import io.agora.sample.agora.R;

/**
 * Created by dandanba on 4/2/16.
 * 声网的对外的接口提供给
 */
public class AgoraPresenter {

    public void initAgora(AgoraApplication application, String username) {
        final String vendorKey = application.getString(R.string.vendor_key);
        application.setUserInformation(vendorKey, username);
        application.setRtcEngine(vendorKey);
    }

    public void setEngineHandlerActivity(AgoraApplication application, BaseEngineHandlerActivity activity) {
        application.setEngineHandlerActivity(activity);
    }

    public void startEchoTest(AgoraApplication application) {
        final RtcEngine rtcEngine = application.getRtcEngine();
        rtcEngine.startEchoTest();
    }

    public void stopEchoTest(AgoraApplication application) {
        final RtcEngine rtcEngine = application.getRtcEngine();
        rtcEngine.stopEchoTest();
    }
}

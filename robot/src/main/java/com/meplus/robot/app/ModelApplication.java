package com.meplus.robot.app;

import com.meplus.avos.objects.AVOSRobot;

/**
 * Created by dandanba on 3/1/16.
 */
public class ModelApplication extends BaseApplication {
    private AVOSRobot mRobot;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setRobot(AVOSRobot robot) {
        mRobot = robot;
    }

    public AVOSRobot getRobot() {
        return mRobot;
    }
}

package com.meplus.utils;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.meplus.avos.Constants;

/**
 * Created by dandanba on 4/3/16.
 */
public class AVOSUtils {

    public static void initialize(Application application) {
        AVOSCloud.initialize(application, Constants.AVOS_APP_ID, Constants.AVOS_APP_KEY); // 初始化参数依次为 this, AppId, AppKey
        // AVOSCloud.useAVCloudUS(); // 启用北美节点
        // AVAnalytics.enableCrashReport(this.getApplicationContext(), true);  // 启用崩溃错误统计
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
    }
}

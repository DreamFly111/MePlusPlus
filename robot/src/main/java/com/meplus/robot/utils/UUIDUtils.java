package com.meplus.robot.utils;

import android.content.Context;
import android.provider.Settings.Secure;

/**
 * Created by dandanba on 3/3/16.
 */
public class UUIDUtils {
    public static String getUUID(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }


}

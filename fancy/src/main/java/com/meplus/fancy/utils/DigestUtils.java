package com.meplus.fancy.utils;

import java.security.MessageDigest;

/**
 * Created by dandanba on 3/2/16.
 */
public class DigestUtils extends cn.trinea.android.common.util.DigestUtils {
    /**
     * encode By MD5
     *
     * @param str
     * @return String
     */
    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("utf-8"));
            return new String(encodeHex(messageDigest.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

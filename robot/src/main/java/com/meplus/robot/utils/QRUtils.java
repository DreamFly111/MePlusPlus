package com.meplus.robot.utils;

import android.graphics.Bitmap;

import net.glxn.qrgen.android.QRCode;
/**
 * Created by dandanba on 3/18/16.
 */
public class QRUtils {

    public static Bitmap setupQRBitmap(String code) {
        return QRCode.from(code).bitmap();
    }

}

package com.meplus.robot.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by dandanba on 3/2/16.
 */
public class SnackBarUtils {
    public static void show(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    public static Snackbar make(View view, String text) {
        return Snackbar.make(view, text, Snackbar.LENGTH_LONG);
    }
}

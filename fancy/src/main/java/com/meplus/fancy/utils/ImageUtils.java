package com.meplus.fancy.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by dandanba on 3/9/16.
 */
public class ImageUtils {
    public static void withActivityInto(Activity activity, int resId, ImageView imageView) {
        Glide.with(activity).load(resId).into(imageView);
    }

    public static void withFragmentInto(Fragment fragment, int resId, ImageView imageView) {
        Glide.with(fragment).load(resId).into(imageView);
    }

    public static void withActivityInto(Activity activity, File file, ImageView imageView) {
        Glide.with(activity).load(file).into(imageView);
    }

    public static void withFragmentInto(Fragment fragment, File file, ImageView imageView) {
        Glide.with(fragment).load(file).into(imageView);
    }

    public static void withActivityInto(Activity activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

    public static void withFragmentInto(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment).load(url).into(imageView);
    }

}

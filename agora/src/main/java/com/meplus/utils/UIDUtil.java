package com.meplus.utils;

import java.util.Random;

/**
 * Created by dandanba on 4/5/16.
 */
public class UIDUtil {

    private final static Random random = new Random();

    public static int getUid() {
        return random.nextInt(Math.abs((int) System.currentTimeMillis()));
    }
}

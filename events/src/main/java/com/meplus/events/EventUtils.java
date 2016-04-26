package com.meplus.events;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by dandanba on 4/3/16.
 */
public class EventUtils {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }
}

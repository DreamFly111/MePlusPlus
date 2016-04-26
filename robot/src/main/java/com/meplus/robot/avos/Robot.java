package com.meplus.robot.avos;

import com.avos.avoscloud.AVException;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.events.ErrorEvent;
import com.meplus.events.EventUtils;
import com.meplus.events.QueryEvent;
import com.meplus.events.SaveEvent;

import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.schedulers.Schedulers;


public class Robot {

    @DebugLog
    public static void queryByUUId(String uuId) {
        Observable.just(uuId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(id -> {
                            try {   // 如果，leancloud上，没有表结构，那么会有异常。
                                List<AVOSRobot> list = AVOSRobot.queryRobotByUUID(id);
                                EventUtils.postEvent(new QueryEvent<>(list));
                            } catch (AVException e) {
                                EventUtils.postEvent(new ErrorEvent(e));
                            }
                        },
                        throwable -> EventUtils.postEvent(new ErrorEvent(throwable)));
    }

    @DebugLog
    public static void saveRotot(AVOSRobot avosRobot) {
        Observable.just(avosRobot)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        robot -> {
                            try {
                                robot.save();
                                EventUtils.postEvent(new SaveEvent<>(robot));
                            } catch (AVException e) {
                                EventUtils.postEvent(new ErrorEvent(e));
                            }
                        },
                        throwable -> EventUtils.postEvent(new ErrorEvent(throwable))
                );
    }

}
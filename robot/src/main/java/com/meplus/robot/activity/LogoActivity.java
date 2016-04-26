package com.meplus.robot.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.events.ErrorEvent;
import com.meplus.events.EventUtils;
import com.meplus.events.QueryEvent;
import com.meplus.events.SaveEvent;
import com.meplus.robot.R;
import com.meplus.robot.app.MPApplication;
import com.meplus.robot.avos.Robot;
import com.meplus.robot.utils.UUIDUtils;
import com.meplus.utils.IntentUtils;
import com.meplus.utils.UIDUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import cn.trinea.android.common.util.ListUtils;
import cn.trinea.android.common.util.ToastUtils;
import hugo.weaving.DebugLog;

/**
 * 启动Logo页面
 */
public class LogoActivity extends BaseActivity implements Handler.Callback {
    private static final String TAG = LogoActivity.class.getSimpleName();

    @butterknife.Bind(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;
    private Handler mHandler;

    private String mUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ButterKnife.bind(this);
        EventUtils.register(this);
        mShimmerViewContainer.startShimmerAnimation();

        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
        EventUtils.unregister(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            mUUID = UUIDUtils.getUUID(this);  // 查询是否已经注册过
            Robot.queryByUUId(mUUID);
            return true;
        }
        return false;
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQueryEvent(QueryEvent<AVOSRobot> event) {
        if (event.ok()) {
            final List<AVOSRobot> robotList = event.getList();
            if (ListUtils.isEmpty(robotList)) { // 没有注册过
                final AVOSRobot robot = new AVOSRobot();
                final int robotId = UIDUtil.getUid();
                robot.setRobotId(robotId);
                robot.setUUId(mUUID);
                Robot.saveRotot(robot);
            } else {
                onSaveEvent(new SaveEvent<>(robotList.get(0)));
            }
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveEvent(SaveEvent<AVOSRobot> event) {
        if (event.ok()) {
            MPApplication.getsInstance().setRobot(event.getData());
            startActivity(IntentUtils.generateIntent(this, MainActivity.class));
            finish();
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        if (event.ok()) {
            ToastUtils.show(this, event.getThrowable().getMessage());
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

}

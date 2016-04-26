package com.meplus.robot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.marvinlabs.intents.MediaIntents;
import com.marvinlabs.intents.PhoneIntents;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.events.EventUtils;
import com.meplus.presenters.AgoraPresenter;
import com.meplus.punub.Command;
import com.meplus.punub.PubnubPresenter;
import com.meplus.robot.Constants;
import com.meplus.robot.R;
import com.meplus.robot.app.MPApplication;
import com.meplus.robot.events.BluetoothEvent;
import com.meplus.robot.presenters.BluetoothPresenter;
import com.meplus.robot.utils.SnackBarUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.trinea.android.common.util.ToastUtils;
import hugo.weaving.DebugLog;
import io.agora.sample.agora.AgoraApplication;

/**
 * 设置
 */
public class TestsActivity extends BaseActivity {
    private static final String TAG = TestsActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.bluetooth_state)
    TextView mBluetoothState;
    @Bind(R.id.echo_test_button)
    TextView mTestEchoButton;
    @Bind(R.id.bms_state)
    ImageButton mBMSState;

    /*@Bind(R.id.bz_button)
    ImageButton mBz;*/

    @Bind(R.id.g1)
    ImageView mG1;
    @Bind(R.id.g2)
    ImageView mG2;
    @Bind(R.id.g3)
    ImageView mG3;
    @Bind(R.id.g4)
    ImageView mG4;
    @Bind(R.id.g5)
    ImageView mG5;

    private BluetoothPresenter mBTPresenter;
    private PubnubPresenter mPubnubPresenter = new PubnubPresenter();
    private AgoraPresenter mAgoraPresenter = new AgoraPresenter();
    private String mChannel;

    //private byte[] temp = new byte[11];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getApplicationContext();
        // bluetooth
        mBTPresenter = new BluetoothPresenter(context);
        if (!mBTPresenter.isBluetoothAvailable()) { // 蓝牙模块硬件不支持
            ToastUtils.show(context, getString(R.string.bt_unsupport));
            finish();
            return;
        }

        setContentView(R.layout.activity_tests);
        ButterKnife.bind(this);

        mBTPresenter.create(context);

        // 初始化
        final AVOSRobot robot = MPApplication.getsInstance().getRobot();
        final String username = String.valueOf(robot.getRobotId()); // agora 中的用户名
        final String uuId = robot.getUUId();                        // pubnub 中的用户名
        mChannel = robot.getUUId();                             // pubnub 中的channel

        EventUtils.register(this);

        mAgoraPresenter.initAgora((AgoraApplication) getApplication(), username);

        mPubnubPresenter.initPubnub(uuId);
        mPubnubPresenter.subscribe(this, mChannel);

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("系统自检");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        updateEchoTestButton(false);
        updateBluetoothState(false);
        updateSOC(0);

       //updateObj(0);

        //add avoid object
        updateObj(0);//0正常
      /*  updateObj(0, mG2);//0正常
        updateObj(0, mG3);//0正常
        updateObj(0, mG4);//0正常
        updateObj(0, mG5);//0正常*/

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!mBTPresenter.isBluetoothEnabled()) {// 蓝牙软件部分不支持
            mBTPresenter.enableBluetooth(this);
        } else {
            if (!mBTPresenter.isServiceAvailable()) { // 蓝牙服务没启动
                mBTPresenter.startBluetoothService();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBTPresenter.disconnect();
        mBTPresenter.stopBluetoothService();
        mPubnubPresenter.destroy();
        EventUtils.unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBTPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBluetoothEvent(BluetoothEvent event) {
        if (event.ok()) {

            final int soc = event.getSOC();

            /*byte[] obj2 = new byte[5];//要赋值的数组
            byte[] obj = event.getAvoidObj();//接收一个数组*/

          //  byte[] obj2 = new byte[5];
            byte obj = event.getmAvoidObj2();

            //Log.i("TAG",obj.toString());

           /* for(int i=0;i<5;i++){
                obj2[i] = obj[i];
            }*/

            //滤波，结果不对
            /*for(int i=0;i<5;i++){
                temp[10] = obj[i];
                for(int j=0;j<10;j++){
                    temp[j] = temp[j+1];
                }
                obj2[i] = (byte)((temp[0]+temp[1]+temp[2]+temp[3]+temp[4]+temp[5]+temp[6]+temp[7]+temp[8]+temp[9])/10);
            }*/

            if (soc > 0) {// 发送电量的数据
                updateSOC(soc);
                mBTPresenter.sendDefault();// 自主避障功能使能（默认关闭）
                updateObj(obj);
                /*
                updateObj(obj2[1],mG2);
                updateObj(obj2[2],mG3);
                updateObj(obj2[3],mG4);
                updateObj(obj2[4],mG5);*/
            } else { // 只发送连接的数据
                mBTPresenter.sendDefault();// 自主避障功能使能（默认关闭）
                updateObj(obj);

               /* updateObj(obj2[0],mG1);
                updateObj(obj2[1],mG2);
                updateObj(obj2[2],mG3);
                updateObj(obj2[3],mG4);
                updateObj(obj2[4],mG5);*/
            }

           /* if (soc > 0) {// 发送电量的数据
                updateSOC(soc);
                mBTPresenter.sendDefault();
                updateObj(obj,);
            } else { // 只发送连接的数据
                mBTPresenter.sendDefault();// 自主避障功能使能（默认关闭）
                updateObj(obj);
            }*/


            updateBluetoothState(event.isConnected());
        }
    }

    @OnClick({R.id.bluetooth_state, R.id.channel_test_button, R.id.bms_state,
            R.id.net_test_button, R.id.echo_test_button, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bms_state:
                if (!mBTPresenter.sendGoHome()) {
                    ToastUtils.show(this, getString(R.string.bt_unconnected));
                }
                break;
            case R.id.channel_test_button:
                AVOSRobot robot = MPApplication.getsInstance().getRobot();
                startActivity(com.meplus.activity.IntentUtils.generateVideoIntent(this, mChannel, robot.getRobotId()));
                break;
            case R.id.echo_test_button:
                toggleEchoTest();
                break;
            case R.id.net_test_button:
                startActivity(MediaIntents.newOpenWebBrowserIntent(Constants.HOME_URL));
                break;
            case R.id.bluetooth_state:
                mBTPresenter.connectDeviceList(this);
                break;
            case R.id.fab:
                SnackBarUtils.make(view, getString(R.string.feedback))
                        .setAction(getString(R.string.me_ok), v -> startActivity(PhoneIntents.newCallNumberIntent(Constants.SERVICE_PHONENUMBER)))
                        .show();
                break;
        }
    }


    @OnTouch({R.id.left_button, R.id.up_button, R.id.right_button, R.id.down_button})
    public boolean onTouch(View view, MotionEvent event) {
        final int action = event.getAction();
        final int id = view.getId();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return sendDirection(id);
            case MotionEvent.ACTION_UP:
                return sendDirection(MotionEvent.ACTION_UP);
        }
        return false;
    }

    private void updateSOC(int soc) {
        int index = soc / 10 + 1;
        index = index > 10 ? 10 : index;
        String resName = String.format("battery%1$d", index * 10);
        mBMSState.setImageResource(getResources().getIdentifier(resName, "drawable", getPackageName()));
    }

    /*private void updateObj(int flag){
        if(flag == 0){//无障碍
            mBz.setEnabled(true);
        }else{
            mBz.setEnabled(false);
        }
    }*/

    private void updateObj(int flag) {
        if (flag == 0) {
            mG1.setEnabled(true);
               /* mG1.setEnabled(true);
                mG2.setEnabled(true);
                mG3.setEnabled(true);
                mG4.setEnabled(true);
                mG5.setEnabled(true);*/
        } else if (flag == 1) {
            mG1.setEnabled(false);
               /* mG1.setEnabled(false);
                mG2.setEnabled(false);
                mG3.setEnabled(false);
                mG4.setEnabled(false);
                mG5.setEnabled(false);*/
        }
    }

    private boolean sendDirection(int id) {
        String message = "";
        switch (id) {
            case MotionEvent.ACTION_UP:
                message = Command.ACTION_STOP;
                break;
            case R.id.left_button:
                message = Command.ACTION_LEFT;
                break;
            case R.id.up_button:
                message = Command.ACTION_UP;
                break;
            case R.id.right_button:
                message = Command.ACTION_RIGHT;
                break;
            case R.id.down_button:
                message = Command.ACTION_DOWN;
                break;
        }
        return sendDirection(message);
    }

    private boolean sendDirection(String action) {
        if (!TextUtils.isEmpty(action)) {
            if (!mBTPresenter.sendDirection(action)) {
                ToastUtils.show(this, getString(R.string.bt_unconnected));
            }
            return true;
        }
        return false;
    }

    private void toggleEchoTest() {
        boolean isStarted = (boolean) mTestEchoButton.getTag();
        if (isStarted) {
            mAgoraPresenter.stopEchoTest((AgoraApplication) getApplication());
        } else {
            mAgoraPresenter.startEchoTest((AgoraApplication) getApplication());
        }
        updateEchoTestButton(!isStarted);
    }

    private void updateEchoTestButton(boolean isStarted) {
        mTestEchoButton.setText(isStarted ? getString(R.string.stop_echo_test) : getString(R.string.start_echo_test));
        mTestEchoButton.setTag(isStarted);
    }

    private void updateBluetoothState(boolean state) {
        mBluetoothState.setText(state ? getString(R.string.bt_connect) : getString(R.string.bt_unconnect));
    }
}

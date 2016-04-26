package com.meplus.robot.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.events.EventUtils;
import com.meplus.events.SaveEvent;
import com.meplus.presenters.AgoraPresenter;
import com.meplus.punub.Command;
import com.meplus.punub.CommandEvent;
import com.meplus.punub.PubnubPresenter;
import com.meplus.robot.R;
import com.meplus.robot.app.MPApplication;
import com.meplus.robot.events.BluetoothEvent;
import com.meplus.robot.presenters.BluetoothPresenter;
import com.meplus.robot.viewholder.NavHeaderViewHolder;
import com.meplus.robot.viewholder.QRViewHolder;
import com.meplus.speech.Constants;
import com.meplus.speech.event.LangEvent;
import com.meplus.speech.event.Speech;
import com.meplus.speech.event.SpeechEvent;
import com.meplus.speech.presents.TtsPresenter;
import com.meplus.speech.presents.UnderstandPersenter;
import com.meplus.utils.IntentUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.common.util.ToastUtils;
import hugo.weaving.DebugLog;
import io.agora.sample.agora.AgoraApplication;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, Handler.Callback {
    private static final int START_SPEEKING = 1;
    private static final int START_UNDERSTANDING = 2;
    private static final long START_SPEEKING_DELAY = 100;
    private static final long START_UNDERSTANDING_DELAY = 100;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.bluetooth_state)
    TextView mBluetoothState;
    @Bind(R.id.speech_state)
    TextView mSpeechhState;
    @Bind(R.id.face_image)
    ImageView mFaceImage;
    @Bind(R.id.answer_text)
    TextView mAnswerText;
    @Bind(R.id.question_text)
    TextView mQuestText;
    @Bind(R.id.bms_state)
    ImageButton mBMSState;

   /* @Bind(R.id.g1)
    ImageView mG1;
    @Bind(R.id.g2)
    ImageView mG2;
    @Bind(R.id.g3)
    ImageView mG3;
    @Bind(R.id.g4)
    ImageView mG4;
    @Bind(R.id.g5)
    ImageView mG5;*/

    ImageView objImg;


    private NavHeaderViewHolder mHeaderHolder;

    private BluetoothPresenter mBTPresenter;
    private PubnubPresenter mPubnubPresenter = new PubnubPresenter();
    private AgoraPresenter mAgoraPresenter = new AgoraPresenter();
    private UnderstandPersenter mUnderstandPersenter = new UnderstandPersenter();
    private TtsPresenter mTtsPresenter = new TtsPresenter();
    private String mChannel;
    private boolean mOpenSpeech = false;
    private Handler mSpeechHandler = new Handler();
    private boolean mBluetoothSupport = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // keep screen on - turned on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final Context context = getApplicationContext();
        // bluetooth
        mBTPresenter = new BluetoothPresenter(context);
        if (!mBTPresenter.isBluetoothAvailable()) { // 蓝牙模块硬件支持
            mBluetoothSupport = false;
            ToastUtils.show(context, getString(R.string.bt_unsupport));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        EventUtils.register(this);

        mBTPresenter.create(context);

        // 初始化
        final AVOSRobot robot = MPApplication.getsInstance().getRobot();
        final String username = String.valueOf(robot.getRobotId()); // agora 中的用户名
        final String uuId = robot.getUUId();                        // pubnub 中的用户名
        mChannel = robot.getUUId();                                 // pubnub 中的channel

        mAgoraPresenter.initAgora((AgoraApplication) getApplication(), username);

        mPubnubPresenter.initPubnub(uuId);
        mPubnubPresenter.subscribe(getApplicationContext(), mChannel);

        mSpeechHandler = new Handler(this);

        mUnderstandPersenter.create(this);
        mTtsPresenter.create(this);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("首页");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        final View headerView = mNavigationView.getHeaderView(0);
        mHeaderHolder = new NavHeaderViewHolder(headerView);
        mHeaderHolder.updateView(robot);

        updateBluetoothState(false);
        updateSOC(0);//默认值是0，即为空电量
        toggleSpeech(mOpenSpeech);

       /* //add avoid object
        updateObj(0,mG1);//0正常
        updateObj(0,mG2);//0正常
        updateObj(0,mG3);//0正常
        updateObj(0,mG4);//0正常
        updateObj(0,mG5);//0正常*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mOpenSpeech) {
            startUnderstand();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        toggleSpeech(false);
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
        // keep screen on - turned off
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!mBluetoothSupport) {
            return;
        }
        mSpeechHandler.removeCallbacksAndMessages(null);

        mBTPresenter.disconnect();
        mBTPresenter.stopBluetoothService();
        mPubnubPresenter.destroy();
        mUnderstandPersenter.destroy();
        mTtsPresenter.destroy();
        EventUtils.unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBTPresenter.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case START_SPEEKING:
                if (mOpenSpeech) {
                    final String answer = msg.getData().getString("answer");
                    mTtsPresenter.startSpeaking(answer);
                }
                return true;
            case START_UNDERSTANDING:
                if (mOpenSpeech) {
                    mUnderstandPersenter.startUnderstanding();
                }
                return true;
            default:
                break;
        }
        return false;
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnderstandEvent(SpeechEvent event) {
        if (event.ok()) {
            final Speech speech = event.getSpeech();
            final String action = speech.getAction();
            if (action.equals(Speech.ACTION_SPEECH_ERROR)) { // tts错误
                ToastUtils.show(this, speech.getError());
            } else if (action.equals(Speech.ACTION_UNDERSTAND_ERROR)) { // understand的错误
                startUnderstand();
            } else if (action.equals(Speech.ACTION_UNDERSTAND_END)) {// 理解后的内容
                final String question = speech.getQuestion();
                final String answer = speech.getAnswer();
                startSpeek(question, answer); // 此处的answer一定不为null
            } else if (action.equals(Speech.ACTION_UNDERSTAND_BEGINE)) { // 开始理解
                mFaceImage.setImageResource(R.drawable.thinking_anim); // thinking
                AnimationDrawable animationDrawable = (AnimationDrawable) mFaceImage.getDrawable();
                animationDrawable.start();
            } else if (action.equals(Speech.ACTION_SPEECH_BEGIN)) { // 开始听
                mFaceImage.setImageResource(R.drawable.listening_anim); // listening
                AnimationDrawable animationDrawable = (AnimationDrawable) mFaceImage.getDrawable();
                if (!animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
            } else if (action.equals(Speech.ACTION_SPEECH_END)) { // 说完了
                startUnderstand();
            } else if (action.equals(Speech.ACTION_STOP)) { // 别说了
                toggleSpeech(false);
            }
        }
    }


    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLanEvent(LangEvent event) {
        if (event.ok()) {
            mUnderstandPersenter.setParam();
            mTtsPresenter.setParam();
        }
    }

    private void startSpeek(String question, String answer) {
        mQuestText.setText(question);
        mAnswerText.setText(answer);

        final Message msg = mSpeechHandler.obtainMessage();
        msg.what = START_SPEEKING;
        Bundle data = msg.getData();
        data = data == null ? new Bundle() : data;
        data.putString("question", question);
        data.putString("answer", answer);
        msg.setData(data);

        mSpeechHandler.sendMessageDelayed(msg, START_SPEEKING_DELAY);
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveEvent(SaveEvent<AVOSRobot> event) {
        if (event.ok()) {
            final AVOSRobot robot = event.getData();
            MPApplication.getsInstance().setRobot(robot);
            mHeaderHolder.updateView(robot);
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBluetoothEvent(BluetoothEvent event) {
        if (event.ok()) {
            if (event.isConnected()) {
                final int soc = event.getSOC();
                final byte[] obj = event.getAvoidObj();
                if (soc > 0) {// 发送电量的数据
                    updateSOC(soc);
                    mBTPresenter.sendDefault();// 自主避障功能使能（默认关闭）
                  /*  updateObj(obj[0], mG1);
                    updateObj(obj[1],mG2);
                    updateObj(obj[2],mG3);
                    updateObj(obj[3],mG4);
                    updateObj(obj[4],mG5);*/
                } else { // 只发送连接的数据
                    mBTPresenter.sendDefault();// 自主避障功能使能（默认关闭）
                   // updateObj(obj);
                /*    updateObj(obj[0], mG1);
                    updateObj(obj[1],mG2);
                    updateObj(obj[2],mG3);
                    updateObj(obj[3],mG4);
                    updateObj(obj[4],mG5);*/
                }
            }
            updateBluetoothState(event.isConnected());
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommandEvent(CommandEvent event) {
        if (event.ok()) {
            final Command command = event.getCommand();
            final String message = command.getMessage();
            switch (message) {
                case Command.ACTION_LEFT:
                case Command.ACTION_UP:
                case Command.ACTION_RIGHT:
                case Command.ACTION_DOWN:
                case Command.ACTION_STOP:
                    if (!mBTPresenter.sendDirection(message)) {
                        ToastUtils.show(this, getString(R.string.bt_unconnected));
                    }
                    break;
                case Command.ACTION_CALL:
                    if (!MPApplication.getsInstance().getIsInChannel()) { // 如果正在通电话那么就不能在进入了
                        AVOSRobot robot = MPApplication.getsInstance().getRobot();
                        startActivity(com.meplus.activity.IntentUtils.generateVideoIntent(this, mChannel, robot.getRobotId()));
                    }
                    break;
                case Command.ACTION_HOME:
                    if (!mBTPresenter.sendGoHome()) {
                        ToastUtils.show(this, getString(R.string.bt_unconnected));
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                break;
            case R.id.nav_history:
                startActivity(com.meplus.activity.IntentUtils.generateRecordIntent(this));
                break;
            case R.id.nav_settings:
                startActivity(IntentUtils.generateIntent(this, SettingsActivity.class));
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.fab, R.id.bluetooth_state, R.id.bms_state, R.id.speech_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.speech_state:
                toggleSpeech(!mOpenSpeech);
                break;
            case R.id.fab: // 展示二维码的图片
                showQRDialog();
                break;
            case R.id.bluetooth_state:
                mBTPresenter.connectDeviceList(this);
                break;
            case R.id.bms_state:
                goHome();
                break;
        }
    }

    private void showQRDialog() {
        boolean wrapInScrollView = true;
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.me_bind_tip)
                .customView(R.layout.dialog_qr, wrapInScrollView)
                .positiveText(R.string.me_ok)
                .show();
        final View view = dialog.getView();
        final QRViewHolder viewHolder = new QRViewHolder(view);
        final String code = MPApplication.getsInstance().getRobot().getUUId();
        viewHolder.update(code);
    }

    private void goHome() {
        Snackbar.make(mToolbar, "距离充电桩在1米才能使用", Snackbar.LENGTH_LONG).setAction("确定", v -> {
            if (!mBTPresenter.sendGoHome()) {
                ToastUtils.show(this, getString(R.string.bt_unconnected));
            }
        }).show();
    }

    private void updateSOC(int soc) {
        int index = soc / 10 + 1;
        index = index > 10 ? 10 : index;
        String resName = String.format("battery%1$d", index * 10);
        mBMSState.setImageResource(getResources().getIdentifier(resName, "drawable", getPackageName()));
    }

    private void updateObj(int flag,ImageView img) {
            if(flag==0){
                img.setEnabled(true);
               /* mG1.setEnabled(true);
                mG2.setEnabled(true);
                mG3.setEnabled(true);
                mG4.setEnabled(true);
                mG5.setEnabled(true);*/
            }else if(flag==1){
                img.setEnabled(false);
               /* mG1.setEnabled(false);
                mG2.setEnabled(false);
                mG3.setEnabled(false);
                mG4.setEnabled(false);
                mG5.setEnabled(false);*/
            }
    }

    private void updateBluetoothState(boolean state) {
        mBluetoothState.setText(state ? getString(R.string.bt_connect) : getString(R.string.bt_unconnect));
    }

    private void startUnderstand() {
        mQuestText.setText("");
        mAnswerText.setText("");
        mFaceImage.setImageResource(R.drawable.hello_world);

        final Message msg = mSpeechHandler.obtainMessage();
        msg.what = START_UNDERSTANDING;
        mSpeechHandler.sendMessageDelayed(msg, START_UNDERSTANDING_DELAY);
    }

    private void stopUnderstand() {
        mUnderstandPersenter.stopUnderstanding();
        mTtsPresenter.stopSpeaking();
    }

    private void updateSpeechState() {
        mSpeechhState.setText(mOpenSpeech ? getString(R.string.open_understanding) : getString(R.string.close_understanding));
        if (!mOpenSpeech) {
            mQuestText.setText("");
            mAnswerText.setText("");
            mFaceImage.setImageResource(R.drawable.hello_world);
        }
    }

    private void toggleSpeech(boolean openOrClose) {
        mOpenSpeech = openOrClose;
        if (openOrClose) {
            startSpeek(Constants.getHelloMeplus(), Constants.getHelloMeplus());
        } else {
            stopUnderstand();
        }
        updateSpeechState();
    }

}

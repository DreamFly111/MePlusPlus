package com.meplus.robot.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.kyleduo.switchbutton.SwitchButton;
import com.meplus.activity.BaseActivity;
import com.meplus.events.EventUtils;
import com.meplus.robot.R;
import com.meplus.robot.utils.SnackBarUtils;
import com.meplus.speech.event.LangEvent;
import com.meplus.utils.FIRUtils;
import com.meplus.utils.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.fir.sdk.VersionCheckCallback;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivity {
    @Bind(R.id.root)
    LinearLayout mRoot;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.sb_text)
    SwitchButton mSpeechLanButton;

    private final VersionCheckCallback mCallback = new VersionCheckCallback() {

        @Override
        public void onSuccess(String json) {
            FIRUtils.onSuccess(SettingsActivity.this, json);
        }

        @Override
        public void onFail(Exception e) {
            SnackBarUtils.show(mRoot, e.toString());
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onFinish() {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mSpeechLanButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            com.meplus.speech.Constants.LANG = isChecked ? com.meplus.speech.Constants.ZH_LANG : com.meplus.speech.Constants.EN_LANG;
            EventUtils.postEvent(new LangEvent(isChecked ? LangEvent.ZH_LANG : LangEvent.EN_LANG));
        });

        mSpeechLanButton.setChecked(com.meplus.speech.Constants.LANG.equals(com.meplus.speech.Constants.ZH_LANG));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.password_layout, R.id.feeback_layout, R.id.update_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_layout: // 设备检测
                startActivity(IntentUtils.generateIntent(this, TestsActivity.class));
                break;
            case R.id.feeback_layout: // 问题反馈
                FeedbackAgent agent = new FeedbackAgent(this);
                agent.startDefaultThreadActivity();
                break;
            case R.id.update_layout: // 检查更新
                FIRUtils.checkForUpdateInFIR(this, mCallback);
                break;
            default:
                break;
        }
    }


}

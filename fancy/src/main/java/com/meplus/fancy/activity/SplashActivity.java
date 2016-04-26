package com.meplus.fancy.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.meplus.fancy.utils.IntentUtils;

public class SplashActivity extends BaseActivity implements Handler.Callback {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this);
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            startActivity(IntentUtils.generateIntent(this, MainActivity.class));
            finish();
            return true;
        }
        return false;
    }
}

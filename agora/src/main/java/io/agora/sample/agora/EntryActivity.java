package io.agora.sample.agora;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by apple on 15/9/15.
 */
public class EntryActivity extends BaseEngineHandlerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agora_activity_entry);
        goApp(2000);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void goApp(long delayInMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toMain = new Intent(EntryActivity.this, MainActivity.class);
                startActivity(toMain);
                finish();
            }
        }, delayInMillis);
    }
}

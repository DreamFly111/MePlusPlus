package io.agora.sample.agora;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by apple on 15/9/18.
 */
public class AboutActivity extends BaseEngineHandlerActivity {
    private TextView overallTime;
    private int time;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstance);
        setContentView(R.layout.agora_activity_about);
        initViews();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onUserInteraction(View view) {
        int i1 = view.getId();
        if (i1 == R.id.about_back) {
            finish();
        } else if (i1 == R.id.about_issue) {
            Intent i = new Intent(AboutActivity.this, WebActivity.class);
            i.putExtra(WebActivity.EXTRA_KEY_URL, WebActivity.AGORA_URL_FAQ);
            startActivity(i);
        } else if (i1 == R.id.about_overall) {
            finish();
        } else {
            super.onUserInteraction(view);

        }
    }

    private void initViews() {
        RelativeLayout overallButton = (RelativeLayout) findViewById(R.id.about_overall);
        overallButton.setOnClickListener(getViewClickListener());

        findViewById(R.id.about_back).setOnClickListener(getViewClickListener());
        findViewById(R.id.about_issue).setOnClickListener(getViewClickListener());

        if (((AgoraApplication) getApplication()).getIsInChannel()) {
            overallButton.setVisibility(View.VISIBLE);
            time = ((AgoraApplication) getApplication()).getChannelTime();
            overallTime = ((TextView) overallButton.findViewById(R.id.overall_time));
            setupTime();
        } else {
            overallButton.setVisibility(View.GONE);
        }
    }

    private void setupTime() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        if (time >= 3600) {
                            overallTime.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
                        } else {
                            overallTime.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }
}

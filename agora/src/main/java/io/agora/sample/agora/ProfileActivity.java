package io.agora.sample.agora;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by apple on 15/9/18.
 */
public class ProfileActivity extends BaseEngineHandlerActivity {
    private EditText mVendorKey;
    private EditText mUsername;
    private Button mEdit;
    private TextView overallTime;
    private int time;
    private String originalKey;

    public final static String EXTRA_NEW_KEY = "EXTRA_NEY_KEY";
    public final static String EXTRA_NEW_NAME = "EXTRA_NEW_NAME";
    public final static String EXTRA_HAS_CHANGED = "EXTRA_HAS_CHANGED";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstance);
        setContentView(R.layout.agora_activity_profile);
        originalKey = ((AgoraApplication) getApplication()).getVendorKey();
        initViews();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onUserInteraction(View view) {
        int id = view.getId();
        if (id == R.id.profile_back) {
            finish();
        } else if (id == R.id.profile_edit) {
            if (mEdit.getText().toString().equals(getString(R.string.profile_edit))) {
                setEditable(true);
            } else if (mEdit.getText().toString().equals(getString(R.string.profile_confirm))) {
                if (!validateInput()) {
                    return;
                }
                setEditable(false);
                ((AgoraApplication) getApplication()).setUserInformation(mVendorKey.getText().toString(), mUsername.getText().toString());
                Intent data = new Intent();
                data.putExtra(EXTRA_NEW_KEY, mVendorKey.getText().toString());
                data.putExtra(EXTRA_NEW_NAME, mUsername.getText().toString());
                if (originalKey.equals(mVendorKey.getText().toString())) {
                    data.putExtra(EXTRA_HAS_CHANGED, false);
                } else {
                    data.putExtra(EXTRA_HAS_CHANGED, true);
                }
                setResult(RESULT_OK, data);
                finish();
            }
        } else if (id == R.id.profile_overall) {
            finish();
        } else {
            super.onUserInteraction(view);
        }
    }

    private void initViews() {
        mVendorKey = (EditText) findViewById(R.id.profile_key_input);
        mUsername = (EditText) findViewById(R.id.profile_user_input);
        mVendorKey.setText(((AgoraApplication) getApplication()).getVendorKey());
        mUsername.setText(((AgoraApplication) getApplication()).getUsername());
        mEdit = (Button) findViewById(R.id.profile_edit);
        mEdit.setOnClickListener(getViewClickListener());

        View overallButton = findViewById(R.id.profile_overall);
        overallButton.setOnClickListener(getViewClickListener());

        if (((AgoraApplication) getApplication()).getIsInChannel()) {
            overallButton.setVisibility(View.VISIBLE);
            time = ((AgoraApplication) getApplication()).getChannelTime();
            overallTime = ((TextView) overallButton.findViewById(R.id.overall_time));
            setupTime();
        } else {
            overallButton.setVisibility(View.GONE);
        }
        findViewById(R.id.profile_back).setOnClickListener(getViewClickListener());
    }

    private void setEditable(boolean isEditable) {
        mVendorKey.setEnabled(isEditable);
        mUsername.setEnabled(isEditable);
        mEdit.setText(isEditable ? R.string.profile_confirm : R.string.profile_edit);
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

    private boolean validateInput() {
        if (TextUtils.isEmpty(mVendorKey.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.login_key_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mUsername.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.login_user_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

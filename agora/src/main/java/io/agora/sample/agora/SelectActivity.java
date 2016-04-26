package io.agora.sample.agora;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import io.agora.rtc.RtcEngine;

/**
 * Created by apple on 15/9/16.
 */
public class SelectActivity extends BaseEngineHandlerActivity {
    private DrawerLayout mDrawerLayout;

    private SeekBar mResolutionSeekBar;
    private SeekBar mRateSeekBar;
    private SeekBar mFrameSeekBar;
    private SeekBar mVolumeSeekBar;
    private Switch mTapeSwitch;
    private Switch mFloatSwitch;

    private EditText mChannelInput;

    private TextView mVendorKey;
    private TextView mUsername;
    private TextView mResolutionValue;
    private TextView mRateValue;
    private TextView mFrameValue;
    private TextView mVolumeValue;
    private TextView mPathValue;

    private RelativeLayout mTestContainer;
    private TextView mTestNotice;
    private ImageView mTestVolume;
    private Button mTestTest;

    private RtcEngine rtcEngine;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstance);
        setContentView(R.layout.agora_activity_select);
        setupRtcEngine();
        initViews();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            mVendorKey.setText(data.getStringExtra(ProfileActivity.EXTRA_NEW_KEY));
            mUsername.setText(data.getStringExtra(ProfileActivity.EXTRA_NEW_NAME));
            if (data.getBooleanExtra(ProfileActivity.EXTRA_HAS_CHANGED, false)) {
                setupRtcEngine();
                initViews();
            }
        }
    }

    @Override
    public void onUserInteraction(View view) {
        int id = view.getId();
        if (id == R.id.select_test_button) {
            rtcEngine.setEnableSpeakerphone(true);
            showOrHideTest(true);
            //stop test
        } else if (id == R.id.select_test_cancel) {
            showOrHideTest(false);
            rtcEngine.stopEchoTest();
            //start test
        } else if (id == R.id.select_test_test) {
            mTestNotice.setText(getString(R.string.test_bottom_normal_two));
            mTestTest.setText(getString(R.string.test_test_two));
            mTestVolume.setImageResource(R.drawable.ic_voicetest_dialog_volume_green);
            rtcEngine.startEchoTest();
            //open or close drawer
        } else if (id == R.id.select_drawer_button) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
            //go to Profile
        } else if (id == R.id.select_drawer_profile) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
            Intent toProfile = new Intent(SelectActivity.this, ProfileActivity.class);
            startActivityForResult(toProfile, 0);
            //go to record
        } else if (id == R.id.select_drawer_record) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
            Intent toRecord = new Intent(SelectActivity.this, RecordActivity.class);
            startActivity(toRecord);
            //go to About
        } else if (id == R.id.select_drawer_about) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
            Intent toAbout = new Intent(SelectActivity.this, AboutActivity.class);
            startActivity(toAbout);
            //join channel by video
        } else if (id == R.id.select_video_calling) {
            if (!validateInput()) {
                return;
            }
            Intent toChannel = new Intent(SelectActivity.this, ChannelActivity.class);
            toChannel.putExtra(ChannelActivity.EXTRA_TYPE, ChannelActivity.CALLING_TYPE_VIDEO);
            toChannel.putExtra(ChannelActivity.EXTRA_CHANNEL, mChannelInput.getText().toString());
            startActivity(toChannel);
            finish();
            //join channel by voice
        } else if (id == R.id.select_voice_calling) {
            if (!validateInput()) {
                return;
            }
            Intent toChannel = new Intent(SelectActivity.this, ChannelActivity.class);
            toChannel.putExtra(ChannelActivity.EXTRA_TYPE, ChannelActivity.CALLING_TYPE_VOICE);
            toChannel.putExtra(ChannelActivity.EXTRA_CHANNEL, mChannelInput.getText().toString());
            startActivity(toChannel);
            finish();
        } else {
            super.onUserInteraction(view);
            //voice test
        }
    }

    private void setupRtcEngine() {
        ((AgoraApplication) getApplication()).setRtcEngine(((AgoraApplication) getApplication()).getVendorKey());
        rtcEngine = ((AgoraApplication) getApplication()).getRtcEngine();
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.select_drawer_layout);
        mTestContainer = (RelativeLayout) findViewById(R.id.select_test);
        mTestNotice = (TextView) findViewById(R.id.select_test_notice);
        mTestVolume = (ImageView) findViewById(R.id.select_test_volume);
        mChannelInput = (EditText) findViewById(R.id.select_channel_input);
        mResolutionValue = (TextView) findViewById(R.id.select_drawer_resolution_value);
        mRateValue = (TextView) findViewById(R.id.select_drawer_rate_value);
        mFrameValue = (TextView) findViewById(R.id.select_drawer_frame_value);
        mVolumeValue = (TextView) findViewById(R.id.select_drawer_volume_value);

        mTestTest = (Button) findViewById(R.id.select_test_test);
        mTestTest.setOnClickListener(getViewClickListener());

        findViewById(R.id.select_test_button).setOnClickListener(getViewClickListener());
        findViewById(R.id.select_drawer_button).setOnClickListener(getViewClickListener());
        findViewById(R.id.select_test_cancel).setOnClickListener(getViewClickListener());
        findViewById(R.id.select_drawer_profile).setOnClickListener(getViewClickListener());
        findViewById(R.id.select_drawer_record).setOnClickListener(getViewClickListener());
        findViewById(R.id.select_drawer_about).setOnClickListener(getViewClickListener());
        findViewById(R.id.select_video_calling).setOnClickListener(getViewClickListener());
        findViewById(R.id.select_voice_calling).setOnClickListener(getViewClickListener());

        mVendorKey = (TextView) findViewById(R.id.select_drawer_profile_key);
        mUsername = (TextView) findViewById(R.id.select_drawer_profile_name);
        mPathValue = (TextView) findViewById(R.id.select_drawer_path_value);

        mVendorKey.setText(((AgoraApplication) getApplication()).getVendorKey());
        mUsername.setText(((AgoraApplication) getApplication()).getUsername());
        mPathValue.setText(((AgoraApplication) getApplication()).getPath());

        mResolutionSeekBar = (SeekBar) findViewById(R.id.select_drawer_resolution_seekbar);
        mResolutionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setResolution(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mRateSeekBar = (SeekBar) findViewById(R.id.select_drawer_rate_seekbar);
        mRateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setRate(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mFrameSeekBar = (SeekBar) findViewById(R.id.select_drawer_frame_seekbar);
        mFrameSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setFrame(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mVolumeSeekBar = (SeekBar) findViewById(R.id.select_drawer_volume_seekbar);
        mVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mTapeSwitch = (Switch) findViewById(R.id.select_drawer_tape_switch);
        mTapeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTape(isChecked);
            }
        });

        mFloatSwitch = (Switch) findViewById(R.id.select_drawer_float_switch);
        mFloatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setFloat(isChecked);
            }
        });

        setResolution(((AgoraApplication) getApplication()).getResolution());
        setRate(((AgoraApplication) getApplication()).getRate());
        setFrame(((AgoraApplication) getApplication()).getFrame());
        setVolume(((AgoraApplication) getApplication()).getVolume());
        setTape(((AgoraApplication) getApplication()).getTape());
        setFloat(((AgoraApplication) getApplication()).getFloat());
    }

    //set resolution
    private void setResolution(int resolution) {
        String[] resolutionValues = new String[]{
                getString(R.string.sliding_value_cif),
                getString(R.string.sliding_value_480),
                getString(R.string.sliding_value_720),
                getString(R.string.sliding_value_1080)};
        int[][] videoResolutions = {
                {352,288},
                {640,480},
                {1280,720},
                {1920,1080}};
        mResolutionSeekBar.setProgress(resolution);
        ((AgoraApplication) getApplication()).setResolution(resolution);
        mResolutionValue.setText(resolutionValues[resolution]);
//        rtcEngine.setVideoResolution(videoResolutions[resolution][0],videoResolutions[resolution][1]);
    }

    //set rate
    private void setRate(int rate) {
        String[] rateValues = new String[]{
                getString(R.string.sliding_value_150k),
                getString(R.string.sliding_value_500k),
                getString(R.string.sliding_value_800k),
                getString(R.string.sliding_value_1m),
                getString(R.string.sliding_value_2m),
                getString(R.string.sliding_value_5m),
                getString(R.string.sliding_value_10m)};
        int[] maxBitRates = new int[]{
                150,
                500,
                800,
                1024,
                2048,
                5120,
                10240
        };
        mRateSeekBar.setProgress(rate);
        ((AgoraApplication) getApplication()).setRate(rate);
        mRateValue.setText(rateValues[rate]);
//        rtcEngine.setVideoMaxBitrate(maxBitRates[rate]);
    }

    //set frame
    private void setFrame(int frame) {
        String[] frameValues = new String[]{
                getString(R.string.sliding_value_15),
                getString(R.string.sliding_value_20),
                getString(R.string.sliding_value_24),
                getString(R.string.sliding_value_30),
                getString(R.string.sliding_value_60)};
        int[] videoMaxFrameRates = new int[]{
                15,
                20,
                24,
                30,
                60};
        mFrameSeekBar.setProgress(frame);
        ((AgoraApplication) getApplication()).setFrame(frame);
        mFrameValue.setText(frameValues[frame]);
//        rtcEngine.setVideoMaxFrameRate(videoMaxFrameRates[frame]);
    }

    //set volume
    private void setVolume(int volume) {
        String[] volumeValues = new String[]{
                getString(R.string.sliding_value_0),
                getString(R.string.sliding_value_50),
                getString(R.string.sliding_value_100),
                getString(R.string.sliding_value_150),
                getString(R.string.sliding_value_200),
                getString(R.string.sliding_value_255)};

        int[] speakerPhoneVolume = new int[]{
                0,
                50,
                100,
                150,
                200,
                255};

        mVolumeSeekBar.setProgress(volume);
        ((AgoraApplication) getApplication()).setVolume(volume);
        //mVolumeValue.setText(volumeValues[volume]);
        rtcEngine.setSpeakerphoneVolume(speakerPhoneVolume[volume]);
    }

    //set tape
    private void setTape(boolean isChecked) {
        mTapeSwitch.setChecked(isChecked);
        ((AgoraApplication) getApplication()).setTape(isChecked);
        if (isChecked) {
            rtcEngine.startAudioRecording(((AgoraApplication) getApplication()).getPath() + "/" + Integer.toString((int) System.currentTimeMillis()) + ".wav");
        } else {
            rtcEngine.stopAudioRecording();
        }
    }

    //set float
    private void setFloat(boolean isChecked) {
        mFloatSwitch.setChecked(isChecked);
        ((AgoraApplication) getApplication()).setFloat(isChecked);
    }

    //show test
    private void showOrHideTest(boolean show) {
        mTestNotice.setText(getString(R.string.test_bottom_normal_one));
        mTestVolume.setImageResource(R.drawable.ic_voicetest_dialog_volume_grey);
        mTestContainer.setVisibility(show? View.VISIBLE: View.GONE);
    }

    //check input
    private boolean validateInput() {
        if (TextUtils.isEmpty(mChannelInput.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.select_channel_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

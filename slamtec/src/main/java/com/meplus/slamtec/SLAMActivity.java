package com.meplus.slamtec;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.slamtec.slamware.SlamwareSdpPlatform;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SLAMActivity extends AppCompatActivity {

    @Bind(R.id.down_button)
    ImageButton mDownButton;
    @Bind(R.id.down_left)
    ImageButton mDownLeft;
    @Bind(R.id.down_right)
    ImageButton mDownRight;
    @Bind(R.id.down_up)
    ImageButton mDownUp;
    @Bind(R.id.control_layotut)
    RelativeLayout mControlLayotut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slam);
        ButterKnife.bind(this);

        final String ip = "192.168.11.1";
        final int port = 1445;
        SlamwareSdpPlatform.connect(ip, port);
    }

    @OnClick({R.id.down_button, R.id.down_left, R.id.down_right, R.id.down_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.down_button:
                break;
            case R.id.down_left:
                break;
            case R.id.down_right:
                break;
            case R.id.down_up:
                break;
        }
    }
}

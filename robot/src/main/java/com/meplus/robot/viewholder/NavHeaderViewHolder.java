package com.meplus.robot.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meplus.avos.objects.AVOSRobot;
import com.meplus.robot.R;
import com.meplus.robot.activity.ModifyActivity;
import com.meplus.utils.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavHeaderViewHolder {
    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.content)
    TextView mContent;
    @Bind(R.id.text)
    TextView mText;

    public NavHeaderViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void updateView(AVOSRobot robot) {
        final String name = robot.getRobotName();
        final String description = robot.getRobotDescription();
        mTitle.setText(String.format("机器人:%1$s", TextUtils.isEmpty(name) ? "" : robot.getRobotName()));
        mContent.setText(TextUtils.isEmpty(description) ? "写点什么内容呢？" : description);
        mText.setText(String.format("机器ID:%1$s", robot.getUUId()));
    }

    @OnClick(R.id.image)
    public void onClick(View view) {
        final Context context = view.getContext();
        context.startActivity(IntentUtils.generateIntent(context, ModifyActivity.class));
    }

}
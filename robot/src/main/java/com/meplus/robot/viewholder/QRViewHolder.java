package com.meplus.robot.viewholder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meplus.robot.R;
import com.meplus.robot.utils.QRUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QRViewHolder {
    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.tip)
    TextView mTitle;

    public QRViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void update(String code) {
        final Bitmap bitmap = QRUtils.setupQRBitmap(code);
        mImage.setImageBitmap(bitmap);
        mTitle.setText(code);
    }
}
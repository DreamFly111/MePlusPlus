package com.meplus.activity;

import android.content.Context;
import android.content.Intent;

/**
 * Created by dandanba on 3/31/16.
 */
public class IntentUtils {
    public static Intent generateVideoIntent(Context context, String channel, int userId) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(VideoActivity.EXTRA_TYPE, VideoActivity.CALLING_TYPE_VIDEO);
        intent.putExtra(VideoActivity.EXTRA_USER_ID, userId);
        intent.putExtra(VideoActivity.EXTRA_CHANNEL, channel);
        return intent;
    }

    public static Intent generateRecordIntent(Context context) {
        Intent intent = new Intent(context, RecordActivity.class);
        return intent;
    }
}

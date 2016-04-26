package io.agora.sample.agora;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by apple on 15/9/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onUserInteraction(view);
        }
    };

    public View.OnClickListener getViewClickListener() {
        return onClickListener;
    }

    public void onUserInteraction(View view) {
    }
}

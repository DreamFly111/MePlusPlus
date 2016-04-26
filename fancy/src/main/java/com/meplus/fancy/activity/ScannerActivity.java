package com.meplus.fancy.activity;

import android.os.Bundle;

import com.meplus.fancy.R;
import com.meplus.fancy.app.FancyApplication;
import com.meplus.fancy.events.ScannerEvent;
import com.meplus.fancy.fragments.SimpleScannerFragment;
import com.meplus.fancy.model.ApiService;
import com.meplus.fancy.utils.SignUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.TreeMap;

import cn.trinea.android.common.util.ToastUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 扫描页面
 */
public class ScannerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        EventBus.getDefault().register(this);
        replaceContainer(R.id.frame_layout, SimpleScannerFragment.newInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScannerEvent(ScannerEvent event) {
        ToastUtils.show(this, event.getContent());


        final TreeMap<String, String> args = new TreeMap<>();
        final String Data = "samplestring1";
        final String LibraryId = "samplestring2";
        args.put("Data", Data);
        args.put("LibraryId", LibraryId);
        final String sign = SignUtils.sign(args);
        final String timestamp = args.remove("time");

        final ApiService apiService = FancyApplication.getInstance().getApiService();
        apiService.getborrowlistbyrobot(args, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            ToastUtils.show(this, response.toString());
                            finish();
                        },
                        throwable -> {
                            ToastUtils.show(this, throwable.toString());
                            SimpleScannerFragment simpleScannerFragment = (SimpleScannerFragment) findFragmentById(R.id.frame_layout);
                            simpleScannerFragment.resumeScanner();
                        },
                        () -> {
                        }
                );

    }
}

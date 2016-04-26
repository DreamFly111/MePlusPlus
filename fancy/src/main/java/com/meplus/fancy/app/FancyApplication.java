package com.meplus.fancy.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.meplus.fancy.BuildConfig;
import com.meplus.fancy.model.ApiService;
import com.meplus.fancy.utils.RetrofitUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import im.fir.sdk.FIR;
import io.fabric.sdk.android.Fabric;
import retrofit2.Retrofit;

/**
 * Created by dandanba on 3/8/16.
 */
public class FancyApplication extends MultiDexApplication {

    private static FancyApplication sInstance;

    private Retrofit mRetrofit;

    private RefWatcher mRefWatcher;

    public static FancyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics(), new Answers());

        FIR.init(this);
        mRetrofit = RetrofitUtils.getClient();
        mRefWatcher = BuildConfig.DEBUG ? LeakCanary.install(this) : RefWatcher.DISABLED;
        sInstance = this;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public ApiService getApiService() {
        return mRetrofit.create(ApiService.class);
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }
}

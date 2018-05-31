package com.midas.mobile.activities;

import android.content.Intent;
import android.os.Bundle;

import com.midas.mobile.R;
import com.midas.mobile.networks.RetrofitManager;

import io.reactivex.disposables.CompositeDisposable;

public class SplashActivity extends BaseActivity {
    private final String TAG = SplashActivity.class.getSimpleName();

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    private void init() {
        RetrofitManager.getInstance().init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                finish();
            }
        }).start();
    }

}

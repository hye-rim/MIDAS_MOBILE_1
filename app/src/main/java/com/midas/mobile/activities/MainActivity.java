package com.midas.mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.midas.mobile.R;
import com.midas.mobile.service_customer.views.CustomerMainActivity;
import com.midas.mobile.service_manager.activities.ManagerActivity;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends BaseActivity {
    final static String TAG = MainActivity.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Context mContext;

    /* View Components*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mContext = this;

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
        
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.btn_chart_start: {
                startActivity(new Intent(this, ChartExampleActivity.class));
                break;
            }
            case R.id.btn_manager: {
                startActivity(new Intent(this, ManagerActivity.class));
                break;
            }

            case R.id.btn_user: {
                startActivity(new Intent(this, CustomerMainActivity.class));
                break;
            }

        }
    }
}

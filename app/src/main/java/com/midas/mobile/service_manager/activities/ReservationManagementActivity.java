package com.midas.mobile.service_manager.activities;

import android.content.Context;
import android.os.Bundle;

import com.midas.mobile.R;
import com.midas.mobile.activities.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class ReservationManagementActivity extends BaseActivity {
    private final static String TAG = ReservationManagementActivity.class.getSimpleName();
    private Context mContext;

    /* View Components*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_reservation_management);
        ButterKnife.bind(this);
        this.mContext = this;

        init();

    }

    private void init() {

    }


}

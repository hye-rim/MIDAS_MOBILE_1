package com.midas.mobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class BaseActivity extends AppCompatActivity {
    final static String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        
        super.onDestroy();
    }
}

package com.midas.mobile.activities;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.midas.mobile.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicenseActivity extends AppCompatActivity {

    @BindView(R.id.txt_license)
    TextView txtLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        ButterKnife.bind(this);

        read();
    }

    private void read() {
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("license");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);

            txtLicense.setText(text);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}

package com.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.common.selfdefineview.MainMenuButton;
import com.example.jean.rakvideotest.R;

/**
 * Created by Jean on 2016/1/12.
 */
public class DeviceConnectFailed extends Activity{
    private MainMenuButton _deviceConnectFailedBack;
    private static DeviceConnectFailed _self;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect_failed);
        _self = this;
        _deviceConnectFailedBack=(MainMenuButton)findViewById(R.id.device_connect_failed_back);
        _deviceConnectFailedBack.setOnClickListener(_deviceConnectFailedBack_Click);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     *  Back
     */
    View.OnClickListener _deviceConnectFailedBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Self
     */
    public static DeviceConnectFailed self() {
        return _self;
    }
}



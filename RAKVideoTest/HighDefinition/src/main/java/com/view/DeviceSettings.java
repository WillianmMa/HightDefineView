package com.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.selfdefineview.MainMenuButton;
import com.example.jean.rakvideotest.R;
import com.common.bean.DeviceEntity;
import com.demo.sdk.Lx520;
import com.common.api.RemoteTunnel;

import com.common.paints.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class DeviceSettings extends Activity{
    private MainMenuButton _videoSettingsBack;
    private EditText _videoSettingsPsk;
    private EditText _videoSettingsConfirmPsk;
    private ImageView _videoSettingsShowPsk;
    private ImageView _videoSettingsShowConfirmPsk;
    private Button _videoSettingsBtn;
    private static DeviceSettings _self;

    private String _deviceName="";
    private String _deviceId="";
    private String _deviceIp="";
    private String _devicePsk="";
    private int _voicePort=80;
    private Dialog _modifyProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_settings);
        _self = this;
        _modifyProgressDialog = new Dialog(this,R.style.myDialogTheme);
        _videoSettingsBack=(MainMenuButton)findViewById(R.id.video_settings_back);
        _videoSettingsBack.setOnClickListener(_videoSettingsBack_Click);
        _videoSettingsPsk=(EditText)findViewById(R.id.video_settings_psk);
        _videoSettingsConfirmPsk=(EditText)findViewById(R.id.video_settings_confirm_psk);
        _videoSettingsShowPsk=(ImageView)findViewById(R.id.video_settings_showpsk);
        _videoSettingsShowPsk.setOnClickListener(_videoSettingsShowPsk_Click);
        _videoSettingsShowConfirmPsk=(ImageView)findViewById(R.id.video_settings_showconfirmpsk);
        _videoSettingsShowConfirmPsk.setOnClickListener(_videoSettingsShowConfirmPsk_Click);
        _videoSettingsBtn=(Button)findViewById(R.id.video_settings_btn);
        _videoSettingsBtn.setOnClickListener(_videoSettingsBtn_Click);

        Intent intent = getIntent();
        _deviceName = intent.getStringExtra("devicename");
        _deviceId = intent.getStringExtra("deviceid");
        _deviceIp = intent.getStringExtra("deviceip");
        _devicePsk = intent.getStringExtra("devicepsk");
        _voicePort=intent.getIntExtra("voicport",80);
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
    View.OnClickListener _videoSettingsBack_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Modify Password
     */
    private RemoteTunnel _remoteTunnel=null;
    View.OnClickListener _videoSettingsBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_videoSettingsPsk.getText().toString().equals("")){
                Toast.show(getApplicationContext(), getString(R.string.video_settings_psk_text));
                return;
            }
            if(_videoSettingsConfirmPsk.getText().toString().equals("")){
                Toast.show(getApplicationContext(), getString(R.string.video_settings_new_psk_text));
                return;
            }
            modifyDevicesPasswordIndicator();
            Modify_Password(_voicePort);
        }
    };

    /**
     * Modify Password Indicator
     */
    private void modifyDevicesPasswordIndicator() {
        LayoutInflater getdeviceDialog_inflater =getLayoutInflater();
        View getdeviceDialog_admin=getdeviceDialog_inflater.inflate(R.layout.dialog_indicator, (ViewGroup) findViewById(R.id.dialog_indicator1));
        TextView dialog_indicator_title =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_title);
        TextView dialog_indicator_text =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_text);
        TextView dialog_indicator_line =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_line);
        dialog_indicator_line.setVisibility(View.GONE);
        LinearLayout dialog_indicator_btn =(LinearLayout)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_btn);
        dialog_indicator_btn.setVisibility(View.GONE);
        _modifyProgressDialog.setCanceledOnTouchOutside(true);
        _modifyProgressDialog.setContentView(getdeviceDialog_admin);
        dialog_indicator_title.setText(getApplication().getString(R.string.device_modify_indicator_title));
        dialog_indicator_text.setText(R.string.device_modify_indicator_text);
        _modifyProgressDialog.show();
    }

    /**
     *  Modify Password
     */
   void Modify_Password(int port){
        Lx520 lx520 = new Lx520(_deviceIp+":"+port, _videoSettingsPsk.getText().toString());
        lx520.setOnResultListener(new Lx520.OnResultListener()
        {
            @Override
            public void onResult(Lx520.Response result)
            {
                if (result.type==14) {
                    if (result.statusCode == 200) {
                        DeviceEntity.modifyDevicePasswordById(_self, _deviceId, _videoSettingsConfirmPsk.getText().toString());
                        Toast.show(getApplicationContext(), getString(R.string.video_settings_new_psk_success));
                        if (_remoteTunnel != null){
                            _remoteTunnel.closeTunnels();
                            _remoteTunnel=null;
                        }
                        VideoPlay videoPlay1  = VideoPlay.self();
                        if (videoPlay1 != null)
                            videoPlay1.self().finish();
                        finish();
                    } else {
                        Toast.show(getApplicationContext(), getString(R.string.video_settings_new_psk_error));
                    }
                    if (_modifyProgressDialog != null)
                        _modifyProgressDialog.dismiss();
                }
            }
        });
        lx520.Set_Password(_videoSettingsConfirmPsk.getText().toString());
    }

    /**
     *  Show Password
     */
    private  boolean psk_open=false;
    View.OnClickListener _videoSettingsShowConfirmPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open=!psk_open;
            if(psk_open)
            {
                _videoSettingsConfirmPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _videoSettingsShowConfirmPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _videoSettingsConfirmPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _videoSettingsShowConfirmPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Show Confirm Password
     */
    private  boolean psk_open1=false;
    View.OnClickListener _videoSettingsShowPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open1=!psk_open1;
            if(psk_open1)
            {
                _videoSettingsPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _videoSettingsShowPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _videoSettingsPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _videoSettingsShowPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Self
     */
    public static DeviceSettings self() {
        return _self;
    }
}



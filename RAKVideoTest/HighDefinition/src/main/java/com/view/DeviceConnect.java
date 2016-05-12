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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.common.paints.Toast;

/**
 * Created by Jean on 2016/1/12.
 */
public class DeviceConnect extends Activity{
    private MainMenuButton _deviceConnect_Back;
    private Button _deviceConnectBtn;
    private EditText _deviceConnectPsk;
    private ImageView _deviceConnectShowPsk;
    private String _password="";
    private static DeviceConnect _self;
    private String _deviceName="";
    private String _deviceId="";
    private String _deviceIp="";
    private Dialog _checkProgressDialog;
    private TextView _deviceForgetPskBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect);
        _self = this;
        _checkProgressDialog = new Dialog(this,R.style.myDialogTheme);
        _deviceConnect_Back=(MainMenuButton)findViewById(R.id.device_connect_back);
        _deviceConnect_Back.setOnClickListener(_deviceConnect_Back_Click);
        _deviceConnectBtn=(Button)findViewById(R.id.device_connect_btn);
        _deviceConnectBtn.setOnClickListener(_deviceConnectBtn_Click);
        _deviceConnectPsk=(EditText)findViewById(R.id.device_connect_psk);
        _deviceConnectShowPsk=(ImageView)findViewById(R.id.device_connect_showpsk);
        _deviceConnectShowPsk.setOnClickListener(_deviceConnectShowPsk_Click);
        _deviceForgetPskBtn=(TextView)findViewById(R.id.device_forget_psk_btn);
        _deviceForgetPskBtn.setOnClickListener(_deviceForgetPskBtn_Click);
    }
    @Override
    protected void onResume() {
        Intent intent = getIntent();
        _deviceName = intent.getStringExtra("devicename");
        _deviceId = intent.getStringExtra("deviceid");
        _deviceIp = intent.getStringExtra("deviceip");
        _deviceConnectPsk.setText(DeviceEntity.getDevicePasswordFromId(_self,_deviceId));
        super.onResume();
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
    View.OnClickListener _deviceConnect_Back_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     *  Connect
     */
    private RemoteTunnel _remoteTunnel=null;
    View.OnClickListener _deviceConnectBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_deviceConnectPsk.getText().toString().equals("")){
                Toast.show(getApplicationContext(), getString(R.string.device_connect_psk_empty));
                return;
            }
            //Save Password
            DeviceEntity.modifyDevicePasswordById(_self, _deviceId, _deviceConnectPsk.getText().toString());
            Intent intent = new Intent();
            intent.setClass(DeviceConnect.this, VideoPlay.class);
            intent.putExtra("devicename", _deviceName);
            intent.putExtra("deviceid", _deviceId);
            intent.putExtra("deviceip", _deviceIp);
            intent.putExtra("devicepsk", _deviceConnectPsk.getText().toString());
            startActivity(intent);

//            checkDevicesPasswordIndicator();
//            if(_deviceIp.equals("127.0.0.1")){
//                if(_remoteTunnel==null)
//                    _remoteTunnel=new RemoteTunnel(getApplicationContext());
//                _remoteTunnel.openTunnel(0, 3333, 80, _deviceId);
//                _remoteTunnel.setOnResultListener(new RemoteTunnel.OnResultListener() {
//                    @Override
//                    public void onResult(int id, String result) {
//                        // TODO Auto-generated method stub
//                        if (result.equals("CONNECT_TIMEOUT") ||
//                                result.equals("NTCS_CLOSED") ||
//                                result.equals("NTCS_UNKNOWN") ||
//                                result.equals("FAILED")) {
//                            Toast.show(getApplicationContext(), getString(R.string.device_connect_network_error));
//                            if (_remoteTunnel != null){
//                                _remoteTunnel.closeTunnels();
//                                _remoteTunnel=null;
//                            }
//                            if (_checkProgressDialog != null)
//                                _checkProgressDialog.dismiss();
//                        } else {
//                            checkDevicesPassword(3333);
//                        }
//                    }
//                });
//            }
//            else{
//                checkDevicesPassword(80);
//            }
        }
    };

    /**
     * Check Password Indicator
     */
    private void checkDevicesPasswordIndicator() {
        LayoutInflater getdeviceDialog_inflater =getLayoutInflater();
        View getdeviceDialog_admin=getdeviceDialog_inflater.inflate(R.layout.dialog_indicator, (ViewGroup) findViewById(R.id.dialog_indicator1));
        TextView dialog_indicator_title =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_title);
        TextView dialog_indicator_text =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_text);
        TextView dialog_indicator_line =(TextView)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_line);
        dialog_indicator_line.setVisibility(View.GONE);
        LinearLayout dialog_indicator_btn =(LinearLayout)getdeviceDialog_admin.findViewById(R.id.dialog_indicator_btn);
        dialog_indicator_btn.setVisibility(View.GONE);
        _checkProgressDialog.setCanceledOnTouchOutside(true);
        _checkProgressDialog.setContentView(getdeviceDialog_admin);
        dialog_indicator_title.setText(getApplication().getString(R.string.device_connect_psk_check_title));
        dialog_indicator_text.setText(R.string.device_connect_psk_check);
        _checkProgressDialog.show();
    }

    /**
     * Check Password
     */
    Lx520 lx520 =null;
    private void checkDevicesPassword(int port){
        lx520 = new Lx520(_deviceIp + ":" + port, _deviceConnectPsk.getText().toString());
        lx520.setOnResultListener(new Lx520.OnResultListener() {
            @Override
            public void onResult(Lx520.Response result) {
                if (result.type == 13) {
                    if (result.statusCode == 200) {
                        String password = DeviceEntity.Find_Str(result.body, DeviceEntity._passwordKey);
                        if (password.equals(_deviceConnectPsk.getText().toString())) {
                            Date curDate = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            String date = sdf.format(curDate);
                            String hour = curDate.getHours() + "";
                            if (hour.length() == 1)
                                hour = "0" + hour;
                            String min = curDate.getMinutes() + "";
                            if (min.length() == 1)
                                min = "0" + min;
                            String sec = curDate.getSeconds() + "";
                            if (sec.length() == 1)
                                sec = "0" + sec;
                            TimeZone tz = TimeZone.getDefault();
                            String tzStr = tz.getDisplayName(false, TimeZone.SHORT);
                            tzStr = tzStr.replace("格林尼治标准时间", "");
                            lx520.Set_SDRecord_Time(date, hour, min, sec, tzStr);//Set Sd Time
                        } else {
                            Toast.show(getApplicationContext(), getString(R.string.modify_device_success_psk_error));
                            if (_checkProgressDialog != null)
                                _checkProgressDialog.dismiss();
                        }
                    } else {
                        Toast.show(getApplicationContext(), getString(R.string.modify_device_success_psk_error));
                        if (_checkProgressDialog != null)
                            _checkProgressDialog.dismiss();
                    }
                }
                if (result.type == 15) {
                    if (result.statusCode == 200) {
                    } else {
                        Toast.show(_self, getApplication().getString(R.string.video_set_sd_time_error));
                    }
                    if (_checkProgressDialog != null)
                        _checkProgressDialog.dismiss();
                    if (_remoteTunnel != null) {
                        _remoteTunnel.closeTunnels();
                        _remoteTunnel = null;
                    }
                    //Save Password
                    DeviceEntity.modifyDevicePasswordById(_self, _deviceId, _deviceConnectPsk.getText().toString());
                    Intent intent = new Intent();
                    intent.setClass(DeviceConnect.this, VideoPlay.class);
                    intent.putExtra("devicename", _deviceName);
                    intent.putExtra("deviceid", _deviceId);
                    intent.putExtra("deviceip", _deviceIp);
                    intent.putExtra("devicepsk", _deviceConnectPsk.getText().toString());
                    startActivity(intent);
                }
            }
        });
        lx520.Get_Password();
    }

    /**
     *  Show password
     */
    private  boolean psk_open=false;
    View.OnClickListener _deviceConnectShowPsk_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            psk_open=!psk_open;
            if(psk_open)
            {
                _deviceConnectPsk.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _deviceConnectShowPsk.setImageResource(R.drawable.psk_open);
            }
            else
            {
                _deviceConnectPsk.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _deviceConnectShowPsk.setImageResource(R.drawable.psk_close);
            }
        }
    };

    /**
     *  Forget password
     */
    View.OnClickListener _deviceForgetPskBtn_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Toast.show(_self,getApplication().getString(R.string.device_connect_forget_psk_indicator));
        }
    };


    /**
     *  Self
     */
    public static DeviceConnect self() {
        return _self;
    }
}



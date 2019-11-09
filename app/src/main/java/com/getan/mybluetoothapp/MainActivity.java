package com.getan.mybluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getan.mybluetoothapp.adapter.MainPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 2;
    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private String[] names ={"按键操控","重力感应"};
    private BluetoothAdapter mBluetoothAdapter;
    public static BluetoothDevice device;
    private BluetoothService mBluetoothService;
    //private BluetoothService mBluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);继承的是activity或FragmentActivity的页面时用
        getSupportActionBar().hide();// 隐藏ActionBar./ 如果继承的不是activity，而是AppCompatActivity在onCreate调用getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏，隐藏状态栏
        setContentView(R.layout.activity_main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);       //强制为横屏,,,,,,,用这行代码设置横屏，发现bug，取消打开蓝牙对话框会出现两次，不知原因
        ButterKnife.bind(this);

        initPager();
        setTabs(mTab,getLayoutInflater(),names);



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null){
            Toast.makeText(this,"你的设备不支持蓝牙,程序将退出",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            initBth();
        }

    }

    private void initBth() {
        if (!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BT);
            //bluetoothAdapter.enable();
            return;
        }
        mBluetoothService = new BluetoothService(this,handler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //case 5:
                    //Toast.makeText(MainActivity.this,msg.what,Toast.LENGTH_SHORT).show();
                   // return;
                default:
                    return;
            }
            //Toast.makeText(MainActivity.this.getApplicationContext(), msg.getData().getString("toast"),Toast.LENGTH_SHORT).show();
        }
    };

    private void setTabs(TabLayout tabLayout, LayoutInflater layoutInflater, String[] names) {
        for (int i = 0; i <names.length ; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View inflate = layoutInflater.inflate(R.layout.item_tab, null);
            tab.setCustomView(inflate);
            TextView tv = inflate.findViewById(R.id.txt_tab);
            tv.setText(names[i]);
            mTab.addTab(tab);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_CANCELED){
                    //结果码为0时，弹出提示，蓝牙未连接，退出程序
                    Toast.makeText(this, R.string.Bluetooth_untapped, Toast.LENGTH_SHORT).show();
                    //finish();
                }
                if ((resultCode==RESULT_OK)&&(this.mBluetoothService == null)){
                    //结果码为1时，开启蓝牙的服务类，用来搜索蓝牙设备
                    mBluetoothService = new BluetoothService(this, this.handler);
                    Toast.makeText(this, R.string.Bluetooth_opened, Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (resultCode == -1){
                    String string = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    device = this.mBluetoothAdapter.getRemoteDevice(string);
                    System.out.println(string);
                    Log.i("MainActivity","蓝牙地址为:"+string);
                    System.out.println(data);
                    System.out.println(device);
                    mBluetoothService.connect(device);
                    //sensorbtn.setClickable(true);
                }
                break;
            default:
                break;
        }

    }

    private void initPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(adapter);
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 取消平滑切换
                mViewpager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTab));
    }



}

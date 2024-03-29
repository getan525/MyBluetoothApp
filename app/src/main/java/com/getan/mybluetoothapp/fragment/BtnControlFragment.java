package com.getan.mybluetoothapp.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.getan.mybluetoothapp.BluetoothService;
import com.getan.mybluetoothapp.DeviceListActivity;
import com.getan.mybluetoothapp.MainActivity;
import com.getan.mybluetoothapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2019/8/18.
 * 邮箱：405181076@qq.com
 */

public class BtnControlFragment extends Fragment implements View.OnTouchListener{
    Unbinder mUnbinder;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothService mBluetoothService;

    private byte[] byte1 = "ON1".getBytes();
    private byte[] byte2 = "ON2".getBytes();
    private byte[] byte3 = "ON3".getBytes();
    private byte[] byte4 = "ON4".getBytes();
    private byte[] byte5 = "ON5".getBytes();
    private byte[] byte6 = "ON6".getBytes();
    private byte[] byte7 = "ON7".getBytes();
    private byte[] byte8 = "ON8".getBytes();
    private byte[] byte9 = "ON9".getBytes();
    private byte[] bytedown = "ONB".getBytes();
    private byte[] bytef = "ONF".getBytes();
    private byte[] byteleft = "ONC".getBytes();
    private byte[] byteright = "OND".getBytes();
    private byte[] byteup = "ONA".getBytes();
    private byte[] bytestop = "ONE".getBytes();
    @BindView(R.id.btn_go)
    Button mBtnGo;
    @BindView(R.id.btn_left)
    Button mBtnLeft;
    @BindView(R.id.btn_stop)
    Button mBtnStop;
    @BindView(R.id.btn_right)
    Button mBtnRight;
    @BindView(R.id.btn_back)
    Button mBtnBack;
    @BindView(R.id.btn_list)
    Button mBtnList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_btncontrol, null);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()){
                    Toast.makeText(getContext(), "您还没打开蓝牙", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getContext(), DeviceListActivity.class);
                    getActivity().startActivityForResult(intent, 1);
                }
            }
        });
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().registerReceiver(mReceiver, filter);
        getActivity().registerReceiver(mReceiver, filter1);

        mBtnGo.setOnTouchListener(this);
        mBtnBack.setOnTouchListener(this);
        mBtnLeft.setOnTouchListener(this);
        mBtnRight.setOnTouchListener(this);
        mBtnStop.setOnTouchListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        if (BluetoothService.connectedThread != null){
            mBtnGo.setEnabled(true);
            mBtnBack.setEnabled(true);
            mBtnLeft.setEnabled(true);
            mBtnRight.setEnabled(true);
            mBtnStop.setEnabled(true);
        }else {
            //Toast.makeText(getContext(), "蓝牙未连接", Toast.LENGTH_SHORT).show();
            mBtnGo.setEnabled(false);
            mBtnBack.setEnabled(false);
            mBtnLeft.setEnabled(false);
            mBtnRight.setEnabled(false);
            mBtnStop.setEnabled(false);
        }
    }
    /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                mBtnGo.setEnabled(true);
                mBtnBack.setEnabled(true);
                mBtnLeft.setEnabled(true);
                mBtnRight.setEnabled(true);
                mBtnStop.setEnabled(true);
                Toast.makeText(getContext(), "蓝牙已连接", Toast.LENGTH_SHORT).show();
            }else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
                mBtnGo.setEnabled(false);
                mBtnBack.setEnabled(false);
                mBtnLeft.setEnabled(false);
                mBtnRight.setEnabled(false);
                mBtnStop.setEnabled(false);
                Toast.makeText(getContext(), "蓝牙没连接", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


            switch (v.getId()){
                case R.id.btn_go:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            BluetoothService.connectedThread.write(byteup);
                            break;
                        case MotionEvent.ACTION_UP:
                            BluetoothService.connectedThread.write("ONF".getBytes());
                            break;
                    }
                    break;
                case R.id.btn_back:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            BluetoothService.connectedThread.write(bytedown);
                            break;
                        case MotionEvent.ACTION_UP:
                            BluetoothService.connectedThread.write("ONF".getBytes());
                            break;
                    }
                    break;
                case R.id.btn_left:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            BluetoothService.connectedThread.write(byteleft);
                            break;
                        case MotionEvent.ACTION_UP:
                            BluetoothService.connectedThread.write("ONF".getBytes());
                            break;
                    }
                    break;
                case R.id.btn_right:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            BluetoothService.connectedThread.write(byteright);
                            break;
                        case MotionEvent.ACTION_UP:
                            BluetoothService.connectedThread.write("ONF".getBytes());
                            break;
                    }
                    break;
                case R.id.btn_stop:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            BluetoothService.connectedThread.write(bytestop);
                            break;
                        case MotionEvent.ACTION_UP:
                            BluetoothService.connectedThread.write("ONF".getBytes());
                            break;
                    }
                    break;
            }

        return false;
    }
}

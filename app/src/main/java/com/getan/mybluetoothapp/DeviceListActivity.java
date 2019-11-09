package com.getan.mybluetoothapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/8/20.
 * 邮箱：405181076@qq.com
 */

public class DeviceListActivity extends Activity implements AdapterView.OnItemClickListener{
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    @BindView(R.id.btn_discovery)
    Button mBtnDiscovery;

    @BindView(R.id.bth_list_paired)
    ListView mBthListPaired;
    @BindView(R.id.bth_list_new)
    ListView mBthListNew;
    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;
    private BluetoothAdapter mBluetoothAdapter;
    //private String[] btlist = new String[]{"s;j;a;l",";ls;j;gj",";lajljih"};
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayAdapter<String> mArrayAdapter1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt_list);
        ButterKnife.bind(this);


        //MyAdapter adapter = new MyAdapter();
        //MyAdapter adapter = new ArrayAdapter<String>(R.layout.support_simple_spinner_dropdown_item);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBthListPaired.setAdapter(mArrayAdapter);

        mArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBthListNew.setAdapter(mArrayAdapter1);

        //初始化已绑定设备列表
        initBondedDevicesView();
        //初始化新的可用设备
        initNewDevicesView();

        //列表条目点击事件
        initItemClick();

        mBtnDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.startDiscovery();
                mProgressbar.setVisibility(View.VISIBLE);
            }
        });

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        IntentFilter filter2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        registerReceiver(mReceiver,filter2);
    }


    private void initItemClick() {
        mBthListPaired.setOnItemClickListener(this);
        mBthListNew.setOnItemClickListener(this);

       /* mBthListPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
    }

    private void initNewDevicesView() {
       /* mBluetoothAdapter.startDiscovery();
        mProgressbar.setVisibility(View.VISIBLE);*/

            if (mBluetoothAdapter.isDiscovering()) {
                //判断蓝牙是否正在扫描，如果是调用取消扫描方法；如果不是，则开始扫描
                mBluetoothAdapter.cancelDiscovery();
            } else{
                mBluetoothAdapter.startDiscovery();
                mProgressbar.setVisibility(View.VISIBLE);
            }



    }

    private void initBondedDevicesView() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            Toast.makeText(this, "暂无已配对设备", Toast.LENGTH_SHORT).show();
        }

    }


    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter1.add(device.getName() + "\n" + device.getAddress());
                mArrayAdapter1.notifyDataSetChanged();
                //Toast.makeText(DeviceListActivity.this, "发现了新设备", Toast.LENGTH_SHORT).show();
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                mProgressbar.setVisibility(View.INVISIBLE);
            }
        }
    };

    protected void onDestroy(){
        super.onDestroy();//解除注册
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.cancelDiscovery();
        String s = ((TextView) view).getText().toString();
        String substring = s.substring(s.length()-17);//截取字符串的意思，蓝牙地址的length刚好17位，s.length-17即代表从第0位截取后面的所有
        Intent intent = new Intent();
        //Intent intent = new Intent(DeviceListActivity.this,ConnectActivity.class);
        intent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, substring);
        setResult(-1, intent);
        //startActivity(intent);
        finish();
    }

    /*private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }*/
}

package com.getan.mybluetoothapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.getan.mybluetoothapp.fragment.BtnControlFragment;
import com.getan.mybluetoothapp.fragment.SensorControlFragment;

/**
 * Created by Administrator on 2019/8/18.
 * 邮箱：405181076@qq.com
 */

public class MainPagerAdapter extends FragmentPagerAdapter{
    public MainPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new BtnControlFragment();
                break;
            case 1:
                fragment = new SensorControlFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

package com.example.andriod.daigoutally;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class MyLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //mapView 销毁后不在处理新接收的位置
        if (location == null){
            return;
        }
    }
}

package com.example.andriod.daigoutally;

import android.content.Context;
import android.location.Location;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationHelper {
    Location mLocation=null;
    private LocationClient mLocationClient;

    public Location getLocation() {
        return mLocation;
    }

    public LocationHelper(Context context, BDAbstractLocationListener listener) {
        mLocationClient = new LocationClient(context.getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setPriority(LocationClientOption.GpsFirst);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(listener);
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    public void Stop(){
        mLocationClient.stop();
    }
}


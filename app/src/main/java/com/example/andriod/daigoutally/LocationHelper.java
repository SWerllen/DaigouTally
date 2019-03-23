package com.example.andriod.daigoutally;

import android.content.Context;
import android.location.Location;
import android.media.ExifInterface;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public Location getLocationFromFile(String path){
        @SuppressWarnings("unused")
        String context ;
        Location location=null;
        try {
            ExifInterface exifInterface=new ExifInterface(path);
            String latValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String datetime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
            String latRef   = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String lngValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String lngRef   = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
                try {
                    LatLng srcLatlng=new LatLng(convertRationalLatLonToFloat(latValue, latRef),convertRationalLatLonToFloat(lngValue, lngRef));
                    CoordinateConverter converter  = new CoordinateConverter();
                    converter.from(CoordinateConverter.CoordType.GPS);
                    converter.coord(srcLatlng);
                    LatLng desLatLng = converter.convert();
                    location=new Location("dummyprovider");
                    location.setLatitude(desLatLng.latitude);
                    location.setLongitude(desLatLng.longitude);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            if(datetime!=null){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                Date date = null;
                date = formatter.parse(datetime);
                location.setTime(date.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
    private static float convertRationalLatLonToFloat(
            String rationalString, String ref) {
        try {
            String[] parts = rationalString.split(",");

            String[] pair;
            pair = parts[0].split("/");
            double degrees = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            pair = parts[1].split("/");
            double minutes = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            pair = parts[2].split("/");
            double seconds = Double.parseDouble(pair[0].trim())
                    / Double.parseDouble(pair[1].trim());

            double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
            if ((ref.equals("S") || ref.equals("W"))) {
                return (float) -result;
            }
            return (float) result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }
}


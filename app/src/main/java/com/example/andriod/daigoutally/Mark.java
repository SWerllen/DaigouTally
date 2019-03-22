package com.example.andriod.daigoutally;

import android.graphics.Bitmap;
import android.location.Location;

import java.io.File;
import java.util.Date;

public class Mark {
    public long id=-1;
    public String description="";
    public Date date=new Date();
    public Bitmap map;
    public String filepath;
    public boolean isAll=false;
    public Location location;

    public boolean loadbyheight(int height){
        if(map!=null&&map.getHeight()>0) return true;
        File f=new File(filepath);
        if(f.exists()){
            map=BitmapThumb.decodeSampledBitmapFromFdByHeight(filepath,height);
            return true;
        }
        return false;
    }
    public void unload(){
        map=null;
    }
}

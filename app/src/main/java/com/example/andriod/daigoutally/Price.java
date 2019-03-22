package com.example.andriod.daigoutally;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Price {
    double nums;
    String unit;        //"CNY" "USD"

    Price(String price){
        Pattern pattern = Pattern.compile("[A-Z]+");
        Matcher matcher =pattern.matcher(price);
        if(matcher.find())
            unit=matcher.group();
        pattern = Pattern.compile("[0-9\\.]+");
        Matcher ma2=pattern.matcher(price);
        if(ma2.find())
            nums=Double.valueOf(ma2.group());
    }

    String getString(){
        StringBuilder sb=new StringBuilder();
        sb.append(unit);
        sb.append(" ");
        sb.append(nums);
        return sb.toString();
    }

    public void getPrice(String price){
        Pattern pattern = Pattern.compile("[A-Z]+");
        Matcher matcher =pattern.matcher(price);
        if(matcher.find())
            unit=matcher.group();
        pattern = Pattern.compile("[0-9\\.]+");
        Matcher ma2=pattern.matcher(price);
        if(ma2.find())
            nums=Double.valueOf(ma2.group());
    }
}

package com.example.andriod.daigoutally;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;

public class CheckInput {

    static String checkCommodity(String name){
        if(name.equals("")){
            return "The name should not be empty!";
        }
        else if(name.length()>40){
            return "The name should not be more than 40 characters!";
        }
        return "yes";
    }
    static String checkQuantity(String quantity){
        if(quantity.equals("")){
            return "The quantity should not be empty!";
        }
        int quan=Integer.valueOf(quantity);
        if(quan<=0){
            return "he quantity should be more than 0!";
        }
        if(quan>1000){
            return "he quantity should be less than 1000!";
        }
        return "yes";
    }

    static String checkPrice(String price){
        if(price.equals("")){
            return "The price should not be empty!";
        }
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(price);
        if(match.matches()==false){
            return "The price has a maximum of two decimal places!";
        }
        double pri=Double.valueOf(price);
        if(pri<0){
            return "The price should be more than 0!";
        }
        return "yes";
    }

    static String checkDiscount(String discount){
        if(discount.equals("")){
            return "The discount should not be empty!";
        }
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(discount);
        if(match.matches()==false){
            return "The discount has a maximum of two decimal places!";
        }
        double num=Double.valueOf(discount);
        if(num<0){
            return "The discount should not be less than 0!";
        }
        return "yes";
    }

    static String checkEXRate(String ex){
        if(ex.equals("")){
            return "The exchange rate should not be empty!";
        }
        double num=Double.valueOf(ex);
        if(num<0){
            return "The exchange rate should not be less than 0!";
        }
        return "yes";
    }

    static String checkBuyerName(String name){
        if(name.equals("")){
            return "The buyer's name should not be empty!";
        }
        if(name.length()>30){
            return "The buyer's name should be less than 30!";
        }
        return "yes";
    }
    static  String checkLocation(String location){
        if(location.equals("")){
            return "The location should not be empty!";
        }
        return "yes";
    }
    static  String checkPhone(String phone){
        if(phone.equals("")){
            return "The phone number should not be empty!";
        }
        return "yes";
    }
}

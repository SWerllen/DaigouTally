package com.example.andriod.daigoutally;

import java.util.Date;

public class Stock {
    public long id=-1;
    public String commodity="";
    public Price showprice=new Price("1.0CNY");
    public int quantity=0;
    public Date date=new Date();
    public double discount=0.55;
    public double exrate=6.65;      //exchange rate= n defaultUnit/showprice's Unit
}

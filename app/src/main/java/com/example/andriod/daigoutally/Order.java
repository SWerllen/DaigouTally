package com.example.andriod.daigoutally;

import java.util.Date;

public class Order {
    enum ORDERSTATE{NOSHIP,WAITRECIEVE,RECIEVED};
    public long id=-1;
    public String commodity="Basketball";
    public String buyer="Werllen";
    public Price sellprice=new Price("222.0CNY");
    public int quantity=0;
    public Date selltime=new Date();
    public String location="11";
    public String phone="00000000000";
    public ORDERSTATE state=ORDERSTATE.NOSHIP;
}

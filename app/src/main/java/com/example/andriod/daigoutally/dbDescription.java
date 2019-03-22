package com.example.andriod.daigoutally;

public class dbDescription {
    public static final class OrderTable{
        public static final String NAME="Orders";
        public static final class Cols {
            public static final String ID= "id";
            public static final String COMMODITY= "commodity";
            public static final String QUANTITY= "quantity";
            public static final String BUYER = "buyer";
            public static final String SELLPRICE = "sellprice";
            public static final String SELLTIME = "selltime";
            public static final String LOCATION = "location";
            public static final String PHONE = "phone";
            public static final String STATE = "state";

            public static final String IDDEC= " INTEGER PRIMARY KEY AUTOINCREMENT ";
            public static final String COMMODITYDEC= " TEXT ";
            public static final String QUANTITYDEC= " INTEGER ";
            public static final String BUYERDEC = " TEXT ";
            public static final String SELLPRICEDEC = " TEXT ";
            public static final String SELLTIMEDEC = " INTEGER ";
            public static final String LOCATIONDEC = " TEXT ";
            public static final String PHONEDEC = " TEXT ";
            public static final String STATEDEC = " INTEGER ";
        }
    }
    public static final class StockTable{
        public static final String NAME="Stocks";
        public static final class Cols {
            public static final String ID= "id";
            public static final String NAME= "name";
            public static final String SHOWPRICE = "price";
            public static final String QUANTITY = "quantity";
            public static final String DISCOUNT = "discount";
            public static final String BUYTIME = "buytime";
            public static final String EXRATE = "exrate";

            public static final String IDDEC=" INTEGER PRIMARY KEY AUTOINCREMENT ";
            public static final String NAMEDEC= " TEXT ";
            public static final String SHOWPRICEDEC = " TEXT  ";  //num+unit
            public static final String QUANTITYDEC = " INTEGER ";
            public static final String DISCOUNTDEC = " DOUBLE ";
            public static final String BUYTIMEDEC = " INTEGER ";
            public static final String EXRATEDEC = " DOUBLE ";
        }
    }
    public static final class MarkTable{
        public static final String NAME="Marks";
        public static final class Cols {
            public static final String ID= "id";
            public static final String DESCRIPTION= "description";
            public static final String TIME= "time";
            public static final String FILE= "filepath";

            public static final String IDDEC= " INTEGER PRIMARY KEY AUTOINCREMENT ";
            public static final String DESCRIPTIONDEC= " TEXT ";
            public static final String TIMEDEC = " INTEGER ";
            public static final String FILEDEC = " TEXT ";
        }
    }
}

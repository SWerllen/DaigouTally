package com.example.andriod.daigoutally;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.andriod.daigoutally.dbDescription.OrderTable;
import com.example.andriod.daigoutally.dbDescription.StockTable;

public class MyDataHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Base.db";

    public MyDataHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + StockTable.NAME + "(" +
                StockTable.Cols.ID + StockTable.Cols.IDDEC+ ", " +
                StockTable.Cols.NAME + StockTable.Cols.NAMEDEC +", " +
                StockTable.Cols.QUANTITY + StockTable.Cols.QUANTITYDEC +", " +
                StockTable.Cols.DISCOUNT + StockTable.Cols.DISCOUNTDEC +","+
                StockTable.Cols.SHOWPRICE+ StockTable.Cols.SHOWPRICEDEC +","+
                StockTable.Cols.EXRATE+ StockTable.Cols.EXRATEDEC +","+
                StockTable.Cols.BUYTIME +StockTable.Cols.BUYTIMEDEC +")");

        db.execSQL("CREATE TABLE " + OrderTable.NAME + "(" +
                OrderTable.Cols.ID + OrderTable.Cols.IDDEC+ ", " +
                OrderTable.Cols.COMMODITY + OrderTable.Cols.COMMODITYDEC +", " +
                OrderTable.Cols.QUANTITY + OrderTable.Cols.QUANTITYDEC +", " +
                OrderTable.Cols.SELLTIME + OrderTable.Cols.SELLTIMEDEC +","+
                OrderTable.Cols.BUYER+ OrderTable.Cols.BUYERDEC +","+
                OrderTable.Cols.LOCATION +OrderTable.Cols.LOCATIONDEC +","+
                OrderTable.Cols.PHONE +OrderTable.Cols.PHONEDEC +","+
                OrderTable.Cols.STATE +OrderTable.Cols.STATEDEC +","+
                OrderTable.Cols.SELLPRICE +OrderTable.Cols.SELLPRICEDEC +")");

        db.execSQL("CREATE TABLE " + dbDescription.MarkTable.NAME + "(" +
                dbDescription.MarkTable.Cols.ID + dbDescription.MarkTable.Cols.IDDEC+ ", " +
                dbDescription.MarkTable.Cols.FILE + dbDescription.MarkTable.Cols.FILEDEC +", " +
                dbDescription.MarkTable.Cols.TIME + dbDescription.MarkTable.Cols.TIMEDEC +", " +
                dbDescription.MarkTable.Cols.LATITUDE + dbDescription.MarkTable.Cols.LATITUDEDEC +", " +
                dbDescription.MarkTable.Cols.LONGITUDE + dbDescription.MarkTable.Cols.LONGITUDEDEC +", " +
                dbDescription.MarkTable.Cols.DESCRIPTION + dbDescription.MarkTable.Cols.DESCRIPTIONDEC  +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

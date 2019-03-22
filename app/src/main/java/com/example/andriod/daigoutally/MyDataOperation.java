package com.example.andriod.daigoutally;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import com.example.andriod.daigoutally.dbDescription.OrderTable;
import com.example.andriod.daigoutally.dbDescription.StockTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDataOperation {
    public SQLiteDatabase mDataBase;
    private Context context;


    public boolean isexist(String name){
        String que=" select count(*) from sqlite_master where type='table' and name='"+name+"'";
        Cursor cursor=mDataBase.rawQuery(que.toString(),null);
        if(cursor.moveToNext()){
            int count = cursor.getInt(0);
            if(count>0){
                return true;
            }
        }
        return false;
    }
    MyDataOperation(Context context){
        this.context = context;
        mDataBase=new MyDataHelper(context).getWritableDatabase();
        if(!isexist(StockTable.NAME)) {
            mDataBase.execSQL("CREATE TABLE " + StockTable.NAME + "(" +
                    StockTable.Cols.ID + StockTable.Cols.IDDEC+ ", " +
                    StockTable.Cols.NAME + StockTable.Cols.NAMEDEC +", " +
                    StockTable.Cols.QUANTITY + StockTable.Cols.QUANTITYDEC +", " +
                    StockTable.Cols.DISCOUNT + StockTable.Cols.DISCOUNTDEC +","+
                    StockTable.Cols.SHOWPRICE+ StockTable.Cols.SHOWPRICEDEC +","+
                    StockTable.Cols.EXRATE+ StockTable.Cols.EXRATEDEC +","+
                    StockTable.Cols.BUYTIME +StockTable.Cols.BUYTIMEDEC +")");
        }
        if(!isexist(OrderTable.NAME)){
            mDataBase.execSQL("CREATE TABLE " + OrderTable.NAME + "(" +
                    OrderTable.Cols.ID + OrderTable.Cols.IDDEC+ ", " +
                    OrderTable.Cols.COMMODITY + OrderTable.Cols.COMMODITYDEC +", " +
                    OrderTable.Cols.QUANTITY + OrderTable.Cols.QUANTITYDEC +", " +
                    OrderTable.Cols.SELLTIME + OrderTable.Cols.SELLTIMEDEC +","+
                    OrderTable.Cols.BUYER+ OrderTable.Cols.BUYERDEC +","+
                    OrderTable.Cols.LOCATION +OrderTable.Cols.LOCATIONDEC +","+
                    OrderTable.Cols.PHONE +OrderTable.Cols.PHONEDEC +","+
                    OrderTable.Cols.STATE +OrderTable.Cols.STATEDEC +","+
                    OrderTable.Cols.SELLPRICE +OrderTable.Cols.SELLPRICEDEC +")");
        }
        if(!isexist(dbDescription.MarkTable.NAME)){
            mDataBase.execSQL("CREATE TABLE " + dbDescription.MarkTable.NAME + "(" +
                    dbDescription.MarkTable.Cols.ID + dbDescription.MarkTable.Cols.IDDEC+ ", " +
                    dbDescription.MarkTable.Cols.FILE + dbDescription.MarkTable.Cols.FILEDEC +", " +
                    dbDescription.MarkTable.Cols.TIME + dbDescription.MarkTable.Cols.TIMEDEC +", " +
                    dbDescription.MarkTable.Cols.LATITUDE + dbDescription.MarkTable.Cols.LATITUDEDEC +", " +
                    dbDescription.MarkTable.Cols.LONGITUDE + dbDescription.MarkTable.Cols.LONGITUDEDEC +", " +
                    dbDescription.MarkTable.Cols.DESCRIPTION + dbDescription.MarkTable.Cols.DESCRIPTIONDEC  +")");
        }
    }
    public boolean addStock(Stock c) {
        if(!isexist(StockTable.NAME)) return false;
        ContentValues values = getContentValues(c);
        Log.i("DATAOP:",String.format("add stock %d",c.id));
        return mDataBase.insert(StockTable.NAME, null, values)!=-1;
    }

    public boolean removeStock(Stock c) {
        int state=mDataBase.delete(StockTable.NAME,
                StockTable.Cols.ID + " = ?",
                new String[] {String.format("%d",c.id)});
        if(state<0) return false;
        Log.i("DATAOP:",String.format("remove stock %d",c.id));
        return true;
    }
    public boolean removeStockbyid(long id) {
        int state=mDataBase.delete(StockTable.NAME,
                StockTable.Cols.ID + " = ?",
                new String[] {String.format("%d",id)});
        if(state<0) return false;
        Log.i("DATAOP:",String.format("remove stock %d",id));
        return true;
    }

    public boolean updateStock(Stock c) {
        String idString = String.format("%d",c.id);
        ContentValues values = getContentValues(c);

        int state=mDataBase.update(StockTable.NAME, values,
                StockTable.Cols.ID + " = ?",
                new String[] {idString});
        if(state<0) return false;
        Log.i("DATAOP:",String.format("update stock %d",c.id));
        return true;
    }

    public boolean addOrder(Order c) {
        if(!isexist(OrderTable.NAME)) return false;
        ContentValues values = getContentValues(c);
        return mDataBase.insert(OrderTable.NAME, null, values)!=-1;
    }

    public boolean removeOrder(Order c) {
        int state=mDataBase.delete(OrderTable.NAME,
                OrderTable.Cols.ID + " = ?",
                new String[] {String.format("%d",c.id)});
        if(state<0) return false;
        Log.i("DATAOP:",String.format("remove order %s",c.commodity));
        return true;
    }
    public boolean removeOrderbyid(long id) {
        int state=mDataBase.delete(OrderTable.NAME,
                OrderTable.Cols.ID + " = ?",
                new String[] {String.format("%d",id)});
        if(state<0) return false;
        return true;
    }

    public boolean updateOrder(Order c) {
        String idString = String.format("%d",c.id);
        ContentValues values = getContentValues(c);

        int state=mDataBase.update(OrderTable.NAME, values,
                OrderTable.Cols.ID + " = ?",
                new String[] {idString});
        if(state<0) return false;
        return true;
    }

    public boolean addMark(Mark m) {
        if(!isexist(dbDescription.MarkTable.NAME)) return false;
        ContentValues values = getContentValues(m);
        return mDataBase.insert(dbDescription.MarkTable.NAME, null, values)!=-1;
    }

    public boolean removeMark(Mark m) {
        int state=mDataBase.delete(dbDescription.MarkTable.NAME,
                dbDescription.MarkTable.Cols.ID + " = ?",
                new String[] {String.format("%d",m.id)});
        if(state<0) return false;
        return true;
    }
    public boolean removeMarkbyid(long id) {
        int state=mDataBase.delete(dbDescription.MarkTable.NAME,
                dbDescription.MarkTable.Cols.ID + " = ?",
                new String[] {String.format("%d",id)});
        if(state<0) return false;
        return true;
    }

    public boolean updateMark(Mark m) {
        String idString = String.format("%d",m.id);
        ContentValues values = getContentValues(m);

        int state=mDataBase.update(dbDescription.MarkTable.NAME, values,
                dbDescription.MarkTable.Cols.ID + " = ?",
                new String[] {idString});
        if(state<0) return false;
        return true;
    }

    private StockCursorWrapper queryStocks(String whereClause, String[] whereArgs) {
        if(!mDataBase.isOpen()) open();
        Cursor cursor = mDataBase.query(
                StockTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                StockTable.Cols.BUYTIME+" DESC");
        return new StockCursorWrapper(cursor);
    }
    private OrderCursorWrapper queryOrders(String whereClause, String[] whereArgs) {
        if(!mDataBase.isOpen()) open();
        Cursor cursor = mDataBase.query(
                OrderTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                OrderTable.Cols.STATE+","+OrderTable.Cols.SELLTIME+" DESC");
        return new OrderCursorWrapper(cursor);
    }
    private MarkCursorWrapper queryMark(String whereClause, String[] whereArgs) {
        if(!mDataBase.isOpen()) open();
        Cursor cursor = mDataBase.query(
                dbDescription.MarkTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                dbDescription.MarkTable.Cols.TIME+" DESC");
        return new MarkCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(StockTable.Cols.NAME, stock.commodity);
        values.put(StockTable.Cols.QUANTITY, stock.quantity);
        values.put(StockTable.Cols.DISCOUNT, stock.discount);
        values.put(StockTable.Cols.SHOWPRICE,stock.showprice.getString());
        values.put(StockTable.Cols.BUYTIME, stock.date.getTime());
        values.put(StockTable.Cols.EXRATE, stock.exrate);
        return values;
    }
    private static ContentValues getContentValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.Cols.COMMODITY, order.commodity);
        values.put(dbDescription.OrderTable.Cols.QUANTITY, order.quantity);
        values.put(OrderTable.Cols.BUYER, order.buyer);
        values.put(OrderTable.Cols.LOCATION, order.location);
        values.put(OrderTable.Cols.SELLPRICE, order.sellprice.getString());
        values.put(OrderTable.Cols.SELLTIME, order.selltime.getTime());
        values.put(OrderTable.Cols.PHONE,order.phone);
        values.put(OrderTable.Cols.STATE,order.state.ordinal());
        return values;
    }
    private static ContentValues getContentValues(Mark mark) {
        ContentValues values = new ContentValues();
        values.put(dbDescription.MarkTable.Cols.DESCRIPTION, mark.description);
        values.put(dbDescription.MarkTable.Cols.TIME, mark.date.getTime());
        values.put(dbDescription.MarkTable.Cols.FILE, mark.filepath);
        values.put(dbDescription.MarkTable.Cols.LONGITUDE,mark.location.getLongitude());
        values.put(dbDescription.MarkTable.Cols.LATITUDE,mark.location.getLatitude());
        return values;
    }

    private class StockCursorWrapper extends CursorWrapper {
        public StockCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Stock getStock() {
            //CursorWrapper继承Cursor的全部方法
            long stockid = getLong(getColumnIndex(StockTable.Cols.ID));
            String commodity = getString(getColumnIndex(StockTable.Cols.NAME));
            int quantity=getInt(getColumnIndex(StockTable.Cols.QUANTITY));
            long date = getLong(getColumnIndex(StockTable.Cols.BUYTIME));
            double discount = getDouble(getColumnIndex(StockTable.Cols.DISCOUNT));
            String price = getString(getColumnIndex(StockTable.Cols.SHOWPRICE));
            double exrate = getDouble(getColumnIndex(StockTable.Cols.EXRATE));

            Stock stock = new Stock();
            stock.commodity=commodity;
            stock.discount=discount;
            stock.date=new Date(date);
            stock.id=stockid;
            stock.showprice.getPrice(price);
            stock.exrate=exrate;
            stock.quantity=quantity;

            return stock;
        }
    }

    public List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();

        StockCursorWrapper cursor = queryStocks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                stocks.add(cursor.getStock());
                cursor.moveToNext();
            }
        } finally {
            //使用后关闭
            cursor.close();
        }

        return stocks;
    }
    public List<Stock> getStocks(String[] target) {
        if(target.length==0) return getStocks();

        List<Stock> stocks = new ArrayList<>();
        StringBuilder query=new StringBuilder();
        for(int i=0;i<target.length;i++){
            query.append(StockTable.Cols.NAME+" like '%"+target[i]+"%'"+" and ");
        }
        query.delete(query.length()-4,query.length());

        StockCursorWrapper cursor = queryStocks(query.toString(), null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                stocks.add(cursor.getStock());
                cursor.moveToNext();
            }
        } finally {
            //使用后关闭
            cursor.close();
        }

        return stocks;
    }
    private class OrderCursorWrapper extends CursorWrapper {
        public OrderCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Order getOrder() {
            //CursorWrapper继承Cursor的全部方法
            long orderid = getLong(getColumnIndex(OrderTable.Cols.ID));
            String commodity = getString(getColumnIndex(OrderTable.Cols.COMMODITY));
            long selltime = getLong(getColumnIndex(OrderTable.Cols.SELLTIME));
            int quantity  = getInt(getColumnIndex(OrderTable.Cols.QUANTITY));
            String location = getString(getColumnIndex(OrderTable.Cols.LOCATION));
            String sellprice = getString(getColumnIndex(OrderTable.Cols.SELLPRICE));
            String buyer = getString(getColumnIndex(OrderTable.Cols.BUYER));
            String phone = getString(getColumnIndex(OrderTable.Cols.PHONE));
            int state = getInt(getColumnIndex(OrderTable.Cols.STATE));

            Order order = new Order();
            order.id=orderid;
            order.commodity=commodity;
            order.location=location;
            order.selltime=new Date(selltime);
            order.quantity=quantity;
            order.sellprice.getPrice(sellprice);
            order.buyer=buyer;
            order.phone=phone;
            order.state= Order.ORDERSTATE.values()[state];

            return order;
        }
    }
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();

        OrderCursorWrapper cursor = queryOrders(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                orders.add(cursor.getOrder());
                cursor.moveToNext();
            }
        } finally {
            //使用后关闭
            cursor.close();
        }

        return orders;
    }
    public List<Order> getOrders(String[] target) {
        List<Order> orders = new ArrayList<>();
        StringBuilder query=new StringBuilder();
        for(int i=0;i<target.length;i++){
            query.append(OrderTable.Cols.COMMODITY+" like '%"+target[i]+"%'"+" or ");
            query.append(OrderTable.Cols.BUYER+" like '%"+target[i]+"%'"+" or ");
            query.append(OrderTable.Cols.LOCATION+" like '%"+target[i]+"%'"+" and ");
        }
        query.delete(query.length()-4,query.length());

        OrderCursorWrapper cursor = queryOrders(query.toString(), null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                orders.add(cursor.getOrder());
                cursor.moveToNext();
            }
        } finally {
            //使用后关闭
            cursor.close();
        }

        return orders;
    }
    private class MarkCursorWrapper extends CursorWrapper {
        public MarkCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Mark getMark() {
            //CursorWrapper继承Cursor的全部方法
            long markid=getLong(getColumnIndex(dbDescription.MarkTable.Cols.ID));
            String description=getString(getColumnIndex(dbDescription.MarkTable.Cols.DESCRIPTION));
            long date = getLong(getColumnIndex(dbDescription.MarkTable.Cols.TIME));
            String filepath= getString(getColumnIndex(dbDescription.MarkTable.Cols.FILE));
            Location location=new Location("dummyprovider");
            location.setLatitude(getDouble(getColumnIndex(dbDescription.MarkTable.Cols.LATITUDE)));
            location.setLongitude(getDouble(getColumnIndex(dbDescription.MarkTable.Cols.LONGITUDE)));

            Mark mark=new Mark();
            mark.date=new Date(date);
            mark.id=markid;
            mark.filepath=filepath;
            mark.description=description;
            mark.location=location;
            return mark;
        }
    }

    public List<Mark> getmarks() {
        List<Mark> marks = new ArrayList<>();
        open();
        MarkCursorWrapper cursor = queryMark(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                marks.add(cursor.getMark());
                cursor.moveToNext();
            }
        } finally {
            //使用后关闭
            cursor.close();
        }

        return marks;
    }

    public List<Mark> getmarks(final String[] target) {
        List<Mark> marks = new ArrayList<>();
        open();
        StringBuilder query=new StringBuilder();
        for(int i=0;i<target.length;i++){
            query.append(dbDescription.MarkTable.Cols.DESCRIPTION+" like '%"+target[i]+"%'"+" and ");
        }
        query.delete(query.length()-4,query.length());

        MarkCursorWrapper cursor = queryMark(query.toString(), null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                marks.add(cursor.getMark());
                cursor.moveToNext();
            }
        } finally {
            //使用后关闭
            cursor.close();
        }

        return marks;
    }

    public void close(){
        if(mDataBase.isOpen()) mDataBase.close();
    }
    public void open(){
        if(mDataBase!=null&&mDataBase.isOpen()) mDataBase.close();
        mDataBase=new MyDataHelper(context).getWritableDatabase();
    }
}

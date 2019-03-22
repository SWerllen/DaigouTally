package com.example.andriod.daigoutally;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddStockActivity extends AppCompatActivity {

    TextView tv_commodity;
    TextView tv_quantity;
    TextView tv_pricenum;
    Spinner  sp_priceunit;
    TextView tv_discount;
    TextView tv_exchhangerate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        tv_commodity=findViewById(R.id.pane_stock_commodity_text);
        tv_quantity=findViewById(R.id.pane_stock_quantity_text);
        tv_pricenum=findViewById(R.id.pane_stock_price_text);
        sp_priceunit=findViewById(R.id.pane_stock_priceunit_spinner);
        tv_discount=findViewById(R.id.pane_stock_discount_text);
        tv_exchhangerate=findViewById(R.id.pane_stock_exrate_text);
        tv_commodity.requestFocus();
    }

    public void onClickCheck(View v){
        new getExchangerate().execute();
    }
    public void onClickAdd(View v){
        String commodity=tv_commodity.getText().toString();
        String strprice=tv_pricenum.getText().toString();
        String strquantity=tv_quantity.getText().toString();
        String strdiscount=tv_discount.getText().toString();
        String strexchangerate=tv_exchhangerate.getText().toString();
        String unit=sp_priceunit.getSelectedItem().toString();

        String checkresult=CheckInput.checkCommodity(commodity);
        if(!checkresult.equals("yes")) {
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkQuantity(strquantity);
        if(!checkresult.equals("yes")){
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkPrice(strprice);
        if(!checkresult.equals("yes")){
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkDiscount(strdiscount);
        if(!checkresult.equals("yes")){
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkEXRate(strexchangerate);
        if(!checkresult.equals("yes")){
            ToastShow(checkresult); return;
        }
        if(unit.equals("")){
            ToastShow("You should choose a currency unit!");
            return;
        }

        MyDataOperation operation=new MyDataOperation(getBaseContext());

        Stock addstock=new Stock();
        addstock.commodity=commodity;
        addstock.quantity=Integer.valueOf(strquantity);
        addstock.showprice=new Price(strprice+unit);
        addstock.discount=Double.valueOf(strdiscount);
        addstock.exrate=Double.valueOf(strexchangerate);
        if(operation.addStock(addstock)){
            ToastShow("Add Stock successfully!");
            operation.close();
            finish();
        }
        else{
            ToastShow("Insert fail! Please check your input!");
            operation.close();
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorBackground),true);
    }

    private void ToastShow(String s){
        Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
    }
    public void onClickBack(View v){
        finish();
    }
    public void onDestroy() {
        setContentView(R.layout.nulllayout);
        super.onDestroy();
    }
    private class getExchangerate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String scur=sp_priceunit.getSelectedItem().toString();
            SharedPreferences preferences=getSharedPreferences("user_set",MODE_PRIVATE);
            String tcur=preferences.getString("unit","CNY");
            if(scur.equals(tcur))
                return "equal";
            String forecast_url_string=String.format("http://api.k780.com/?app=finance.rate&scur=%s&tcur=%s&appkey=40650&sign=793687a0d296f630b2c0ac49caef5ff8&format=json",scur,tcur);
            HttpURLConnection conn=null;
            try {
                URL foreurl = new URL(forecast_url_string);
                conn=(HttpURLConnection)foreurl.openConnection();
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(10000);
                Log.i("GETEXRATE","Begin connect！");
                conn.connect();
                int statecode=conn.getResponseCode();
                if(statecode!=200){
                    Toast.makeText(getBaseContext(),"Network not good！",Toast.LENGTH_SHORT).show();
                    Log.i("GETEXRATE","Internet not so good！");
                    return null;
                }
                Log.i("GETEXRATE","Get the feedback！");
                Log.i("GETEXRATE","Connect successfully!！");
                InputStream foreis=conn.getInputStream();
                InputStreamReader foreisr = new InputStreamReader(foreis, "utf-8"); // 设置读取流的编码格式，自定义编码
                BufferedReader reader = new BufferedReader(foreisr);
                String line = null;
                StringBuilder sb=new StringBuilder();
                while((line=reader.readLine())!=null)
                    sb.append(line+" ");
                reader.close();
                foreisr.close();
                foreis.close();
                return sb.toString();
            } catch (java.io.IOException e) {
                Log.i("GETEXRATE","IO throw fault!");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String info) {
            if(info==null||info==""){
                ToastShow("Failure! Please check the Internet!");
                return;
            }
            if(info.equals("equal")){
                tv_exchhangerate.setText("1");
                return;
            }
            Gson gson=new Gson();
            ExchangeRateBean exratebean=gson.fromJson(info,ExchangeRateBean.class);
            if(exratebean.getSuccess().equals("1")){
                tv_exchhangerate.setText(exratebean.getResult().getRate());
                return;
            }
            else{
                ToastShow("Failure! Please check the Internet!");
            }
        }
    }
}

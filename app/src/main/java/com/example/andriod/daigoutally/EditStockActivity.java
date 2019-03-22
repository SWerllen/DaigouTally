package com.example.andriod.daigoutally;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

public class EditStockActivity extends AppCompatActivity {

    TextView tv_commodity;
    TextView tv_quantity;
    TextView tv_pricenum;
    TextView  tv_unit;
    TextView tv_discount;
    TextView tv_exchhangerate;
    static Stock mstock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stock);
        tv_commodity=findViewById(R.id.pane_stock_commodity_text);
        tv_quantity=findViewById(R.id.pane_stock_quantity_text);
        tv_pricenum=findViewById(R.id.pane_stock_price_text);
        tv_unit=findViewById(R.id.pane_stock_unit_text);
        tv_discount=findViewById(R.id.pane_stock_discount_text);
        tv_exchhangerate=findViewById(R.id.pane_stock_exrate_text);

        tv_commodity.setText(mstock.commodity);
        tv_quantity.setText(String.valueOf(mstock.quantity));
        tv_pricenum.setText(String.valueOf(mstock.showprice.nums));
        tv_unit.setText(mstock.showprice.unit);
        tv_discount.setText(String.valueOf(mstock.discount));
        tv_exchhangerate.setText(String.valueOf(mstock.exrate));

        tv_commodity.requestFocus();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorBackground),true);
    }

    public void onClickAdd(View v){
        String commodity=tv_commodity.getText().toString();
        String strprice=tv_pricenum.getText().toString();
        String strquantity=tv_quantity.getText().toString();
        String strdiscount=tv_discount.getText().toString();
        String strexchangerate=tv_exchhangerate.getText().toString();
        String unit=tv_unit.getText().toString();

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

        Stock addstock=mstock;
        addstock.commodity=commodity;
        addstock.quantity=Integer.valueOf(strquantity);
        addstock.showprice=new Price(strprice+unit);
        addstock.discount=Double.valueOf(strdiscount);
        addstock.exrate=Double.valueOf(strexchangerate);
        if(operation.updateStock(addstock)){
            ToastShow("Edit Stock successfully!");
            operation.close();
            finish();
        }
        else{
            ToastShow("Edit fail! Please check your input!");
            operation.close();
        }
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
}

package com.example.andriod.daigoutally;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class AddOrderActivity extends AppCompatActivity {
    TextView tv_commodity;
    TextView tv_quantity;
    TextView tv_pricenum;
    //Spinner sp_priceunit;
    TextView tv_unit;
    TextView tv_buyer;
    TextView tv_location;
    TextView tv_phone;
    TextView tv_phoneregion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        tv_commodity=findViewById(R.id.pane_order_commodity_text);
        tv_quantity=findViewById(R.id.pane_order_quantity_text);
        tv_pricenum=findViewById(R.id.pane_order_price_text);
        tv_unit=findViewById(R.id.pane_order_unit_text);
        tv_buyer=findViewById(R.id.pane_order_buyer_text);
        tv_location=findViewById(R.id.pane_order_location_text);
        tv_phone=findViewById(R.id.pane_order_phone_text);
        tv_phoneregion=findViewById(R.id.pane_order_phoneregion_text);
        tv_commodity.requestFocus();

        SharedPreferences preferences=getSharedPreferences("user_set",MODE_PRIVATE);
        String unit=preferences.getString("unit","CNY");
        tv_unit.setText(unit);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorBackground),true);
    }
    public void onClickBack(View v){
        finish();
    }

    public void onClickAdd(View v){
        String commodity=tv_commodity.getText().toString();
        String strprice=tv_pricenum.getText().toString();
        String strquantity=tv_quantity.getText().toString();
        String buyername=tv_buyer.getText().toString();
        String location=tv_location.getText().toString();
        String phone="+"+tv_phoneregion.getText().toString()+tv_phone.getText().toString();
        String unit=tv_unit.getText().toString();

        String checkresult=CheckInput.checkCommodity(commodity);
        if(!checkresult.equals("yes")) {
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkQuantity(strquantity);
        if(!checkresult.equals("yes")) {
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkPrice(strprice);
        if(!checkresult.equals("yes")) {
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkBuyerName(buyername);
        if(!checkresult.equals("yes")) {
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkLocation(location);
        if(!checkresult.equals("yes")) {
            ToastShow(checkresult);return;
        }
        checkresult=CheckInput.checkPhone(phone);
        if(!checkresult.equals("yes")) {
            ToastShow(checkresult);return;
        }

        PhoneNumberUtil util=PhoneNumberUtil.createInstance(getBaseContext());
        try {
            final Phonenumber.PhoneNumber phoneNumber = util.parse(phone, "US");
            if(!util.isValidNumber(phoneNumber)){
                ToastShow("The phone number is illegal!");
                return;
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        MyDataOperation operation=new MyDataOperation(getBaseContext());

        Order addorder=new Order();
        addorder.commodity=commodity;
        addorder.quantity=Integer.valueOf(strquantity);
        addorder.sellprice=new Price(strprice+unit);
        addorder.buyer=buyername;
        addorder.location=location;
        addorder.phone=phone;
        if(operation.addOrder(addorder)){
            ToastShow("Add order successfully!");
            operation.close();
            finish();
        }
        else{
            ToastShow("Insert fail! Please check your input!");
            operation.close();
        }
    }
    private void ToastShow(String s){
        Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
    }
}

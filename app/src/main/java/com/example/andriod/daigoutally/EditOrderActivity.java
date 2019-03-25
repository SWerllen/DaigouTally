package com.example.andriod.daigoutally;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class EditOrderActivity extends AppCompatActivity {
    TextView tv_commodity;
    TextView tv_quantity;
    TextView tv_pricenum;
    TextView tv_unit;
    TextView tv_buyer;
    TextView tv_location;
    TextView tv_phone;
    TextView tv_phoneregion;
    Button btn_add;
    static Order morder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(morder==null){
            ToastShow("There isn't order to edit!");
            return;
        }
        setContentView(R.layout.activity_edit_order);
        tv_commodity=findViewById(R.id.pane_order_commodity_text);
        tv_quantity=findViewById(R.id.pane_order_quantity_text);
        tv_pricenum=findViewById(R.id.pane_order_price_text);
        tv_unit=findViewById(R.id.pane_order_unit_text);
        tv_buyer=findViewById(R.id.pane_order_buyer_text);
        tv_location=findViewById(R.id.pane_order_location_text);
        tv_phone=findViewById(R.id.pane_order_phone_text);
        tv_phoneregion=findViewById(R.id.pane_order_phoneregion_text);

        tv_commodity.setText(morder.commodity);
        tv_quantity.setText(String.valueOf(morder.quantity));
        tv_pricenum.setText(String.valueOf(morder.sellprice.nums));
        tv_unit.setText(morder.sellprice.unit);
        tv_buyer.setText(morder.buyer);
        tv_location.setText(morder.location);
        String qianzhui=morder.phone.substring(1,3);
        String houmian=morder.phone.substring(3,morder.phone.length());
        tv_phoneregion.setText(qianzhui);
        tv_phone.setText(houmian);

        tv_commodity.requestFocus();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorBackground),true);
    }
    @Override
    protected void onStart() {
        findViewById(R.id.pane_order).setAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.list_enter));
        super.onStart();
    }
    public void onClickBack(View v){
        finish();
    }

    public void onClickEdit(View v){
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

        Order addorder=morder;
        addorder.commodity=commodity;
        addorder.quantity=Integer.valueOf(strquantity);
        addorder.sellprice=new Price(strprice+unit);
        addorder.buyer=buyername;
        addorder.location=location;
        addorder.phone=phone;
        if(operation.updateOrder(addorder)){
            ToastShow("Edit order successfully!");
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
}

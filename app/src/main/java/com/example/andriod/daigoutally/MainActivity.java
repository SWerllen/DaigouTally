package com.example.andriod.daigoutally;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.githang.statusbar.StatusBarCompat;

public class MainActivity extends AppCompatActivity {
    MyDataOperation  dataope;
    StockFragment fragStock;
    private StockFragment mStockFragment;
    private OrderFragment mOrderFragment;
    private MarkFragment  mMarkFragment;
    private SetFragment   mSetFragment;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    BottomNavigationView navigationView;
    public enum Jiemian{ORDER,STOCK,MARK,SET};
    Jiemian jiemian;
    boolean issearch=false;

    Button btn_add;
    Button btn_search;
    Button btn_help;
    TextView tv_search;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction=mFragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_stock:
                    outSet();
                    jiemian=Jiemian.STOCK;
                    hidothers(transaction);
                    transaction.show(mStockFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_order:
                    outSet();
                    jiemian=Jiemian.ORDER;
                    hidothers(transaction);
                    transaction.show(mOrderFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_mark:
                    outSet();
                    jiemian=Jiemian.MARK;
                    hidothers(transaction);
                    transaction.show(mMarkFragment);
                    transaction.commit();
                    mMarkFragment.closedetails();
                    return true;
                case R.id.navigation_setting:
                    inSet();
                    jiemian=Jiemian.SET;
                    hidothers(transaction);
                    transaction.show(mSetFragment);
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences shared=getSharedPreferences("user_set", MODE_PRIVATE);
        boolean isfer=shared.getBoolean("isfer", true);
        if(isfer){
            Intent helpintent=new Intent(getBaseContext(),HelpActivity.class);
            startActivity(helpintent);
            SharedPreferences.Editor editor=shared.edit();
            editor.putBoolean("isfer",false);
            editor.commit();
        }

        Intent intent=new Intent(getBaseContext(),StartViewActivity.class);
        startActivity(intent);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorBackground),true);

        btn_add=findViewById(R.id.btn_add);
        btn_search=findViewById(R.id.btn_search);
        tv_search=findViewById(R.id.tv_search);
        navigationView=findViewById(R.id.navigation);
        btn_help=findViewById(R.id.btn_help);

        tv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(count>100){
                    Toast.makeText(getBaseContext(),"You should not input more than 100 charactors!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onEditChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);

        dataope=new MyDataOperation(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mStockFragment=StockFragment.newInstance();
        mOrderFragment=OrderFragment.newInstance();
        mMarkFragment=MarkFragment.newInstance();
        mSetFragment=SetFragment.newInstance();

        FragmentTransaction transaction=mFragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container,mOrderFragment);
        transaction.add(R.id.fragment_container,mStockFragment);
        transaction.add(R.id.fragment_container,mMarkFragment);
        transaction.add(R.id.fragment_container,mSetFragment);
        transaction.hide(mStockFragment);
        transaction.hide(mOrderFragment);
        transaction.hide(mSetFragment);
        transaction.commit();
        jiemian=Jiemian.MARK;
        //onCreateOrderList();
    }

    private void hidothers(FragmentTransaction transaction){
        if(mOrderFragment != null&&jiemian!=Jiemian.ORDER){
            transaction.hide(mOrderFragment);
        }
        if(mStockFragment != null&&jiemian!=Jiemian.STOCK){
            transaction.hide(mStockFragment);
        }
        if(mMarkFragment != null&&jiemian!=Jiemian.MARK){
            transaction.hide(mMarkFragment);
        }
        if(mSetFragment!=null&&jiemian!=Jiemian.SET){
            transaction.hide(mSetFragment);
        }
    }

    public void onClickAdd(View view){
        Intent intent;
        switch (jiemian){
            case ORDER:
                intent=new Intent(this,AddOrderActivity.class);
                startActivity(intent);
                break;
            case STOCK:
                intent=new Intent(this,AddStockActivity.class);
                startActivity(intent);
                break;
            case MARK:
                intent=new Intent(this,AddMarkActivity.class);
                startActivity(intent);
                break;
            case SET:
                break;
        }
    }

    public void onResume() {
        switch (jiemian) {
            case ORDER:
                mOrderFragment.refresh();
                break;
            case STOCK:
                mStockFragment.refresh();
                break;
            case MARK:
                mMarkFragment.refresh();
                break;
            case SET:
                break;
        }
        super.onResume();
    }

    public void onClickSearch(View v){
        if(!issearch){  //begin in the state of searching
            switchsearch();
        }
        else {
            dosearch();
        }
    }

    public void onEditChange() {
        if (!issearch) return;
        dosearch();
    }

    private void dosearch(){
        String content=tv_search.getText().toString();
        if(content.length()>100) return;

        switch (jiemian) {
            case ORDER:
                mOrderFragment.search(content);
                break;
            case STOCK:
                mStockFragment.search(content);
                break;
            case MARK:
                mMarkFragment.search(content);
                break;
            case SET:
                break;
        }
    }

    public void onBackPressed() {
        if(issearch)
            switchsearch();
        else{
            finish();
        }
    }

    void switchsearch(){
        if(!issearch){
            issearch=true;
            btn_add.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_left));
            tv_search.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_enter));
            btn_add.setVisibility(View.INVISIBLE);
            tv_search.setVisibility(View.VISIBLE);
            tv_search.requestFocus();
            InputMethodManager manager = ((InputMethodManager)getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null) manager.showSoftInput(tv_search, 0);
            navigationView.setVisibility(View.GONE);
            navigationView.setAnimation(AnimationUtils.loadAnimation(this,R.anim.navigation_left));
        }
        else{
            issearch=false;
            btn_add.setVisibility(View.VISIBLE);
            tv_search.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_left));
            tv_search.setText("");
            btn_add.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_enter));
            tv_search.setVisibility(View.INVISIBLE);
            InputMethodManager manager = ((InputMethodManager)getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE));
            if (manager != null)
                manager.hideSoftInputFromWindow(tv_search.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            navigationView.setVisibility(View.VISIBLE);
            navigationView.setAnimation(AnimationUtils.loadAnimation(this,R.anim.navigation_enter));
            switch (jiemian) {
                case ORDER:
                    mOrderFragment.closesearch();
                    break;
                case STOCK:
                    mStockFragment.closesearch();
                    break;
                case MARK:
                    mMarkFragment.closesearch();
                    break;
                case SET:
                    break;
            }
        }
    }

    private void inSet(){
        if(jiemian==Jiemian.SET) return;
        tv_search.setVisibility(View.INVISIBLE);
        btn_search.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_left));
        btn_add.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_left));
        btn_help.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_enter));
        btn_search.setVisibility(View.INVISIBLE);
        btn_add.setVisibility(View.INVISIBLE);
        btn_help.setVisibility(View.VISIBLE);
    }
    private void outSet(){
        if(jiemian!=Jiemian.SET) return;
        btn_search.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_enter));
        btn_add.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_enter));
        btn_help.setAnimation(AnimationUtils.loadAnimation(this,R.anim.add_left));
        btn_search.setVisibility(View.VISIBLE);
        btn_add.setVisibility(View.VISIBLE);
        btn_help.setVisibility(View.INVISIBLE);
    }

    public void onClickHelp(View v){
        Intent intent=new Intent(this,HelpActivity.class);
        startActivity(intent);
    }
}

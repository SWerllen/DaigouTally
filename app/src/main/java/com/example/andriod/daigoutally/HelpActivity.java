package com.example.andriod.daigoutally;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorBackground),true);
    }
    public void onClickBack(View view){
        finish();
    }
}

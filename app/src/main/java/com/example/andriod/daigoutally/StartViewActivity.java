package com.example.andriod.daigoutally;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class StartViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_start_view);
                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}

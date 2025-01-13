package com.example.roomdemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.roomdemo.test1.TestActivity1;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 自定义View实现
     */
    public void onTest1(View v) {
        startActivity(new Intent(this, TestActivity1.class));
    }


}
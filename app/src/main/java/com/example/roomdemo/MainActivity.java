package com.example.roomdemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.roomdemo.test1.TestActivity1;
import com.example.roomdemo.test2.TestActivity2;
import com.example.roomdemo.test3.TestActivity3;

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

    /**
     * RecyclerView+PagerSnapHelper 小视频界面
     */
    public void onTest2(View v) {
        startActivity(new Intent(this, TestActivity2.class));
    }

    /**
     * ViewPager2 测试
     */
    public void onTest3(View v) {
        startActivity(new Intent(this, TestActivity3.class));
    }

}
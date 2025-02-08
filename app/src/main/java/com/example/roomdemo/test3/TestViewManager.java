package com.example.roomdemo.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.roomdemo.R;

public class TestViewManager {

    private Context mContext;
    private View mRootView;
    private TextView tvTest;

    public TestViewManager(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        // 播放器根布局
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.view_test3_root, null);
        tvTest = mRootView.findViewById(R.id.tv_root);
    }

    public View getRootView() {
        return mRootView;
    }

    public void onRemove() {

    }

    public void onAdd(String str) {
        tvTest.setText(str);
    }

}

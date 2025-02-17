package com.example.roomdemo.test3;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;

    public MarginItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 添加左边距和上边距，右边距和下边距可以根据需要调整或保持为0
        outRect.left = 0;
        outRect.top = space;
        outRect.right = 0; // 可以根据需要调整
        outRect.bottom = 0; // 可以根据需要调整
    }
}
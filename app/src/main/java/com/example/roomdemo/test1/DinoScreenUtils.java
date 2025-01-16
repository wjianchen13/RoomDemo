package com.example.roomdemo.test1;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.roomdemo.BaseApp;

/**
 * Created by Administrator on 2018/3/5 0005.
 */

public class DinoScreenUtils {


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * getDensity(BaseApp.app()) + 0.5f);
    }

    /**
     * 返回屏幕密度
     */
    public static float getDensity(Context context) {
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2.0f;
    }

    private static DisplayMetrics displayMetrics = new DisplayMetrics();

}


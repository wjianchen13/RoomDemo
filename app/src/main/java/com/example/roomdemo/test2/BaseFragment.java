package com.example.roomdemo.test2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class BaseFragment extends Fragment {

    protected String pageName = "default";
    protected boolean isNeedTGameRecord;
    public  boolean mFragmentVisible;
    private WeakReference<Activity> mContext;

    public Activity getActivityExp() {
        return getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mContext == null || mContext.get() == null)
            mContext = new WeakReference<>((Activity) context);
    }

    @Nullable
    @Override
    public Activity getContext() {
        super.getContext();
        return getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isNeedTGameRecord = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isNeedTGameRecord){
            Map<String,Object> map = new HashMap<>();
            map.put("frg_onPause", pageName);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(isNeedTGameRecord){
            Map<String,Object> map = new HashMap<>();
            map.put("frg_onResume", pageName);
        }
    }

    protected void setIsNeedTGameRecord(boolean flag){
        isNeedTGameRecord = flag;
    }

}
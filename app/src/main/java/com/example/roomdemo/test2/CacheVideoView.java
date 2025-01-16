package com.example.roomdemo.test2;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.roomdemo.BaseApp;

import xyz.doikki.videoplayer.player.VideoView;

/**
 * @author: Administrator
 * @date: 2022/7/15/015
 */
public class CacheVideoView extends VideoView {
    HttpProxyCacheServer cacheServer = ProxyVideoCacheManager.getProxy(BaseApp.app());

    public CacheVideoView(@NonNull Context context) {
        super(context);
    }

    public CacheVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CacheVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        super.initView();
        //默认背景为黑色，切换会有概率闪烁
        mPlayerContainer.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void setUrl(String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                String proxyUrl = cacheServer.getProxyUrl(url);
                super.setUrl(proxyUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

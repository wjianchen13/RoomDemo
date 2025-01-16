package com.example.roomdemo.test2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.roomdemo.R;

import xyz.doikki.videoplayer.controller.BaseVideoController;
import xyz.doikki.videoplayer.player.VideoView;


public class RecyclerPageController extends BaseVideoController {
    private final int[] COLORS = new int[]{R.drawable.green_dot, R.drawable.green_dot, R.drawable.green_dot};
    private TextView tv_debug_info;
    private ImageView iv_cover;
    private ViewGroup ll_actions;
    private View v_status;
    private TextView tv_status;
    private OnVideoProgressListener mOnVideoProgressListener;

    public RecyclerPageController(@NonNull Context context) {
        super(context);
    }

    public RecyclerPageController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerPageController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_video_cover_dynamic;
    }

    @Override
    protected void initView() {
        super.initView();
        iv_cover = findViewById(R.id.iv_cover);
        ll_actions = findViewById(R.id.ll_actions);
        tv_debug_info = findViewById(R.id.tv_view_video_cover_dynamic_test_info);
        v_status = findViewById(R.id.view_bg_left);
        tv_status = findViewById(R.id.tv_status_left);
    }

    public void setOnVideoProgressListener(OnVideoProgressListener mOnVideoProgressListener) {
        this.mOnVideoProgressListener = mOnVideoProgressListener;
    }

    @Override
    public void setPlayState(int playState) {
        super.setPlayState(playState);

        switch (playState) {
            case VideoView.STATE_IDLE:
                iv_cover.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
                iv_cover.setVisibility(GONE);
                break;
            case VideoView.STATE_PREPARED:
                break;
        }
    }

    public void setVideoModel(VideoModel videoModel) {

        String debugInfo = (videoModel.isIs_see() ? "已看" : "未看") + "\n" +
                "视频id:" + videoModel.getId();
        tv_debug_info.setVisibility(VISIBLE);
        tv_debug_info.setText(debugInfo);

        if ("0".equals(videoModel.getOnline())) {
            v_status.setBackgroundResource(COLORS[1]);
            tv_status.setText("可以消息");
        } else if ("1".equals(videoModel.getOnline())) {
            v_status.setBackgroundResource(COLORS[1]);
            tv_status.setText("可以视频");
        } else {
            v_status.setBackgroundResource(COLORS[2]);
            tv_status.setText("忙");
        }
    }

    public ImageView getThumb() {
        return iv_cover;
    }

    public ViewGroup getActionViewGroup() {
        return ll_actions;
    }

    @Override
    protected void setProgress(int duration, int position) {
        if (mOnVideoProgressListener != null) {
            mOnVideoProgressListener.onProgressChange(duration, position);
        }
    }
}

package com.example.roomdemo.test2;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;


public class RecyclerPageAdapter extends BaseQuickAdapter<VideoModel, BaseViewHolder> {
    private int mCurPosition;

    public RecyclerPageAdapter(int layoutResId, @Nullable List<VideoModel> data) {
        super(layoutResId, data);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.getLayoutPosition() < getDefItemCount()) {
            VideoModel item = getItemOrNull(holder.getLayoutPosition());
//            if (item!=null){
//                PreloadManager.getInstance(holder.itemView.getContext()).removePreloadTask(item.getUrl());
//            }
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, VideoModel videoModel) {
        //是否反向滑动
        boolean mIsReverseScroll = baseViewHolder.getLayoutPosition() < mCurPosition;
        mCurPosition = baseViewHolder.getLayoutPosition();
        if (mIsReverseScroll) {
            for (int i = 0; i < 5; i++) {
                int index = baseViewHolder.getLayoutPosition() - i;
                if (index > -1) {
//                    if (!TextUtils.isEmpty(videoModel.getUrl())){
//                        PreloadManager.getInstance(baseViewHolder.itemView.getContext()).addPreloadTask(videoModel.getUrl(), index);
//                    }
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                int index = baseViewHolder.getLayoutPosition() + i;
                if (index < getItemCount()) {
//                    if (!TextUtils.isEmpty(videoModel.getUrl())){
//                        PreloadManager.getInstance(baseViewHolder.itemView.getContext()).addPreloadTask(videoModel.getUrl(), index);
//                    }
                }
            }
        }
//        if (videoModel.getIs_show() == 0 && AppUser.getInstance().getUser().getVip() == 0 && !VideoManager.getInstance().hasPlayed(videoModel.getId())) {
//            GlideUtil.loadBlurImage(baseViewHolder.getView(R.id.thumb), videoModel.getFirst_photo(), 10, 2, R.drawable.ic_common_video_default);
//        } else {
//            GlideUtil.loadImage(baseViewHolder.getView(R.id.thumb), videoModel.getFirst_photo(), R.drawable.ic_common_video_default);
//        }
    }
}
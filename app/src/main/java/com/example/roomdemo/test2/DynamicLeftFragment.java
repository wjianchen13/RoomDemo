package com.example.roomdemo.test2;

import static android.os.Looper.getMainLooper;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdemo.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.doikki.videoplayer.player.VideoView;


/**
 * 动态二级
 */
public class DynamicLeftFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = DynamicLeftFragment.class.getSimpleName();

    public static final String TAG_LIST = "TAG_LIST";
    public static final String TAG_POSITION = "TAG_POSITION";
    public static final String TAG_FROM = "TAG_FROM";
    public static final String TAG_FORBID_HEAD = "TAG_FORBID_HEAD";
    public static final String TAG_REQUEST_TYPE = "TAG_REQUEST_TYPE";
    public static final int FROM_HOME_TAB = 1;
    public static final int FROM_RECOMMENT_LIST = 2;
    public static final int FROM_VIP_LIST = 3;
    public static final int FROM_USER_DETAILS = 4; // 从个人资料页进入
    public static final int FROM_CHAT = 5; // 从私聊进入
    private static final int HAS_LIKE = 1;

    private static final int TYPE_ERROR = -1; // 错误
    private static final int TYPE_NORMAL = 0; // 正常查看
    private static final int TYPE_NEED_VIP = 1; // 需要VIP查看视频
    private static final int TYPE_NEED_COINS = 2; // 需要付费查看视频

    private final String CONCERN = "concern";

    private List<VideoModel> modelList;
    private String name, headImg, desc, userId;
    private int identity, videoPrice, status, level;
    private boolean follow;
    private String msgJsonAryStr;
    private Map<Integer,String> mVideoFinishedMap = new HashMap<>();//完播视频记录
    private Map<Integer,String> mVideoSeenMap = new HashMap<>();//看过视频记录
    private int thresholTime;//记录视频秒数阈值

    private ImageView iv_play, iv_user_icon, iv_like, imgv_chat, iv_follow, iv_red_like, iv_has_like,iv_back,iv_more;
//    private SVGAImageView svgaFree;
//    private SVGAImageView svgaPraise;
//    private SVGAImageView svgaVideo;
    private RelativeLayout rlytEmpty;
    private TextView tv_like_count, tv_user_name, tv_desc;
    private int currentPosition;
    private AnimatorSet likeAnimSet;
    private Animator hasLikeShowAnim, hasLikeHideAnim;
    private GestureDetector gestureDetector;
    private boolean likeCanClick = true;

    private CacheVideoView mIjkVideoView;
    private RecyclerPageController recyclerPageController;
    private RecyclerView rcv_video_dynamic;
    private RecyclerPageAdapter pageAdapter;
    private SmartRefreshLayout refreshLayout;
    private int fromType;
    private int mForbidHead; // 1 禁止点击头像

    private boolean hasNoMore;
    private View ll_noble;
    private View btn_get_noble;

    private View ll_charge;
    private View btn_charge;
    private TextView tv_charge_tips;
    private int mPlayType;
    private boolean isFirstStart = true; // 不是第一次启动
//    private UserDetailsInfo mUserInfo;
    private int mRequestType;
    private boolean isRequestData;
    private boolean isLoadMoreing;

    private int mStatus = -1;

    public static DynamicLeftFragment newInstance(int from, List<VideoModel> models, int position, int forbidHead, int requestType) {
        Bundle args = new Bundle();
        args.putInt(TAG_FROM, from);
        args.putInt(TAG_POSITION, position);
        args.putSerializable(TAG_LIST, (Serializable) models);
        args.putInt(TAG_FORBID_HEAD, forbidHead);
        args.putInt(TAG_REQUEST_TYPE, requestType);
        DynamicLeftFragment fragment = new DynamicLeftFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Handler mHandler = new Handler(getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg != null) {
                int pos = msg.arg1;
                requestAnchorState(pos);
            }
        }
    };

    /**
     * 获取主播最新的状态，决定是否需要跳转拨号，在线状态才跳转
     */
    private void requestAnchorState(final int pos) {
        if (modelList == null || modelList.size() - 1 < pos) {
            return;
        }
//        if(modelList.get(pos) != null) {
//            AsyncHttpHelper.RequestParams params = new AsyncHttpHelper.RequestParams();
//            params.put("uid", AppUser.getInstance().getUser().getId() + "");
//            params.put("user_id", modelList.get(pos).getAnchor_id());
//            AppAsyncHttpHelper.httpsGet(Constants.USER_INFO, params, new AsyncHttpHelper.OnHttpListener<JSONObject>() {
//                @Override
//                public void onHttpListener(boolean httpSuccess, JSONObject obj) {
//                    if (obj == null || obj.optInt("code") != Constants.HTTP_OK) {
//                        mStatus = -1;
//                        return;
//                    }
//                    JSONObject data = obj.optJSONObject("data");
//                    if (data != null) {
//                        JSONObject userInfo = data.optJSONObject("msg");
//                        if (userInfo != null) {
//                            mStatus = userInfo.optInt("status");
//                            if (mStatus == 1 && currentPosition == pos) {
//                                tryToRing(getCall(modelList.get(currentPosition)));
//                            }
//                        }
//                    }
//                }
//            });
//        }
    }

    /**
     * 需要非VIP才进行跳转
     * @return
     */
//    public boolean tryToRing(Call call) {
//        if(ApplicationUtil.isNormalMaleUser() && call != null) {
//            if (ShareDataManager.getInstance().isVideoCallToday(userId)){
//                ApplicationUtil.autoCall(getActivityExp(), call);
//                return true;
//            }
//        }
//        return false;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_left, container, false);
        getActivityExp().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews(view);
        initArgs();
        init();
        adjustUi();
        initAnim();
//        try {
//            if (!EventBus.getDefault().isRegistered(this)){
//                EventBus.getDefault().register(this);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return view;
    }

    private void initArgs() {
        modelList = new ArrayList<>();
        if (getArguments() != null) {
            fromType = getArguments().getInt(TAG_FROM);
            mForbidHead = getArguments().getInt(TAG_FORBID_HEAD);
            mRequestType = getArguments().getInt(TAG_REQUEST_TYPE);
            List<VideoModel> tmpList = (List<VideoModel>) getArguments().getSerializable(TAG_LIST);
            for (int i = 0; i < tmpList.size(); i++) {
                modelList.add(tmpList.get(i));
            }
            currentPosition = getArguments().getInt(TAG_POSITION);

        }
    }

    /**
     * 从个人资料进入UI有区别
     */
    private void adjustUi() {
        if(fromType == FROM_USER_DETAILS) {
            imgv_chat.setVisibility(View.VISIBLE);
        } else {
            imgv_chat.setVisibility(View.GONE);
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateNoble(RefreshNobleEvent nobleEvent) {
//        try {
//            if (getActivityExp() != null){
//                getActivityExp().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPlayType = TYPE_NORMAL;
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void sendRingMsg(long delay, int pos) {
        if(mHandler != null) {
            clearMsg();
            Message msg = mHandler.obtainMessage();
            msg.arg1 = pos;
            mHandler.sendMessageDelayed(msg, delay * 2);
        }
    }

    public void clearMsg() {
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void init() {
        mIjkVideoView = new CacheVideoView(getActivityExp());
        mIjkVideoView.setBackgroundColor(Color.GREEN);
        recyclerPageController = new RecyclerPageController(getActivityExp());
        recyclerPageController.setOnVideoProgressListener(new OnVideoProgressListener() {
            @Override
            public void onProgressChange(int duration, int position) {
                if (thresholTime > 0 && thresholTime <= position / 1000) {
                    recordOperteVideo(-1, 1);
                }
            }
        });
        mIjkVideoView.setVideoController(recyclerPageController);
        mIjkVideoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener(){
            @Override
            public void onPlayStateChanged(int playState) {
                switch (playState) {
                    case VideoView.STATE_PLAYBACK_COMPLETED:
                        recordOperteVideo(-1, 1);
                        resumingPlay();
                        break;
                    case VideoView.STATE_PLAYING:
                        iv_play.setVisibility(View.GONE);
                        recyclerPageController.startProgress();
                        break;
                    case VideoView.STATE_BUFFERING:
                        // 停止刷新进度
                        recyclerPageController.stopProgress();
                        break;
                    case VideoView.STATE_BUFFERED:
                        // 开始刷新进度
                        recyclerPageController.startProgress();
                    default:
                        break;
                }
            }
        });
        pageAdapter = new RecyclerPageAdapter(R.layout.item_recycler_page, modelList);
        ViewPagerLayoutManager layoutManager = new ViewPagerLayoutManager(getActivityExp(), OrientationHelper.VERTICAL);
        rcv_video_dynamic.setLayoutManager(layoutManager);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        if (fromType == FROM_HOME_TAB){
            refreshLayout.setEnableRefresh(true);
            refreshLayout.setOnMultiListener(new SimpleMultiListener(){

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    requestData(false);
                    refreshLayout.finishRefresh(1000/*,false*/);//传入false表示加载失败
                }

//                @Override
//                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                requestData(true);
//                    refreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
//                }

                @Override
                public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                    if (oldState == RefreshState.LoadFinish || oldState == RefreshState.RefreshFinish) {
                        if (!TextUtils.isEmpty(msgJsonAryStr)) {
//                            List<VideoModel> models = new Gson().fromJson(msgJsonAryStr, new TypeToken<List<VideoModel>>() {
//                            }.getType());
//                            if (modelList == null) {
//                                modelList = new ArrayList<>();
//                            }
//                            hasNoMore = models.isEmpty();
//                            playerOnPause();
//                            if (mIjkVideoView != null) {
//                                mIjkVideoView.release();
//                            }
//                            modelList.clear();
//                            currentPosition = 0;
////                            for (int i = 0; i < models.size(); i++) {
////                                if (!BlockManager.getInstance().isBlocked(models.get(i).getAnchor_id())) {
////                                    modelList.add(models.get(i));
////                                }
////                            }
//                            pageAdapter.notifyDataSetChanged();
//                            rcv_video_dynamic.scrollToPosition(0);
//                            msgJsonAryStr = "";
                        }
                        rlytEmpty.setVisibility(modelList.size() > 0 ? View.GONE : View.VISIBLE);
                    }
                }
            });
        }

        rcv_video_dynamic.setAdapter(pageAdapter);
        if (currentPosition >0) {
            rcv_video_dynamic.scrollToPosition(currentPosition);
        }
        layoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                //自动播放第一条
                videoLog(TAG, "============================================================> onInitComplete() currentPosition: " + currentPosition);
                VideoModel model = modelList.get(currentPosition);
                videoLog(TAG, "=========> onInitComplete() info: " + model.toString());
                initPlayType(model);
                setActionView(model);
                renfreshFollow();
                tryPlayVideo(model, currentPosition);
                recyclerPageController.setVideoModel(model);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (currentPosition == position) {
                    mIjkVideoView.release();
                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                if (currentPosition == position) return;
                videoLog(TAG, "============================================================> onPageSelected() currentPosition: " + currentPosition);
                VideoModel model = modelList.get(position);
                videoLog(TAG, "=========> onPageSelected() info: " + model.toString());
                initPlayType(model);
                setActionView(model);
                tryPlayVideo(model, position);
                currentPosition = position;
                renfreshFollow();
                likeCanClick = true;
                videoLog(TAG, "=========> size = " + modelList.size() + "  currentPosition = " + currentPosition);
                if (!isLoadMoreing && fromType == FROM_HOME_TAB && modelList.size() - currentPosition <= 2) {
                    isLoadMoreing = true;
                    String noSeeIdStrs = "";
                    if (mRequestType == 1) {//1.discover 2.new 3.private
                        for (int i = currentPosition; i < modelList.size(); i++) {
                            VideoModel videoModel = modelList.get(i);
                            if (TextUtils.isEmpty(noSeeIdStrs)) {
                                noSeeIdStrs = videoModel.getId() + "";
                            } else {
                                noSeeIdStrs += "," + videoModel.getId();
                            }
                        }
                    }
                    requestData(noSeeIdStrs, true);
                }
                if (fromType == FROM_HOME_TAB){
                    recyclerPageController.setVideoModel(model);
//                    if (hasNoMore &&modelList.size() -1 == currentPosition){
//                        ApplicationUtil.showToast(getActivityExp(),R.string.last_video);
//                    }
                }else{
//                    if (modelList.size() -1 == currentPosition){
//                        ApplicationUtil.showToast(getActivityExp(),R.string.last_video);
//                    }
                }

            }
        });
        initControllerViews(recyclerPageController.getRootView());
        initListener();
        if (fromType == FROM_CHAT && recyclerPageController.getActionViewGroup() != null) {
            for (int i = 0; i < recyclerPageController.getActionViewGroup().getChildCount(); i++) {
                if (i > 0) {
                    recyclerPageController.getActionViewGroup().getChildAt(i).setVisibility(View.GONE);
                }
            }
        }
        if (isRequestData){
            onRefresh();
        }
    }

    public void onRefresh(){
        if (refreshLayout!=null){
            if (rcv_video_dynamic != null) {
                rcv_video_dynamic.scrollToPosition(0);
            }
            refreshLayout.autoRefresh();
        }
    }

//    private Call getCall(VideoModel model) {
//        try {
//            if (model != null) {
//                AVideo aVideo = new AVideo();
//                aVideo.setName(model.getNickname());
//                aVideo.setRemarks("");
//                aVideo.setSex(0);
//                aVideo.setId(model.getAnchor_id());
//                aVideo.setAvatar(model.getHead_image());
//                aVideo.setVideoUrl(model.getUrl());
//                aVideo.setAudioPrice(0);
//                aVideo.setVideoPrice(Integer.parseInt(model.getVideo_price()));
//                aVideo.setLevel(Integer.parseInt(model.getLevel()));
//                Call mCall = new Call();
//                mCall.setAVideo(aVideo);
//                mCall.setCallId(0);
//                mCall.setReceiver(false);
//                mCall.setType(Call.VIDEO);
//                mCall.setCallMass(1);
//                mCall.setFromGroup(true);
//                mCall.setRejectType(3);
//                return mCall;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private void tryPlayVideo(VideoModel model, int pos) {
        if(model == null) {
            return ;
        }
        videoLog(TAG, "=========> tryPlayVideo() pos:" + pos);
        videoLog(TAG, "=========> tryPlayVideo() getIs_show:" + model.getIs_show());
        preStartPlay(model);
        seeVideo(pos);
        playVideo();
    }

    private void playerOnPause(){
        if (mIjkVideoView != null) {
            mIjkVideoView.pause();
        }
        if (iv_play != null) {
            iv_play.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取当前查看视频的状态
     * @return -1数据异常 0 正常查看 1 需要VIP查看 2 需要付费查看
     */
    private void initPlayType(VideoModel model) {
        if(model == null) {
            mPlayType = TYPE_ERROR;
            return ;
        }
        mPlayType = TYPE_NORMAL;
    }

    private boolean isAutoPlay(){
        return mPlayType == TYPE_NORMAL;
    }

    private void initViews(View view) {
        TextView tv_none_tips = view.findViewById(R.id.tv_none_tips);
//        tv_none_tips.setText(R.string.empty_hint);

        rcv_video_dynamic = view.findViewById(R.id.rv_refresh);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        rlytEmpty = view.findViewById(R.id.ll_empty);
    }

    /**
     * 先显示封面等相关的视频播放前期工作
     * 对于切换会闪的情况，应该是有视频封面图片没有加载去完成，导致使用了默认的图片作为视频封面
     * @param model
     */
    private void preStartPlay(VideoModel model) {
        View itemView = rcv_video_dynamic.getChildAt(0); // 获取到屏幕显示的部分，实际只有一个
        FrameLayout frameLayout = itemView.findViewById(R.id.container);
//        if (mPlayType != TYPE_NORMAL) {
//            GlideUtil.loadBlurImage(recyclerPageController.getThumb(), model.getFirst_photo(), 10, 2, R.drawable.ic_common_video_default);
//        } else {
//            GlideUtil.loadImage(recyclerPageController.getThumb(), model.getFirst_photo(), R.drawable.ic_common_video_default);
//        }
        ViewParent parent = mIjkVideoView.getParent();
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(mIjkVideoView);
        }
        mIjkVideoView.setUrl(model.getUrl());
        frameLayout.addView(mIjkVideoView);
    }

    private void initControllerViews(View view) {
        iv_play = view.findViewById(R.id.iv_play);
        iv_user_icon = view.findViewById(R.id.iv_user_icon);
        iv_has_like = view.findViewById(R.id.iv_has_like);
        imgv_chat = view.findViewById(R.id.imgv_chat);
//        svgaVideo = view.findViewById(R.id.svga_video);
        iv_like = view.findViewById(R.id.iv_like);
        tv_like_count = view.findViewById(R.id.tv_like_count);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_desc = view.findViewById(R.id.tv_desc);
        iv_follow = view.findViewById(R.id.iv_follow);
        iv_red_like = view.findViewById(R.id.iv_red_like);
        iv_back = view.findViewById(R.id.iv_back);
        iv_more = view.findViewById(R.id.iv_more);
        ll_noble = view.findViewById(R.id.ll_noble);
        btn_get_noble = view.findViewById(R.id.btn_get_noble);
        ll_charge = view.findViewById(R.id.ll_charge);
        btn_charge = view.findViewById(R.id.btn_charge);
        tv_charge_tips = view.findViewById(R.id.tv_charge_tips);
//        tv_charge_tips.setText(getString(R.string.unlock_watch_this_video, String.valueOf(PreferenceHelper.getInstance().getVideoCount())));
        iv_back.setVisibility(fromType == FROM_HOME_TAB ?View.INVISIBLE:View.VISIBLE);
//        svgaFree = view.findViewById(R.id.svga_free);
//        svgaPraise = view.findViewById(R.id.svga_praise);
//        svgaFree.setVisibility(ApplicationUtil.isFreeVideoCount() ? View.VISIBLE : View.GONE);
    }

    private void initListener() {
        iv_play.setOnClickListener(this);
        iv_follow.setOnClickListener(this);
        imgv_chat.setOnClickListener(this);
//        svgaVideo.setOnClickListener(this);
        iv_has_like.setOnClickListener(this);
        iv_user_icon.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        btn_get_noble.setOnClickListener(this);
        btn_charge.setOnClickListener(this);

        gestureDetector = new GestureDetector(getActivityExp(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return super.onDown(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (modelList != null && modelList.size() > currentPosition) {
                    VideoModel model = modelList.get(currentPosition);
                    if (model.getIs_click() != HAS_LIKE) {
                        like(model.getId(), 1);
                    }
                }
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mIjkVideoView != null) {
                    if (!mIjkVideoView.isPlaying()) {
                       playVideo();
                    } else {
                        mIjkVideoView.pause();
                    }
                    if (isAutoPlay()){
                        iv_play.setVisibility(!mIjkVideoView.isPlaying() ? View.VISIBLE : View.GONE);
                    }else{
                        iv_play.setVisibility(View.VISIBLE);
                    }
                }
                return super.onSingleTapUp(e);
            }
        });

        rcv_video_dynamic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play:
//                if (!isAutoPlay()){
//                    if(mPlayType == TYPE_NEED_VIP) {
//                        showDialog(modelList.get(currentPosition));
//                    } else if(mPlayType == TYPE_NEED_COINS) {
//                        if(ShareDataManager.getInstance().isFreeVideo()) {
//                            requestVideoStatus();
//                        } else {
//                            NeedCoinsFragment needCoinsFragment = NeedCoinsFragment.newInstance(PreferenceHelper.getInstance().getVideoCount(), NeedCoinsFragment.TYPE_VIDEO, new NeedCoinsFragment.OnCallBack() {
//                                @Override
//                                public void onUnlock() {
//                                    requestVideoStatus();
//                                }
//                            });
//                            if(getActivityExp() != null && getActivityExp() instanceof FragmentActivity)
//                                DialogFragmentHelper.showDialogFragment(((FragmentActivity)getActivityExp()).getSupportFragmentManager(), needCoinsFragment);
//                        }
//                    }
//                    return;
//                }
//                if (mIjkVideoView != null) {
//                   playVideo();
//                   if (isAutoPlay()){
//                       iv_play.setVisibility(View.GONE);
//                   }
//                }
                playVideo();
                break;
            case R.id.iv_follow:
                follow();
                break;
//            case R.id.svga_video:
//                if(LokApp.getInstance().getMainActivity() != null
//                        && LokApp.getInstance().getMainActivity().getChatManager() != null
//                        && CallStatusManager.getInstance().isCurrentCalling()) {
//                    ApplicationUtil.showToast(getActivityExp(), getResources().getString(R.string.currently_calling));
//                    return ;
//                }
//                videoChat();
//                break;
            case R.id.iv_has_like:
                if (likeCanClick) {
                    likeCanClick = false;
                    VideoModel model = modelList.get(currentPosition);
                    if (model.getIs_click() == HAS_LIKE) {
                        like(model.getId(), 0);
                    } else {
                        likeCanClick = true;
                    }
                }
                break;
            case R.id.iv_like:
                if (likeCanClick) {
                    likeCanClick = false;
                    VideoModel model1 = modelList.get(currentPosition);
                    if (model1.getIs_click() != HAS_LIKE) {
                        like(model1.getId(), 1);
                        requestAnchorState(currentPosition);
                    } else {
                        likeCanClick = true;
                    }
                }
                break;
            case R.id.iv_back:
//                if (getActivityExp() instanceof DynamicVideoActivity){
//                    (getActivityExp()).onBackPressed();
//                }
                break;
            case R.id.iv_more:
//                MyBottomListPopupView myBottomListPopupView = new MyBottomListPopupView(getActivityExp());
//                myBottomListPopupView.setUserId(Integer.parseInt(modelList.get(currentPosition).getAnchor_id()));
//                myBottomListPopupView.setUserName(modelList.get(currentPosition).getNickname());
//                myBottomListPopupView.setImgUrl(modelList.get(currentPosition).getHead_image());
//                new XPopup.Builder(getActivityExp())
//                        .asCustom(myBottomListPopupView)
//                        .show();
                break;
            case R.id.btn_get_noble:
//                SubscribeMemberActivity.start(getActivityExp(), FunctionConstants.NewOrderFromType.VIDEO_NOBLE, 0);
                break;
            case R.id.iv_user_icon:
//                if(mForbidHead != 1)
//                    UserDetailsActivity.start(getActivityExp(), modelList.get(currentPosition).getAnchor_id(), 2);
                break;
            case R.id.btn_charge:
//                if(ShareDataManager.getInstance().isFreeVideo()) {
//                    requestVideoStatus();
//                } else {
//                    NeedCoinsFragment needCoinsFragment = NeedCoinsFragment.newInstance(PreferenceHelper.getInstance().getVideoCount(), NeedCoinsFragment.TYPE_VIDEO, new NeedCoinsFragment.OnCallBack() {
//                        @Override
//                        public void onUnlock() {
//                            requestVideoStatus();
//                        }
//                    });
//                    if(getActivityExp() != null && getActivityExp() instanceof FragmentActivity)
//                        DialogFragmentHelper.showDialogFragment(((FragmentActivity)getActivityExp()).getSupportFragmentManager(), needCoinsFragment);
//                }
                break;
            case R.id.imgv_chat:
//                FunctionUtils.gotoMessage(getActivityExp(), mUserInfo);
                break;
        }
    }

    private void requestVideoStatus() {
        if(modelList != null && modelList.size() > currentPosition) {
            VideoModel model = modelList.get(currentPosition);
            requestChargeVideo(model, currentPosition);
        }
    }

    private void setActionView(VideoModel model) {
        tv_like_count.setText(model.getClick() + "");
        iv_has_like.setVisibility(model.getIs_click() == 1 ? View.VISIBLE : View.INVISIBLE);
//        ImageHelper.loadCircleImage(model.getHead_image(), iv_user_icon, R.drawable.ic_head_female);
        iv_has_like.setScaleX(1);
        iv_has_like.setScaleY(1);
        name = model.getNickname();
        headImg = model.getHead_image();
        tv_desc.setText(model.getSignature());
        tv_user_name.setText(model.getNickname());
        status = model.getStatus(); // 全部是审核通过状态
        identity = model.getIs_identity();
        follow = model.getIs_follow() == 1;
        userId = model.getAnchor_id();
        try {
            videoPrice = Integer.parseInt(!TextUtils.isEmpty(model.getVideo_price()) ? model.getVideo_price() : "0");
            level = Integer.parseInt(!TextUtils.isEmpty(model.getLevel()) ? model.getLevel() : "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(AppConfig.isDebug()) {
//            String str = model.getIs_show() == 0 ? "收费视频 " : "免费视频 ";
//            String str1 = VideoManager.getInstance().hasPlayed(model.getId()) ? "已购买" : "未购买";
//            String str2 = " 剩余免费次数: " + ShareDataManager.getInstance().getToday_private_video_count() + " 次";
//            tvDebug.setText(str + str1 + str2);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        AppLog.i("DynamicTabFragment", "==================> DynamicLeftFragment onPause");
        playerOnPause();
        clearMsg();
    }

    @Override
    public void onResume() {
        super.onResume();
//        AppLog.i("DynamicTabFragment", "==================> DynamicLeftFragment onResume");
        if(!isFirstStart) {
            renfreshFollow();
            refreshVideoState();
            isFirstStart = false;
        }
        if (mIjkVideoView != null) {
            resumingPlay();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        try {
//            if (EventBus.getDefault().isRegistered(this)){
//                EventBus.getDefault().unregister(this);
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        if (mIjkVideoView != null) {
            mIjkVideoView.release();
        }
    }

    private void follow() {
//        if (userId.equals(String.valueOf(AppUser.getInstance().getUser().getId()))) {
//            ApplicationUtil.showToast(getActivityExp(), R.string.can_not_follow_self);
//            return;
//        }
//        CommonUtils.follow(getActivityExp(), userId, String.valueOf(AppUser.getInstance().getUser().getId()), CONCERN, AttendStatusEvent.TYPE_FROM_VIDEO_CHECK, new IBaseListener<Integer>() {
//            @Override
//            public void onSuccess(Integer obj) {
//                if(obj == 1) {
//                    iv_follow.setVisibility(View.INVISIBLE);
//                    follow = true;
//                }
//            }
//
//            @Override
//            public void onFail(int code, String desc) {
//
//            }
//        });
    }

    private void videoChat() {
//        if(LokApp.getInstance().getMainActivity() != null
//                && LokApp.getInstance().getMainActivity().getChatManager() != null
//                && CallStatusManager.getInstance().isCurrentCalling()) {
//            ApplicationUtil.showToast(getActivityExp(), getResources().getString(R.string.currently_calling));
//            return ;
//        }
//
//        CallUtils.goVideoChat(getActivityExp(), getCall(Call.VIDEO), true,
//                videoPrice, FunctionConstants.NewOrderFromType.VIDEO_NOBLE, Integer.parseInt(userId), 1, identity, modelList.get(currentPosition).getIs_supreme()==1, new VideoChatListener() {
//                    @Override
//                    public void notOnline() {
//                        showGuideDialog(Call.VIDEO);
//                    }
//
//                    @Override
//                    public void notIdentity() {
//
//                    }
//                });
    }

    //引导去匹配
    private void showGuideDialog(int type) {
//        GuideUtil guideUtil = new GuideUtil(getActivityExp(), getCall(type).getAVideo(), type);
//        guideUtil.showGuideDialog(false);
    }

//    private Call getCall(int callType) {
//        AVideo avideo = new AVideo();
//        avideo.setName(name);
//        avideo.setId(String.valueOf(userId));
//        avideo.setAvatar(headImg);
//        avideo.setLevel(level);
//        if (AppUser.getInstance().getUser().getSex() == User.FEMALE) {
//            avideo.setSex(User.MALE);
//            avideo.setVideoPrice(AppUser.getInstance().getUser().getVideoPrice());
//            avideo.setAudioPrice(AppUser.getInstance().getUser().getAudioPrice());
//        } else {
//            avideo.setSex(User.FEMALE);
//            avideo.setVideoPrice(videoPrice);
//        }
//        Call call = new Call();
//        call.setCallId(-1);
//        call.setCallMass(0);
//        call.setAVideo(avideo);
//        call.setReceiver(false);
//        call.setType(callType);
//        return call;
//    }

    @SuppressLint({"ObjectAnimatorBinding", "ResourceType"})
    private void initAnim() {
//        likeAnimSet = new AnimatorSet();
//        Animator alphaIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 0f);
//        Animator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.6f, 1f, 1.4f, 1f, 1.2f, 1);
//        Animator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.6f, 1f, 1.4f, 1f, 1.2f, 1);
//        alphaIn.setDuration(1500);
//        alphaIn.setTarget(iv_red_like);
//        scaleX.setTarget(iv_red_like);
//        scaleY.setTarget(iv_red_like);
//        scaleX.setDuration(1000);
//        scaleY.setDuration(1000);
//        likeAnimSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//                iv_red_like.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                iv_red_like.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//        likeAnimSet.playTogether(alphaIn, scaleX, scaleY);
//        hasLikeShowAnim = AnimatorInflater.loadAnimator(getActivityExp(), R.anim.scale_show);
//        //显示的调用invalidate
//        iv_has_like.invalidate();
//        hasLikeShowAnim.setTarget(iv_has_like);
//        hasLikeShowAnim.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//                iv_has_like.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                likeCanClick = true;
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//        hasLikeHideAnim = AnimatorInflater.loadAnimator(getActivityExp(), R.anim.scale_hide);
//        //显示的调用invalidate
//        iv_has_like.invalidate();
//        hasLikeHideAnim.setTarget(iv_has_like);
//        hasLikeHideAnim.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                iv_has_like.setVisibility(View.INVISIBLE);
//                likeCanClick = true;
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
    }

    private void like(int videoId, final int type) {
//        User user = AppUser.getInstance().getUser();
//        if (user == null) {
//            return;
//        }
//        recordOperteVideo(type, -1);
    }

    private void recordOperteVideo(int fabulous,int finish){
//        int videoId = modelList.get(currentPosition).getId();
//        AsyncHttpHelper.RequestParams params = new AsyncHttpHelper.RequestParams();
//        params.put("video_id", videoId + "");//
//        params.put("anchor_id", modelList.get(currentPosition).getAnchor_id());//发布视频主播id
//        if (fabulous > -1) {
//            params.put("fabulous", fabulous + "");//点赞（1点赞0取消）
//        }
//        if (finish > -1) {
//            if (mVideoFinishedMap.containsKey(videoId)) {//已经记录过播放完成
//                return;
//            }
//            params.put("finish", finish + "");//视频是否完播
//        }
//        AppAsyncHttpHelper.httpsPost(Constants.OPERTE_VIDEO, params, new AsyncHttpHelper.OnHttpListener<JSONObject>() {
//            @Override
//            public void onHttpListener(boolean httpSuccess, JSONObject obj) {
//                if (fabulous > -1) {//点赞处理
//                    handleResponse(httpSuccess, obj, fabulous, videoId);
//                }
//                if (!httpSuccess) {
//                    ApplicationUtil.showToast(getActivityExp(), getActivityExp().getResources().getString(R.string.network_err));
//                    return;
//                }
//                JSONObject data = obj.optJSONObject("data");
//                int result = data.optInt("result");
//                if (result == 1) {
//                    if (finish > -1) {
//                        if (!mVideoFinishedMap.containsKey(videoId)) {//已经记录过播放完成
//                            mVideoFinishedMap.put(videoId, modelList.get(currentPosition).getUrl());
//                        }
//                    }
//                }
//            }
//        });
    }

    private void seeVideo(int index){
//        if (modelList.size() - 1 < index) {
//            return;
//        }
//        VideoModel videoModel = modelList.get(index);
//        int videoId = videoModel.getId();
//
////        if (mVideoSeenMap.containsKey(videoId) && isAutoPlay()) {//未购买视频播放前后都要请求see_video
////            return;
////        }
////        if (!mVideoSeenMap.containsKey(videoId)) {//已经记录过播放完成
////            mVideoSeenMap.put(videoId, videoModel.getUrl());
////        }
//        AsyncHttpHelper.RequestParams params = new AsyncHttpHelper.RequestParams();
//        params.put("video_id", videoId + "");//
//        params.put("anchor_id", videoModel.getAnchor_id());//发布视频主播id
//        AppAsyncHttpHelper.httpsPost(Constants.SEE_VIDEO, params, new AsyncHttpHelper.OnHttpListener<JSONObject>() {
//            @Override
//            public void onHttpListener(boolean httpSuccess, JSONObject obj) {
//                //result 0.视频私有并且未在购买列表 1.可观看
//                //finish_time：完播视频秒数
//            }
//        });
    }

//    private void handleResponse(boolean httpSuccess, JSONObject obj, int type, int videoId) {
//        if (!httpSuccess) {
//            ApplicationUtil.showToast(getActivityExp(), getActivityExp().getResources().getString(R.string.network_err));
//            likeCanClick = true;
//            return;
//        }
//        JSONObject data = obj.optJSONObject("data");
//        int result = data.optInt("result");
//
//        int clickCount = modelList.get(currentPosition).getClick();
//        if (result == 1) {
//            modelList.get(currentPosition).setIs_click(type);
//            if (type == 1) {
////                hasLikeShowAnim.start();
//                ApplicationUtil.playSvga("anim/video_praise.svga", svgaPraise, 0, 1, true, new SVGACallback() {
//                    @Override
//                    public void onFinished() {
//                        svgaPraise.setVisibility(View.GONE);
//                        iv_has_like.setScaleX(1);
//                        iv_has_like.setScaleY(1);
//                        iv_has_like.setVisibility(View.VISIBLE);
//                        likeCanClick = true;
//                    }
//
//                    @Override
//                    public void onPause() {
//                    }
//
//                    @Override
//                    public void onRepeat() {
//                    }
//
//                    @Override
//                    public void onStep(int i, double v) {
//                    }
//                });
//                clickCount++;
//                likeAnimSet.start();
//            } else {
//                hasLikeHideAnim.start();
//                clickCount--;
//
//            }
//            EventBus.getDefault().post(new MediaUpdateEvent(videoId, type, MediaUpdateEvent.TYPE_FROM_VIDEO)); // 这个应该是通知其他列表状态
//            modelList.get(currentPosition).setClick(clickCount);
//            tv_like_count.setText(clickCount + "");
//
//        } else {
//            likeCanClick = true;
//        }
//    }

    private void requestData(boolean isLoadMore) {
        requestData("", isLoadMore);
    }

    private void requestData(String noSeeIdStrs, boolean isLoadMore) {
//        AsyncHttpHelper.RequestParams params = new AsyncHttpHelper.RequestParams();
//        params.put("type", mRequestType + "");//1.discover 2.new 3.private
//        if (!TextUtils.isEmpty(noSeeIdStrs) && mRequestType==1) {
//            params.put("skip_id", noSeeIdStrs);
//        }
//        AppAsyncHttpHelper.httpsGet(Constants.ANCHOR_VIDEO_LIST, params, new AsyncHttpHelper.OnHttpListener<JSONObject>() {
//            @Override
//            public void onHttpListener(boolean httpSuccess, JSONObject obj) {
//                Log.e("dynamic_list", obj.toString());
//                handleResponse(httpSuccess, obj, isLoadMore);
//            }
//        });
    }

//    private void handleResponse(boolean httpSuccess, JSONObject obj,boolean isLoadMore) {
//        if (isLoadMore){
//            isLoadMoreing = false;
//        }
//        if (getActivityExp() == null) {
//            return;
//        }
//        if (!httpSuccess) {
//            rlytEmpty.setVisibility(modelList.size() > 0 ? View.GONE : View.VISIBLE);
//            ApplicationUtil.showToast(getActivityExp(), getActivityExp().getResources().getString(R.string.network_err));
//            return;
//        }
//        if (obj.optInt("code") != Constants.HTTP_OK) {
//            rlytEmpty.setVisibility(modelList.size() > 0 ? View.GONE : View.VISIBLE);
//            ApplicationUtil.showToast(getActivityExp(), obj.optString("desc", getActivityExp().getResources().getString(R.string.network_err)));
//            return;
//        }
//        JSONObject data = obj.optJSONObject("data");
//        msgJsonAryStr = data.optString("msg");
//        thresholTime = data.optInt("finish_time");
//        if (isLoadMore) {
//            List<VideoModel> models = new Gson().fromJson(msgJsonAryStr, new TypeToken<List<VideoModel>>() {
//            }.getType());
//            if (modelList == null) {
//                modelList = new ArrayList<>();
//            }
//            hasNoMore = models.isEmpty();
//            for (int i = 0; i < models.size(); i++) {
//                if (!BlockManager.getInstance().isBlocked(models.get(i).getAnchor_id())) {
//                    modelList.add(models.get(i));
//                }
//            }
//            pageAdapter.notifyDataSetChanged();
//            rlytEmpty.setVisibility(modelList.size() > 0 ? View.GONE : View.VISIBLE);
//            msgJsonAryStr = "";
//        }
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mFragmentVisible = isVisibleToUser;
        if (isVisibleToUser) {
            if (mIjkVideoView != null) {
               playVideo();
            }
            renfreshFollow();
            refreshVideoState();
        } else {
            playerOnPause();
            clearMsg();
        }
    }

    public List<VideoModel> getModelList() {
        return modelList;
    }

    public void setVideoPlayStatus(boolean play){
        mFragmentVisible = play;
        if (play){
            if (mIjkVideoView != null) {
               playVideo();
            }
            renfreshFollow();
            refreshVideoState();
        }else{
            playerOnPause();
        }
    }

    private void renfreshFollow(){
//        if (FollowManager.getInstance().hasLocalRecord(userId)){
//            follow = FollowManager.getInstance().isFollowed(userId);
//        }
//        if (iv_follow != null){
//            iv_follow.setVisibility(follow ? View.GONE : View.VISIBLE);
//        }
    }

    private void refreshVideoState() {
//        if (modelList != null && modelList.size() > 0 && modelList.size() > currentPosition) {
//            VideoModel model = modelList.get(currentPosition);
//            initPlayType(model);
//            if(mPlayType == TYPE_NEED_VIP) {
//                ll_noble.setVisibility(View.VISIBLE);
//                ll_charge.setVisibility(View.GONE);
//            } else if(mPlayType == TYPE_NEED_COINS) {
//                if(ShareDataManager.getInstance().isShowTipToday()) {
//                    ll_noble.setVisibility(View.GONE);
//                    ll_charge.setVisibility(ShareDataManager.getInstance().isFreeVideo() ? View.GONE : View.VISIBLE);
//                } else {
//                    requestChargeVideo(model, currentPosition);
//                }
//            } else {
//                ll_noble.setVisibility(View.GONE);
//                ll_charge.setVisibility(View.GONE);
////                GlideUtil.loadImage(recyclerPageController.getThumb(), modelList.get(currentPosition).getFirst_photo());
//            }
//        }
    }

    private void playVideo(){
        if (mIjkVideoView != null) {
            if (mIjkVideoView.getCurrentPlayState() != VideoView.STATE_PAUSED) {
                mIjkVideoView.release();
            }
            mIjkVideoView.start();
            iv_play.setVisibility(View.GONE);
            ll_noble.setVisibility(View.GONE);
            ll_charge.setVisibility(View.GONE);
//            if (modelList != null && modelList.size() > currentPosition) {
//                VideoModel model = modelList.get(currentPosition);
//                FunctionUtils.uploadUserBehavior(model.getAnchor_id(), String.valueOf(model.getId()));
//            }
        }
    }

    private void resumingPlay(){
        if (!isAutoPlay()){
            return;
        }
//        if (fromType == FROM_HOME_TAB){
//            if (mFragmentVisible){
//                if (mIjkVideoView.getCurrentPlayState() == BaseVideoView.STATE_PLAYBACK_COMPLETED) {
//                    mIjkVideoView.replay(true);
//                } else if (mIjkVideoView.getCurrentPlayState() == BaseVideoView.STATE_IDLE) {
//                    mIjkVideoView.start();
//                } else {
//                    mIjkVideoView.resume();
//                }
//            }
//        } else {
//            if (AppUser.getInstance().getUser().getNoble() == 1
//                    || (modelList != null && modelList.size() > currentPosition && modelList.get(currentPosition) != null && modelList.get(currentPosition).getIs_show() == 1)) {
//                if (mIjkVideoView.getCurrentPlayState() == BaseVideoView.STATE_PLAYBACK_COMPLETED) {
//                    mIjkVideoView.replay(true);
//                } else if (mIjkVideoView.getCurrentPlayState() == BaseVideoView.STATE_IDLE) {
//                    mIjkVideoView.start();
//                } else {
//                    mIjkVideoView.resume();
//                }
//            }
//        }
    }

    private void showDialog(final VideoModel model){
        if (getActivityExp() == null){
            return;
        }
//        RechargeDialogFragment fragment = RechargeDialogFragment.newInstance(R.drawable.gz_hg,getString(R.string.hint_vip_for_private_video),getString(R.string.jxds),getString(R.string.go_get_noble),
//                RechargeDialogFragment.JUMP_TYPE_NOBLE,FunctionConstants.NewOrderFromType.VIDEO_NOBLE);
//        DialogFragmentHelper.showDialogFragment(getFragmentManager(),fragment);
//        ApplicationUtil.goToSubscribeActivity(getContext(), FunctionConstants.NewOrderFromType.VIDEO_NOBLE);
    }

    /**
     * 参数sub_type=44表示解锁私密视频，result=1表示解锁成功，此时会返回is_free字段，is_free=0表示付费解锁，
     * is_free=1表示免费解锁，同时这个接口还会返回result=8，表示用户已经购买过当前视频了
     * @param model
     * @param pos
     */
    private void requestChargeVideo(final VideoModel model, final int pos) {
//        AsyncHttpHelper.RequestParams params = new AsyncHttpHelper.RequestParams();
//        params.put("user_id", model.getAnchor_id());
//        params.put("sub_type", fromType == FROM_CHAT ? "119" : "44");
//        params.put("money", PreferenceHelper.getInstance().getVideoCount()+"");
//        params.put("extend", model.getId() + "");
//        AppAsyncHttpHelper.httpsPost(Constants.CHARGE, params, new AsyncHttpHelper.OnHttpListener<JSONObject>() {
//            @Override
//            public void onHttpListener(boolean httpSuccess, JSONObject obj) {
//                videoLog(TAG, "=========> charge_video:" + obj);
//                if (getActivityExp()== null || obj == null){
//                    return;
//                }
//                if (!httpSuccess) {
//                    ApplicationUtil.showToast(getActivityExp(), getActivityExp().getResources().getString(R.string.network_err));
//                    return;
//                }
//                if (obj.optInt("code") != Constants.HTTP_OK) {
//                    ApplicationUtil.showToast(getActivityExp(), obj.optString("desc", getActivityExp().getResources().getString(R.string.network_err)));
//                }
//                JSONObject data = obj.optJSONObject("data");
//                if(data == null)
//                    return ;
//                int result = data.optInt("result");
//                JSONObject msg = data.optJSONObject("msg");
//                videoLog(TAG, "=========> charge_video pos:" + pos + "   currentPosition: " + currentPosition);
//                if (result == 1 || result == 8) {
//                    if(pos == currentPosition) { // 预防返回和当前视频对应不上
//                        modelList.get(currentPosition).setIs_follow(1);
//                        mPlayType = TYPE_NORMAL;
////                        preStartPlay(model);
//                        playVideo();
//                        if (fromType == FROM_CHAT){
//                            EventBus.getDefault().post(new VideoChangeEvent(model.getId(),currentPosition));//通知私聊界面视频改变状态
//                        } else {
//                            VideoManager.getInstance().addVideo(model.getId());
//                        }
//                        EventBus.getDefault().post(new RefreshVideoEvent(model.getAnchor_id())); // 这个应该是通知其他列表状态
//
//                        if(msg != null && msg.optInt("is_free") == 1) {
//                            ShareDataManager.getInstance().consumeVideoCount();
//                        } else { // 当天没有付费查看视频，提示一直存在 is_free=1表示免费解锁
//                            videoLog(TAG, "=========> save showTipToday1:" + ShareDataManager.getInstance().isShowTipToday());
//                            ShareDataManager.getInstance().addVideoCheckToday();
//                            videoLog(TAG, "=========> save showTipToday2:" + ShareDataManager.getInstance().isShowTipToday());
//                        }
//                    }
//                } else if (result == 2) { // 未登录
//                    LokApp.getInstance().showAccountDialog();
//                } else if (result == 6) { // 金币不足
//                    ApplicationUtil.goToMembershipActivity(getActivityExp(), FunctionConstants.NewOrderFromType.VIDEO_GOLD, Integer.parseInt(model.getAnchor_id()), true);
//                } else if(result == -2){ // -2表示用户非vip，需要购买vip
//                    SubscribeMemberActivity.start(getActivityExp(), FunctionConstants.NewOrderFromType.VIDEO_NOBLE, 0);
//                } else {
//                    ApplicationUtil.showToast(getActivityExp(), obj.optString("desc", getActivityExp().getResources().getString(R.string.network_err)));
//                }
//            }
//        });
    }

//    @Subscribe
//    public void onEventBlockChange(BlockBean blockBean) {
//        if (fromType != FROM_RECOMMENT_LIST && fromType != FROM_VIP_LIST){
//            requestData(false);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onFreeVideoCountUpdate(FreeVideoCountEvent freeVideo) {
//        if(svgaFree != null)
//            svgaFree.setVisibility(ApplicationUtil.isFreeVideoCount() ? View.VISIBLE : View.GONE);
//    }

    /**********************************************************************************************
     * 测试调试使用
     *********************************************************************************************/
    private static boolean isLog = false;
    
    public static void videoLog(String tag, String msg) {
        if(isLog) {
//            AppLog.i(tag, msg);
        }
    }

    private final static int LogUtilMaxLen = 1024 * 2;

    public static void printMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (msg.length() <= LogUtilMaxLen) {
//            AppLog.i(TAG, msg);
            return;
        }

        String subStr = msg.substring(0,LogUtilMaxLen);
        Log.i(TAG,subStr);
        String nextStr = msg.substring(LogUtilMaxLen);

        //递归调用
        printMsg(nextStr);
    }
}

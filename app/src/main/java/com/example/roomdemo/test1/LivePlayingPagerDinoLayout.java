package com.example.roomdemo.test1;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.roomdemo.R;


public class LivePlayingPagerDinoLayout extends FrameLayout implements View.OnClickListener, View.OnTouchListener{
    private final String TAG = LivePlayingPagerDinoLayout.class.getSimpleName();

    private OnLivingPagerDinoListener onLivingPagerDinoListener;
    private View viewDinoUp, viewDinoDown;
    private Rect messageListDinoLocation;
    private long moveDinoRecordTime;
    private final PointF preDinoPoint = new PointF();
    private final int pageDinoDistance = DinoScreenUtils.dip2px(100);
    private final PointF downDinoPoint = new PointF();
    private boolean isFixedAsDinoDown;
    private boolean isDinoUp;   // 向上，反之向下，向上是下一个，
    private final PointF distanceDinoPoint = new PointF();
    private ViewDinoHolder upDinoHolder, downDinoHolder;
    private final PointF lastDinoPoint = new PointF();
    private boolean moveDinoResult; // 移动的结果，是否触发了有效的上下移动

    public LivePlayingPagerDinoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LivePlayingPagerDinoLayout(Context context) {
        super(context);
        init();
    }

    public LivePlayingPagerDinoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void checkLocation(/*IDinoLivePlayingRoom activity*/){
//        playingDinoActivity = activity;
//        messageListDinoLocation = null;
//        LiveDinoChatMessage<?> message2 = ((LiveDinoActivity<?, ?>)activity).getDinoLiveChatMessage();
//        if(message2 != null){
//            ViewGroup parent = (ViewGroup) message2.getDinoParent();
//            if(parent != null){
//
//                if(messageChatDinoListView == null)
//                    messageChatDinoListView = parent.findViewById(R.id.rv_dino_public_chat);
//
//                if (messageChatDinoListView != null) {
//                    messageChatDinoListView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            int[] loc = new int[2];
//                            messageChatDinoListView.getLocationOnScreen(loc);
//                            if(loc[1] == 0) {
//                                messageChatDinoListView.post(this);
//                                return;
//                            }
//
//                            messageListDinoLocation = new Rect(loc[0]
//                                    , loc[1]
//                                    , loc[0] + messageChatDinoListView.getMeasuredWidth()
//                                    , loc[1] + messageChatDinoListView.getMeasuredHeight());
//
//                        }
//                    });
//
//                    messageChatDinoListView.addOnInterceptViewListener(this);
//                }
//            }
//        }

        // 添加上下两个页面
        if(viewDinoDown == null && viewDinoUp == null) {
            viewDinoUp = LayoutInflater.from(getContext()).inflate(R.layout.dino_layout_playingpager_subview, this, false);
            viewDinoDown = LayoutInflater.from(getContext()).inflate(R.layout.dino_layout_playingpager_subview, this, false);
            TextView tvName = viewDinoDown.findViewById(R.id.tv_dino_name);
            FrameLayout.LayoutParams nameLayoutParams = (LayoutParams) tvName.getLayoutParams();
            nameLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            nameLayoutParams.bottomMargin = 0;
            nameLayoutParams.topMargin = DinoScreenUtils.dip2px(28);
            if(viewDinoUp.getParent() == null) {
                addView(viewDinoUp);
            }
            if(viewDinoDown.getParent() == null)
                addView(viewDinoDown);

            upDinoHolder = new ViewDinoHolder(viewDinoUp.findViewById(R.id.tv_dino_name)
                    , viewDinoUp.findViewById(R.id.iv_dino_header));

            downDinoHolder = new ViewDinoHolder(tvName, viewDinoDown.findViewById(R.id.iv_dino_header));

            notifyLoadImageResource();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setOnLivingPagerDinoListener(OnLivingPagerDinoListener onLivingPagerDinoListener) {
        this.onLivingPagerDinoListener = onLivingPagerDinoListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            Log.iTag(TAG, "dispatchTouchEvent:" + ev);
            if(viewDinoDown == null || viewDinoUp == null)
                return super.dispatchTouchEvent(ev);
            switch (ev.getAction()) {
                case ACTION_DOWN:
                    downDinoPoint.set(ev.getX(), ev.getY());
                    isFixedAsDinoDown = false;
                    moveDinoResult = false;

//                    if (playingDinoActivity != null) {
//                        try {
//                            if (!playingDinoActivity.isDinoCanDragAnchor()
//                                    || playingDinoActivity.isDinoChatListCanScroll(messageListDinoLocation
//                                    , ev.getY() < preDinoPoint.y, ev.getRawX(), ev.getRawY())
//                                    || anyMaskIsShown()
//                                    || playingDinoActivity.isTouchOnAction(ev.getRawX(), ev.getRawY())) {
//                                Log.iTag(TAG + "_action", "Don't block all events");
//                                isFixedAsDinoDown = true;
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            isFixedAsDinoDown = true;
//                        }
//                    }
                    isFixedAsDinoDown = false;
                    break;
                case ACTION_MOVE:
                    if (!isFixedAsDinoDown) {
                        if (moveDinoRecordTime != ev.getDownTime()) {
                            if (moveValid(getContext(), downDinoPoint, ev)) {
                                moveDinoResult = isUpDownMove(getContext(), downDinoPoint, ev);
                                isDinoUp = downDinoPoint.y > ev.getY();

                                // 如果没有足够的距离触发下一页，跳过
                                moveDinoResult = moveDinoResult
                                        && (isDinoUp ? ev.getY() : getMeasuredHeight() - ev.getY()) > pageDinoDistance;

                                // 是否允许切换
                                if (onLivingPagerDinoListener != null) {
                                    moveDinoResult = moveDinoResult
                                            && (isDinoUp ? onLivingPagerDinoListener.isCanDragUp() : onLivingPagerDinoListener.isCanDragDown());
                                }

//                                if (moveDinoResult && playingDinoActivity != null)
//                                    playingDinoActivity.notifyDinoDragStart(isDinoUp);

                                moveDinoRecordTime = ev.getDownTime();
                                distanceDinoPoint.set(ev.getX(), ev.getY());

                                Log.iTag(TAG + "_action", (moveDinoResult ? "" : "not") + "Block all events");
                            }
                        }

                        if (moveDinoResult) {

                            final View moveView = isDinoUp ? viewDinoDown : viewDinoUp;
                            int orgTransY = isDinoUp ? getMeasuredHeight() : -getMeasuredHeight();
                            if (moveView.getVisibility() != VISIBLE) {
                                moveView.setTranslationY(orgTransY);
                                moveView.setAlpha(1f);
                                moveView.setScaleX(1f);
                                moveView.setScaleY(1f);
                                moveView.setVisibility(VISIBLE);
                                bringChildToFront(moveView);
                            }
                            float disEvent = ev.getY() - distanceDinoPoint.y;
                            float disSubTransY = orgTransY + disEvent;
                            disSubTransY = isDinoUp ? Math.min(orgTransY, disSubTransY) : Math.max(orgTransY, disSubTransY);
                            if (disSubTransY == orgTransY) {
                                distanceDinoPoint.set(ev.getX(), ev.getY());
                                notifyMaskDisappear(true);
                            }

                            moveView.setTranslationY(disSubTransY);
                        }
                    }
                    break;
                case ACTION_CANCEL:
                case ACTION_UP:
                    if (!isFixedAsDinoDown && moveDinoResult) {
                        isFixedAsDinoDown = true;// 预防连带事件，比如smartlayout的上滑下滑，下次down事件会被重新判断
                        final View moveView = isDinoUp ? viewDinoDown : viewDinoUp;
                        final ViewDinoHolder viewHolder = isDinoUp ? downDinoHolder : upDinoHolder;
                        // 移动了多少trasY的距离，正数
                        float transYSub = getMeasuredHeight() - Math.abs(moveView.getTranslationY());
                        if ((isDinoUp ? preDinoPoint.y > ev.getY() : preDinoPoint.y < ev.getY())
                                && transYSub > pageDinoDistance) {

                            if (viewHolder != null
                                    && viewHolder.dinoName != null)
                                viewHolder.dinoName.setVisibility(GONE);

                            AnimatorSet animatorSetTotal = new AnimatorSet();
                            // 执行动画显示
                            int animTransY = Float.valueOf(Math.abs(moveView.getTranslationY())).intValue();
                            ObjectAnimator animator = ObjectAnimator.ofFloat(moveView, "translationY", 0);
                            animator.setDuration(Float.valueOf(((float) animTransY / DinoScreenUtils.dip2px(1)) * 0.6f).intValue());
                            animator.setInterpolator(new DecelerateInterpolator());
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (onLivingPagerDinoListener != null)
                                        onLivingPagerDinoListener.changeRoomByPage(isDinoUp);
                                }

                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    if (onLivingPagerDinoListener != null)
                                        onLivingPagerDinoListener.startChangeRoomByPage(isDinoUp);
                                }
                            });

                            ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(moveView, "alpha", 0);
                            ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(moveView, "scaleX", 1.3f);
                            ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(moveView, "scaleY", 1.3f);

                            AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.setStartDelay(200);
                            animatorSet.setDuration(250);
                            animatorSet.setInterpolator(new AccelerateInterpolator());
                            animatorSet.playTogether(animatorAlpha, animatorScaleX, animatorScaleY);

                            animatorSet.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    notifyMaskDisappear();
                                }
                            });

                            animatorSetTotal.play(animatorSet).after(animator);

                            animatorSetTotal.start();
                        } else if (transYSub > 0) {
                            // 执行动画隐藏
                            float animValue = isDinoUp ? getMeasuredHeight() : -getMeasuredHeight();
                            ObjectAnimator animator = ObjectAnimator.ofFloat(moveView, "translationY", animValue);
                            animator.setDuration(Float.valueOf((transYSub / DinoScreenUtils.dip2px(1)) * 0.6f).intValue());
                            animator.setInterpolator(new DecelerateInterpolator());
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    moveDinoResult = false;
                                    notifyMaskDisappear();
                                }

                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                }
                            });
                            animator.start();
                        }
                    }
                    break;
            }

            preDinoPoint.set(lastDinoPoint.x, lastDinoPoint.y);
            lastDinoPoint.set(ev.getX(), ev.getY());

            return (moveDinoResult || anyMaskIsShown()) || super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void init(){
        try {
//            EventBus.getDefault().register(this);
        } catch (Exception e){
            e.printStackTrace();
        }
        checkLocation();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventDinoCheckLivingResult(LiveDinoEvent.CheckLivingDinoResult event) {
//        Log.iTag("CheckLivingResult" + "_layout", "receive next anchor change event");
//        if(event != null){
//            View view = event.isNext ? viewDinoDown : viewDinoUp;
//            if(event.isChanged){
//                Log.iTag("CheckLivingResult", "live room has changed ");
//                if(view.getVisibility() != VISIBLE) {
//                    notifyLoadImageResource();
//                    Log.iTag("CheckLivingResult", "view has gone，Reload avatar resources");
//                }
//            } else {
//                Log.iTag("CheckLivingResult", "live room hasn't changed");
//                notifyLoadImageResource();
//            }
//        }
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.iTag(TAG, "onInterceptTouchEvent:" + ev);
        return super.onInterceptTouchEvent(ev);
    }

    private boolean anyMaskIsShown(){
        return (viewDinoUp != null && viewDinoUp.getVisibility() == VISIBLE)
                || (viewDinoDown != null && viewDinoDown.getVisibility() ==VISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            Log.iTag(TAG, "onTouchEvent:" + event);
//            return super.onTouchEvent(event);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean moveValid(Context context, PointF dpoint, MotionEvent event){
        int touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        float disX = Math.abs(event.getX() - dpoint.x);
        float disY = Math.abs(event.getY() - dpoint.y);
        return disX > touchSlop || disY > touchSlop;
    }

    public void notifyMaskDisappear(boolean result){
        moveDinoResult = result;
        if(viewDinoDown != null) {
            viewDinoDown.setVisibility(INVISIBLE);
            viewDinoDown.setTranslationY(getMeasuredHeight());
        }
        if(viewDinoUp != null) {
            viewDinoUp.setVisibility(INVISIBLE);
            viewDinoUp.setTranslationY(-getMeasuredHeight());
        }
        if(downDinoHolder != null){
            downDinoHolder.dinoName.setVisibility(VISIBLE);
        }
        if(upDinoHolder != null){
            upDinoHolder.dinoName.setVisibility(VISIBLE);
        }
        notifyLoadImageResource();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    public void notifyLoadImageResource(){
        if(onLivingPagerDinoListener != null)
            onLivingPagerDinoListener.loadImageResource(upDinoHolder, downDinoHolder);
    }

    public void notifyMaskDisappear(){
        notifyMaskDisappear(false);
    }

    public boolean isLoadFreshEnable(){
        return !isFixedAsDinoDown()
                && viewDinoUp != null
                && viewDinoUp.getVisibility() != VISIBLE
                && viewDinoDown != null
                && viewDinoDown.getVisibility() != VISIBLE;
    }

    public boolean isFixedAsDinoDown() {
        return isFixedAsDinoDown;
    }

    @Override
    protected void onDetachedFromWindow() {
//        try {
//            EventBus.getDefault().unregister(this);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        super.onDetachedFromWindow();
    }

    private boolean isUpDownMove(Context context, PointF dpoint, MotionEvent event){
        return moveValid(context, dpoint, event)
                && Math.abs(event.getX() - dpoint.x) < Math.abs(event.getY() - dpoint.y);
    }

    public static class ViewDinoHolder {
        public ImageView dinoHead;
        public TextView dinoName;

        public ViewDinoHolder(TextView dinoName, ImageView dinoHead) {
            this.dinoName = dinoName;
            this.dinoHead = dinoHead;
        }
    }

    public interface OnLivingPagerDinoListener {
        boolean isCanDragUp();
        void loadImageResource(ViewDinoHolder up, ViewDinoHolder down);
        boolean isCanDragDown();
        void startChangeRoomByPage(boolean isNext);
        void changeRoomByPage(boolean isNext);
    }
}

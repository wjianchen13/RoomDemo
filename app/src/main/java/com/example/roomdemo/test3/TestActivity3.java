package com.example.roomdemo.test3;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.roomdemo.R;
import com.example.roomdemo.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 暂未实现
 */
public class TestActivity3 extends AppCompatActivity {

    private int mCurrentItem;
    private int mLastPosition = -1;
    private TestViewManager mLiveProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        mLiveProxy = new TestViewManager(this);
        final List<TestBean3> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TestBean3 bean = new TestBean3("", "test" + i);
            datas.add(bean);
        }

        ViewPager2 vp = findViewById(R.id.vp2);
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        vp.setOffscreenPageLimit(1);
        TestAdapter3 adapter = new TestAdapter3(this, datas);
        vp.setAdapter(adapter);

        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                mCurrentItem = position;
                Utils.log("===================> onPageScrolled position: " + position + "  positionOffset: " + positionOffset + "  positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Utils.log("===================> onPageSelected position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        vp.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
//                Utils.log("transformPage page: " + page + "  pageId: " + page.getId() + "  position: " + position);
                handleChange(page, position);
            }
        });

    }

    /**
     * 当从下往上滑时，同时满足条件条件1和条件2，先remove再添加
     * 当从上往下滑时，满足条件2，在条件2内部remove再添加
     * @param page
     * @param position
     */
    private void handleChange(View page, float position) {
        ViewGroup viewGroup = (ViewGroup) page;
        Utils.log("setPageTransformer  viewId: " + viewGroup.getId() + "  position: " + position + "  mCurrentItem: " + mCurrentItem);
        if ((position < 0 && viewGroup.getId() != mCurrentItem)) { // 条件1
            Utils.log("==============================> position: " + position + "  viewGroup.getId(): " + viewGroup.getId() + "  mCurrentItem: " + mCurrentItem);
            // room_container 为视频播放的根布局 id
            View rootView = viewGroup.findViewById(R.id.room_container);
            if (rootView != null && rootView.getParent() != null && rootView.getParent() instanceof ViewGroup) {
                Utils.log("==============================> remove view1: ");
                ((ViewGroup) (rootView.getParent())).removeView(rootView);
                mLiveProxy.onRemove();
            }
        }

        // 满足此种条件，表明需要加载直播视频，以及聊天室了
        if (viewGroup.getId() == mCurrentItem && (position == 0) && mCurrentItem != mLastPosition) { // 条件2
            Utils.log("-------------------------------> position: " + position + "  viewGroup.getId(): " + viewGroup.getId() + "  mCurrentItem: " + mCurrentItem + "  mLastPosition：" + mLastPosition);
            View rootView = mLiveProxy.getRootView();
            if (rootView.getParent() != null && rootView.getParent() instanceof ViewGroup) {
                Utils.log("==============================> remove view2: ");
                ((ViewGroup) (rootView.getParent())).removeView(rootView);
                mLiveProxy.onRemove();
            }
            loadData(viewGroup, mCurrentItem);
        }
    }

    private void loadData(ViewGroup viewGroup, int currentItem) {
        mLastPosition = currentItem;
        viewGroup.addView(mLiveProxy.getRootView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLiveProxy.onAdd(currentItem + "");
    }

}
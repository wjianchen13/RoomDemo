package com.example.roomdemo.test2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;

import com.example.roomdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView+PagerSnapHelper 小视频界面
 */
public class TestActivity2 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.flyt_fragment, DynamicLeftFragment.newInstance(1, getTestData(), 2, 1, 1), "DynamicLeftFragment");
        fragmentTransaction.commitAllowingStateLoss();
    }

    public List<VideoModel> getTestData() {
        List<VideoModel> datas = new ArrayList<>();
        for(int i = 0; i < 10; i ++) {
//            VideoModel vm = new VideoModel(i, "https://media.w3.org/2010/05/sintel/trailer.mp4");
//            VideoModel vm = new VideoModel(i, "https://www.w3schools.com/html/movie.mp4");
            VideoModel vm = new VideoModel(i, "https://www.w3school.com.cn/example/html5/mov_bbb.mp4");
            datas.add(vm);

        }
        return datas;
    }


}
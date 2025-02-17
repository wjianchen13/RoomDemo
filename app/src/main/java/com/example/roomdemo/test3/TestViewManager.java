package com.example.roomdemo.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.roomdemo.R;

public class TestViewManager implements View.OnClickListener{

    private Context mContext;
    private View mRootView;
    private TextView tvTest;
    private Button btnShow;
    private Button btnShow1;
    private EditText edtvInput;

    public TestViewManager(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        // 播放器根布局
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.view_test3_root, null);
        tvTest = mRootView.findViewById(R.id.tv_root);
        btnShow = mRootView.findViewById(R.id.btn_show);
        btnShow1 = mRootView.findViewById(R.id.btn_show1);
        edtvInput = mRootView.findViewById(R.id.edtv_input);
        btnShow.setOnClickListener(this);
        btnShow1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_show) {
            edtvInput.setVisibility(View.VISIBLE);
            edtvInput.setFocusable(true);
            edtvInput.setFocusableInTouchMode(true);
            edtvInput.requestFocus();
            edtvInput.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mContext != null) {
                        InputMethodManager imm = null;

                        imm = (InputMethodManager)
                                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                        if (imm == null) return;
                        imm.showSoftInput(edtvInput, InputMethodManager.SHOW_IMPLICIT);
//                        edtvInput.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                edtvInput.setFocusable(true);
//                                edtvInput.setFocusableInTouchMode(true);
//                                edtvInput.requestFocus();
//                            }
//                        }, 280);
                    }
                }
            }, 280);
        } else if(v.getId() == R.id.btn_show1) {
            edtvInput.setFocusable(true);
            edtvInput.setFocusableInTouchMode(true);
            edtvInput.requestFocus();
        }
    }

    public View getRootView() {
        return mRootView;
    }

    public void onRemove() {

    }

    public void onAdd(String str) {
        tvTest.setText(str);
    }

}

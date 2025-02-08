package com.example.roomdemo.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdemo.R;

import java.io.PipedOutputStream;
import java.util.List;

/**
 * Viewpager2 使用RecyclerView.Adapter
 */
public class TestAdapter3 extends RecyclerView.Adapter {

    private Context mContext;
    private List<TestBean3> mData;

    public TestAdapter3(Context context, List<TestBean3> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext != null) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_test3, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setItemViewId(position);
        viewHolder.tvTest.setText(mData.get(position).getTip());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgvTest;
        TextView tvTest;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvTest = itemView.findViewById(R.id.imgv_test);
            tvTest = itemView.findViewById(R.id.tv_test);
            tvTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, tvTest.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setItemViewId(int id) {
            itemView.setId(id);
        }
    }
}

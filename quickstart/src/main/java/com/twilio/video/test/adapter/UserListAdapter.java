package com.twilio.video.test.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twilio.video.Video;
import com.twilio.video.test.Model.DataItem;
import com.twilio.video.test.R;
import com.twilio.video.test.activity.VideoActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Activity activity;
    public List<DataItem> map_list;

    public UserListAdapter(Activity activity, List<DataItem> map_list) {
        this.activity = activity;
        this.map_list = map_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {

        int position = holder.getAdapterPosition();

        holder.edt_name.setText(map_list.get(position).getName());

        holder.edt_name.setOnClickListener(v->{
            Intent intent=new Intent(activity, VideoActivity.class);
            intent.putExtra("type","create");
            intent.putExtra("device_id",map_list.get(position).getDeviceId());
            activity.startActivity(new Intent(intent));
        });


    }

    @Override
    public int getItemCount() {
        return map_list == null ? 0 : map_list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView edt_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edt_name = itemView.findViewById(R.id.edt_name);

        }
    }
}

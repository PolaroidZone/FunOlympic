package com.creapptors.funolympic.adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creapptors.funolympic.BroadCast;
import com.creapptors.funolympic.R;
import com.creapptors.funolympic.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

public class home_adapter extends RecyclerView.Adapter<home_adapter.ViewHolder> {

    private Context context;
    private List<Video> videoList;

    public home_adapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.titleTextView.setText(video.getTitle());
        String eventId = video.getEid();
        String videoTitle = video.getTitle();
        String videoUrl = video.getVid();
        String desc = video.getDescription();
        // Load thumbnail image using Picasso library
        //Picasso.get().load(video.getThumbNail()).placeholder(R.drawable.thumbnail_def).into(holder.thumbnailImageView);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, BroadCast.class);
            intent.putExtra("vid", videoUrl);
            intent.putExtra("eid", eventId);
            intent.putExtra("title", videoTitle);
            intent.putExtra("desc", desc);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        ImageView thumbnailImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.home_Evt_name);
        }
    }
}

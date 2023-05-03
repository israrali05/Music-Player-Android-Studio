package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    ArrayList<AudioModel> songlist;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songlist, Context context) {
        this.songlist = songlist;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycler_view,parent,false);
       return new MusicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AudioModel SongData = songlist.get(position);
        holder.musictitle.setText(SongData.getTitle());
        if(MyNediaPlayer.currentIndex==position){
            holder.musictitle.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.musictitle.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyNediaPlayer.getInstance().reset();
                MyNediaPlayer.currentIndex  = holder.getAdapterPosition();
                Intent intent = new Intent(context,MusicPlayer.class);
                intent.putExtra("list", songlist);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView musictitle;
        ImageView iconview;

        public ViewHolder(View itemView){
            super(itemView);
          musictitle = itemView.findViewById(R.id.music_title_text);
          iconview = itemView.findViewById(R.id.icon_view);


        }
    }
}

package com.example.musicplayer;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
    TextView songtitle, totaltime, currenttime;
    SeekBar seekBar;
    ImageView pauseplay, nextbtn, previousbtn, musicicon;
    ArrayList<AudioModel> songlist;
    AudioModel currentsong;
    int x = 0;
    MediaPlayer mediaPlayer = MyNediaPlayer.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songtitle = findViewById(R.id.song_title);
        currenttime = findViewById(R.id.current_time);
        totaltime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pauseplay = findViewById(R.id.pause);
        nextbtn = findViewById(R.id.next);
        previousbtn = findViewById(R.id.previous);
        musicicon = findViewById(R.id.music_icon_big);
        songtitle.setSelected(true);

        songlist = (ArrayList<AudioModel>) getIntent().getSerializableExtra("list");
        setResourcesWithMusic();
        MusicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currenttime.setText(convertToMMS(mediaPlayer.getCurrentPosition()+""));
                    if(mediaPlayer.isPlaying()){
                        pauseplay.setImageResource(R.drawable.pause);
                        musicicon.setRotation(x=x+3);
                    }else {
                        musicicon.setRotation(0);
                        pauseplay.setImageResource(R.drawable.play);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void setResourcesWithMusic(){
        currentsong = songlist.get(MyNediaPlayer.currentIndex);
        songtitle.setText(currentsong.getTitle());
        totaltime.setText(convertToMMS(currentsong.getDuration()));
        pauseplay.setOnClickListener((v -> pausePlay()));
        nextbtn.setOnClickListener((v -> playNextSong()));
        previousbtn.setOnClickListener((v -> playPreviousSong()));
        playMusic();
    }
    private void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentsong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
    private void playNextSong(){
        if(MyNediaPlayer.currentIndex==songlist.size()-1)
            return;
        MyNediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }
    private void playPreviousSong(){
        if(MyNediaPlayer.currentIndex==0)
            return;
        MyNediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }
    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }
    public static String convertToMMS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        );
    }
}
package com.example.musicplayer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding Binding;
    ArrayList<AudioModel> songslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Check if permission to read external storage is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, read data from external storage here
        } else {
            // Permission is not granted, request permission
            int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 122;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }


        Binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = Binding.getRoot();
        setContentView(view);
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if (new File(songData.getPath()).exists()) {
                songslist.add(songData);
            }
            if (songslist.size() == 0) {
                Binding.noSongText.setVisibility(View.VISIBLE);
            } else {
                Binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                Binding.recyclerView.setAdapter(new MusicListAdapter(songslist, getApplicationContext()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Binding.recyclerView!=null){
            Binding.recyclerView.setAdapter(new MusicListAdapter(songslist, getApplicationContext()));
        }
    }
}
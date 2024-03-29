package com.example.onlymusic;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SongInfo> songInfos = new ArrayList<SongInfo>();
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;
    MediaPlayer mediaPlayer;
    Handler handler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        seekBar = findViewById(R.id.seekBar);


      //  songInfos s= new SongInfo("Scheap Thrills","sia",s);
        songAdapter=new SongAdapter(this,songInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songAdapter);


        songAdapter.setOnitemClickListener(new SongAdapter.OnitemClickListener() {
            @Override
            public void onItemClick(final Button b, View v, final SongInfo obj, int position) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {

                        if (b.getText().toString().equals("Stop")){

                            b.setText("Play");
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }else {


                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(obj.getSongUrl());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                    seekBar.setProgress(0);
                                    seekBar.setMax(mp.getDuration());

                                }
                            });
                            b.setText("Stop");}
                    }catch (IOException e){

                    }

                }
            };
            handler.postDelayed(r,100);

               }
        });



        CheckPermission();

        Thread t = new MyThread();
        t.start();

    }


    public class MyThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                if (mediaPlayer!= null){
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }} catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void CheckPermission() {
        if (Build.VERSION.SDK_INT>=23)
        {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);




        }else{
            finish();
        }
    }else {
            loadsongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode)
       {
           case 123:
               if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                   loadsongs();
               }else {
                   Toast.makeText(this,"Permissionn Denied",Toast.LENGTH_SHORT).show();
                   CheckPermission();

               }
               break;

               default:
                   super.onRequestPermissionsResult(requestCode, permissions, grantResults);


       }







          }

    private void loadsongs() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
             do {
                 String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                 String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                 String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                SongInfo s = new SongInfo(name,artist,url);
                songInfos.add(s);

             }while (cursor.moveToFirst());

            }
            cursor.close();
            songAdapter = new SongAdapter(this,songInfos);




        }

    }
}

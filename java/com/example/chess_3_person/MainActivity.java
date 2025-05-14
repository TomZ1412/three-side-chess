package com.example.chess_3_person;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
//主界面
public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btnStart);
        Button btnQuit = findViewById(R.id.btnQuit);

        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnRule = findViewById(R.id.btnRule);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        btnRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RulesActivity.class));
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(false); // 人人对战
            }
        });


        /*btnHumanVsAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(true); // 人机对战
            }
        });*/

        //mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        //mediaPlayer.setLooping(true); // 设置循环播放
        //mediaPlayer.start();
    }

    private void startGame(boolean isAI) {
        Intent intent = new Intent(this, MatchActivity.class);
        intent.putExtra("isAI", isAI);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放 MediaPlayer 资源
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
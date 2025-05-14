package com.example.chess_3_person;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//个人信息界面
public class ProfileActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 显示用户信息
        TextView tvUsername = findViewById(R.id.tvUsername);
        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvLevel = findViewById(R.id.tvLevel);


        tvUsername.setText("用户名：" + Player.getUsername());
        tvScore.setText("分数：" + Player.getScore());
        tvLevel.setText("等级：" + Player.getLevel());
    }
}
package com.example.chess_3_person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

//游戏结束 Activity
public class GameOverActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView nickname;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // 初始化视图
        avatar = findViewById(R.id.avatar);
        nickname = findViewById(R.id.nickname);
        score = findViewById(R.id.score);

        // 获取传递的数据
        Intent intent = getIntent();
        String playerName = intent.getStringExtra("playerName");
        int playerScore = intent.getIntExtra("playerScore", 0);
        int player = intent.getIntExtra("player",0);

        // 设置头像、昵称和分数
        nickname.setText(playerName);
        setScoreText(score, playerScore);

        switch(player){
            case 1:avatar.setImageResource(R.drawable.avatar_red);break;
            case 2:avatar.setImageResource(R.drawable.avatar_blue);break;
            case 3:avatar.setImageResource(R.drawable.avatar_green);break;
        }

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到 MainActivity
                Intent mainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // 关闭当前 Activity
            }
        });

    }

    // 根据分数设置文本和颜色
    private void setScoreText(TextView scoreView, int score) {
        if (score > 0) {
            scoreView.setText("+" + score);
            scoreView.setTextColor(getResources().getColor(R.color.green));
        } else if (score < 0) {
            scoreView.setText(String.valueOf(score));
            scoreView.setTextColor(getResources().getColor(R.color.red));
        } else {
            scoreView.setText("0");
            scoreView.setTextColor(getResources().getColor(R.color.gray));
        }
    }
}
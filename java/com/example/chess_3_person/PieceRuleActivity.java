package com.example.chess_3_person;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//规则子界面
public class PieceRuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_rule);

        // 绑定视图
        TextView tvPieceName = findViewById(R.id.tvPieceName);
        TextView tvPieceRule = findViewById(R.id.tvPieceRule);

        // 获取传递的数据
        String pieceName = getIntent().getStringExtra("pieceName");
        String pieceRule = getIntent().getStringExtra("pieceRule");

        // 显示数据
        tvPieceName.setText(pieceName);
        tvPieceRule.setText(pieceRule);
    }
}
package com.example.chess_3_person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
//规则界面
public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        // 绑定按钮
        Button btnPawn = findViewById(R.id.btnPawn);
        Button btnRook = findViewById(R.id.btnRook);
        Button btnKnight = findViewById(R.id.btnKnight);
        Button btnBishop = findViewById(R.id.btnBishop);
        Button btnGuard = findViewById(R.id.btnGuard);
        Button btnCannon = findViewById(R.id.btnCannon);

        Button btnCommon = findViewById(R.id.btnCommon);

        // 设置点击事件
        btnPawn.setOnClickListener(v -> openPieceRule("士兵",
                "士兵在己方城堡内可以向前、后、左、右四个方向移动一格。出城后，士兵可以向周围六个相邻的格子移动一格。\n" +
                        "当敌方棋子周围存在至少两个己方棋子，且敌方棋子位于士兵的可移动范围内时，士兵可以吃掉该敌方棋子。\n" +
                        "注意：士兵一旦离开己方城堡，便无法返回，但可以进入敌方城堡。"));

        btnRook.setOnClickListener(v -> openPieceRule("城堡",
                "车在城堡内可以沿方格所指示的前、后、左、右四个方向移动任意距离。\n" +
                        "离开城堡后，车可以向六个方向所在直线移动任意距离，且可以跨越棋盘上的其他棋子。"));

        btnKnight.setOnClickListener(v -> openPieceRule("骑士",
                "骑士按照“日”字移动，即横向移动两格再纵向移动一格，或纵向移动两格再横向移动一格。\n" +
                        "骑士可以跳过其他棋子，不受路径阻挡的限制。"));

        btnBishop.setOnClickListener(v -> openPieceRule("主教",
                "主教在城堡内可以沿左上、左下、右上、右下四个对角线方向移动任意距离。\n" +
                        "离开城堡后，主教可以向棋盘六个顶点所指示的对角线方向移动任意距离。"));

        btnGuard.setOnClickListener(v -> openPieceRule("国王",
                "国王只能在己方城堡内移动，每次可以向周围八个方向（前、后、左、右及四个对角线方向）移动一格。\n" +
                        "国王是游戏的核心棋子，若被将死则游戏结束。"));

        btnCannon.setOnClickListener(v -> openPieceRule("皇后",
                "皇后结合了车和主教的移动规则。\n" +
                        "在城堡内，皇后可以沿前、后、左、右及四个对角线方向移动任意距离。\n" +
                        "离开城堡后，皇后可以向六个方向所在直线及对角线方向移动任意距离，是棋盘上最强大的棋子。"));

        btnCommon.setOnClickListener(v -> openPieceRule("总体规则",
                "游戏开始后，玩家依次轮流操作己方棋子。\n" +
                        "当一方的国王被将死时，该方玩家被判负，其所有棋子将被清空。\n" +
                        "若此时场上仍有两名玩家存活，游戏将继续进行，直至只剩一名玩家。\n" +
                        "游戏结束时，按照玩家失败的先后顺序，分别获得 -100、0、100 的积分。"));
    }

    // 打开规则介绍页面
    private void openPieceRule(String pieceName, String pieceRule) {
        Intent intent = new Intent(this, PieceRuleActivity.class);
        intent.putExtra("pieceName", pieceName);
        intent.putExtra("pieceRule", pieceRule);
        startActivity(intent);
    }
}
package gameserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChessPiece {
    public enum Type {
        KING, QUEEN, ROOK, BISHOP, LEFTKNIGHT, PAWN,RIGHTKNIGHT
    }

    private Type type; // 棋子类型
    private float x, y;  // 棋子的位置
    private int player; // 所属玩家（1, 2, 或 3）

    private int area;

    private float sqrt3_2 = (float) Math.sin(Math.PI/3);
    private float sqrt2_2 = (float) Math.sin(Math.PI/4);


    public ChessPiece(Type type, float x, float y, int player) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.player = player;
        this.area = player;
    }

    public Type getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getPlayer() {
        return player;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getArea(){
        return area;
    }

}
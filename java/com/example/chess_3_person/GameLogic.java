package com.example.chess_3_person;


import static java.lang.Math.abs;

import android.util.Log;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//游戏逻辑
public class GameLogic {
    private List<ChessPiece> pieces; // 所有棋子
    private ChessPiece selectedPiece; // 当前选中的棋子
    private ChessBoardView chessBoardView;
    private int currentPlayer; // 当前玩家（1, 2, 或 3）
    private int centerX;
    private int centerY;
    private float cellSize;

    private float cellHeight;
    private float cellWidth = cellSize;

    public GameLogic() {
        pieces = new ArrayList<>();
        currentPlayer = GlobalData.player; // 默认玩家 1 先手
    }

    public void setCurrentPlayer(int player) {
        this.currentPlayer = player;
    }

    protected void setValue(int _centerX, int _centerY, float _cellSize) {
        centerX = _centerX;
        centerY = _centerY;
        cellSize = _cellSize;
        cellWidth = _cellSize;
        cellHeight = (float) (cellSize * Math.sin(Math.PI / 3));
    }

    // 旋转 120 度的函数
    private PointF rotatePoint(float centerX, float centerY, float x, float y, float angleDegrees) {
        // 将角度转换为弧度
        float angleRadians = (float) Math.toRadians(angleDegrees);

        // 计算相对于中心点的坐标偏移
        float dx = x - centerX;
        float dy = y - centerY;

        // 使用旋转矩阵计算旋转后的坐标
        float newX = centerX + dx * (float) Math.cos(angleRadians) - dy * (float) Math.sin(angleRadians);
        float newY = centerY + dx * (float) Math.sin(angleRadians) + dy * (float) Math.cos(angleRadians);

        return new PointF(newX, newY);
    }
    //初始化棋子
    protected void initializePieces() {
        //玩家 1
        pieces.add(new ChessPiece(ChessPiece.Type.KING, centerX, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.QUEEN, centerX, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.LEFTKNIGHT, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.RIGHTKNIGHT, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.ROOK, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.ROOK, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.BISHOP, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.BISHOP, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX - 3 * cellWidth, centerY + 6 * cellHeight, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX, centerY + 6 * cellHeight, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX + 3 * cellWidth, centerY + 6 * cellHeight, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 1));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 1));

        //玩家 2

        pieces.add(new ChessPiece(ChessPiece.Type.KING, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.QUEEN, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.LEFTKNIGHT, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.RIGHTKNIGHT, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.ROOK, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.ROOK, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.BISHOP, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.BISHOP, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 6 * cellHeight, 120).x, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 6 * cellHeight, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX, centerY + 6 * cellHeight, 120).x, rotatePoint(centerX, centerY, centerX, centerY + 6 * cellHeight, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 6 * cellHeight, 120).x, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 6 * cellHeight, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).y, 2));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).x, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, 120).y, 2));

        //玩家 3

        pieces.add(new ChessPiece(ChessPiece.Type.KING, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.QUEEN, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.LEFTKNIGHT, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.RIGHTKNIGHT, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.ROOK, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.ROOK, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 3 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.BISHOP, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.BISHOP, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 5 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 6 * cellHeight, -120).x, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 6 * cellHeight, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX, centerY + 6 * cellHeight, -120).x, rotatePoint(centerX, centerY, centerX, centerY + 6 * cellHeight, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 6 * cellHeight, -120).x, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 6 * cellHeight, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX - (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX - 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX + (float) 3 / 2 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).y, 3));
        pieces.add(new ChessPiece(ChessPiece.Type.PAWN, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).x, rotatePoint(centerX, centerY, centerX + 3 * cellWidth, centerY + 7 * cellHeight + (float) 1 / 2 * cellWidth, -120).y, 3));
    }
    // 初始化棋子
    /*protected void setPieces(float x,float y,int player,ChessPiece.Type type) {
        // 玩家 1 的棋子
        pieces.add(new ChessPiece(type, x, y, player));
    }*/

    // 获取所有棋子
    public List<ChessPiece> getPieces() {
        return pieces;
    }

    // 获取当前选中的棋子
    public ChessPiece getSelectedPiece() {
        return selectedPiece;
    }

    // 选中棋子
    public ChessPiece selectPiece(float x, float y) {
        for (ChessPiece piece : pieces) {
            if (abs(piece.getX() - x) <= 1 && abs(piece.getY() - y) <= 1 && piece.getPlayer() == currentPlayer) {
                return piece;
            }
        }
        return null;
    }

    public void setSelectedPiece(float x, float y) {
        for (ChessPiece piece : pieces) {
            if (abs(piece.getX() - x) <= 1 && abs(piece.getY() - y) <= 1 && piece.getPlayer() == currentPlayer) {
                this.selectedPiece = piece;
            }
        }
    }

    public void clearSelectedPiece() {
        this.selectedPiece = null;
    }
    //清空列表棋子
    public void deletePiece(float x, float y) {
        synchronized (pieces) { // 同步代码块
            Iterator<ChessPiece> iterator = pieces.iterator();
            while (iterator.hasNext()) {
                ChessPiece piece = iterator.next();
                if (abs(piece.getX() - x) <= 1 && abs(piece.getY() - y) <= 1) {
                    System.out.println(piece.getX());
                    System.out.println(piece.getY());
                    iterator.remove(); // 使用迭代器的 remove 方法安全删除
                    //Log.d("DeletePiece", "Deleted piece at: (" + x + ", " + y + ")");
                    break;
                }
            }
        }
    }


    public void addPiece(float x, float y, ChessPiece.Type type, int player) {
        pieces.add(new ChessPiece(type, x, y, player));

    }


    // 获取当前玩家
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    private boolean isEaten(float x, float y) {
        for (ChessPiece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y && piece.getPlayer() != currentPlayer) {
                pieces.remove(piece);
                return true;
            }
        }
        return false;
    }

    public boolean isLose(){
        boolean isLost=true;
        for (ChessPiece piece : pieces) {
            if (piece.getType() == ChessPiece.Type.KING) {
                isLost = false;
                break;
            }
        }
        return isLost;
    }
}
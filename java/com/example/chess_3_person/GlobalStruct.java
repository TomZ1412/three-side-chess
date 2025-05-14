package com.example.chess_3_person;

import android.util.Log;
//记录棋盘某一方格信息的结构体
public class GlobalStruct {
    float x;
    float y;
    public ChessPiece chessPiece;

    public GlobalStruct(float x,float y){
        this.x=x;
        this.y=y;
        this.chessPiece=null;
    }

    public synchronized void deleteChessPiece() {
        Log.d("DeletePiece", "Deleted ChessPiece at: (" + x + ", " + y + ")");
        this.chessPiece = null;
    }
    public void setChessPiece(ChessPiece.Type type,int player){
        this.chessPiece=new ChessPiece(type,this.x,this.y,player);
    }

    public float getX() {
        return this.x;
    }
    public float getY(){
        return this.y;
    }

    public ChessPiece getChessPiece(){
        return this.chessPiece;
    }

}

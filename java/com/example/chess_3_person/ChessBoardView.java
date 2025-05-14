package com.example.chess_3_person;

import static java.lang.Math.abs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

// 实现游戏界面的实时渲染

public class ChessBoardView extends View {

    Context context;
    private Paint paint;

    boolean isLose=false;

    private int hexagonSize=7; // 六边形的边长（菱形数）
    private int extensionSize=3; // 延伸矩形区域的格子数

    private float sqrt_3_div_2 = (float) Math.cos(Math.PI / 6);
    private float sqrt_2_div_2 = (float) Math.cos(Math.PI / 4);
    private float selectedX = -1, selectedY = -1; // 选中棋子的位置

    private int color_flag = 1;
    private int cellSize; // 每个菱形的大小

    private int centerX;
    private int centerY;

    int losePlayer=0;

    private float lambda = 0.7F;  //棋子缩放比例
    GameLogic gameLogic = new GameLogic();

    private Thread serverListenerThread;

    boolean isFirstDraw=true;

    int reward=-100;

    int loseNum=0;

    Socket socket;
    BufferedReader in;
    PrintWriter out;
    //加载棋子图片
    private Bitmap kingRed = BitmapFactory.decodeResource(getResources(), R.drawable.red_king);
    private Bitmap queenRed = BitmapFactory.decodeResource(getResources(), R.drawable.red_queen);
    private Bitmap leftKnightRed = BitmapFactory.decodeResource(getResources(), R.drawable.red_knight_left);
    private Bitmap rightKnightRed = BitmapFactory.decodeResource(getResources(), R.drawable.red_knight_right);
    private Bitmap BishopRed = BitmapFactory.decodeResource(getResources(), R.drawable.red_bishop);
    private Bitmap PawnRed = BitmapFactory.decodeResource(getResources(), R.drawable.red_pawn);
    private Bitmap RookRed = BitmapFactory.decodeResource(getResources(), R.drawable.red_rook);
    private Bitmap kingGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green_king);
    private Bitmap queenGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green_queen);
    private Bitmap leftKnightGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green_knight_left);
    private Bitmap rightKnightGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green_knight_right);
    private Bitmap BishopGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green_bishop);
    private Bitmap PawnGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green_pawn);
    private Bitmap RookGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green_rook);
    private Bitmap kingYellow = BitmapFactory.decodeResource(getResources(), R.drawable.blue_king);
    private Bitmap queenYellow = BitmapFactory.decodeResource(getResources(), R.drawable.blue_queen);
    private Bitmap leftKnightYellow = BitmapFactory.decodeResource(getResources(), R.drawable.blue_knight_left);
    private Bitmap rightKnightYellow = BitmapFactory.decodeResource(getResources(), R.drawable.blue_knight_right);
    private Bitmap BishopYellow = BitmapFactory.decodeResource(getResources(), R.drawable.blue_bishop);
    private Bitmap PawnYellow = BitmapFactory.decodeResource(getResources(), R.drawable.blue_pawn);
    private Bitmap RookYellow = BitmapFactory.decodeResource(getResources(), R.drawable.blue_rook);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;

        GlobalData.centerX=centerX;
        GlobalData.centerY=centerY;
        calculateCellSize();
    }

    //根据屏幕尺寸动态调整棋盘参数
    private void calculateCellSize() {
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) return; // 避免未初始化时计算
        cellSize = Math.min(width, height) / (2 * hexagonSize + extensionSize) * 17 / 20;

        GlobalData.cellSize=cellSize;


        kingRed = Bitmap.createScaledBitmap(kingRed, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        queenRed = Bitmap.createScaledBitmap(queenRed, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        rightKnightRed = Bitmap.createScaledBitmap(rightKnightRed, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        leftKnightRed = Bitmap.createScaledBitmap(leftKnightRed, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        BishopRed = Bitmap.createScaledBitmap(BishopRed, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        PawnRed = Bitmap.createScaledBitmap(PawnRed, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        RookRed = Bitmap.createScaledBitmap(RookRed, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        kingGreen = Bitmap.createScaledBitmap(kingGreen, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        queenGreen = Bitmap.createScaledBitmap(queenGreen, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        rightKnightGreen = Bitmap.createScaledBitmap(rightKnightGreen, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        leftKnightGreen = Bitmap.createScaledBitmap(leftKnightGreen, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        BishopGreen = Bitmap.createScaledBitmap(BishopGreen, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        PawnGreen = Bitmap.createScaledBitmap(PawnGreen, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        RookGreen = Bitmap.createScaledBitmap(RookGreen, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        kingYellow = Bitmap.createScaledBitmap(kingYellow, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        queenYellow = Bitmap.createScaledBitmap(queenYellow, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        rightKnightYellow = Bitmap.createScaledBitmap(rightKnightYellow, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        leftKnightYellow = Bitmap.createScaledBitmap(leftKnightYellow, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        BishopYellow = Bitmap.createScaledBitmap(BishopYellow, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        PawnYellow = Bitmap.createScaledBitmap(PawnYellow, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);
        RookYellow = Bitmap.createScaledBitmap(RookYellow, Math.round(cellSize * lambda), Math.round(cellSize * lambda), true);

    }
    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        this.context=context;

        socket = SocketManager.getInstance().getSocket();
        in = SocketManager.getInstance().getBufferedReader();
        out = SocketManager.getInstance().getPrintWriter();

        initServerListener();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(255, 192, 203));
        calculateCellSize();
        drawHexagonBoard(canvas,1);
        drawExtensionAreas(canvas);
        if(isFirstDraw){
            gameLogic.setValue(centerX,centerY,cellSize);
            gameLogic.initializePieces();
        }

        drawPieces(canvas);
        drawHighlights(canvas);
        isFirstDraw=false;
    }
    //绘制棋盘中心部分的六边形
    private void drawHexagonBoard(Canvas canvas,int self_corlor){
        paint.setColor(Color.BLACK);
        //System.out.println(centerX);
        /*centerX = getWidth()/2;
        centerY = getHeight()/2;*/
        // 绘制六边形的六条边
        for (int i = 0; i < 6; i++) {
            float startX = centerX + (float) (hexagonSize * cellSize * Math.cos(Math.PI / 3 * i));
            float startY = centerY + (float) (hexagonSize * cellSize * Math.sin(Math.PI / 3 * i));
            float endX = centerX + (float) (hexagonSize * cellSize * Math.cos(Math.PI / 3 * (i + 1)));
            float endY = centerY + (float) (hexagonSize * cellSize * Math.sin(Math.PI / 3 * (i + 1)));
            canvas.drawLine(startX, startY, endX, endY, paint);
        }

        //Black
        for(int i=0;i<3;i++){
            for(int j=0;j<5-i;j++){
                float x = centerX - (6-(float)3*i/2)*cellSize + j*3*cellSize ;
                float y = centerY - 3*i*cellSize*sqrt_3_div_2;
                drawSix(canvas,x,y,0);
            }
        }
        for(int i=1;i<3;i++){
            for(int j=0;j<5-i;j++){
                float x = centerX - (6-(float)3*i/2)*cellSize + j*3*cellSize ;
                float y = centerY + 3*i*cellSize*sqrt_3_div_2;
                drawSix(canvas,x,y,0);
            }
        }

        //White
        for(int i=0;i<2;i++){
            for(int j=0;j<4-i;j++){
                float x = centerX - ((float)9/2-(float)3*i/2)*cellSize + j*3*cellSize ;
                float y = centerY - 3*i*cellSize*sqrt_3_div_2 - cellSize*sqrt_3_div_2;
                drawSix(canvas,x,y,4);
                //Log.d("White","x:"+x+",y:"+y);
            }
        }
        for(int i=0;i<2;i++){
            for(int j=0;j<3-i;j++){
                float x = centerX - (3-(float)3*i/2)*cellSize + j*3*cellSize ;
                float y = centerY + (3*i+2)*cellSize*sqrt_3_div_2;
                drawSix(canvas,x,y,4);
            }
        }

        //self_corlor
        drawSix(canvas,centerX,centerY-cellSize*sqrt_3_div_2*2,self_corlor);
        drawSix(canvas,centerX,centerY+cellSize*sqrt_3_div_2*4,self_corlor);
        drawSix(canvas,centerX-(float)9/2*cellSize,centerY+cellSize*sqrt_3_div_2,self_corlor);
        drawSix(canvas,centerX+(float)9/2*cellSize,centerY+cellSize*sqrt_3_div_2,self_corlor);

        //rignt_corlor
        int right_corlor = self_corlor%4+1;
        drawSix(canvas,centerX+(float)3*cellSize/2,centerY-cellSize*sqrt_3_div_2*5,right_corlor);
        drawSix(canvas,centerX+(float)3*cellSize/2,centerY+cellSize*sqrt_3_div_2,right_corlor);
        drawSix(canvas,centerX-3*cellSize,centerY+cellSize*sqrt_3_div_2*4,right_corlor);
        drawSix(canvas,centerX-3*cellSize,centerY-cellSize*sqrt_3_div_2*2,right_corlor);

        //left_corlor
        int left_corlor = self_corlor%4+2;
        drawSix(canvas,centerX-(float)3*cellSize/2,centerY-cellSize*sqrt_3_div_2*5,left_corlor);
        drawSix(canvas,centerX-(float)3*cellSize/2,centerY+cellSize*sqrt_3_div_2,left_corlor);
        drawSix(canvas,centerX+3*cellSize,centerY+cellSize*sqrt_3_div_2*4,left_corlor);
        drawSix(canvas,centerX+3*cellSize,centerY-cellSize*sqrt_3_div_2*2,left_corlor);

        //river
        drawRiver(canvas,centerX-(float)3/2*cellSize,centerY+cellSize*7*sqrt_3_div_2,1);
        drawRiver(canvas,centerX+(float)3/2*cellSize,centerY+cellSize*7*sqrt_3_div_2,1);
        drawRiver(canvas,centerX-6*cellSize,centerY-cellSize*2*sqrt_3_div_2,3);
        drawRiver(canvas,centerX-(float)9/2*cellSize,centerY-cellSize*5*sqrt_3_div_2,3);
        drawRiver(canvas,centerX+6*cellSize,centerY-cellSize*2*sqrt_3_div_2,2);
        drawRiver(canvas,centerX+(float)9/2*cellSize,centerY-cellSize*5*sqrt_3_div_2,2);
    }
    //绘制单个六边形
    private void drawSix(Canvas canvas,float _x,float _y,int corlor){

        if(isFirstDraw){
            Collections.addAll(GlobalData.globalList, new GlobalStruct(_x,_y));
        }
        Path path = new Path();
        for (int i = 0; i < 6; i++) {
            float x = _x + (float) (cellSize * Math.cos(Math.PI / 3 * i));
            float y = _y + (float) (cellSize * Math.sin(Math.PI / 3 * i));

            if (i == 0) {
                path.moveTo(x, y); // 移动到第一个顶点
            } else {
                path.lineTo(x, y); // 连接到下一个顶点
            }
        }

        path.close();
        switch(corlor) {
            case 0:
                paint.setColor(Color.BLACK);
                break;
            case 1:
                paint.setColor(Color.WHITE);
                break;
            case 2:
                paint.setColor(Color.WHITE);
                break;
            case 3:
                paint.setColor(Color.WHITE);
                break;
            case 4:
                paint.setColor(Color.WHITE);
                break;
        }
        canvas.drawPath(path, paint);
    }
    //绘制河流
    private void drawRiver(Canvas canvas, float x,float y,int rotation){
        Path path = new Path();
        switch(rotation){
            case 1:
                path.moveTo(x - cellSize, y); // 左上顶点
                path.lineTo(x-(float)cellSize/2, y - cellSize* sqrt_3_div_2); // 右上顶点
                path.lineTo(x+(float)cellSize/2, y - cellSize* sqrt_3_div_2); // 右下顶点
                path.lineTo(x + cellSize, y); // 左下顶点
                path.close();
                break;
            case 2:
                path.moveTo(x - (float)cellSize/2, y-cellSize*sqrt_3_div_2); // 左上顶点
                path.lineTo(x-cellSize, y); // 右上顶点
                path.lineTo(x - (float)cellSize/2, y + cellSize* sqrt_3_div_2); // 右下顶点
                path.lineTo(x + (float)cellSize/2, y + cellSize* sqrt_3_div_2); // 左下顶点
                path.close();
                break;
            case 3:
                path.moveTo(x + (float)cellSize/2, y-cellSize*sqrt_3_div_2); // 左上顶点
                path.lineTo(x+cellSize, y); // 右上顶点
                path.lineTo(x + (float)cellSize/2, y + cellSize* sqrt_3_div_2); // 右下顶点
                path.lineTo(x - (float)cellSize/2, y + cellSize* sqrt_3_div_2); // 左下顶点
                path.close();
                break;
        }

        paint.setColor(Color.BLUE);
        canvas.drawPath(path, paint);

    }
    //绘制单个菱形
    private void drawDiomand(Canvas canvas,float x, float y,int rotation){
        Path path=new Path();

        switch (rotation){
            case 1:
                path.moveTo(x, y); // 左上顶点
                path.lineTo(x+cellSize*sqrt_3_div_2, y - cellSize/2); // 右上顶点
                path.lineTo(x+cellSize*sqrt_3_div_2+cellSize/2, y - cellSize/2+cellSize*sqrt_3_div_2 ); // 右下顶点
                path.lineTo(x+cellSize/2, y + cellSize* sqrt_3_div_2); // 左下顶点
                path.close();
                break;
            case 2:
                path.moveTo(x, y); // 左上顶点
                path.lineTo(x-cellSize*sqrt_3_div_2, y - cellSize/2); // 右上顶点
                path.lineTo(x-cellSize*sqrt_3_div_2-cellSize/2, y - cellSize/2+cellSize*sqrt_3_div_2 ); // 右下顶点
                path.lineTo(x-cellSize/2, y + cellSize* sqrt_3_div_2); // 左下顶点
                path.close();
                break;
            case 3:
                path.moveTo(x, y); // 左上顶点
                path.lineTo(x+cellSize, y); // 右上顶点
                path.lineTo(x+cellSize, y+cellSize); // 右下顶点
                path.lineTo(x, y+cellSize); // 左下顶点
                path.close();
                break;
        }
        paint.setColor(color_flag%2==0?Color.WHITE:Color.BLACK);
        canvas.drawPath(path, paint);
    }

    //绘制城堡
    private void drawExtensionAreas(Canvas canvas) {
        /*int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;*/

        // 延伸区域 1：向右上方延伸
        color_flag=0;
        float startX = centerX+(float) hexagonSize/2*cellSize;
        float startY = centerY-(float) hexagonSize*sqrt_3_div_2*cellSize;
        for (int j = 0; j < extensionSize; j++) {
            for (int i = 0; i < hexagonSize; i++) {
                float x = startX+(float)i*cellSize/2+(float)j*cellSize*sqrt_3_div_2;
                float y = startY +(float)i*cellSize*sqrt_3_div_2-(float)j*cellSize/2;
                drawDiomand(canvas, x, y,1);
                if(i!=1&&i!=4) {
                    if(i!=2&&i!=5){
                        Collections.addAll(GlobalData.globalList, new GlobalStruct((float) (x+cellSize*sqrt_2_div_2*Math.cos(Math.PI/12)), (float) (y+cellSize*sqrt_2_div_2*Math.sin(Math.PI/12))));
                    }
                    color_flag++;
                }
                else{
                    Collections.addAll(GlobalData.globalList, new GlobalStruct((float)(x+cellSize*sqrt_2_div_2*Math.cos(Math.PI/12))+ (float) cellSize /4,(float) (y+cellSize*sqrt_2_div_2*Math.sin(Math.PI/12))+(float)cellSize*sqrt_3_div_2/2));
                }
            }
        }
        color_flag=0;
        // 延伸区域 2：向左上方延伸
        startX = centerX-(float) hexagonSize/2*cellSize;
        startY = centerY-(float) hexagonSize*sqrt_3_div_2*cellSize;
        for (int j = 0; j < extensionSize; j++) {
            for (int i = 0; i < hexagonSize; i++) {
                float x = startX-(float)i*cellSize/2-(float)j*cellSize*sqrt_3_div_2;
                float y = startY +(float)i*cellSize*sqrt_3_div_2-(float)j*cellSize/2;
                drawDiomand(canvas, x, y,2);
                if(i!=1&&i!=4) {
                    if(i!=2&&i!=5){
                        Collections.addAll(GlobalData.globalList, new GlobalStruct((float) (x-cellSize*sqrt_2_div_2*Math.cos(Math.PI/12)), (float) (y+cellSize*sqrt_2_div_2*Math.sin(Math.PI/12))));
                    }
                    color_flag++;
                }
                else{
                    Collections.addAll(GlobalData.globalList, new GlobalStruct((float)(x-cellSize*sqrt_2_div_2*Math.cos(Math.PI/12))- (float) cellSize /4,(float) (y+cellSize*sqrt_2_div_2*Math.sin(Math.PI/12))+(float)cellSize*sqrt_3_div_2/2));
                }
            }
        }
        color_flag=0;
        // 延伸区域 3：
        startX = centerX-(float) hexagonSize/2*cellSize;
        startY = centerY+(float) hexagonSize*sqrt_3_div_2*cellSize;
        for (int j = 0; j < extensionSize; j++) {
            for (int i = 0; i < hexagonSize; i++) {
                float x = startX + i * cellSize;
                float y = startY + j * cellSize;
                drawDiomand(canvas, x, y,3);
                if(i!=1&&i!=4){
                    if(i!=2&&i!=5){
                        Collections.addAll(GlobalData.globalList, new GlobalStruct(x+ (float) cellSize /2,y+ (float) cellSize /2));
                    }
                    color_flag++;
                }
                else{
                    Collections.addAll(GlobalData.globalList, new GlobalStruct(x+ (float) cellSize,y+ (float) cellSize /2));

                }
            }
        }
    }
    //绘制棋子
    private void drawPieces(Canvas canvas) {
        List<ChessPiece> pieces = gameLogic.getPieces();
        synchronized (pieces) { // 同步代码块
            for (ChessPiece piece : pieces) {
                float x = GlobalData.normPosition(piece.getX(), piece.getY())[0];
                float y = GlobalData.normPosition(piece.getX(), piece.getY())[1];
                if (isFirstDraw) {
                    GlobalData.seleceGlobalStruct(x, y).setChessPiece(piece.getType(), piece.getPlayer());
                }
                Bitmap pieceBitmap = getPieceBitmap(piece);
                if (pieceBitmap != null) {
                    // 绘制棋子图片
                    canvas.drawBitmap(pieceBitmap, x - (float) cellSize / 2 * lambda, y - (float) cellSize / 2 * lambda, paint);
                }
            }
        }
    }

    private Bitmap getPieceBitmap(ChessPiece piece) {
        switch (piece.getType()) {
            case KING:
                switch (piece.getPlayer()) {
                    case 1:
                        return kingRed;
                    case 2:
                        return kingYellow;
                    case 3:
                        return kingGreen;
                }
                break;
            case QUEEN:
                switch (piece.getPlayer()) {
                    case 1:
                        return queenRed;
                    case 2:
                        return queenYellow;
                    case 3:
                        return queenGreen;
                }
                break;
            case LEFTKNIGHT:
                switch (piece.getPlayer()) {
                    case 1:
                        return leftKnightRed;
                    case 2:
                        return leftKnightYellow;
                    case 3:
                        return leftKnightGreen;
                }
                break;
            case RIGHTKNIGHT:
                switch (piece.getPlayer()) {
                    case 1:
                        return rightKnightRed;
                    case 2:
                        return rightKnightYellow;
                    case 3:
                        return rightKnightGreen;
                }
                break;
            case BISHOP:
                switch (piece.getPlayer()) {
                    case 1:
                        return BishopRed;
                    case 2:
                        return BishopYellow;
                    case 3:
                        return BishopGreen;
                }
                break;
            case PAWN:
                switch (piece.getPlayer()) {
                    case 1:
                        return PawnRed;
                    case 2:
                        return PawnYellow;
                    case 3:
                        return PawnGreen;
                }
                break;
            case ROOK:
                switch (piece.getPlayer()) {
                    case 1:
                        return RookRed;
                    case 2:
                        return RookYellow;
                    case 3:
                        return RookGreen;
                }
                break;
            // 其他棋子的图片可以继续添加
        }
        return null;
    }
    //绘制棋子的下一步可以到达的位置
    private void drawHighlights(Canvas canvas) {
        /*System.out.println(selectedX);
        System.out.println(selectedY);*/
        if (selectedX != -1 && selectedY != -1) {
            // 高亮选中棋子
            List<float[]> destination = GlobalData.seleceGlobalStruct(selectedX, selectedY).chessPiece.get_next_destination();
            paint.setColor(Color.YELLOW);
            for (float[] move : destination) {
                canvas.drawCircle(move[0], move[1], (float) cellSize / 3, paint);
            }
        }
    }
    //点击逻辑
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /*System.out.println(GlobalData.currentPlayer);
            System.out.println(GlobalData.player);*/
            //跳过阵亡玩家
            if (GlobalData.currentPlayer == losePlayer) {
                GlobalData.currentPlayer = getNextPlayer(GlobalData.currentPlayer);
            }
            if (GlobalData.currentPlayer == GlobalData.player) {
                float x = GlobalData.normPosition(event.getX(), event.getY())[0];
                float y = GlobalData.normPosition(event.getX(), event.getY())[1];

                //首次选中棋子
                if (gameLogic.getSelectedPiece() == null) {
                    // 选中棋子
                    ChessPiece selectedPiece = gameLogic.selectPiece(x, y);
                    if (selectedPiece == null || selectedPiece.getPlayer() != gameLogic.getCurrentPlayer()) {
                        return false; // 没有选中棋子 或 选中其他玩家的棋子
                    }
                    gameLogic.setSelectedPiece(x, y);
                    selectedX = x;
                    selectedY = y;
                } else {
                    // 移动棋子
                    List<float[]> destination = GlobalData.seleceGlobalStruct(selectedX, selectedY).chessPiece.get_next_destination();
                    boolean isValidMove = false;
                    for (float[] move : destination) {
                        if (Math.abs(x - move[0]) <= 1 && Math.abs(y - move[1]) <= 1) {
                            isValidMove = true;
                            break;
                        }
                    }
                    if (isValidMove) {
                        // 移动棋子
                        selectedX = (selectedX - centerX) / cellSize;
                        selectedY = (selectedY - centerY) / cellSize;
                        x = (x - centerX) / cellSize;
                        y = (y - centerY) / cellSize;
                        String moveData = "MOVE:" + selectedX + ":" + selectedY +
                                ":" + x + ":" + y;
                        sendMoveDataToServer(moveData);

                        // 重置选中状态
                        selectedX = -1;
                        selectedY = -1;
                        gameLogic.clearSelectedPiece();

                        // 刷新界面
                        invalidate();
                        return true;
                    } else {
                        // 目标位置不合法，则尝试选中其他棋子
                        ChessPiece newSelectedPiece = gameLogic.selectPiece(x, y);
                        if (newSelectedPiece != null && newSelectedPiece.getPlayer() == gameLogic.getCurrentPlayer()) {
                            gameLogic.setSelectedPiece(x, y);
                            selectedX = x;
                            selectedY = y;
                        } else {
                            // 取消选中
                            gameLogic.clearSelectedPiece();
                            selectedX = -1;
                            selectedY = -1;
                        }
                    }
                }
                invalidate();
                return true;
            }

        }
        return super.onTouchEvent(event);
    }

    private void sendMoveDataToServer(String moveData) {
        new Thread(() -> {
            if (out != null) {
                out.println(moveData);  // 发送消息
                out.flush();  // 刷新流
            }
        }).start();
    }

    private void initServerListener() {
        serverListenerThread = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    String message = in.readLine();

                    if (message != null) {
                        GlobalData.currentPlayer = getNextPlayer(GlobalData.currentPlayer);
                        /*System.out.println(message);*/

                        //移动信息
                        if (message.startsWith("MOVE:")) {
                            String[] parts = message.split(":");
                            float fromX = Float.parseFloat(parts[1]) * cellSize + centerX;
                            float fromY = Float.parseFloat(parts[2]) * cellSize + centerY;
                            float toX = Float.parseFloat(parts[3]) * cellSize + centerX;
                            float toY = Float.parseFloat(parts[4]) * cellSize + centerY;

                            post(() -> updateGameUI(fromX, fromY, toX, toY));

                        }
                        //玩家阵亡信息
                        else if (message.startsWith("END:")) {
                            String[] parts = message.split(":");
                            losePlayer = Integer.parseInt(parts[1]);
                            if (losePlayer != GlobalData.player) {
                                if(!isLose){
                                    reward += 100;
                                }
                            }
                            else{
                                isLose=true;
                            }
                            loseNum++;
                            deletePlayerChess(losePlayer);

                            if(loseNum>=2){
                                navigateToGameOverActivity();
                            }

                        }
                        post(this::invalidate);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverListenerThread.start();
    }

    private void updateGameUI(float fromX, float fromY, float toX, float toY) {
        GlobalStruct fromStruct = GlobalData.seleceGlobalStruct(fromX, fromY);
        GlobalStruct toStruct = GlobalData.seleceGlobalStruct(toX, toY);

        if (fromStruct == null || toStruct == null) {
            return; // 如果位置无效，直接返回
        }

        ChessPiece piece = fromStruct.getChessPiece();
        if (piece == null) {
            return; // 如果没有棋子，直接返回
        }

        if (toStruct.getChessPiece() != null) {
            ChessPiece targetPiece = toStruct.getChessPiece();
            if (targetPiece.getType() == ChessPiece.Type.KING && targetPiece.getPlayer()==GlobalData.player) {
                sendMoveDataToServer("END:" + targetPiece.getPlayer());
            }
            toStruct.deleteChessPiece();
            gameLogic.deletePiece(toX, toY);
        }

        // 更新棋盘状态
        toStruct.setChessPiece(piece.getType(), piece.getPlayer());
        gameLogic.addPiece(toX, toY, piece.getType(), piece.getPlayer());
        fromStruct.deleteChessPiece();
        gameLogic.deletePiece(fromX, fromY);

        //post(this::invalidate);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 停止监听线程
        if (serverListenerThread != null) {
            serverListenerThread.interrupt();
        }
    }
    //清空阵亡玩家的棋子
    private void deletePlayerChess(int losePlayer) {
        for (GlobalStruct element : GlobalData.globalList) {
            if (element.getChessPiece() != null && element.getChessPiece().getPlayer() == losePlayer) {
                float x = element.getX();
                float y = element.getY();
                GlobalStruct struct = GlobalData.seleceGlobalStruct(x, y);
                if (struct != null) {
                    struct.deleteChessPiece(); // 删除棋子
                    gameLogic.deletePiece(x, y); // 调用修改后的 deletePiece 方法
                    GlobalData.globalList.remove(element); // 直接删除
                }
            }
        }
    }

    private int getNextPlayer(int currentPlayer) {
        int nextPlayer = currentPlayer % 3 + 1;
        while (nextPlayer == losePlayer) {
            nextPlayer = nextPlayer % 3 + 1;
        }
        return nextPlayer;
    }

    public void navigateToGameOverActivity() {
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra("playerName", Player.getUsername());
        intent.putExtra("playerScore", reward); // 分数值
        switch(GlobalData.player){
            case 1:intent.putExtra("avatarUrl", 1);break;
            case 2:intent.putExtra("avatarUrl", 2);break;
            case 3:intent.putExtra("avatarUrl", 3);break;
        }
        context.startActivity(intent);
    }



}
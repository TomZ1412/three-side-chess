package com.example.chess_3_person;

import static com.example.chess_3_person.GlobalData.cellSize;
import static com.example.chess_3_person.GlobalData.centerY;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//构建单个棋子类
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


    //获取某一棋子的可能移动位置
    public List<float[]> get_next_destination(){
        float centerX=GlobalData.centerX;
        float centerY= GlobalData.centerY;
        float cellSize= GlobalData.cellSize;

        List<float[]> possibleMoves = new ArrayList<>();
        float left_bound = centerX-3*cellSize;
        float right_bound = centerX+3*cellSize;
        float up_bound = centerY+7*cellSize*sqrt3_2+cellSize/2;
        float down_bound = centerY+7*cellSize*sqrt3_2+5*cellSize/2;

        float river = centerY+6*cellSize*sqrt3_2;

        float temp_x,temp_y;

        this.area=returnArea(this.x,this.y);

        switch(this.area){
            case 1:break;
            case 2:
                temp_x=GlobalData.Rotation(this.x,this.y,120)[0];
                temp_y=GlobalData.Rotation(this.x,this.y,120)[1];
                this.x=temp_x;
                this.y=temp_y;
                break;
            case 3:
                temp_x=GlobalData.Rotation(this.x,this.y,-120)[0];
                temp_y=GlobalData.Rotation(this.x,this.y,-120)[1];
                this.x=temp_x;
                this.y=temp_y;
                break;
        }


        if(this.type==Type.KING) {
            // 王的移动规则（周围一格）
            float[][] kingMoves = {
                    {1.5F*cellSize, 0}, {-1.5F*cellSize, 0}, {0, cellSize}, {0, -cellSize},
                    {1.5F*cellSize, cellSize}, {1.5F*cellSize, -cellSize}, {-1.5F*cellSize, cellSize}, {-1.5F*cellSize, -cellSize}
            };
            for (float[] move : kingMoves) {
                float newRow = this.x + move[0];
                float newCol = this.y + move[1];
                if (isUsPiece(newRow, newCol) && isWithinBoard(left_bound, right_bound, up_bound, down_bound, newRow, newCol)) {
                    possibleMoves.add(new float[]{newRow, newCol});
                }
            }
        }
        else if(this.type==Type.LEFTKNIGHT||this.type== Type.RIGHTKNIGHT){
            float[][] knightMoves = {
                    {1.5F, 2}, {3, 1}, {-1.5F, 2}, {-3, 1},
                    {1.5F, -2}, {3, -1}, {-1.5F, -2}, {-3, -1}
            };
            for (float[] move : knightMoves) {
                float newRow = this.x + cellSize*move[0];
                float newCol = this.y + cellSize*move[1];
                if (isUsPiece(newRow,newCol)&&isWithinBoard(left_bound, right_bound, up_bound, down_bound, newRow, newCol)) {
                    possibleMoves.add(new float[]{newRow, newCol});
                }
            }
            if(this.y==up_bound){
                if(this.x==centerX){
                    if (isUsPiece(centerX-3*cellSize/2, centerY+5*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX-3*cellSize/2, centerY+5*cellSize*sqrt3_2});
                    }
                    if(isUsPiece(centerX+3*cellSize/2, centerY+5*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX+3*cellSize/2, centerY+5*cellSize*sqrt3_2});
                    }
                }
                else if (this.x==centerX-3*cellSize) {
                    if (isUsPiece(centerX-3*cellSize/2, centerY+5*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX-3*cellSize/2, centerY+5*cellSize*sqrt3_2});
                    }                        }
                else if(this.x==centerX+3*cellSize){
                    if(isUsPiece(centerX+3*cellSize/2, centerY+5*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX+3*cellSize/2, centerY+5*cellSize*sqrt3_2});
                    }                        }
            }
            if(this.y==up_bound+cellSize){
                if(this.x==centerX-3*cellSize/2){
                    if (isUsPiece(centerX-3*cellSize, centerY+6*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX-3*cellSize, centerY+6*cellSize*sqrt3_2});
                    }
                    if(isUsPiece(centerX, centerY+6*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX, centerY+6*cellSize*sqrt3_2});
                    }
                }
                else if(this.x==centerX+3*cellSize/2){
                    if (isUsPiece(centerX+3*cellSize, centerY+6*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX+3*cellSize, centerY+6*cellSize*sqrt3_2});
                    }
                    if(isUsPiece(centerX, centerY+6*cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{centerX, centerY+6*cellSize*sqrt3_2});
                    }
                }
            }
            if(this.y<=river){
                knightMoves= new float[][]{
                        {-1.5F,5*sqrt3_2},{1.5F,5*sqrt3_2},
                        {-1.5F,-5*sqrt3_2},{1.5F,-5*sqrt3_2},
                        {-4.5F,sqrt3_2},{4.5F,sqrt3_2}
                };
                for (float[] move : knightMoves) {
                    float newRow = this.x + cellSize*move[0];
                    float newCol = this.y + cellSize*move[1];
                    if (isUsPiece(newRow,newCol)&&isWithCenter(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol});
                    }
                }
                if(this.y==river){
                    if(this.x==centerX){
                        if(isUsPiece(centerX-3*cellSize/2, river+cellSize*sqrt3_2+3*cellSize/2)){
                            possibleMoves.add(new float[]{centerX-3*cellSize/2, river+cellSize*sqrt3_2+3*cellSize/2});
                        }
                        if(isUsPiece(centerX+3*cellSize/2, river+cellSize*sqrt3_2+3*cellSize/2)){
                            possibleMoves.add(new float[]{centerX+3*cellSize/2, river+cellSize*sqrt3_2+3*cellSize/2});
                        }
                    }
                    else if(this.x==centerX-3*cellSize){
                        if(isUsPiece(centerX-3*cellSize/2, river+cellSize*sqrt3_2+3*cellSize/2)) {
                            possibleMoves.add(new float[]{centerX - 3 * cellSize / 2, river + cellSize * sqrt3_2 + 3 * cellSize / 2});
                        }
                    }
                    else if(this.x==centerX+3*cellSize){
                        if(isUsPiece(centerX+3*cellSize/2, river+cellSize*sqrt3_2+3*cellSize/2)) {
                            possibleMoves.add(new float[]{centerX + 3 * cellSize / 2, river + cellSize * sqrt3_2 + 3 * cellSize / 2});
                        }
                    }
                }
                else if(this.y==river-cellSize*sqrt3_2){
                    if(this.x==centerX-3*cellSize/2){
                        if(isUsPiece(centerX-3*cellSize, up_bound)) {
                            possibleMoves.add(new float[]{centerX - 3 * cellSize, up_bound});
                        }
                        if(isUsPiece(centerX, up_bound)) {
                            possibleMoves.add(new float[]{centerX, up_bound});
                        }
                    }
                    else if(this.x==centerX+3*cellSize/2){
                        if(isUsPiece(centerX+3*cellSize, up_bound)) {
                            possibleMoves.add(new float[]{centerX + 3 * cellSize, up_bound});
                        }
                        if(isUsPiece(centerX, up_bound)) {
                            possibleMoves.add(new float[]{centerX, up_bound});
                        }
                    }
                }
            }
        }
        else if (this.type == Type.ROOK) {
            // 定义车的移动方向
            float[][] rookDirections = {
                    {-1.5F, 0}, // 左
                    {1.5F, 0},  // 右
                    {0, -1},     // 上
                    {0, 1}     // 下
            };

            // 遍历每个方向
            for (float[] direction : rookDirections) {
                float dx = direction[0];
                float dy = direction[1];

                // 沿着该方向逐步移动
                for (int step = 1; step <= 4; step++) { // 假设车最多移动 4 步
                    float newRow = this.x + cellSize * dx * step;
                    float newCol = this.y + cellSize * dy * step;

                    // 检查是否超出棋盘范围
                    if (!isWithinBoard(left_bound, right_bound, up_bound, down_bound, newRow, newCol)) {
                        break; // 超出范围，停止该方向的搜索
                    }

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
            }

            // 特殊逻辑：如果车在中心线或特定位置，可以沿垂直方向移动
            if (this.x == centerX || this.x == centerX - 3 * cellSize || this.x == centerX + 3 * cellSize) {
                for (int i = 0; i < 6; i++) {
                    float newRow = this.x;
                    float newCol = up_bound - cellSize/2 - cellSize*sqrt3_2 - cellSize * i*sqrt3_2*2;

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
                for(int i=0;i<3;i++){
                    float newRow = this.x;
                    float newCol = up_bound + cellSize * i;

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
            }

            // 特殊逻辑：如果车在河流附近，可以沿对角线移动
            if (this.y <= river) {
                for (int j = 0; j < 6; j++) {
                    for (int i = 1; i < 7; i++) {
                        float d_x = (float) (this.x + 2* i * cellSize * sqrt3_2 * Math.cos(j * Math.PI / 3 + Math.PI / 6));
                        float d_y = (float) (this.y - 2* i * cellSize * sqrt3_2 * Math.sin(j * Math.PI / 3 + Math.PI / 6));

                        if(!isPointInHexagon(d_x,d_y)){
                            continue;
                        }
                        // 检查是否有己方棋子
                        if (!isUsPiece(d_x, d_y)) {
                            break; // 遇到己方棋子，停止该方向的搜索
                        }

                        // 检查是否有敌方棋子
                        if (isEnemyPiece(d_x, d_y)) {
                            possibleMoves.add(new float[]{d_x, d_y}); // 可以移动到敌方棋子的位置
                            break; // 遇到敌方棋子，停止该方向的搜索
                        }

                        // 如果没有棋子，可以移动到该位置
                        possibleMoves.add(new float[]{d_x, d_y});
                    }
                }
                if(this.x==centerX-3*cellSize||this.x==centerX+3*cellSize){
                    for(int i=0;i<3;i++){
                        if(!isUsPiece(this.x,up_bound+cellSize*i)){
                            break;
                        }
                        if(isEnemyPiece(this.x,up_bound+cellSize*i)){
                            possibleMoves.add(new float[]{this.x, up_bound+cellSize*i});
                            break;
                        }
                        possibleMoves.add(new float[]{this.x, up_bound+cellSize*i});
                    }
                }
                if(this.x==centerX-3*cellSize&&this.y==river){
                    float startX= (float) (centerX+6*cellSize+(cellSize*sqrt3_2+cellSize/2)*Math.cos(Math.PI/6));
                    float startY= (float) (centerY-(cellSize*sqrt3_2+cellSize/2)*Math.sin(Math.PI/6));
                    for(int i=0;i<3;i++){
                        if(!isUsPiece((float) (startX+cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6)))){
                            break;
                        }
                        if(isEnemyPiece((float) (startX+cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6)))){
                            possibleMoves.add(new float[]{(float) (startX+cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6))});
                            break;
                        }
                        possibleMoves.add(new float[]{(float) (startX+cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6))});
                    }
                }
                else if(this.x==centerX+3*cellSize&&this.y==river){
                    float startX= (float) (centerX-6*cellSize-(cellSize*sqrt3_2+cellSize/2)*Math.cos(Math.PI/6));
                    float startY= (float) (centerY-(cellSize*sqrt3_2+cellSize/2)*Math.sin(Math.PI/6));
                    for(int i=0;i<3;i++){
                        if(!isUsPiece((float) (startX-cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6)))){
                            break;
                        }
                        if(isEnemyPiece((float) (startX-cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6)))){
                            possibleMoves.add(new float[]{(float) (startX-cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6))});
                            break;
                        }
                        possibleMoves.add(new float[]{(float) (startX-cellSize*Math.cos(Math.PI/6)), (float) (startY-cellSize*Math.sin(Math.PI/6))});
                    }
                }
                if(this.x==centerX&&this.y==centerY){
                    for(int i=0;i<3;i++) {
                        float d_y = up_bound + cellSize * i;
                        if (!isUsPiece(centerX, d_y)) {
                            break;
                        }
                        if (isEnemyPiece(centerX, d_y)) {
                            possibleMoves.add(new float[]{centerX, d_y});
                            break;
                        }
                        possibleMoves.add(new float[]{centerX, d_y});
                    }
                    for(int i=0;i<3;i++) {
                        float d_y = up_bound + cellSize * i;
                        if(!isUsPiece(centerX, GlobalData.Rotation(centerX,d_y,120)[1])) {
                            break;
                        }
                        if(isEnemyPiece(centerX, GlobalData.Rotation(centerX,d_y,120)[1])) {
                            possibleMoves.add(new float[]{centerX, GlobalData.Rotation(centerX,d_y,120)[1]});
                            break;
                        }
                        possibleMoves.add(new float[]{centerX, GlobalData.Rotation(centerX,d_y,120)[1]});
                    }
                    for(int i=0;i<3;i++) {
                        float d_y = up_bound + cellSize * i;
                        if(!isUsPiece(centerX, GlobalData.Rotation(centerX,d_y,-120)[1])) {
                            break;
                        }
                        if(isEnemyPiece(centerX, GlobalData.Rotation(centerX,d_y,-120)[1])) {
                            possibleMoves.add(new float[]{centerX, GlobalData.Rotation(centerX,d_y,-120)[1]});
                            break;
                        }
                        possibleMoves.add(new float[]{centerX, GlobalData.Rotation(centerX,d_y,-120)[1]});
                    }

                }
            }
        }
        else if (this.type == Type.BISHOP) {
            // 定义车的移动方向
            float[][] rookDirections = {
                    {-1.5F, 1}, // 左
                    {1.5F, 1},  // 右
                    {-1.5F, -1},     // 上
                    {1.5F, -1}     // 下
            };

            // 遍历每个方向
            for (float[] direction : rookDirections) {
                float dx = direction[0];
                float dy = direction[1];

                // 沿着该方向逐步移动
                for (int step = 1; step <= 2; step++) { // 假设车最多移动 4 步
                    float newRow = this.x + cellSize * dx * step;
                    float newCol = this.y + cellSize * dy * step;

                    // 检查是否超出棋盘范围
                    if (!isWithinBoard(left_bound, right_bound, up_bound, down_bound, newRow, newCol)) {
                        break; // 超出范围，停止该方向的搜索
                    }

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
            }

            // 特殊逻辑：如果车在中心线或特定位置，可以沿垂直方向移动
            if (this.x == centerX - 1.5F * cellSize&&this.y==up_bound) {
                for (int i = 0; i < 4; i++) {
                    float newRow = centerX + 1.5F*cellSize*i;
                    float newCol = up_bound - cellSize/2 - cellSize*sqrt3_2 - cellSize * i*sqrt3_2*3;

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
                for (int i = 0; i < 3; i++) {
                    float newRow = centerX - 1.5F*cellSize*i - 3*cellSize;
                    float newCol = up_bound - cellSize/2 - cellSize*sqrt3_2 - cellSize * i*sqrt3_2*3;

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
            }

            if (this.x == centerX + 1.5F * cellSize&&this.y==up_bound) {
                for (int i = 0; i < 4; i++) {
                    float newRow = centerX - 1.5F*cellSize*i;
                    float newCol = up_bound - cellSize/2 - cellSize*sqrt3_2 - cellSize * i*sqrt3_2*3;

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
                for (int i = 0; i < 3; i++) {
                    float newRow = centerX + 1.5F*cellSize*i + 3*cellSize;
                    float newCol = up_bound - cellSize/2 - cellSize*sqrt3_2 - cellSize * i*sqrt3_2*3;

                    // 检查是否有己方棋子
                    if (!isUsPiece(newRow, newCol)) {
                        break; // 遇到己方棋子，停止该方向的搜索
                    }

                    // 检查是否有敌方棋子
                    if (isEnemyPiece(newRow, newCol)) {
                        possibleMoves.add(new float[]{newRow, newCol}); // 可以移动到敌方棋子的位置
                        break; // 遇到敌方棋子，停止该方向的搜索
                    }

                    // 如果没有棋子，可以移动到该位置
                    possibleMoves.add(new float[]{newRow, newCol});
                }
            }

            // 特殊逻辑：如果车在河流附近，可以沿对角线移动
            if (this.y <= river) {
                for (int j = 0; j < 6; j++) {
                    for (int i = 1; i < 5; i++) {
                        float d_x = (float) (this.x + 3*i * cellSize * Math.cos(j * Math.PI / 3));
                        float d_y = (float) (this.y - 3*i * cellSize * Math.sin(j * Math.PI / 3));

                        // 检查是否超出棋盘范围
                        if(!isPointInHexagon(d_x,d_y)){
                            continue;
                        }
                        // 检查是否有己方棋子
                        if (!isUsPiece(d_x, d_y)) {
                            break; // 遇到己方棋子，停止该方向的搜索
                        }

                        // 检查是否有敌方棋子
                        if (isEnemyPiece(d_x, d_y)) {
                            possibleMoves.add(new float[]{d_x, d_y}); // 可以移动到敌方棋子的位置
                            break; // 遇到敌方棋子，停止该方向的搜索
                        }

                        // 如果没有棋子，可以移动到该位置
                        possibleMoves.add(new float[]{d_x, d_y});
                    }
                }
                if(this.y==river){
                    float d_x,d_y;
                    if(this.x==centerX-3*cellSize){
                        for(int i=0;i<3;i++){
                            d_x=centerX-3*cellSize/2+3*cellSize/2*i;
                            d_y=up_bound+cellSize*i;
                            if(!isUsPiece(d_x,d_y)){
                                break;
                            }
                            if(isEnemyPiece(d_x,d_y)){
                                possibleMoves.add(new float[]{d_x, d_y});
                                break;// 可以移动到敌方棋子的位置
                            }
                            possibleMoves.add(new float[]{d_x, d_y}); // 可以移动到敌方棋子的位置
                        }
                    }
                    else if(this.x==centerX+3*cellSize){
                        for(int i=0;i<3;i++){
                            d_x=centerX+3*cellSize/2-3*cellSize/2*i;
                            d_y=up_bound+cellSize*i;
                            if(!isUsPiece(d_x,d_y)){
                                break;
                            }
                            if(isEnemyPiece(d_x,d_y)){
                                possibleMoves.add(new float[]{d_x, d_y});
                                break;// 可以移动到敌方棋子的位置
                            }
                            possibleMoves.add(new float[]{d_x, d_y});
                        }
                    }
                    else if(this.x==centerX){
                        for(int i=0;i<2;i++){
                            d_x=centerX-3*cellSize/2*i;
                            d_y=up_bound+cellSize*i;
                            if(!isUsPiece(d_x,d_y)){
                                break;
                            }
                            if(isEnemyPiece(d_x,d_y)){
                                possibleMoves.add(new float[]{d_x, d_y});
                                break;// 可以移动到敌方棋子的位置
                            }
                            possibleMoves.add(new float[]{d_x, d_y});
                        }
                        for(int i=0;i<2;i++){
                            d_x=centerX+3*cellSize/2*i;
                            d_y=up_bound+cellSize*i;
                            if(!isUsPiece(d_x,d_y)){
                                break;
                            }
                            if(isEnemyPiece(d_x,d_y)){
                                possibleMoves.add(new float[]{d_x, d_y});
                                break;// 可以移动到敌方棋子的位置
                            }
                            possibleMoves.add(new float[]{d_x, d_y});
                        }
                    }
                }

            }


        }
        else if(this.type==Type.QUEEN){
            List<float[]> possibleMoves_rook = new ArrayList<>();
            List<float[]> possibleMoves_bishop = new ArrayList<>();
            this.type=Type.ROOK;
            possibleMoves_rook=get_next_destination();
            this.type=Type.BISHOP;
            possibleMoves_bishop=get_next_destination();
            possibleMoves.addAll(possibleMoves_rook);
            possibleMoves.addAll(possibleMoves_bishop);
        }
        else if(this.type==Type.PAWN){
            if(this.y==up_bound){
                if(this.x==centerX||this.x==left_bound||this.x==right_bound){

                    if(isUsPiece(this.x,this.y-cellSize/2-cellSize*sqrt3_2)){
                        possibleMoves.add(new float[]{this.x, this.y-cellSize/2-cellSize*sqrt3_2});
                    }
                }
                else{
                    if(isUsPiece(this.x-1.5F*cellSize,this.y)&&isWithinBoard(left_bound,right_bound,up_bound,down_bound,this.x,this.y)){
                        possibleMoves.add(new float[]{this.x-1.5F*cellSize, this.y});
                    }
                    if(isUsPiece(this.x+1.5F*cellSize,this.y)&&isWithinBoard(left_bound,right_bound,up_bound,down_bound,this.x,this.y)){
                        possibleMoves.add(new float[]{this.x+1.5F*cellSize, this.y});
                    }
                }
            }
            if(this.y<=river){
                for(int j=0;j<6;j++){
                    float d_x= (float) (this.x+2*sqrt3_2*cellSize*Math.cos(j*Math.PI/3+Math.PI/6));
                    float d_y= (float) (this.y+2*sqrt3_2*cellSize*Math.sin(j*Math.PI/3+Math.PI/6));
                    if(isUsPiece(d_x,d_y)&&isWithCenter(d_x,d_y)){
                        possibleMoves.add(new float[]{d_x, d_y});
                    }
                }
            }
            if(this.area!=this.player&&this.y>=up_bound){
                float[][] pawnDirections = {
                        {-1.5F, 0}, // 左
                        {1.5F, 0},  // 右
                        {0, -1},     // 上
                        {0, 1}     // 下
                };
                for (float[] direction : pawnDirections) {
                    float dx = direction[0];
                    float dy = direction[1];

                    // 沿着该方向逐步移动
                    for (int step = 1; step <= 1; step++) { // 假设车最多移动 4 步
                        float newRow = this.x + cellSize * dx * step;
                        float newCol = this.y + cellSize * dy * step;

                        // 检查是否超出棋盘范围
                        if (!isWithinBoard(left_bound, right_bound, up_bound, down_bound, newRow, newCol)) {
                            break; // 超出范围，停止该方向的搜索
                        }

                        // 检查是否有己方棋子
                        if (!isUsPiece(newRow, newCol)) {
                            break; // 遇到己方棋子，停止该方向的搜索
                        }

                        // 检查是否有敌方棋子
                        if (isEnemyPiece(newRow, newCol)&&countSurroundingAllies(newRow,newCol)<2) {// 可以移动到敌方棋子的位置
                            break; // 遇到敌方棋子，停止该方向的搜索
                        }

                        // 如果没有棋子，可以移动到该位置
                        possibleMoves.add(new float[]{newRow, newCol});
                    }
                }
            }

            if((this.x==centerX||this.x==centerX-3*cellSize||this.x==centerX+3*cellSize)&&this.y==river&&this.area!=this.player){
                if(isUsPiece(this.x,up_bound)){
                    possibleMoves.add(new float[]{this.x,up_bound});
                }
            }
        }
        switch(this.area){
            case 1:break;
            case 2:
                temp_x=GlobalData.Rotation(x,y,-120)[0];
                temp_y=GlobalData.Rotation(x,y,-120)[1];
                this.x=temp_x;
                this.y=temp_y;
                break;
            case 3:
                temp_x=GlobalData.Rotation(x,y,120)[0];
                temp_y=GlobalData.Rotation(x,y,120)[1];
                this.x=temp_x;
                this.y=temp_y;
                break;
        }
        switch(this.area){
            case 1:break;
            case 2:
                for (float[] move : possibleMoves) {
                    temp_x=GlobalData.Rotation(move[0],move[1],-120)[0];
                    temp_y=GlobalData.Rotation(move[0],move[1],-120)[1];
                    move[0]=temp_x;
                    move[1]=temp_y;
                }
                break;
            case 3:
                for (float[] move : possibleMoves) {
                    temp_x=GlobalData.Rotation(move[0],move[1],120)[0];
                    temp_y=GlobalData.Rotation(move[0],move[1],120)[1];
                    move[0]=temp_x;
                    move[1]=temp_y;
                }
                break;
        }
        return possibleMoves;
    }
    //是否在棋盘内
    private static boolean isWithinBoard(float left_bound, float right_bound, float up_bound, float down_bound,float x,float y) {
        return x >= left_bound && x <= right_bound && y >= up_bound && y <= down_bound;
    }
    //是否为己方棋子
    private boolean isUsPiece(float x, float y){
        float temp_x,temp_y;
        switch(this.area){
            case 1:break;
            case 2:
                temp_x=GlobalData.Rotation(x,y,-120)[0];
                temp_y=GlobalData.Rotation(x,y,-120)[1];
                x=temp_x;
                y=temp_y;
                break;
            case 3:
                temp_x=GlobalData.Rotation(x,y,120)[0];
                temp_y=GlobalData.Rotation(x,y,120)[1];
                x=temp_x;
                y=temp_y;
                break;
        }


        if(GlobalData.seleceGlobalStruct(x,y)==null){
            return true;
        }
        else if(Objects.requireNonNull(GlobalData.seleceGlobalStruct(x, y)).chessPiece==null){
            return true;
        }
        else{
            return !(Objects.requireNonNull(GlobalData.seleceGlobalStruct(x, y)).chessPiece.player==this.player);

        }
    }
    //是否在城堡内
    private boolean isWithCenter(float x,float y){

        return !(GlobalData.seleceGlobalStruct(x,y)==null)&&(y<= centerY+6*cellSize*sqrt3_2);
    }
    //是否为地方棋子
    private boolean isEnemyPiece(float x, float y) {
        float temp_x,temp_y;
        switch(this.area){
            case 1:break;
            case 2:
                temp_x=GlobalData.Rotation(x,y,-120)[0];
                temp_y=GlobalData.Rotation(x,y,-120)[1];
                x=temp_x;
                y=temp_y;
                break;
            case 3:
                temp_x=GlobalData.Rotation(x,y,120)[0];
                temp_y=GlobalData.Rotation(x,y,120)[1];
                x=temp_x;
                y=temp_y;
                break;
        }

        if(GlobalData.seleceGlobalStruct(x,y)==null){
            return false;
        }
        else if(Objects.requireNonNull(GlobalData.seleceGlobalStruct(x, y)).chessPiece==null){
            return false;
        }
        ChessPiece piece = GlobalData.seleceGlobalStruct(x, y).getChessPiece();
        return piece.getPlayer() != this.player;
    }
    //是否在棋盘中心区域
    private boolean isPointInHexagon(float px, float py) {
        float cx=GlobalData.centerX;
        float cy= centerY;
        float s= cellSize*7;

        // 正六边形的六个顶点
        float[][] vertices = new float[6][2];
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i; // 60 度
            vertices[i][0] = cx + (float) (s * Math.cos(angle));
            vertices[i][1] = cy + (float) (s * Math.sin(angle));
        }

        // 射线交点法
        boolean inside = false;
        for (int i = 0, j = 5; i < 6; j = i++) {
            float xi = vertices[i][0], yi = vertices[i][1];
            float xj = vertices[j][0], yj = vertices[j][1];

            // 检查点是否在边的范围内
            boolean intersect = ((yi > py) != (yj > py)) &&
                    (px < (xj - xi) * (py - yi) / (yj - yi) + xi);
            if (intersect) {
                inside = !inside;
            }
        }

        return inside;
    }
    //判断所在区域，便于确定棋子移动逻辑
    private int returnArea(float x,float y){
        if(isAreaOne(x,y)){
            return 1;
        }
        float d_x=GlobalData.Rotation(x,y,120)[0];
        float d_y=GlobalData.Rotation(x,y,120)[1];

        if(isAreaOne(d_x,d_y)){
            return 2;
        }

        d_x=GlobalData.Rotation(x,y,-120)[0];
        d_y=GlobalData.Rotation(x,y,-120)[1];
        if(isAreaOne(d_x,d_y)){
            return 3;
        }
        return 4;
    }

    private boolean isAreaOne(float x,float y){
        float centerX=GlobalData.centerX;
        float centerY= GlobalData.centerY;
        float cellSize= GlobalData.cellSize;
        if(y>=centerY+6*cellSize*sqrt3_2){
            return true;
        }
        else if(x<=centerX+(y-centerY)*Math.tan(Math.PI/6)&&x>=centerX-(y-centerY)*Math.tan(Math.PI/6)){
            return true;
        }
        else{
            return false;
        }
    }

    private int countSurroundingAllies(float x, float y) {
        int count = 0;
        for (GlobalStruct element : GlobalData.globalList) {
            if (((element.getX()-x)*(element.getX()-x)+(element.getY()-y)*(element.getY()-y)-(cellSize*sqrt3_2)*(cellSize*sqrt3_2))<=1
            ||((element.getX()-x)*(element.getX()-x)+(element.getY()-y)*(element.getY()-y)-cellSize*cellSize)<=1) {
                if(element.getChessPiece().getPlayer()==this.player){
                    count++;
                }
            }
        }

        return count;
    }
}
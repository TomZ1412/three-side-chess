package com.example.chess_3_person;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//记录全局棋盘状态
public class GlobalData {
    // 定义一个全局数组，所有类都可以访问
    public static List<GlobalStruct> globalList = new CopyOnWriteArrayList<>();

    public static float cellSize;
    public static float centerX;

    public static float centerY;

    public static int player;

    public static int currentPlayer=1;


    public static GlobalStruct seleceGlobalStruct(float x,float y){
        for (GlobalStruct element : globalList) {

            if (abs(element.getX()-x)<=1&&abs(element.getY()-y)<=1) {
                return element;  // 找到并返回该棋子
            }
        }
        return null;
    }
    //绕棋盘中心坐标变换
    public static float[] Rotation(float x,float y,int angle){
        float a=centerX;
        float b=centerY;
        // 将角度转换为弧度
        double theta = Math.toRadians(angle);
        // 计算旋转后的坐标
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        float xPrime = (float) (a + (x - a) * cosTheta + (y - b) * sinTheta);
        float yPrime = (float) (b - (x - a) * sinTheta + (y - b) * cosTheta);
        xPrime=GlobalData.normPosition(xPrime,yPrime)[0];
        yPrime=GlobalData.normPosition(xPrime,yPrime)[1];
        return new float[]{xPrime, yPrime};
    }

    public static float[] normPosition(float x,float y){
        float minDistance = 1000;
        float minx = 0;
        float miny = 0;
        for (GlobalStruct element : globalList) {
            if(abs(element.getX()-x)+ abs(element.getY()-y)<minDistance){
                minDistance= abs(element.getX()-x)+ abs(element.getY()-y);
                minx= element.getX();
                miny= element.getY();
            }
        }
        float[] result=new float[2];
        result[0]=minx;
        result[1]=miny;
        return result;
    }
}


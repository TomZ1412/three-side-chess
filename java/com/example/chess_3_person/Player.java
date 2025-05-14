package com.example.chess_3_person;
//全局玩家类
public class Player {
    private static String username;
    private static String password;
    private static int score;
    private static int level;

    // 构造函数
    public static void setPlayer(String username, String password, int score, int level) {
        Player.username = username;
        Player.password = password;
        Player.score = score;
        Player.level = level;
    }

    // Getter 和 Setter 方法
    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Player.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Player.password = password;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Player.score = score;
    }

    public static int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        Player.level = level;
    }
}
package com.example.chess_3_person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//全局 socket 管理
public class SocketManager {
    private static SocketManager instance;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String ip= "183.173.201.41";

    // 私有构造函数，防止外部实例化
    private SocketManager() {
        // 在此初始化 socket、in 和 out
        try {
            // 初始化 socket，连接到服务器
            socket = new Socket(ip, 12345);  // 替换为实际的服务器地址和端口

            // 从 socket 获取输入流并包装成 BufferedReader
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 从 socket 获取输出流并包装成 PrintWriter
            out = new PrintWriter(socket.getOutputStream(), true);  // 第二个参数设置为 true，启用自动刷新
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void reset() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            socket = new Socket(ip, 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 获取 SocketManager 实例
    public static SocketManager getInstance() {
        if (instance == null) {
            synchronized (SocketManager.class) {
                if (instance == null) {
                    instance = new SocketManager();
                }
            }
        }
        return instance;
    }

    // 初始化 socket、BufferedReader 和 PrintWriter
    public void init(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    // 获取 socket、BufferedReader 和 PrintWriter
    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return in;
    }

    public PrintWriter getPrintWriter() {
        return out;
    }
}

package com.example.chess_3_person;// MatchActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//匹配界面
public class MatchActivity extends AppCompatActivity {

    private static final String SERVER_IP = "183.173.201.41";  // 替换成服务器的 IP 地址
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    boolean isSetPlayer=false;

    //private ServerListener serverListener;

    private TextView playerStatusTextView1;
    private TextView playerStatusTextView2;
    private TextView playerStatusTextView3;

    private ImageView playerStatusImageView1;
    private ImageView playerStatusImageView2;
    private ImageView playerStatusImageView3;

    boolean isStart =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        // 初始化视图
        playerStatusTextView1 = findViewById(R.id.name_bottom);
        playerStatusTextView3 = findViewById(R.id.name_right);
        playerStatusTextView2 = findViewById(R.id.name_left);

        playerStatusImageView1 = findViewById(R.id.avatar_bottom);
        playerStatusImageView3 = findViewById(R.id.avatar_right);
        playerStatusImageView2 = findViewById(R.id.avatar_left);

        // 启动一个线程连接到服务器并等待匹配成功的信号
        new Thread(() -> {
            try {
                connectToServer();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("MatchActivity", "连接服务器失败", e);
            }
        }).start();
    }

    private void connectToServer() throws IOException {
        // 连接到服务器

        SocketManager.getInstance().reset();
        socket = SocketManager.getInstance().getSocket();
        in = SocketManager.getInstance().getBufferedReader();
        out = SocketManager.getInstance().getPrintWriter();

        // 发送玩家加入请求
        out.println("JOIN_GAME");
        out.flush();

        try {
            while (!socket.isClosed() && !isStart) {
                String message = in.readLine(); // 接收来自服务器的消息
                if (message != null) {
                    System.out.println("Match received: " + message);
                    if (message.startsWith("ROOM_STATUS")) {
                        String[] parts = message.split(" ");
                        int num = (int)Float.parseFloat(parts[1]);
                        if(!isSetPlayer){
                            System.out.println("Match received: " + message);
                            GlobalData.player=num;
                            isSetPlayer=true;
                        }

                        updateRoomStatus(num);
                        if (num == 3) {
                            isStart=true;
                            Intent intent = new Intent(MatchActivity.this, GameActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*// 初始化全局的 ServerListener
        serverListener = new ServerListener(socket, in, out, this, null);  // 传递当前 Activity 作为回调接口

        // 启动监听
        serverListener.startListening();*/
    }

    /*@Override
    public void onRoomStatusReceived(int numberOfPlayers) {
        // 更新房间状态
        updateRoomStatus(numberOfPlayers);
        if (numberOfPlayers == 3) {
            Intent intent = new Intent(MatchActivity.this, GameActivity.class);
            startActivity(intent);
            finish();
        }
    }*/

    private void updateRoomStatus(int numberOfPlayers) {
        // 根据房间内玩家数量更新玩家状态显示
        runOnUiThread(() -> {
            if (numberOfPlayers >= 1) {
                playerStatusTextView1.setText("已进入房间");
                playerStatusImageView1.setImageResource(R.drawable.avatar_red);
            }
            if (numberOfPlayers >= 2) {
                playerStatusTextView2.setText("已进入房间");
                playerStatusImageView2.setImageResource(R.drawable.avatar_blue);
            }
            if (numberOfPlayers >= 3) {
                playerStatusTextView3.setText("已进入房间");
                playerStatusImageView3.setImageResource(R.drawable.avatar_green);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

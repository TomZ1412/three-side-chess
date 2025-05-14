package com.example.chess_3_person;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.net.*;


//游戏 Activity
public class GameActivity extends AppCompatActivity {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //new Thread(this::connectToServer).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

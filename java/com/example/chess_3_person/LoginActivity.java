package com.example.chess_3_person;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;

import java.io.PrintWriter;
import java.net.Socket;


//登录 Activity
public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private static final String SERVER_IP = "183.173.201.41"; // 服务器 IP
    private static final int SERVER_PORT = 12345; // 服务器端口

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.Username);
        etPassword = findViewById(R.id.Password);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(false);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(true);
            }
        });
    }

    private void handleLogin(boolean isRegister) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        String command = isRegister ? "REGISTER " : "LOGIN ";
        new ServerTask().execute(command + username + " " + password);
    }

    private class ServerTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Socket socket = SocketManager.getInstance().getSocket();
            BufferedReader reader = SocketManager.getInstance().getBufferedReader();
            PrintWriter writer = SocketManager.getInstance().getPrintWriter();
            try  {
                writer.write(params[0] + "\n");
                writer.flush();
                return reader.readLine();
            } catch (Exception e) {
                return "ERROR: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith("SUCCESS")) {
                // 解析服务器返回的用户信息
                String[] parts = result.split(" ");
                if (parts.length >= 3) {
                    String username = parts[1];
                    String password = parts[2];
                    int score = Integer.parseInt(parts[3]);
                    int level = Integer.parseInt(parts[4]);

                    Player.setPlayer(username,password,score,level);

                    // 跳转到主界面
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "服务器返回数据格式错误", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "失败：" + result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

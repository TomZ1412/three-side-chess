package gameserver;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;


public class GameServer {

    private static final int PORT = 12345;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chess_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "YORE_PASSWORD";
    private static List<ClientHandler> clients = new ArrayList<>();

    private static int playerNum=0;

    public static void main(String[] args) {
        
        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.err.println("MySQL JDBC Driver not found!");
        return;
    }
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                if(playerNum == 3) {
                    //setPlayerIdx();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastRoomStatus() {
        for (ClientHandler client : clients) {
            client.sendRoomStatus(playerNum);
        }
    }

    private static void setPlayerIdx() {
        for (int i = 1; i <= clients.size(); i++) {
            clients.get(i-1).sendMessage("PLAYER_IDX " + i);
        }
    }

    // 内部类：处理每个客户端的通信
    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (!socket.isClosed()) {
                    // 等待客户端发送加入游戏的请求
                    String request = in.readLine();
                    if (request != null) {
                        System.out.println("Received: " + request);

                        if ("JOIN_GAME".equals(request)) {
                            playerNum++;
                            System.out.println("Player has joined the game.");
                            sendMessage("MATCH_SUCCESS");
                            broadcastRoomStatus();
                        } 
                        else if (request.startsWith("MOVE:")) {
                            //System.out.println("Move data: " + request);
                            broadcastMessage(request); // 广播移动数据给所有客户端
                        }
                        else if (request.startsWith("REGISTER")) {
                            handleRegister(request);
                        } 
                        else if (request.startsWith("LOGIN")) {
                            handleLogin(request);
                        }
                        else if(request.startsWith("END")) {
                            broadcastMessage(request);
                        }
                        else {
                            sendMessage("ERROR Invalid request");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 发送消息给客户端
        public void sendMessage(String message) {
            try {
                System.out.println("Sending to client: " + message); // 调试信息
                out.write(message + "\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 广播消息给所有客户端
        public void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }

        public void sendRoomStatus(int numberOfPlayers) {
            try {
                String status = "ROOM_STATUS " + numberOfPlayers;
                System.out.println("Sending to client: " + status); // 调试信息
                out.write(status+"\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleRegister(String request) {
            String[] parts = request.split(" ");
            if (parts.length < 3) {
                sendMessage("ERROR Invalid register format");
                return;
            }
            String username = parts[1];
            String password = parts[2];
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();
                sendMessage("SUCCESS " + username + " " + password + " " + 0 + " " + 1);
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    sendMessage("ERROR Username already exists");
                } else {
                    sendMessage("ERROR Database error");
                    e.printStackTrace();
                }
            }

        }

        private void handleLogin(String request) {
            String[] parts = request.split(" ");
            if (parts.length < 3) {
                sendMessage("ERROR Invalid login format");
                return;
            }
            String username = parts[1];
            String password = parts[2];
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int score = rs.getInt("score");
                    int level = rs.getInt("lebel");
                    sendMessage("SUCCESS " + username + " " + password + " " + score + " " + level);
                } else {
                    sendMessage("ERROR Invalid credentials");
                }
            } catch (SQLException e) {
                sendMessage("ERROR Database error");
                e.printStackTrace();
            }
        }
    }
}

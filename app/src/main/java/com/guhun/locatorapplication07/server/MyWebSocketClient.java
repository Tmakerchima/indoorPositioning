package com.guhun.locatorapplication07.server;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket opened");
        // 在连接建立后，可以在这里添加发送消息的逻辑
        // send("Hello, Server");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        // 处理接收到的消息
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket closed");
        // 处理连接关闭的逻辑
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        // 处理连接错误的逻辑
    }

    // 在需要发送消息时调用该方法
    public void sendMessage(String message) {
        if (isOpen()) {
            send(message);
        } else {
            System.err.println("WebSocket is not open, cannot send message: " + message);
        }
    }

    public static void main(String[] args) {
        String serverUri = "ws://your-server-url"; // 替换成你的 WebSocket 服务器地址
        try {
            MyWebSocketClient client = new MyWebSocketClient(serverUri);
            client.connect(); // 连接到 WebSocket 服务器
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

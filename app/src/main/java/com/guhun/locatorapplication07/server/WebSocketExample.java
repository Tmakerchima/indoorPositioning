package com.guhun.locatorapplication07.server;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketExample {

    private static final String WS_URL = "ws://wifilocation-release.lecangs.com/locator_server/data-websocket";
    private WebSocket webSocket;

    public void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(WS_URL)
                .build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // WebSocket 连接已经建立
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // 接收到 WebSocket 消息
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // 接收到 WebSocket 二进制消息
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                // WebSocket 连接失败处理
            }
        };

        webSocket = client.newWebSocket(request, listener);
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.cancel();
        }
    }
}

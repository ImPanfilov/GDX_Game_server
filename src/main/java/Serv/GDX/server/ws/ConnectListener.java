package Serv.GDX.server.ws;


import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

public interface ConnectListener {
    void handle(StandardWebSocketSession session);
}
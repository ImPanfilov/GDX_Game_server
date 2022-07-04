package Serv.GDX.server;

import Serv.GDX.server.actors.*;
import Serv.GDX.server.ws.WebSocketHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import Serv.GDX.server.desks.Desk;
import com.badlogic.gdx.utils.ObjectMap;

@Component
public class GameLoop extends ApplicationAdapter {
    private final WebSocketHandler socketHandler;
    private final Json json;
    private float lastRender=0;
    private final ObjectMap<String,Snake> snakes=new ObjectMap<>();
    private final Array<Entity> stateToSend=new Array<>();
    private final ForkJoinPool pool=ForkJoinPool.commonPool();


    @Value("${desk.size}")
    int deskSize;
    @Value("${frame.rate}")
    float frameRate;

    @Autowired
    Food food;
    @Autowired
    Desk desk;

    GameLoop(WebSocketHandler socketHandler, Json json){
            this.socketHandler=socketHandler;
            this.json = json;
            }

    @Override
    public void create() {
        socketHandler.setConnectListener(session->{
        Snake snake=new Snake();
        snake.setId(session.getId());
        snakes.put(session.getId(),snake);
            try {
                session.getNativeSession().getBasicRemote().sendText(
                        String.format("{\"class\":\"sessionKey\",\"id\":\"%s\",\"size\":%d}",session.getId(),deskSize)
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        socketHandler.setDisconnectListener(session->{
            sendToEverybody(
                String.format("{\"class\":\"evict\",\"id\":\"%s\"}",session.getId()));
            snakes.get(session.getId()).free(desk);
            snakes.remove(session.getId());

        });

        socketHandler.setMessageListener((session,message)-> pool.execute(()->{
        String type=message.get("type").asText();
        switch (type){
            case "state":
                Snake snake=snakes.get(session.getId());
                snake.setStateKey(message.get("stateKey").asInt());
                break;
            case "name":
                Snake newSnake=snakes.get(session.getId());
                newSnake.setName(message.get("name").asText());
                newSnake.Fill(desk);
                break;
            default:
                throw new RuntimeException("Unknown WS object type" +type);
        }
        }));
    }

    @Override
    public void render() {
            lastRender+= Gdx.graphics.getDeltaTime();
            if (lastRender>=frameRate){
                stateToSend.clear();
               for (ObjectMap.Entry<String,Snake> snakeEntry:snakes) {
                   Snake snake1=snakeEntry.value;
                   snake1.Step(desk);
                   stateToSend.add(snake1);
                   }
                for (BestSnake best:Best.getBest()) {
                    stateToSend.add(best);
                    }
                stateToSend.add(food);
                String stateJsonSnakes=json.toJson(stateToSend);
                sendToEverybody(stateJsonSnakes);
                lastRender=0;
                }
    }

    private void sendToEverybody(String json)
    {
        pool.execute(() -> {
            for (StandardWebSocketSession session : socketHandler.getSessions()) {
                try {
                    if (session.isOpen()) {
                        session.getNativeSession().getBasicRemote().sendText(json);
                        }
                    } catch (IOException e) {
                         e.printStackTrace();
                         }
            }
        });
    }

}


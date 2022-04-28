package Serv.GDX.server;

import Serv.GDX.server.ws.WebSocketHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import Serv.GDX.server.actors.Snake;
import Serv.GDX.server.desks.Desk;
import Serv.GDX.server.actors.Food;
import Serv.GDX.server.actors.BestSnake;
import Serv.GDX.server.actors.Entity;
import com.badlogic.gdx.utils.ObjectMap;

@Component
public class GameLoop extends ApplicationAdapter {
    private final static float frameRate=0.4f;
    private final WebSocketHandler socketHandler;
    private final Json json;
    private float lastRender=0;
    private final ObjectMap<String,Snake> snakes=new ObjectMap<>();
    private final Array<Entity> stateToSend=new Array<>();
    private final ForkJoinPool pool=ForkJoinPool.commonPool();
    private final Entity food=new Food();
    private final Desk desk=new Desk();
    private static final ArrayList<BestSnake> bestScore=new ArrayList<>();
    private final static int Sizedesk=20;
    private final static int  FoodCount=4;

    public  GameLoop(WebSocketHandler socketHandler, Json json){
            this.socketHandler=socketHandler;
            this.json = json;
            }

    public static int getSizedesk() {
        return Sizedesk;
    }

    public static void addBest(String name, int score) {
        bestScore.add(new BestSnake(name,score));
        Collections.sort(bestScore);
        if (bestScore.size()>5)bestScore.remove(0);
        for(BestSnake bs:bestScore){bs.setIndex(bestScore.indexOf(bs));}
    }

    public static int getFoodCount() {
        return FoodCount;
    }

    @Override
    public void create() {
        socketHandler.setConnectListener(session->{
        Snake snake=new Snake();
        snake.setId(session.getId());
        snakes.put(session.getId(),snake);
            try {
                session.getNativeSession().getBasicRemote().sendText(
                        String.format("{\"class\":\"sessionKey\",\"id\":\"%s\",\"size\":%d}",session.getId(),Sizedesk)
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

        socketHandler.setMessageListener(((session,message)->{
            pool.execute(()->{
            String type=message.get("type").asText();
            switch (type){
                case "state":
                    Snake snake=snakes.get(session.getId());
                    snake.setStateKey(message.get("stateKey").asInt());
                    break;
                case "name":
                    Snake snake1=snakes.get(session.getId());
                    snake1.setName(message.get("name").asText());
                    snake1.Fill(desk);
                    break;
                default:
                    throw new RuntimeException("Unknown WS object type" +type);
            }
            });
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
                for (BestSnake best:bestScore) {
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


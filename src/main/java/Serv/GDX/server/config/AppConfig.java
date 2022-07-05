package Serv.GDX.server.config;

import Serv.GDX.server.GameLoop;
import Serv.GDX.server.best.BestSnake;
import Serv.GDX.server.actors.Pos;
import Serv.GDX.server.actors.Snake;
import Serv.GDX.server.actors.Food;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public HeadlessApplication getApplication(GameLoop gameLoop) {
        return new HeadlessApplication(gameLoop);
    }

    @Bean
    public Json getJson() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.addClassTag("pos", Pos.class);
        json.addClassTag("snake", Snake.class);
        json.addClassTag("food", Food.class);
        json.addClassTag("best", BestSnake.class);
        return json;
    }

}


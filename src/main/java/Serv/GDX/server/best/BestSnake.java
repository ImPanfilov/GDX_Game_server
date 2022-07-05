package Serv.GDX.server.best;

import Serv.GDX.server.actors.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BestSnake implements Entity,Comparable<BestSnake>{
    private String name="Undefined";
    private int score=0;
    private int Id;

    public BestSnake(String name,int score) {
        this.name=name;this.score=score;
    }

    @Override
    public int compareTo(BestSnake best) {
        return this.score - best.score;
    }

    @Override
    public void write(Json json) {
        json.writeValue("id", Id);
        json.writeValue("name", name);
        json.writeValue("score", score);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {}

}

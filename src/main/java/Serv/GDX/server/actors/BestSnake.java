package Serv.GDX.server.actors;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class BestSnake implements Entity,Comparable<BestSnake>{
    private String name="Undefined";
    private int score=0;
    private int Id;
    private BestSnake() {
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setIndex(int index) {
        Id=index;
    }
}

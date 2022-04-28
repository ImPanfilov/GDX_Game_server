package Serv.GDX.server.actors;

import Serv.GDX.server.GameLoop;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class Food implements  Entity{

    private static ArrayList<Pos> FoodArr;// ячейки

    public Food() {
        FoodArr = new ArrayList<>(GameLoop.getFoodCount());
        }

    public static void add(Pos pos) {
        FoodArr.add(pos);
        }

    public static boolean check(Pos pos){
        return FoodArr.contains(pos);
        }

    public static void removeFood(Pos pos) {
        FoodArr.remove(pos);
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", "food");
        json.writeValue("pos", FoodArr);
        }

    @Override
    public void read(Json json, JsonValue jsonData) {}
}

package Serv.GDX.server.actors;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.ArrayList;


@Data
@Component
public class Food implements  Entity{

    static ArrayList<Pos> FoodArr= new ArrayList<>();// ячейки

    public void add(Pos pos) {
        FoodArr.add(pos);
        }

     static boolean check(Pos pos){
        return FoodArr.contains(pos);
        }

     static void removeFood(Pos pos) {
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

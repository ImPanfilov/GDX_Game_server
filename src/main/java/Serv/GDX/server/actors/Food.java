package Serv.GDX.server.actors;

import Serv.GDX.server.desks.Desk;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.ArrayList;


@Data
@Component
@Scope("singleton")
public class Food implements  Entity{

    ArrayList<Pos> FoodArr= new ArrayList<>();// ячейки

    public void add(Pos pos) {
        FoodArr.add(pos);
        }

    public boolean check(Pos pos){
        return FoodArr.contains(pos);
        }

    public void removeFood(Pos pos) {
        FoodArr.remove(pos);
        }

    public void clear() {
        FoodArr.clear();
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", "food");
        json.writeValue("pos", FoodArr);
        }

    @Override
    public void read(Json json, JsonValue jsonData) {}
}

package Serv.GDX.server.actors;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pos  implements Json.Serializable
{
    int xPos,yPos;
    int State;//0-пусто,1-игрок,2-еда
    boolean Changed;
    int Direction;//0-none,1-left,2-right,3-up,4-down


    public void CheckSize(int deskSize) {
        xPos=(xPos == -1) ? deskSize - 1 : xPos;
        xPos=(xPos == deskSize) ? 0 : xPos;
        yPos=(yPos == -1) ? deskSize - 1 : yPos;
        yPos=(yPos == deskSize) ? 0 : yPos;
    }


    @Override
    public void write(Json json)
    {
        json.writeValue("x", xPos);
        json.writeValue("y", yPos);
        json.writeValue("state", State);
        json.writeValue("changed", Changed);
        json.writeValue("direction", Direction);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {}

}

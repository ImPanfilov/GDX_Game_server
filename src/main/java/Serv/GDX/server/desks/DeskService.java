package Serv.GDX.server.desks;

import Serv.GDX.server.GameLoop;
import Serv.GDX.server.actors.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeskService {
    @Autowired
    GameLoop gameLoop;
    @Autowired
    Desk desk;
    @Autowired
    Food food;


    public void SetupSizeDesk(int size){
        gameLoop.setDeskSize(size);
        desk.setDeskSize(size);
        desk.init();
        gameLoop.InitSnakes();
        String json=String.format("{\"class\":\"size\",\"size\":%d}",size);
        gameLoop.sendToEverybody(json);
    }

    public void SetupCountFood(int count){
        desk.setFoodCount(count);
        desk.init();
        gameLoop.InitSnakes();
        String json=String.format("{\"class\":\"clear\"}");
        gameLoop.sendToEverybody(json);
        }

        public String GetDeskInfo(){
            int size=gameLoop.getDeskSize();
            int count= desk.getFoodCount();
        return "DeskSize:"+size+"  FoodCount:"+count;
        }
}

package Serv.GDX.server.actors;


import Serv.GDX.server.best.Best;
import Serv.GDX.server.desks.Desk;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.ArrayDeque;
import java.util.Random;


@Data
@Component
@Scope("prototype")
public class Snake  implements Entity{
    int stateKey=0;//{none,leftPressed,rightPressed,upPressed,downPressed}
    int Score=0;
    Pos tmp=new Pos();
    String id;
    String Name="Undefined";
    ArrayDeque<Pos> bodySnake = new ArrayDeque<>();//тело змея
    Pos HeadWorm= new Pos();//голова змея
    Random rnd = new Random();

    @Autowired
    Food food;

    @Autowired
    Desk desk;


    public void Fill(){
        int Rnd = rnd.nextInt(desk.getFreeSize());
        tmp = desk.getFree(Rnd);
        bodySnake.add(tmp);bodySnake.add(tmp);bodySnake.add(tmp);
        HeadWorm.setXPos(tmp.getXPos());HeadWorm.setYPos(tmp.getYPos());
        desk.setState(tmp,1,0);
    }


    public void Step(){//шаг игры
        tmp=desk.getDeskPos(HeadWorm);
        stateKey=SnakeService.CornerStep(tmp.getDirection() % 10,stateKey);//обработка углов при повороте
        tmp.setDirection(stateKey);
        stateKey=stateKey%10;
        switch (stateKey) {//{none,leftPressed,rightPressed,upPressed,downPressed}
            case 0:
                break;
            case 1:
                HeadWorm.setXPos(HeadWorm.getXPos()-1);
                break;
            case 2:
                HeadWorm.setXPos(HeadWorm.getXPos()+1);
                break;
            case 3:
                HeadWorm.setYPos(HeadWorm.getYPos()+1);
                break;
            case 4:
                HeadWorm.setYPos(HeadWorm.getYPos()-1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + stateKey % 10);
        }
        HeadWorm.CheckSize(desk.getDeskSize());
        tmp=desk.getDeskPos(HeadWorm);
        if (!(desk.FreeArrContains(tmp)) && !(food.check(tmp))&&(stateKey != 0))//проверка пересечения
            {
                Free();
                Best.addBest(Name,Score);
                Score=0;
                Fill();//новая игра
            }
        else{
        if (stateKey != 0) {
            desk.setState(tmp, 1,stateKey);
            bodySnake.offer(tmp);//двигаем голову

            if (food.check(tmp))//проверка пересечения с едой
                {
                    food.removeFood(tmp);
                    Score++;
                    desk.AddFood(1);//добавляем еду
                } else
                    {
                    tmp = bodySnake.poll();//обрезаем хвост
                    if (tmp != null && !bodySnake.contains(tmp))
                        {desk.setState(tmp, 0,0);}
                    }
            }
        }
    }

    public void Free() {
        for (Pos pos:bodySnake)
            {desk.setState(pos,0,0);}
        bodySnake.clear();
        stateKey=0;
    }

    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("name", Name);
        json.writeValue("score", Score);
        json.writeValue("pos", bodySnake);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {}



}


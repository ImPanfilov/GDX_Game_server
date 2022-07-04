package Serv.GDX.server.actors;


import Serv.GDX.server.desks.Desk;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Random;


@Data
@Component
public class Snake  implements Entity{
    int stateKey=0;//{none,leftPressed,rightPressed,upPressed,downPressed}
    int Score=0;
    Pos tmp=new Pos();
    String id;
    String Name="Undefined";
    ArrayDeque<Pos> bodySnake = new ArrayDeque<>();//тело змея
    Pos HeadWorm= new Pos();//голова змея
    Random rnd = new Random();


    public void Fill(Desk desk){
        int Rnd = rnd.nextInt(desk.getFreeSize());
        tmp = desk.getFree(Rnd);
        bodySnake.add(tmp);bodySnake.add(tmp);bodySnake.add(tmp);
        HeadWorm.setXPos(tmp.getXPos());HeadWorm.setYPos(tmp.getYPos());
        desk.setState(tmp,1,0);
    }


    public void Step(Desk desk){//шаг игры
        tmp=desk.getDeskPos(HeadWorm);
        stateKey=CornerStep(tmp.getDirection() % 10,stateKey);//обработка углов при повороте
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
        if (!(desk.FreeArrContains(tmp)) && !(Food.check(tmp))&&(stateKey != 0))//проверка пересечения
            {
                free(desk);
                Best.addBest(Name,Score);
                Score=0;
                Fill(desk);//новая игра
            }
        else{
        if (stateKey != 0) {
            desk.setState(tmp, 1,stateKey);
            bodySnake.offer(tmp);//двигаем голову

            if (Food.check(tmp))//проверка пересечения с едой
                {
                    Food.removeFood(tmp);
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

    public void free(Desk desk) {
        for (Pos pos:bodySnake)
            {desk.setState(pos,0,0);}
        bodySnake.clear();
        this.stateKey=0;
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


    private int CornerStep(int oldState,int newState)//обработка углов при повороте
    { int result;
        switch (newState) {//{0-none,1-left,2-right,3-up,4-down,31-upToLeft,41-downToLeft,32-upToRight,42downToRight,24-rightToDown,14-leftToDown,23-rightToUp,13-leftToUp}
            case 1:
                switch (oldState) {
                    case 2:result= 2;break;
                    case 3:result= 31;break;
                    case 4:result= 41;break;
                    default:result= 1;break;
                }break;
            case 2:
                switch (oldState) {
                    case 1:result= 1;break;
                    case 3:result= 32;break;
                    case 4:result= 42;break;
                    default:result= 2;break;
                }break;
            case 3:
                switch (oldState) {
                    case 1:result= 13;break;
                    case 2:result= 23;break;
                    case 4:result= 4;break;
                    default:result= 3;break;
                }break;
            case 4:
                switch (oldState) {
                    case 1: result= 14;break;
                    case 2: result= 24;break;
                    case 3: result= 3;break;
                    default:result= 4;break;
                }break;
            default:result= 0;
        }
        return result;
    }
}


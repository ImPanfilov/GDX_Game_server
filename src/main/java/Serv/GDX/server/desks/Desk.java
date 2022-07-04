package Serv.GDX.server.desks;

import java.util.ArrayList;
import java.util.Random;
import Serv.GDX.server.actors.Food;
import Serv.GDX.server.actors.Pos;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;


@Data
@Component
public class Desk
{
    ArrayList<Pos> freeArr= new ArrayList<>();//свободные ячейки
    ArrayList<Pos> deskBody= new ArrayList<>();//поле
    Pos tmpPos ;
    Random rnd = new Random();

    @Value("${food.count}")
    int foodCount;

    @Value("${desk.size}")
    int deskSize;

    @Autowired
    Food food;

    public int getFreeSize(){
        return freeArr.size();
    }

    public Pos getFree(int rnd){
        return freeArr.get(rnd);
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < deskSize; i++)
            for (int j = 0; j < deskSize; j++)
            {tmpPos = new Pos(i,j,0,true,0);
             deskBody.add(tmpPos);
             freeArr.add(tmpPos);//заполнение свободных ячеек
            }
        AddFood(foodCount);
    }


    public void AddFood(int count){//создание еды
        for(int i=0;i<count;i++) {
            int Rnd = rnd.nextInt(freeArr.size());
            tmpPos = freeArr.get(Rnd);
            food.add(tmpPos);
            setState(tmpPos, 2, 0);
        }
    }

    public void setState(Pos XY,int TR,int stateDirection)
    {
        if (TR==2) { freeArr.remove(XY); XY.setState(TR);XY.setDirection(0);}
        if (TR==1) { freeArr.remove(XY); XY.setState(TR);XY.setDirection(stateDirection);}
        if (TR== 0) { freeArr.add(XY);XY.setState(TR);XY.setDirection(0);}
        XY.setChanged(true);
    }


    public boolean FreeArrContains(Pos pos) {
        return freeArr.contains(pos);
    }

    public Pos getDeskPos(Pos pos){
        return deskBody.get(deskSize*pos.getXPos()+pos.getYPos());
    }

}
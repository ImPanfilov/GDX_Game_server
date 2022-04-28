package Serv.GDX.server.desks;

import java.util.ArrayList;
import java.util.Random;
import Serv.GDX.server.GameLoop;
import Serv.GDX.server.actors.Food;
import Serv.GDX.server.actors.Pos;

public class Desk//класс поля
{
    private ArrayList<Pos> FreeArr;//свободные ячейки
    private Pos[][] DeskBody;
    private Pos Tmp ;
    private Random rnd = new Random();

    public int getFreeSize(){return FreeArr.size();}

    public Pos getFree(int rnd){return FreeArr.get(rnd);}

    public Desk()
    {
        DeskBody = new  Pos[GameLoop.getSizedesk()][GameLoop.getSizedesk()];
        FreeArr = new ArrayList<>(GameLoop.getSizedesk() * GameLoop.getSizedesk());
        for (int i = 0; i < GameLoop.getSizedesk(); i++)
            for (int j = 0; j < GameLoop.getSizedesk(); j++)
            { this.Tmp = new Pos(i,j,0,true,0);
                this.DeskBody[i][j]=this.Tmp;
                this.FreeArr.add(this.Tmp);//заполнение свободных ячеек
            }
        AddFood(GameLoop.getFoodCount());
    }


    public void AddFood(int count)//создание яблока
    { for(int i=0;i<count;i++) {
        int Rnd = rnd.nextInt(FreeArr.size());
        Tmp = FreeArr.get(Rnd);
        Food.add(Tmp);
        Setstate(Tmp, 2, 0);
        }

    }

    public void Setstate(Pos XY,int TR,int stateDirection)
    {
        if (TR==2) { this.FreeArr.remove(XY); this.setPosState(XY,TR,0);}
        if (TR==1) { this.FreeArr.remove(XY); this.setPosState(XY,TR,stateDirection);}
        if (TR== 0) { this.FreeArr.add(XY); this.setPosState(XY,TR,0);}
        DeskBody[XY.getXpos()][XY.getYpos()].setChanged(true);
    }


    public boolean FreeArrContains(Pos pos)
    {return FreeArr.contains(pos);}

    public Pos getDeskPos(Pos pos){
        return DeskBody[pos.getXpos()][pos.getYpos()];
    }

    public void setPosState(Pos pos,int TR,int stateDirection){
        pos.setState(TR,stateDirection);
        }

    public void setPosDirection(Pos pos,int direction){
        DeskBody[pos.getXpos()][pos.getYpos()].setDirection(direction);
        }

    public int getPosDirection(Pos pos){
        return DeskBody[pos.getXpos()][pos.getYpos()].getDirection();
        }
}
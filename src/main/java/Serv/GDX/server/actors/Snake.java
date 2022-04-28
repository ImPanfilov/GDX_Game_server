package Serv.GDX.server.actors;


import Serv.GDX.server.GameLoop;
import Serv.GDX.server.desks.Desk;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayDeque;
import java.util.Random;

public class Snake  implements Entity{
    private Integer stateKey;//{none,leftPressed,rightPressed,upPressed,downPressed}
    private Pos tmp=new Pos();
    private String id;
    private String Name;
    private int Score=0;

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        this.Score = score;
    }

    private final ArrayDeque<Pos> Bodysnake;//тело змея
    private Pos HeadWorm= new Pos(0,0);//голова змея
    Random rnd = new Random();

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Snake() {
        Bodysnake = new ArrayDeque<>();
        stateKey=0;
        Name="Undefined";
    }

    public void Fill(Desk desk){
        int Rnd = rnd.nextInt(desk.getFreeSize());
        tmp = desk.getFree(Rnd);
        Bodysnake.add(tmp);Bodysnake.add(tmp);Bodysnake.add(tmp);HeadWorm.set(tmp);
        desk.Setstate(tmp,1,0);
    }


    public void Step(Desk desk)//шаг игры
    {   stateKey=CornerStep(desk.getPosDirection(HeadWorm) % 10,stateKey);//обработка углов при повороте
        desk.setPosDirection(HeadWorm,stateKey);
        stateKey=stateKey%10;
        switch (stateKey) {//{none,leftPressed,rightPressed,upPressed,downPressed}
            case 0:
                break;
            case 1:
                HeadWorm.setXpos(HeadWorm.getXpos()-1);
                break;
            case 2:
                HeadWorm.setXpos(HeadWorm.getXpos()+1);
                break;
            case 3:
                HeadWorm.setYpos(HeadWorm.getYpos()+1);
                break;
            case 4:
                HeadWorm.setYpos(HeadWorm.getYpos()-1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + stateKey % 10);
        }
        HeadWorm.CheckSize();

        if (!(desk.FreeArrContains(desk.getDeskPos(HeadWorm))) && !(Food.check(desk.getDeskPos(HeadWorm)))&&(stateKey != 0))//проверка пересечения
            {
                free(desk);
                GameLoop.addBest(Name,Score);
                Score=0;
                Fill(desk);//новая игра
            }
        else{
        if (stateKey != 0) {
            desk.Setstate(desk.getDeskPos(HeadWorm), 1,stateKey);
            Bodysnake.offer(desk.getDeskPos(HeadWorm));//двигаем голову

            if (Food.check(desk.getDeskPos(HeadWorm)))//проверка пересечения с яблоком
                {
                    Food.removeFood(desk.getDeskPos(HeadWorm));
                    Score++;
                    desk.AddFood(1);//добавляем еду
                } else
                    {
                    tmp = Bodysnake.poll();//обрезаем хвост
                    if (tmp != null && !Bodysnake.contains(tmp))
                        {desk.Setstate(tmp, 0,0);}
                    }
            }
        }
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public Integer getStateKey() {return stateKey;}

    public void setStateKey(Integer statekey) {
    stateKey=statekey;
    }


    public void free(Desk desk) {
        for (Pos pos:Bodysnake)
            {desk.Setstate(pos,0,0);}
        Bodysnake.clear();
        this.stateKey=0;
    }



    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("name", Name);
        json.writeValue("score", Score);
        json.writeValue("pos", Bodysnake);
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


package Serv.GDX.server.actors;

import Serv.GDX.server.desks.Desk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnakeService {
    @Autowired
    Snake snake;
    @Autowired
    Desk desk;


    public static int CornerStep(int oldState, int newState)//обработка углов при повороте
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

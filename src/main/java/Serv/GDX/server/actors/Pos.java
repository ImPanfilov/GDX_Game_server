package Serv.GDX.server.actors;

import Serv.GDX.server.GameLoop;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Pos  implements Json.Serializable
{
    private int Xpos;
    private int Ypos;
    private int State;//0-пусто,1-игрок,2-яблоко
    private boolean Changed;
    private int Direction;//0-none,1-left,2-right,3-up,4-down


    public Pos(int xpos, int ypos,int State,boolean changed,int direction)
        {
        this.Xpos = xpos;
        this.Ypos = ypos;
        this.State = State;
        this.Changed = changed;
        this.Direction=direction;
        }

    public Pos()
        {
        this.Xpos = 0;
        this.Ypos = 0;
        this.State = 0;
        this.Changed = true;
        this.Direction=0;
        }

    public Pos(int xpos, int ypos)
        {
        this.Xpos = xpos;
        this.Ypos = ypos;
        this.State = 0;
        this.Changed = true;
        this.Direction=0;
        }


    public int getXpos() {
        return Xpos;
    }

    public void setXpos(int xpos) {
        Xpos = xpos;
    }

    public int getYpos() {
        return Ypos;
    }

    public void setYpos(int ypos) {
        Ypos = ypos;
    }

    public int getState() {
        return State;
    }

    public void setState(int state,int direction)
        {
        this.State = state;this.Direction=direction;
        }

    public boolean isChanged() {
        return Changed;
    }

    public void setChanged(boolean changed) {
        Changed = changed;
    }

    public int getDirection() {return Direction;}

    public void setDirection(int direction) {Direction = direction;}

    @Override
    public void write(Json json)
        {
        json.writeValue("x", Xpos);
        json.writeValue("y", Ypos);
        json.writeValue("state", State);
        json.writeValue("changed", Changed);
        json.writeValue("direction", Direction);
        }

    @Override
    public void read(Json json, JsonValue jsonData) {}

    public void set(Pos pos) {
        this.Xpos=pos.getXpos();
        this.Ypos=pos.getYpos();
    }

    public void CheckSize() {
        Xpos=(Xpos == -1) ? GameLoop.getSizedesk() - 1 : Xpos;
        Xpos=(Xpos == GameLoop.getSizedesk()) ? 0 : Xpos;
        Ypos=(Ypos == -1) ? GameLoop.getSizedesk() - 1 : Ypos;
        Ypos=(Ypos == GameLoop.getSizedesk()) ? 0 : Ypos;
    }
}

package Serv.GDX.server.best;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;


@Data
@NoArgsConstructor
@Component
public class Best {
    static ArrayList<BestSnake> bestScore=new ArrayList<>();

    public static Iterable<? extends BestSnake> getBest(){
        return bestScore;
    }

    public static void addBest(String name, int score) {
        bestScore.add(new BestSnake(name,score));
        Collections.sort(bestScore);
        if (bestScore.size()>5)bestScore.remove(0);
        for(BestSnake bs:bestScore){bs.setId(bestScore.indexOf(bs));}
    }
}

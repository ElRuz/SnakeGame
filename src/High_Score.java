import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;

public class High_Score extends GamePanel {

    public ArrayList<Integer> count = new ArrayList<>();

    public void high(Integer applesEaten){
        count.add(applesEaten);
    }

}

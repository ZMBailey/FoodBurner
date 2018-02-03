package soulstudios.caloriecounter;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by soulo_000 on 11/26/2017.
 */
@Entity
public class Food {
    @Id public Long id;
    public String name;
    public int calories;
    public int icon;

    public Food(){}

    public Food(String nm, int cal, int ic){
        name = nm;
        calories = cal;
        icon = ic;
    }
}

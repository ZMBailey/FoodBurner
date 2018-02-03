package soulstudios.caloriecounter;


/**
 * Created by soulo_000 on 1/8/2018.
 */

public class Workout {
    public Food foodtype;
    public double foodamount;
    public double startCal;
    public double currentCal = 0;
    public double goalCal;

    public Workout(Food f, double start,double goal){
        foodtype = f;
        startCal = start;
        goalCal = goal;
    }

    public Workout(Food f, double start,int amount){
        foodtype = f;
        foodamount = amount;
        startCal = start;

        goalCal = start+(f.calories*amount);
    }

}

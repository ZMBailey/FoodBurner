package soulstudios.caloriecounter;

import android.app.Application;

/**
 * Created by soulo_000 on 11/6/2017.
 */

public class AppMain extends Application {
    private static double speed;
    private static double weight;

    static public void setSpeed(double s){
        speed = s;
    }
    static public double getSpeed(){
        return speed;
    }

    static public void setWeight(double w){
        weight = w;
    }
    static public double getWeight(){
        return weight;
    }
}

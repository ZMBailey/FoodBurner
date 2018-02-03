package soulstudios.caloriecounter;

import com.fitbit.authentication.AuthenticationConfiguration;
import com.fitbit.authentication.AuthenticationConfigurationBuilder;
import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.authentication.ClientCredentials;
import com.fitbit.authentication.Scope;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by soulo_000 on 11/6/2017.
 */

public class AppMain extends Application {
    private static double speed;
    private static double weight;
    private static final String CLIENT_ID = "22CKJG";
    private static final String CLIENT_SECRET = "55de34a57e896ebebbca6bb998b0ed78";
    private static final String REDIRECT_URL = "http://finished";
    private static final String SECURE_KEY = "8dLslfR8NWAmhpYv4/yT643KcWcRK6DhVX7gsIxumAk=";
    static String token;
    public static ArrayList<Food> foodlist = new ArrayList<Food>();
    public static BoxStore boxStore;
    public static Workout workout;

    static public void setSpeed(double s){
        speed = s;
    }
    static public double getSpeed(){
        return speed;
    }

    static public void setWeight(double w){
        weight = w;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        boxStore = MyObjectBox.builder().androidContext(AppMain.this).build();
//        foodBox = boxStore.boxFor(Food.class);
        setFood();

        AuthenticationManager.configure(this, generateAuthenticationConfiguration(this,StartActivity.class));
    }

    private void setFood(){
        foodlist.add(new Food("Wine",123,R.drawable.wine));
        foodlist.add(new Food("Martini",127,R.drawable.martini));
        foodlist.add(new Food("Beer",150,R.drawable.beer));
        foodlist.add(new Food("Soda",138,R.drawable.soda));
        foodlist.add(new Food("Burger",354,R.drawable.burger2));
        foodlist.add(new Food("Hot Dog",151,R.drawable.hotdog));
        foodlist.add(new Food("Fries",365,R.drawable.fries));
        foodlist.add(new Food("Steak",679,R.drawable.steak));
        foodlist.add(new Food("Sandwich",560,R.drawable.sandwich));
        foodlist.add(new Food("Pizza",285,R.drawable.pizza2));
        foodlist.add(new Food("Taco",156,R.drawable.taco));
        foodlist.add(new Food("Ice Cream",137,R.drawable.icecream3));
        foodlist.add(new Food("Bacon(Slice)",43,R.drawable.bacon));
        foodlist.add(new Food("Donut",128,R.drawable.donut));
//        List<Food> foodcrate = foodBox.getAll();
//        if(foodcrate.size() > 0){
//            foodlist.addAll(foodcrate);
//        }
    }

    public static AuthenticationConfiguration generateAuthenticationConfiguration(Context context, Class<? extends Activity> mainActivityClass) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;

            ClientCredentials clientCredentials = new ClientCredentials(CLIENT_ID, CLIENT_SECRET, REDIRECT_URL);
            String secureKey = SECURE_KEY;

            return new AuthenticationConfigurationBuilder()
                    .setClientCredentials(clientCredentials)
                    .setEncryptionKey(secureKey)
                    .setBeforeLoginActivity(new Intent(context, mainActivityClass))
                    .addRequiredScopes(Scope.profile, Scope.settings)
                    .addOptionalScopes(Scope.activity, Scope.weight)
                    .setLogoutOnAuthFailure(true)
                    .build();

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    static public BoxStore getBoxStore(){
        return boxStore;
    }

//    public static void updateFood(){
//        List<Food> crate = foodBox.getAll();
//        foodlist.add(crate.get(crate.size()-1));
//    }

    static public double getWeight(){
        return weight;
    }

    static public void setWorkout(Workout wo){
        workout = wo;
    }

    static public Workout getWorkout() {return workout;}

    static public boolean hasWorkout(){
        return (workout != null);
    }
}

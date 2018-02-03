package soulstudios.caloriecounter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import io.objectbox.Box;
import worker8.com.github.radiogroupplus.RadioGroupPlus;

/*
 *Regular tracker with timer
 */

public class TrackerActivity extends AppCompatActivity implements
        FoodFragment.OnFragmentInteractionListener{

    HashMap<String,Integer> food = new HashMap<String,Integer>();
    HashMap<String,Drawable> icons = new HashMap<String,Drawable>();
    private double weight;
    private double total_cals;
    private double miles;
    private double speed;
    private boolean isStarted = false;
    private CountUpTimer timer;
    private int timeleft;
    private TextView curr_speed;
    private LinearLayout counter;
    private FoodFragment foodfragment;
    private Box<Food> foodBox = AppMain.getBoxStore().boxFor(Food.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        weight = AppMain.getWeight();
        speed = AppMain.getSpeed();
        foodfragment = (FoodFragment) getSupportFragmentManager().findFragmentById(R.id.food_frag);
        counter = (LinearLayout) findViewById(R.id.counter);

        ImageButton start = (ImageButton) findViewById(R.id.start);
        start.setOnClickListener(new StartHandler());
        ImageButton stop = (ImageButton) findViewById(R.id.stop);
        stop.setOnClickListener(new StopHandler());

        ImageButton up = (ImageButton) findViewById(R.id.but_up);
        ImageButton down = (ImageButton) findViewById(R.id.but_down);
        up.setOnClickListener(new UpHandler());
        down.setOnClickListener(new DownHandler());

        curr_speed = (TextView) findViewById(R.id.speed_display);
        curr_speed.setText(String.format(Locale.getDefault(),"%.0f mph",speed));

        setTables();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void calcRun(){
        total_cals = (0.75*weight)*miles*100;
    }

    private void calcWalk(){
        total_cals = (0.53*weight)*miles;
    }

    private void calcMiles(int seconds){
        double div = (speed/3600);
        miles = seconds*div;
    }

    private void updateCounter(){
        counter.removeAllViewsInLayout();
        if(foodfragment.getFood() != null) {
            Log.w("Checked ID",Integer.toString(foodfragment.getChecked()));
            RadioButton fooditem = foodfragment.getFood();
            int index = Integer.parseInt(fooditem.getTag().toString());
            ArrayList<Food> crate = new ArrayList<Food>();
            crate.addAll(AppMain.foodlist);
            crate.addAll(foodBox.getAll());
            double cals = crate.get(index).calories;

            int count = (int) total_cals / (int) cals;
            ImageView[] items = new ImageView[count];

            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(100, 100);
            ll.setMargins(10, 15, 0, 5);

            for (int i = 0; i < count; i++) {
                items[i] = new ImageView(this);
                int in = crate.get(index).icon;
                items[i].setImageDrawable(getResources().getDrawable(in));
                counter.addView(items[i], ll);
            }
        }else{
            String warning = "No food type selected, please select food type.";
            Toast.makeText(this,warning,Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer(){
        final TextView tv = (TextView) findViewById( R.id.time_display );
        final TextView cal = (TextView) findViewById(R.id.cal_display);
        timer = new CountUpTimer(600000) {

            public void onTick(int second) {
                calcMiles(second);
                String time = CountUpTimer.ConvertToTime(second);
                tv.setText(time);
                if(speed>=6){
                    calcRun();
                    cal.setText(String.format(Locale.getDefault(),"%.3f",total_cals));
                }else{
                    calcWalk();
                    cal.setText(String.format(Locale.getDefault(),"%.3f",total_cals));
                }
                updateCounter();
            }
        };
        timer.start();
    }

    public void onClickAdd(View v){
        Intent select = new Intent(TrackerActivity.this,AddItemActivity.class);
        startActivity(select);
    }

    public void onClickBack(View v){
        this.finish();
    }

    private class StartHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(!isStarted){
                startTimer();
                isStarted = true;
            }
        }
    }

    private class StopHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(isStarted){
                timer.cancel();
                timer = null;
                isStarted = false;
            }
        }
    }

    private class UpHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            speed++;
            curr_speed.setText(String.format(Locale.getDefault(),"%.0f mph",speed));
        }
    }

    private class DownHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            speed--;
            curr_speed.setText(String.format(Locale.getDefault(),"%.0f mph",speed));
        }
    }

    private void setTables(){
        food.put("wine",123);
        food.put("martini",127);
        food.put("beer",150);
        food.put("soda",138);
        food.put("burger",354);
        food.put("hotdog",151);
        food.put("fries",365);
        food.put("steak",679);
        food.put("sandwich",560);
        food.put("pizza",285);
        food.put("taco",156);
        food.put("icecream",137);
        food.put("bacon",43);
        food.put("donut",128);

        icons.put("wine",getResources().getDrawable(R.drawable.wine));
        icons.put("martini",getResources().getDrawable(R.drawable.martini));
        icons.put("beer",getResources().getDrawable(R.drawable.beer));
        icons.put("soda",getResources().getDrawable(R.drawable.soda));
        icons.put("burger",getResources().getDrawable(R.drawable.burger2));
        icons.put("hotdog",getResources().getDrawable(R.drawable.hotdog));
        icons.put("fries",getResources().getDrawable(R.drawable.fries));
        icons.put("steak",getResources().getDrawable(R.drawable.steak));
        icons.put("sandwich",getResources().getDrawable(R.drawable.sandwich));
        icons.put("pizza",getResources().getDrawable(R.drawable.pizza2));
        icons.put("taco",getResources().getDrawable(R.drawable.taco));
        icons.put("icecream",getResources().getDrawable(R.drawable.icecream3));
        icons.put("bacon",getResources().getDrawable(R.drawable.bacon));
        icons.put("donut",getResources().getDrawable(R.drawable.donut));
    }
}

package soulstudios.caloriecounter;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class TrackerActivity extends AppCompatActivity {

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
    private RadioGroupPlus foodgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        weight = AppMain.getWeight();
        speed = AppMain.getSpeed();

        counter = (LinearLayout) findViewById(R.id.counter);
        foodgroup = (RadioGroupPlus) findViewById(R.id.food_item);
        Button start = (Button) findViewById(R.id.start_stop);
        start.setOnClickListener(new StartHandler());

        ImageButton up = (ImageButton) findViewById(R.id.but_up);
        ImageButton down = (ImageButton) findViewById(R.id.but_down);
        up.setOnClickListener(new UpHandler());
        down.setOnClickListener(new DownHandler());

        curr_speed = (TextView) findViewById(R.id.speed_display);
        curr_speed.setText(String.format(Locale.getDefault(),"%.0f mph",speed));

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
        icons.put("burger",getResources().getDrawable(R.drawable.burger));
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

    private void calcRun(){
        total_cals = (0.75*weight)*miles;
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
        if(foodgroup.getCheckedRadioButtonId() != -1) {
            RadioButton fooditem = (RadioButton) findViewById(foodgroup.getCheckedRadioButtonId());
            String index = fooditem.getTag().toString();
            double cals = food.get(index);

            int count = (int) total_cals / (int) cals;
            ImageView[] items = new ImageView[count];

            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(100, 100);
            ll.setMargins(10, 15, 0, 5);

            for (int i = 0; i < count; i++) {
                items[i] = new ImageView(this);
                items[i].setImageDrawable(icons.get(index));
                counter.addView(items[i], ll);
            }
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

    private class StartHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(isStarted){
                timer.cancel();
                timer = null;
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                Button tv = (Button) v;
                tv.setText(getResources().getString(R.string.start));
                isStarted = false;
            }else {
                startTimer();
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                Button tv = (Button) v;
                tv.setText(getResources().getString(R.string.stop));
                isStarted = true;
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
}

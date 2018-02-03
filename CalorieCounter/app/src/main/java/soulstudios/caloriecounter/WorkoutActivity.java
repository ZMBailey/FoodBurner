package soulstudios.caloriecounter;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fitbit.api.loaders.ResourceLoaderResult;
import com.fitbit.api.models.DailyActivitySummary;
import com.fitbit.api.models.Summary;
import com.fitbit.api.services.ActivityService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.objectbox.Box;

public class WorkoutActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ResourceLoaderResult<DailyActivitySummary>>,
        SwipeRefreshLayout.OnRefreshListener, FoodFragment.OnFragmentInteractionListener{

    HashMap<String,Integer> food = new HashMap<String,Integer>();
    HashMap<String,Integer> icons = new HashMap<String,Integer>();
    private double total_cals = 0;
    private LinearLayout counter;
    private boolean isStarted = false;
    protected final String TAG = getClass().getSimpleName();
    private CountUpTimer timer;
    private Box<Food> foodBox = AppMain.getBoxStore().boxFor(Food.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        counter = (LinearLayout) findViewById(R.id.counter);
        getLoaderManager().initLoader(0,null,this);

        setTables();
    }

    @Override
    public void onRefresh() {
        getLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void updateCounter(){
        if(total_cals < AppMain.getWorkout().goalCal) {
            counter.removeAllViewsInLayout();
            final TextView ct = (TextView) findViewById(R.id.cal_display);
            Workout wo = AppMain.getWorkout();
            double currCals = total_cals - wo.startCal;
            AppMain.getWorkout().currentCal = currCals;
            ct.setText(String.format(Locale.getDefault(), "%.3f", currCals));

            double displayCals = wo.goalCal - total_cals;
            double cals = wo.foodtype.calories;

            int count = (int) displayCals / (int) cals;
            ImageView[] items = new ImageView[count];
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(100, 100);
            ll.setMargins(10, 15, 0, 5);
            float alpha = (float) ((displayCals / cals) % 1);

            for (int i = 0; i < count; i++) {
                items[i] = new ImageView(this);
                int in = wo.foodtype.icon;
                items[i].setImageDrawable(getResources().getDrawable(in));
                counter.addView(items[i], ll);
            }

            if ((alpha > 0)) {
                ImageView item = new ImageView(this);
                int in = wo.foodtype.icon;
                item.setImageDrawable(getResources().getDrawable(in));
                item.setAlpha(alpha);
                counter.addView(item, ll);
            }
        }else{
            stopTimer();
        }
    }

    private void startTimer(){
        timer = new CountUpTimer(600000) {

            public void onTick(int second) {
                if(second % 2 == 0) {
                    //getLoaderManager().getLoader(0).forceLoad();
                    total_cals += 10;
                }
                updateCounter();
            }
        };
        timer.start();
    }

    @Override
    public Loader<ResourceLoaderResult<DailyActivitySummary>> onCreateLoader(int id, Bundle args) {
        return ActivityService.getDailyActivitySummaryLoader(this, new Date());
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<DailyActivitySummary>> loader, ResourceLoaderResult<DailyActivitySummary> data) {
        switch (data.getResultType()) {
            case ERROR:
                Toast.makeText(this, R.string.error_loading_data, Toast.LENGTH_LONG).show();
                break;
            case EXCEPTION:
                Log.e(TAG, "Error loading data", data.getException());
                Toast.makeText(this, R.string.error_loading_data, Toast.LENGTH_LONG).show();
                break;
        }
        if (data.isSuccessful()) {
            getCalories(data.getResult());
        }
    }

    public void getCalories(DailyActivitySummary daily){
        Summary summary = daily.getSummary();
        total_cals = summary.getActivityCalories();
        updateCounter();
    }

    @Override
    public void onLoaderReset(Loader<ResourceLoaderResult<DailyActivitySummary>> loader) {
        //do nothing
    }

    public void onClickReload(View v){
        getLoaderManager().getLoader(0).forceLoad();
    }

    public void onClickStart(View v) {
        if(AppMain.hasWorkout()){
            if(!isStarted) {
                startTimer();
                isStarted = true;
            }
        }else{
            String error = "No workout defined. Please set up workout first.";
            Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickStop(View v) {
        if(isStarted) {
           stopTimer();
        }
    }

    public void stopTimer(){
        timer.cancel();
        timer = null;
        isStarted = false;
    }

    public void onClickAdd(View v){
        Intent select = new Intent(WorkoutActivity.this,CreateWorkoutActivity.class);
        startActivity(select);
    }

    public void onClickBack(View v){
        this.finish();
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

        icons.put("wine",R.drawable.wine);
        icons.put("martini",R.drawable.martini);
        icons.put("beer",R.drawable.beer);
        icons.put("soda",R.drawable.soda);
        icons.put("burger",R.drawable.burger2);
        icons.put("hotdog",R.drawable.hotdog);
        icons.put("fries",R.drawable.fries);
        icons.put("steak",R.drawable.steak);
        icons.put("sandwich",R.drawable.sandwich);
        icons.put("pizza",R.drawable.pizza2);
        icons.put("taco",R.drawable.taco);
        icons.put("icecream",R.drawable.icecream3);
        icons.put("bacon",R.drawable.bacon);
        icons.put("donut",R.drawable.donut);
    }
}

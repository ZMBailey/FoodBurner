package soulstudios.caloriecounter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.LoaderManager;
import android.content.Loader;
import android.widget.Toast;

import com.fitbit.api.loaders.ResourceLoaderResult;
import com.fitbit.api.models.DailyActivitySummary;
import com.fitbit.api.models.Summary;
import com.fitbit.api.services.ActivityService;
import com.fitbit.api.models.User;
import com.fitbit.api.services.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.objectbox.Box;
import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class FBTrackerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ResourceLoaderResult<DailyActivitySummary>>,
        SwipeRefreshLayout.OnRefreshListener, FoodFragment.OnFragmentInteractionListener{

    HashMap<String,Integer> food = new HashMap<String,Integer>();
    HashMap<String,Integer> icons = new HashMap<String,Integer>();
    private double total_cals;
    private LinearLayout counter;
    private CountUpTimer timer;
    private boolean isStarted = false;
    protected final String TAG = getClass().getSimpleName();
    protected ViewDataBinding binding;
    private FoodFragment foodfragment;
    private Box<Food> foodBox = AppMain.getBoxStore().boxFor(Food.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_tracker);

        counter = (LinearLayout) findViewById(R.id.counter);
        foodfragment = (FoodFragment) getSupportFragmentManager().findFragmentById(R.id.food_frag);
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

    private void startTimer(){

        timer = new CountUpTimer(600000) {

            public void onTick(int second) {
                getLoaderManager().getLoader(0).forceLoad();
                updateCounter();
            }
        };
        timer.start();
    }

    private void updateCounter(){
        counter.removeAllViewsInLayout();
        if(foodfragment.getFood() != null) {
            final TextView ct = (TextView) findViewById(R.id.cal_display);
            ct.setText(String.format(Locale.getDefault(),"%.3f",total_cals));
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

    public void onClickStart(View v){
        if(!isStarted){
            startTimer();
            isStarted = true;
        }
    }

    public void onClickStop(View v){
        if(isStarted){
            timer.cancel();
            timer = null;
            isStarted = false;
        }
    }

    public void onClickAdd(View v){
        Intent select = new Intent(FBTrackerActivity.this,AddItemActivity.class);
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

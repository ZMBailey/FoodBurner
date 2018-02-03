package soulstudios.caloriecounter;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fitbit.api.loaders.ResourceLoaderResult;
import com.fitbit.api.models.DailyActivitySummary;
import com.fitbit.api.models.Summary;
import com.fitbit.api.services.ActivityService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.objectbox.Box;
import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class CreateWorkoutActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ResourceLoaderResult<DailyActivitySummary>>,
         FoodFragment.OnFragmentInteractionListener{

    private HashMap<String,Integer> icons = new HashMap<String,Integer>();
    private RadioGroupPlus typegroup;
    protected final String TAG = getClass().getSimpleName();
    private double start_cals;
    private FoodFragment foodfragment;
    private Box<Food> foodBox = AppMain.getBoxStore().boxFor(Food.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        getLoaderManager().initLoader(0,null,this);
        setTable();
        foodfragment = (FoodFragment) getSupportFragmentManager().findFragmentById(R.id.food_frag);
        typegroup = (RadioGroupPlus) findViewById(R.id.workouttype);
        Button add = (Button) findViewById(R.id.add_workout);
        add.setOnClickListener(new AddWorkoutHandler());
    }

    private class AddWorkoutHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            RadioButton fooditem = foodfragment.getFood();
            int index = Integer.parseInt(fooditem.getTag().toString());
            ArrayList<Food> crate = new ArrayList<Food>();
            crate.addAll(AppMain.foodlist);
            crate.addAll(foodBox.getAll());
            Food food = crate.get(index);

            RadioButton checked = (RadioButton) findViewById(typegroup.getCheckedRadioButtonId());

            if(checked.getTag().toString().equals("amount")){
                EditText tv = (EditText) findViewById(R.id.amount_input);
                int amount = Integer.parseInt(tv.getText().toString());
                AppMain.workout = new Workout(food,start_cals,amount);
            }else{
                EditText tv = (EditText) findViewById(R.id.cal_input);
                getLoaderManager().getLoader(0).forceLoad();
                double target = start_cals + Double.parseDouble(tv.getText().toString());
                AppMain.workout = new Workout(food,start_cals,target);
            }

            CreateWorkoutActivity.this.finish();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        start_cals = summary.getActivityCalories();
    }

    @Override
    public void onLoaderReset(Loader<ResourceLoaderResult<DailyActivitySummary>> loader) {
        //do nothing
    }

    private void setTable(){
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

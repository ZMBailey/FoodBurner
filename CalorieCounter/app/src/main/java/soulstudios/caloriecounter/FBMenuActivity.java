package soulstudios.caloriecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FBMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbmenu);
    }

    public void onWorkout(View view){
        Intent intent = new Intent(this,WorkoutActivity.class);
        startActivity(intent);
    }

    public void onFBTracker(View view){
        Intent intent = new Intent(this,FBTrackerActivity.class);
        startActivity(intent);
    }

    public void onClickBack(View view){
        this.finish();
    }
}

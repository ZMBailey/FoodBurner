package soulstudios.caloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Button go = (Button) findViewById(R.id.next);
        go.setOnClickListener(new NextHandler());
    }

    public void onClickBack(View v){
        this.finish();
    }

    private class NextHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            EditText weight = (EditText) findViewById(R.id.weight_input);
            EditText speed = (EditText) findViewById(R.id.speed_input);

            AppMain.setWeight(Integer.parseInt(weight.getText().toString()));
            AppMain.setSpeed(Integer.parseInt(speed.getText().toString()));

            getWindow().setExitTransition(new Explode());
            Intent select = new Intent(SetupActivity.this,TrackerActivity.class);
            startActivity(select);
        }
    }
}

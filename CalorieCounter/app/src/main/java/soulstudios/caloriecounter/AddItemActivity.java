package soulstudios.caloriecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.HashMap;

import io.objectbox.Box;
import worker8.com.github.radiogroupplus.RadioGroupPlus;

public class AddItemActivity extends AppCompatActivity {

    private HashMap<String,Integer> icons = new HashMap<String,Integer>();
    private RadioGroupPlus icongroup;
    private Box<Food> foodBox = AppMain.getBoxStore().boxFor(Food.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTable();
        icongroup = (RadioGroupPlus) findViewById(R.id.icons);
        Button add = (Button) findViewById(R.id.add_item);
        add.setOnClickListener(new AddItemHandler());
    }

    private class AddItemHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            EditText tvn = (EditText) findViewById(R.id.name_input);
            EditText tvc = (EditText) findViewById(R.id.cal_input);
            RadioButton ico = (RadioButton) findViewById(icongroup.getCheckedRadioButtonId());
            String name = tvn.getText().toString();
            int cal = Integer.parseInt(tvc.getText().toString());
            int pic = icons.get(ico.getTag().toString());
            foodBox.put(new Food(name, cal, pic));
            //AppMain.updateFood();
//            AppMain.foodlist.add(new Food(name, cal, pic));
            AddItemActivity.this.finish();
        }
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

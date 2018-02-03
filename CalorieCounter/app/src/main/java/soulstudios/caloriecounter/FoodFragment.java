package soulstudios.caloriecounter;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import io.objectbox.Box;
import worker8.com.github.radiogroupplus.RadioGroupPlus;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Box<Food> foodBox = AppMain.getBoxStore().boxFor(Food.class);
    private HashMap<ImageView,Food> foods = new HashMap<ImageView,Food>();

    private RadioGroupPlus foodgroup;
    private TableLayout foodtable;
    private TableRow header;
    private View fRoot;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public int getChecked(){
        return foodgroup.getCheckedRadioButtonId();
    }

    public RadioButton getFood(){
        return (RadioButton) fRoot.findViewById(foodgroup.getCheckedRadioButtonId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    public void updateView(){
        foodgroup.removeAllViewsInLayout();
        foodtable.removeView(header);
        foodtable = new TableLayout(fRoot.getContext());
        TableLayout.LayoutParams ft = new TableLayout.LayoutParams();
        ft.width = ActionBar.LayoutParams.MATCH_PARENT;
        ft.setMargins(30,16,10,0);
        foodtable.setStretchAllColumns(true);
        foodtable.addView(header);

        ArrayList<Food> fList = new ArrayList<Food>();
        fList.addAll(AppMain.foodlist);
        fList.addAll(foodBox.getAll());
        TableRow[] rows = new TableRow[fList.size()];
        RadioButton[] name = new RadioButton[fList.size()];
        TextView[] cal = new TextView[fList.size()];
        ImageView[] icon = new ImageView[fList.size()];
        ImageView[] del = new ImageView[fList.size()];
        int i = 0;

        Log.w("rows",Integer.toString(fList.size()));
        for(Food item : fList){
            rows[i] = new TableRow(fRoot.getContext());
            name[i] = new RadioButton(fRoot.getContext());
            name[i].setTag(Integer.toString(i));
            name[i].setText(item.name);
            cal[i] = new TextView(fRoot.getContext());
            cal[i].setText(String.format(Locale.getDefault(),"%d",item.calories));
            icon[i] = new ImageView(fRoot.getContext());
            icon[i].setImageDrawable(getResources().getDrawable(item.icon));
            rows[i].addView(name[i], ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rows[i].addView(cal[i], ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rows[i].addView(icon[i],50,50);
            if(i >= 14){
               del[i] = new ImageView(fRoot.getContext());
               del[i].setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_delete));
               del[i].setOnClickListener(new DeleteHandler());
               foods.put(del[i],item);
               rows[i].addView(del[i],50,50);
            }
            foodtable.addView(rows[i],ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            i++;
        }

        foodgroup.addView(foodtable,ft);
    }

    private class DeleteHandler implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            foodBox.remove(foods.get((ImageView) v));
            updateView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fRoot = inflater.inflate(R.layout.fragment_food, container, false);
        foodtable = (TableLayout) fRoot.findViewById(R.id.food_table);
        foodgroup = (RadioGroupPlus) fRoot.findViewById(R.id.food_item);
        header = (TableRow) fRoot.findViewById(R.id.food_titles);
        updateView();
        foodgroup.check(R.id.radio_wine);
        return fRoot;
    }

    public void showIcons(){

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

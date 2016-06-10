package co.edu.udea.compumovil.proyectogr8.foodea.Foods;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodDetailsFragment extends ListFragment {

    final static String ARG_CATEGORY = "Category";
    private ArrayList<String> foodList;
    private String mCurrentCategory;
    OnFoodSelectedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnFoodSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected*/
         void onFoodSelected(String food);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentCategory = getArguments().getString(ARG_CATEGORY);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        //Create the database handler
        DBAdapter dbHandler = new DBAdapter(getContext());
        //Open the connection with the database
        dbHandler.openConnection();
        //get all foods categories

        foodList = dbHandler.getFoodByCategory(mCurrentCategory);
        dbHandler.closeConnection();
        /*for(String c: foodList){
            Log.d("TESTING", c);
        }*/

        // Create an array adapter for the list view, using the data read from the database
        setListAdapter(new ArrayAdapter<>(getActivity(), layout, foodList));
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnFoodSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFoodCategorySelected");
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onFoodSelected(foodList.get(position));
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);

    }

}

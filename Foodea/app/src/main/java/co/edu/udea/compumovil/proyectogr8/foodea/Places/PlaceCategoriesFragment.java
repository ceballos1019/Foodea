package co.edu.udea.compumovil.proyectogr8.foodea.Places;


import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceCategoriesFragment extends ListFragment {

    public static final String TAB_TITLE = "CATEGORIAS";

    OnProductCategorySelectedListener mCallback;
    ArrayList<String> categories;
    ArrayAdapter adapter;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnProductCategorySelectedListener {
        /** Called by HeadlinesFragment when a list item is selected*/
        void onProductCategorySelected(String categories);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;


        //Create the database handler
        DBAdapter dbHandler = new DBAdapter(getContext());
        //Open the connection with the database
        dbHandler.openConnection();
        //get all foods categories

        Bundle args = getArguments();
        int idPlace=0;
        if(args!=null){
            idPlace = args.getInt(PlaceActivity.PLACE_CATEGORIES_KEY);
        }

        categories = dbHandler.getCategoriesByPlace(idPlace);
        dbHandler.closeConnection();

        for(String c: categories){
            Log.d("TESTING", c);
        }

        adapter = new ArrayAdapter<>(getContext(),layout,categories);



        // Create an array adapter for the list view, using the data read from the database
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        /*if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnProductCategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProductCategorySelectedListener");
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onProductCategorySelected(categories.get(position));
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int[] colors = {0, 0xFFB0BEC5, 0}; // red for the example
        getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        getListView().setDividerHeight(5);
    }
}

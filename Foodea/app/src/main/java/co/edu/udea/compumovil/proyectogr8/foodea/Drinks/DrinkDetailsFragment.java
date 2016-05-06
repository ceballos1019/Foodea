package co.edu.udea.compumovil.proyectogr8.foodea.Drinks;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrinkDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkDetailsFragment extends ListFragment {

    onDrinkSelectedListener mCallback;
    final static String ARG_CATEGORY = "Category";
    private ArrayList<String> drinkList;
    private String mCurrentCategory;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface onDrinkSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected*/
         void onDrinkSelected(String category);
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

        drinkList = dbHandler.getDrinkByCategory(mCurrentCategory);
        dbHandler.closeConnection();
        /*for(String c: foodList){
            Log.d("TESTING", c);
        }*/

        // Create an array adapter for the list view, using the data read from the database
        setListAdapter(new ArrayAdapter<>(getActivity(), layout, drinkList));
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
            mCallback = (onDrinkSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFoodCategorySelected");
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onDrinkSelected(drinkList.get(position));
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);

    }
}

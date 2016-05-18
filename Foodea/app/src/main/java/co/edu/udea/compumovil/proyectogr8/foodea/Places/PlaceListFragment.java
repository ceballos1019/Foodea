package co.edu.udea.compumovil.proyectogr8.foodea.Places;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;
import co.edu.udea.compumovil.proyectogr8.foodea.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceListFragment extends ListFragment {

    private ArrayList<Place> places;



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
        places = dbHandler.getAllPlaces();
        ArrayList<String> placesNames = new ArrayList<>();
        for(Place place: places){
               placesNames.add(place.getName());
           }
        dbHandler.closeConnection();
        /*for(String c: places){
            Log.d("TESTING", c);
        }*/
        // Create an array adapter for the list view, using the data read from the database
        setListAdapter(new ArrayAdapter<>(getContext(),layout,placesNames));
    }
}

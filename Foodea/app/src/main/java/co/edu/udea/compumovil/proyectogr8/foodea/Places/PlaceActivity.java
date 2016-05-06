package co.edu.udea.compumovil.proyectogr8.foodea.Places;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import co.edu.udea.compumovil.proyectogr8.foodea.Foods.FoodListFragment;
import co.edu.udea.compumovil.proyectogr8.foodea.R;

public class PlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        setTitle("Lugares");

        //Set the initial fragment for the activity
        if(findViewById(R.id.fragment_place_container) != null){
            Log.d("TESTING", "ON PLACE ACTIIVTY");
            Fragment placeListFragment = new PlaceListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_place_container, placeListFragment);
            transaction.commit();
        }
    }
}

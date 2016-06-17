package co.edu.udea.compumovil.proyectogr8.foodea.Drinks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import co.edu.udea.compumovil.proyectogr8.foodea.Maps.SpecificPlacesActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.R;

public class DrinkActivity extends AppCompatActivity implements DrinkListFragment.OnDrinkCategorySelectedListener, DrinkDetailsFragment.onDrinkSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        setTitle("Categorias - Bebidas");

        if(findViewById(R.id.fragment_drink_container) != null){
            Fragment drinkListFragment = new DrinkListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_drink_container,drinkListFragment);
            transaction.commit();

            //getSupportFragmentManager().beginTransaction()
            //        .add(R.id.fragment_container, headlinesFragment).commit();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle("Categorias - Bebidas");
    }

    @Override
    public void onDrinkCategorySelected(String category) {
        // Create fragment and give it an argument for the selected food category
        DrinkDetailsFragment drinkDetailsFragment = new DrinkDetailsFragment();
        Bundle args = new Bundle();
        args.putString(DrinkDetailsFragment.ARG_CATEGORY,category);
        drinkDetailsFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_food_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_drink_container, drinkDetailsFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
        setTitle(category);
    }

    @Override
    public void onDrinkSelected(String drink) {
        Intent mapIntent = new Intent(this, SpecificPlacesActivity.class);
        mapIntent.putExtra(SpecificPlacesActivity.PRODUCT_NAME_KEY,drink);
        startActivity(mapIntent);
    }
}

package co.edu.udea.compumovil.proyectogr8.foodea.Foods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import co.edu.udea.compumovil.proyectogr8.foodea.R;

public class FoodActivity extends AppCompatActivity implements FoodListFragment.OnFoodCategorySelected, FoodDetailsFragment.OnFoodSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        setTitle("Categorias - Comidas");

        //Set the initial fragment for the activity
        if(findViewById(R.id.fragment_food_container) != null){
            Log.d("TESTING","ON FOOD ACTIIVTY");
            Fragment foodListFragment = new FoodListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_food_container, foodListFragment);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setTitle("Categorias - Comidas");
    }

    @Override
    public void onFoodCategorySelected(String category) {

        // Create fragment and give it an argument for the selected food category
        FoodDetailsFragment foodDetailsFragment = new FoodDetailsFragment();
        Bundle args = new Bundle();
        args.putString(FoodDetailsFragment.ARG_CATEGORY,category);
        foodDetailsFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_food_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_food_container, foodDetailsFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        setTitle(category);

    }

    @Override
    public void onFoodSelected(int position) {

        Log.d("TAG","SIIIIIIIIIIIIIIIII");

    }
}

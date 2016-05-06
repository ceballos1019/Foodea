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
        setTitle("Comidas");

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
    public void onFoodCategorySelected(String category) {
        /*
        // The user selects a category of food the FoodListFragment

        // Capture the article fragment from the activity layout
        ArticleFragment articleFrag = (ArticleFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

        // Create fragment and give it an argument for the selected article*/
        Toast.makeText(getApplicationContext(),category,Toast.LENGTH_LONG).show();
        /*FoodDetailsFragment foodDetailsFragment = new FoodDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(FoodDetailsFragment.ARG_POSITION, position);
        foodDetailsFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();*/

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
       //transaction.replace(R.id.fragment_food_container, foodDetailsFragment);
        //transaction.addToBackStack(null);

        // Commit the transaction
        //transaction.commit();

        //}
    }

    @Override
    public void onFoodSelected(int position) {

        Log.d("TAG","SIIIIIIIIIIIIIIIII");

    }
}

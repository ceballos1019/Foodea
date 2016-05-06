package co.edu.udea.compumovil.proyectogr8.foodea.Drinks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import co.edu.udea.compumovil.proyectogr8.foodea.Foods.FoodDetailsFragment;
import co.edu.udea.compumovil.proyectogr8.foodea.R;

public class DrinkActivity extends AppCompatActivity implements DrinkListFragment.OnDrinkCategorySelectedListener, DrinkDetailsFragment.onDrinkSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        setTitle("Bebidas");

        if(findViewById(R.id.fragment_drink_container) != null){
            Log.d("TESTING","ON DRINK ACTIIVTY");
            Fragment drinkListFragment = new DrinkListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_drink_container,drinkListFragment);
            transaction.commit();

            //getSupportFragmentManager().beginTransaction()
            //        .add(R.id.fragment_container, headlinesFragment).commit();

        }
    }

    @Override
    public void onDrinkCategorySelected(int position) {
        /*
        // The user selected the headline of an article from the HeadlinesFragment

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
        FoodDetailsFragment newFragment = new FoodDetailsFragment();
        Bundle args = new Bundle();
        //args.putInt(FoodDetailsFragment.ARG_POSITION, position);
        //newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_drink_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        //}
    }

    @Override
    public void onDrinkSelected(int position) {

    }
}

package co.edu.udea.compumovil.proyectogr8.foodea.Places;

import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.lang.reflect.Method;

import co.edu.udea.compumovil.proyectogr8.foodea.Adapters.TabsPagerAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Product;
import co.edu.udea.compumovil.proyectogr8.foodea.R;

public class PlaceActivity extends ActionBarActivity implements ActionBar.TabListener, PlaceProductsFragment.OnProductSelectedListener, PlaceCategoriesFragment.OnProductCategorySelectedListener{

    public static final String PRODUCT_KEY = "Product_Key";
    public static final String PLACE_DETAILS_KEY = "Place_Details";
    public static final String PLACE_PRODUCTS_KEY = "Place_Products";
    public static final String PLACE_CATEGORIES_KEY = "Place_Categories";

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    Place place;

    private String[] tabs = { PlaceDetailsFragment.TAB_TITLE, PlaceProductsFragment.TAB_TITLE,PlaceCategoriesFragment.TAB_TITLE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            int placeId = extra.getInt(PRODUCT_KEY);
            DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
            dbAdapter.openConnection();
            place = dbAdapter.getProduct(placeId);
            dbAdapter.closeConnection();
        }
        setTitle(place.getName());
        mAdapter.setPlace(place);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onProductSelected(Product product) {
        Log.d("TESTING", product.getName());
    }

    @Override
    public void onProductCategorySelected(String categories) {
        Log.d("TESTING", categories);
    }

}

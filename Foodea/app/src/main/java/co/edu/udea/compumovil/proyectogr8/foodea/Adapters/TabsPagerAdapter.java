package co.edu.udea.compumovil.proyectogr8.foodea.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;
import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceCategoriesFragment;
import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceDetailsFragment;
import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceProductsFragment;

/**
 * Created by user on 10/06/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    private Place place;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("TESTING",String.valueOf(position));
        Fragment fragmentSelected;
        Bundle args;
        switch(position){
            case 0:
                fragmentSelected = new PlaceDetailsFragment();
                args = new Bundle();
                args.putString(PlaceActivity.PLACE_DETAILS_KEY,place.getDescription());
                fragmentSelected.setArguments(args);
                return fragmentSelected;
            case 1:
                fragmentSelected = new PlaceProductsFragment();
                args = new Bundle();
                args.putInt(PlaceActivity.PLACE_PRODUCTS_KEY,place.getId());
                fragmentSelected.setArguments(args);
                return fragmentSelected;
            case 2:
                fragmentSelected = new PlaceCategoriesFragment();
                args = new Bundle();
                args.putInt(PlaceActivity.PLACE_CATEGORIES_KEY,place.getId());
                fragmentSelected.setArguments(args);
                return fragmentSelected;
                    }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

package co.edu.udea.compumovil.proyectogr8.foodea;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.edu.udea.compumovil.proyectogr8.foodea.Foods.FoodListFragment;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;

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
        switch(position){
            case 0:
                Fragment fragmentSelected = new PlaceDetailsFragment();
                Bundle args = new Bundle();
                args.putString("DETAILS",place.getDescription());
                fragmentSelected.setArguments(args);
                return fragmentSelected;
            case 1:
                return new FoodListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

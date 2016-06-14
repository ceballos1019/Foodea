package co.edu.udea.compumovil.proyectogr8.foodea.Places;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import co.edu.udea.compumovil.proyectogr8.foodea.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceDetailsFragment extends Fragment {


    public static final String TAB_TITLE = "Detalles";

    public PlaceDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details, container, false);
        TextView test = (TextView) view.findViewById(R.id.place_details);

        Bundle args = getArguments();

        if(args!=null) {
            String msg = args.getString(PlaceActivity.PLACE_DETAILS_KEY);
            test.setText(msg);
        }

        double random = Math.random()*5;
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_place);
        ratingBar.setRating((float)random);

        // Inflate the layout for this fragment
        return view;
    }

}

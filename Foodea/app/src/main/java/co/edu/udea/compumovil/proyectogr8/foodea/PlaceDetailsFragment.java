package co.edu.udea.compumovil.proyectogr8.foodea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceDetailsFragment extends Fragment {


    public PlaceDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_details, container, false);


        Bundle args = getArguments();

        String msg = args.getString("DETAILS");

        TextView test = (TextView) view.findViewById(R.id.place_details);
        test.setText(msg);

        // Inflate the layout for this fragment
        return view;
    }

}

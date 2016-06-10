package co.edu.udea.compumovil.proyectogr8.foodea.Places;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;
import co.edu.udea.compumovil.proyectogr8.foodea.ProductActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.R;

public class SpecificPlacesActivity extends FragmentActivity implements OnMapReadyCallback,PlaceDialogFragment.NoticeDialogListener {

    private GoogleMap mMap;
    public static final String PRODUCT_NAME_KEY= "Product_Key";
    private String productName;
    private ArrayList<Place> places;
    private Place placeSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_places);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            productName = extras.getString(PRODUCT_NAME_KEY);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeTitle = marker.getTitle();
                showDetailDialog(placeTitle);
            }
        });
        initMap();
    }

    public void initMap(){
        //Create the database handler
        DBAdapter dbHandler = new DBAdapter(getApplicationContext());
        //Open the connection with the database
        dbHandler.openConnection();
        //get all foods categories
        places = dbHandler.getPlacesByProduct(productName);
        dbHandler.closeConnection();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        LatLng initialPosition = new LatLng(places.get(0).getLatitude(),places.get(0).getLongitude());
        LatLng position;
        for(Place place:places){
            System.out.println(place.getName());
            position = new LatLng(place.getLatitude(),place.getLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title(place.getName())).setDraggable(false);
        }
        CameraPosition cam = new CameraPosition.Builder().target(initialPosition).zoom(17).tilt(50).build();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,19));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam),2000,null);
    }

    public void showDetailDialog(String placeTitle) {

        //Search for the place selected
        for(Place place: places){
            if(place.getName().equals(placeTitle)){
                placeSelected = place;
                break;
            }
        }
        String placeDescription;
        try {
            placeDescription = placeSelected.getDescription();
        }catch(Exception e){placeDescription="";}

        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new PlaceDialogFragment(placeTitle,placeDescription);
        dialog.show(getFragmentManager(), "PlaceDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Log.d("TESTING","VIENDO DETALLES...");
        Intent producIntent = new Intent(this, ProductActivity.class);
        producIntent.putExtra("PRODUCTO",productName);
        producIntent.putExtra("LUGAR",placeSelected.getName());
        producIntent.putExtra("DETALLES",placeSelected.getDescription());

        DBAdapter dbHandler = new DBAdapter(getApplicationContext());
        //Open the connection with the database
        dbHandler.openConnection();
        //get all foods categories
        int price = dbHandler.getPrice(productName,placeSelected.getId());
        dbHandler.closeConnection();

        producIntent.putExtra("PRECIO",price);
        startActivity(producIntent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Log.d("TESTING","VOLVIENDO ATRAS...");
    }
}

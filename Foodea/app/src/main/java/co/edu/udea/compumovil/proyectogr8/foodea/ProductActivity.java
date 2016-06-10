package co.edu.udea.compumovil.proyectogr8.foodea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    private TextView tvPlaceName, tvPlaceDetails, tvProductName, tvProductPrice;
    private String placeName, placeDetails, productName;
    private RatingBar ratingBar;
    private int productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tvPlaceDetails= (TextView) findViewById(R.id.place_details);
        tvProductName = (TextView) findViewById(R.id.product_name);
        tvProductPrice = (TextView) findViewById(R.id.product_price);
        ratingBar = (RatingBar) findViewById(R.id.rating_product);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            placeName = extras.getString("LUGAR");
            placeDetails = extras.getString("DETALLES");
            productName = extras.getString("PRODUCTO");
            productPrice = extras.getInt("PRECIO");
        }
        setTitle(placeName);
        tvPlaceDetails.setText("Descripción:\n" + placeDetails);
        tvProductName.setText("Producto: "+ productName);
        tvProductPrice.setText("Precio: $"+ productPrice);

        double random = Math.random()*5;
        ratingBar.setRating((float)random);
    }
}

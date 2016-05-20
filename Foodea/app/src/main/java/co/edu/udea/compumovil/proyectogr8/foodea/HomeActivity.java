package co.edu.udea.compumovil.proyectogr8.foodea;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Drinks.DrinkActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.Foods.FoodActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.GCM.RegistrationService;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Product;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.User;
import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DBAdapter dbHandler;
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Inicio");
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Capture the views
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
        if(savedInstanceState!=null) {
            String message = savedInstanceState.getString("msg");
            Log.d("MENSAJE",message);
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }else {
            Log.d("MENSAJE","No llego el mensaje");
        }

        dbHandler = new DBAdapter(getApplicationContext());
        dbHandler.openConnection();
        //Check if the database has information or if it need to be initialized
        isEmpty=dbHandler.isEmpty();
        if(isEmpty){
            new InitializeDBTask().execute();
        }else{
            /*for(Product p:dbHandler.getAllProducts()){
                Log.d("TESTING",p.getName());
            }*/
            dbHandler.closeConnection();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Don't go back to the MainActivity
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_food:
                intent = new Intent(this, FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_drink:
                intent = new Intent(this, DrinkActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_places:
                intent = new Intent(this, PlaceActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_help:
                Toast.makeText(getApplicationContext(), "I need help!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_log:
                if (validateLogin()) {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    backToLogin();
                }
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Deslogearse de la aplicacion
    private void backToLogin() {
        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
        dbAdapter.deleteLogin();
        dbAdapter.closeConnection();
        finish();
    }


    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.ib_food:
                intent = new Intent(this, FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_drink:
                intent = new Intent(this, DrinkActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_place:
                intent = new Intent(this, PlaceActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_settings:
                Toast.makeText(getApplicationContext(), "Seleccionó configuración", Toast.LENGTH_SHORT).show();
                DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
                dbAdapter.openConnection();
                ArrayList<Place> listProducts = dbAdapter.getAllPlaces();
                boolean isEmpty = dbAdapter.isEmpty();
                dbAdapter.closeConnection();
                for(Place p:listProducts){
                    Log.d("Checking", String.valueOf(p.getId())+" - "+ p.getName());
                }
                Log.d("Checking", String.valueOf(isEmpty));
                break;
            default:
                break;

        }
    }

    private boolean validateLogin() {
        boolean wrongData=false;
        //Validar que el usuario se ha logeado
        DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.openConnection();
        User currentUser = dbAdapter.getCurrentUser();
        if(currentUser==null){
            wrongData=true;
        }
        return wrongData;
    }

    //Initialize the database in other thread
    private class InitializeDBTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            initializeProductsDB();
            initializePlacesDB();
            dbHandler.closeConnection();
            return null;
        }
    }

    //Method for initializing the database
    public void initializeProductsDB(){
        Log.d("onInitializing","INITIALIZING DATABSE...");

        dbHandler.insertProduct(new Product("Menu Completo","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Seco","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Torta de Carne","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Torta de Pollo","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Porción de Frijoles","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Porción de Arroz","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Sopa","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Porcion de Ensalada","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Parva","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Palito de Queso","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Empanada","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Buñuelo","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Panzerotti","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pastel de Pollo","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Arepa de Queso","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Brownie","Comida","Postre"));
        dbHandler.insertProduct(new Product("Rollo de Canela","Comida","Postre"));
        dbHandler.insertProduct(new Product("Alfajor de Chocolate","Comida","Postre"));
        dbHandler.insertProduct(new Product("Alfajor Confitado","Comida","Postre"));
        dbHandler.insertProduct(new Product("Trufa de Chocolate","Comida","Postre"));
        dbHandler.insertProduct(new Product("Porcion Pie de Manzana","Comida","Postre"));
        dbHandler.insertProduct(new Product("Barrita de Mora","Comida","Postre"));
        dbHandler.insertProduct(new Product("Cañoncito relleno de Arequipe","Comida","Postre"));
        dbHandler.insertProduct(new Product("Galletas Teja Parmesano","Comida","Postre"));
        dbHandler.insertProduct(new Product("Empanada Lolita","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Empanada Argentina","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Empanada Siciliana","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Persianita de Jamón y Queso","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pastel de queso","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Medialuna Porteña Jamon yQueso","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pan de Mandioca","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Porcion torta Maria Luisa","Comida","Postre"));
        dbHandler.insertProduct(new Product("Porcion torta de Chocolate","Comida","Postre"));
        dbHandler.insertProduct(new Product("Cheesecake","Comida","Postre"));
        dbHandler.insertProduct(new Product("Porcion de Cheesecake","Comida","Postre"));
        dbHandler.insertProduct(new Product("Alfajor Maizena","Comida","Postre"));
        dbHandler.insertProduct(new Product("Galletas Teja Pesto","Comida","Postre"));
        dbHandler.insertProduct(new Product("Almojabana","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pandequeso","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Croissant","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Ensalada Piña","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Ensalada Verde","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Ensalada Rusa","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Arepa de Huevo","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Sandwich Tradicional","Comida","Sandwiches"));
        dbHandler.insertProduct(new Product("Hamburguesa Vegetariana","Comida","Vegetariana"));
        dbHandler.insertProduct(new Product("Sandwich Integral","Comida","Sandwiches"));
        dbHandler.insertProduct(new Product("Hamburguesa Vegana","Comida","Vegetariana"));
        dbHandler.insertProduct(new Product("Sandwich Champiñones","Comida","Sandwiches"));
        dbHandler.insertProduct(new Product("Ensalada Aguacate","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Ensalada Remolacha","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Arroz con Pollo","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Arroz con Verduras","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Arroz Paisa","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Pollo con Champiñones","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Alfajor","Comida","Postre"));
        dbHandler.insertProduct(new Product("Porcion Cerdo Agridulce","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Fajitas","Comida","Comida Rapida"));
        dbHandler.insertProduct(new Product("Cazuela","Comida","Almuerzo"));
        dbHandler.insertProduct(new Product("Arepa de Chocolo","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pastel de Guayaba","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pastel de Arequipe","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Croissant Chocolate","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pan leche","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pan integral con pasas","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pastel Hojaldrado de Queso","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pastel Hojaldrado de Arequipe","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Pastel Hojaldrado de Guayaba","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Torta Casera","Comida","Postre"));
        dbHandler.insertProduct(new Product("Torta Golosa","Comida","Postre"));
        dbHandler.insertProduct(new Product("Empanada de Pollo y Verduras - Pequeña","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Empanada de Pollo y Verduras - Grande","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Papa Rellena","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Arepa Tela","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Arepa con Quesito","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Arepa con Huevo Revuelto","Comida","Panaderia"));
        dbHandler.insertProduct(new Product("Lasaña de Carne de Res","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Lasaña de Pollo","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Spaghetti Bolognese","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Fettucine Torino","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Penne Carbonara","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Pizza Margherita","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Pizza Pollo y Champiñones","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Pizza Salami","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Pizza Hawaiana","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Pizza Jamon","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Pizza Vegetales","Comida","Vegetariana"));
        dbHandler.insertProduct(new Product("Pizza Jamon y Champiñones","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Pizza Yommi","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Ensalada Cesar","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Ensalada Torino","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Ensalada Caprese","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Ensalada Lechuga, Tomate, Queso Mozzarella, Aceituna y Salami","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Ensalada Lechuga,Tomate,Queso Mozzarella y Atun","Comida","Ensaladas"));
        dbHandler.insertProduct(new Product("Sandwich Pollo","Comida","Sandwiches"));
        dbHandler.insertProduct(new Product("Sandwich Jamon","Comida","Sandwiches"));
        dbHandler.insertProduct(new Product("Sandwich Vegetariano","Comida","Vegetariana"));
        dbHandler.insertProduct(new Product("Panna Cotta","Comida","Italiana"));
        dbHandler.insertProduct(new Product("Tiramisu","Comida","Postre"));
        dbHandler.insertProduct(new Product("Jugo Natural","Bebida","Bebidas Frias"));
        dbHandler.insertProduct(new Product("Limonada","Bebida","Bebidas Frias"));
        dbHandler.insertProduct(new Product("Té","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Granizado","Bebida","Bebidas Frias"));
        dbHandler.insertProduct(new Product("Tinto","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Café con Leche","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Chocolate","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Capucchino","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Aromatica","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Gaseosa (600ml)","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Gaseosa (25l)","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Hit","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("MrTea","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Botella de Agua","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Gatorade","Bebida","Energizantes"));
        dbHandler.insertProduct(new Product("Pony Malta","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Pony Malta (Mini)","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Vive100","Bebida","Energizantes"));
        dbHandler.insertProduct(new Product("SpeedMax","Bebida","Energizantes"));
        dbHandler.insertProduct(new Product("Gaseosa (9 Onzas)","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Peak","Bebida","Energizantes"));
        dbHandler.insertProduct(new Product("Café de Lolita","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Café Americano Gourmet","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Expresso","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Café Latte","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Capucchino Deslactosado","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Mocaccino","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Chocolate deslactosado","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Machiato","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Milo","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Te Inti Zen","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Te Chai","Bebida","Bebidas Calientes"));
        dbHandler.insertProduct(new Product("Aromatica de Frutas","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Zumo de Uva","Bebida","Bebidas Frias"));
        dbHandler.insertProduct(new Product("Mandarina de Lolita","Bebida","Bebidas Frias"));
        dbHandler.insertProduct(new Product("Ice Tea","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Gaseosa (12 Onzas)","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Chocolate en agua","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Leche","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Agua en vaso","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Bolsa de Agua","Bebida","Otras bebidas"));
        dbHandler.insertProduct(new Product("Gaseosa dispensador","Bebida","Refrescos"));
        dbHandler.insertProduct(new Product("Perico","Bebida","Bebidas Calientes"));
        Log.d("onInitializing","FINISHED WORK ON DATABASE");
    }

    public void initializePlacesDB(){
        dbHandler.insertPlace(new Place("Restaurante Mall de Ingenieria",6.268412,-75.568378,"Cerca al bloque 21"));
        dbHandler.insertPlace(new Place("Reposteria Lolita",6.268306,-75.568385,"Cerca al bloque 21"));
        dbHandler.insertPlace(new Place("Torino - Pasta & Pizza",6.268262,-75.570076,"Cerca al bloque de artes"));
        dbHandler.insertPlace(new Place("PuntoGourmet 1",6.268563,-75.568368,"Cerca al bloque 21"));
        dbHandler.insertPlace(new Place("PuntoGourmet 2",6.268710,-75.568365,"Cerca al bloque 21"));
        dbHandler.insertPlace(new Place("Restaurante-Cafeteria Deportes",6.268476,-75.568749,"Debajo de la libreria"));
        dbHandler.insertPlace(new Place("Restaurante Cafeteria Facultad de Artes",6.267921,-75.569416,"Facultad de Artes"));
        dbHandler.insertPlace(new Place("Sazón y Parrilla",6.268186,-75.568391,"Debajo de la libreria"));
        dbHandler.insertPlace(new Place("Burbuja",6.268185,-75.567491,""));

    }
}

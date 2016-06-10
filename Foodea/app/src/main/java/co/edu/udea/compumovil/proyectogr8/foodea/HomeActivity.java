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
import co.edu.udea.compumovil.proyectogr8.foodea.Places.AllPlacesActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.Users.MainActivity;

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

        /*Intent i = new Intent(this, RegistrationService.class);
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
*/
        dbHandler = new DBAdapter(getApplicationContext());
        dbHandler.openConnection();
        //Check if the database has information or if it need to be initialized
        isEmpty=dbHandler.isEmpty();
        if(isEmpty){
            new InitializeDBTask().execute();
        }else{
            /*for(Product p:dbHandler.getAllProducts()){
                Log.d("TESTING PRODUCTS",p.getName());
            }
            for(Place pl : dbHandler.getAllPlaces()){
                Log.d("TESTING PLACES", pl.getName());
            }
            */
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
                intent = new Intent(this, AllPlacesActivity.class);
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
                intent = new Intent(this, AllPlacesActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_settings:
                intent = new Intent(this,PlaceActivity.class);
                startActivity(intent);
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
            initializeProductxPlacesDB();
            dbHandler.closeConnection();
            return null;
        }
    }

    //Methods for initializing the database
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

    public void initializeProductxPlacesDB(){
        dbHandler.insertProductxPlace(1,1,6500);
        dbHandler.insertProductxPlace(1,2,5800);
        dbHandler.insertProductxPlace(1,3,2600);
        dbHandler.insertProductxPlace(1,4,3100);
        dbHandler.insertProductxPlace(1,5,1400);
        dbHandler.insertProductxPlace(1,6,800);
        dbHandler.insertProductxPlace(1,7,1000);
        dbHandler.insertProductxPlace(1,8,1000);
        dbHandler.insertProductxPlace(1,9,1500);
        dbHandler.insertProductxPlace(1,10,2100);
        dbHandler.insertProductxPlace(1,11,1200);
        dbHandler.insertProductxPlace(1,12,1300);
        dbHandler.insertProductxPlace(1,13,2100);
        dbHandler.insertProductxPlace(1,14,2000);
        dbHandler.insertProductxPlace(1,15,1900);
        dbHandler.insertProductxPlace(1,104,600);
        dbHandler.insertProductxPlace(1,119,1300);
        dbHandler.insertProductxPlace(1,142,1000);
        dbHandler.insertProductxPlace(1,108,500);
        dbHandler.insertProductxPlace(1,105,1200);
        dbHandler.insertProductxPlace(1,110,4500);
        dbHandler.insertProductxPlace(1,109,2000);
        dbHandler.insertProductxPlace(1,111,1600);
        dbHandler.insertProductxPlace(1,112,1600);
        dbHandler.insertProductxPlace(1,116,1000);
        dbHandler.insertProductxPlace(1,113,1600);
        dbHandler.insertProductxPlace(1,120,1500);
        dbHandler.insertProductxPlace(2,16,2300);
        dbHandler.insertProductxPlace(2,17,3300);
        dbHandler.insertProductxPlace(2,18,2700);
        dbHandler.insertProductxPlace(2,19,2600);
        dbHandler.insertProductxPlace(2,20,3100);
        dbHandler.insertProductxPlace(2,21,3500);
        dbHandler.insertProductxPlace(2,22,1900);
        dbHandler.insertProductxPlace(2,23,2900);
        dbHandler.insertProductxPlace(2,24,5600);
        dbHandler.insertProductxPlace(2,25,3000);
        dbHandler.insertProductxPlace(2,26,3800);
        dbHandler.insertProductxPlace(2,27,3400);
        dbHandler.insertProductxPlace(2,28,3600);
        dbHandler.insertProductxPlace(2,14,3500);
        dbHandler.insertProductxPlace(2,29,3000);
        dbHandler.insertProductxPlace(2,10,2300);
        dbHandler.insertProductxPlace(2,30,4500);
        dbHandler.insertProductxPlace(2,31,2800);
        dbHandler.insertProductxPlace(2,32,3700);
        dbHandler.insertProductxPlace(2,33,4300);
        dbHandler.insertProductxPlace(2,34,39000);
        dbHandler.insertProductxPlace(2,35,6500);
        dbHandler.insertProductxPlace(2,36,2500);
        dbHandler.insertProductxPlace(2,37,5600);
        dbHandler.insertProductxPlace(2,104,600);
        dbHandler.insertProductxPlace(2,121,1000);
        dbHandler.insertProductxPlace(2,122,1500);
        dbHandler.insertProductxPlace(2,123,1300);
        dbHandler.insertProductxPlace(2,105,1200);
        dbHandler.insertProductxPlace(2,124,1600);
        dbHandler.insertProductxPlace(2,125,2300);
        dbHandler.insertProductxPlace(2,126,2800);
        dbHandler.insertProductxPlace(2,127,1900);
        dbHandler.insertProductxPlace(2,106,1800);
        dbHandler.insertProductxPlace(2,107,2200);
        dbHandler.insertProductxPlace(2,142,1200);
        dbHandler.insertProductxPlace(2,128,2900);
        dbHandler.insertProductxPlace(2,129,3300);
        dbHandler.insertProductxPlace(2,130,4200);
        dbHandler.insertProductxPlace(2,131,2500);
        dbHandler.insertProductxPlace(2,132,800);
        dbHandler.insertProductxPlace(2,133,4600);
        dbHandler.insertProductxPlace(2,134,3100);
        dbHandler.insertProductxPlace(2,103,3100);
        dbHandler.insertProductxPlace(2,135,2300);
        dbHandler.insertProductxPlace(2,119,1300);
        dbHandler.insertProductxPlace(2,136,1500);
        dbHandler.insertProductxPlace(2,113,2400);
        dbHandler.insertProductxPlace(3,77,6500);
        dbHandler.insertProductxPlace(3,78,6500);
        dbHandler.insertProductxPlace(3,79,5500);
        dbHandler.insertProductxPlace(3,80,6000);
        dbHandler.insertProductxPlace(3,81,6500);
        dbHandler.insertProductxPlace(3,82,2600);
        dbHandler.insertProductxPlace(3,83,3600);
        dbHandler.insertProductxPlace(3,84,2900);
        dbHandler.insertProductxPlace(3,85,3000);
        dbHandler.insertProductxPlace(3,86,2600);
        dbHandler.insertProductxPlace(3,87,2800);
        dbHandler.insertProductxPlace(3,88,2900);
        dbHandler.insertProductxPlace(3,89,3700);
        dbHandler.insertProductxPlace(3,90,5400);
        dbHandler.insertProductxPlace(3,91,3900);
        dbHandler.insertProductxPlace(3,92,3500);
        dbHandler.insertProductxPlace(3,93,5000);
        dbHandler.insertProductxPlace(3,94,5400);
        dbHandler.insertProductxPlace(3,95,4900);
        dbHandler.insertProductxPlace(3,96,3900);
        dbHandler.insertProductxPlace(3,97,3500);
        dbHandler.insertProductxPlace(3,98,2200);
        dbHandler.insertProductxPlace(3,99,2700);
        dbHandler.insertProductxPlace(3,34,2700);
        dbHandler.insertProductxPlace(3,100,1700);
        dbHandler.insertProductxPlace(3,129,2500);
        dbHandler.insertProductxPlace(3,146,2200);
        dbHandler.insertProductxPlace(4,11,1200);
        dbHandler.insertProductxPlace(4,14,1700);
        dbHandler.insertProductxPlace(4,3,2100);
        dbHandler.insertProductxPlace(4,44,1700);
        dbHandler.insertProductxPlace(4,1,7500);
        dbHandler.insertProductxPlace(4,45,2800);
        dbHandler.insertProductxPlace(4,46,5600);
        dbHandler.insertProductxPlace(4,47,3500);
        dbHandler.insertProductxPlace(4,48,4700);
        dbHandler.insertProductxPlace(4,49,3500);
        dbHandler.insertProductxPlace(4,100,1200);
        dbHandler.insertProductxPlace(4,101,700);
        dbHandler.insertProductxPlace(4,102,1000);
        dbHandler.insertProductxPlace(4,103,2300);
        dbHandler.insertProductxPlace(5,15,2400);
        dbHandler.insertProductxPlace(5,60,2400);
        dbHandler.insertProductxPlace(5,1,7200);
        dbHandler.insertProductxPlace(5,100,1200);
        dbHandler.insertProductxPlace(5,101,700);
        dbHandler.insertProductxPlace(5,102,1000);
        dbHandler.insertProductxPlace(5,105,1000);
        dbHandler.insertProductxPlace(5,106,1000);
        dbHandler.insertProductxPlace(5,132,1000);
        dbHandler.insertProductxPlace(5,104,500);
        dbHandler.insertProductxPlace(5,107,1500);
        dbHandler.insertProductxPlace(6,1,5400);
        dbHandler.insertProductxPlace(6,2,4800);
        dbHandler.insertProductxPlace(6,64,600);
        dbHandler.insertProductxPlace(6,65,1100);
        dbHandler.insertProductxPlace(6,39,1300);
        dbHandler.insertProductxPlace(6,38,1400);
        dbHandler.insertProductxPlace(6,29,1000);
        dbHandler.insertProductxPlace(6,10,1500);
        dbHandler.insertProductxPlace(6,13,1700);
        dbHandler.insertProductxPlace(6,66,1400);
        dbHandler.insertProductxPlace(6,67,1400);
        dbHandler.insertProductxPlace(6,14,1400);
        dbHandler.insertProductxPlace(6,145,1400);
        dbHandler.insertProductxPlace(6,40,1400);
        dbHandler.insertProductxPlace(6,69,1350);
        dbHandler.insertProductxPlace(6,70,1200);
        dbHandler.insertProductxPlace(6,11,1100);
        dbHandler.insertProductxPlace(6,71,1000);
        dbHandler.insertProductxPlace(6,72,1700);
        dbHandler.insertProductxPlace(6,73,1200);
        dbHandler.insertProductxPlace(6,74,400);
        dbHandler.insertProductxPlace(6,75,1100);
        dbHandler.insertProductxPlace(6,76,1500);
        dbHandler.insertProductxPlace(6,104,500);
        dbHandler.insertProductxPlace(6,123,600);
        dbHandler.insertProductxPlace(6,107,1500);
        dbHandler.insertProductxPlace(6,142,800);
        dbHandler.insertProductxPlace(6,105,1100);
        dbHandler.insertProductxPlace(6,108,400);
        dbHandler.insertProductxPlace(6,102,700);
        dbHandler.insertProductxPlace(6,137,1000);
        dbHandler.insertProductxPlace(6,106,1200);
        dbHandler.insertProductxPlace(6,138,1100);
        dbHandler.insertProductxPlace(6,113,1600);
        dbHandler.insertProductxPlace(6,139,1000);
        dbHandler.insertProductxPlace(6,140,500);
        dbHandler.insertProductxPlace(6,141,1000);
        dbHandler.insertProductxPlace(6,114,2300);
        dbHandler.insertProductxPlace(6,14,1600);
        dbHandler.insertProductxPlace(7,1,5400);
        dbHandler.insertProductxPlace(7,2,4800);
        dbHandler.insertProductxPlace(7,5,1150);
        dbHandler.insertProductxPlace(7,6,600);
        dbHandler.insertProductxPlace(7,8,700);
        dbHandler.insertProductxPlace(7,9,1400);
        dbHandler.insertProductxPlace(7,10,1400);
        dbHandler.insertProductxPlace(7,11,1100);
        dbHandler.insertProductxPlace(7,12,1000);
        dbHandler.insertProductxPlace(7,104,500);
        dbHandler.insertProductxPlace(7,119,1000);
        dbHandler.insertProductxPlace(7,142,800);
        dbHandler.insertProductxPlace(7,108,400);
        dbHandler.insertProductxPlace(7,105,1000);
        dbHandler.insertProductxPlace(8,1,6500);
        dbHandler.insertProductxPlace(8,10,1700);
        dbHandler.insertProductxPlace(8,38,1500);
        dbHandler.insertProductxPlace(8,39,1500);
        dbHandler.insertProductxPlace(8,40,1900);
        dbHandler.insertProductxPlace(8,13,1900);
        dbHandler.insertProductxPlace(8,41,2300);
        dbHandler.insertProductxPlace(8,42,1600);
        dbHandler.insertProductxPlace(8,43,2400);
        dbHandler.insertProductxPlace(8,50,2300);
        dbHandler.insertProductxPlace(8,51,1800);
        dbHandler.insertProductxPlace(8,52,3800);
        dbHandler.insertProductxPlace(8,53,3200);
        dbHandler.insertProductxPlace(8,54,5000);
        dbHandler.insertProductxPlace(8,55,3400);
        dbHandler.insertProductxPlace(8,56,2000);
        dbHandler.insertProductxPlace(8,57,3300);
        dbHandler.insertProductxPlace(8,58,3300);
        dbHandler.insertProductxPlace(8,59,7600);
        dbHandler.insertProductxPlace(8,110,4500);
        dbHandler.insertProductxPlace(8,109,2400);
        dbHandler.insertProductxPlace(8,111,2000);
        dbHandler.insertProductxPlace(8,112,1400);
        dbHandler.insertProductxPlace(8,113,1500);
        dbHandler.insertProductxPlace(8,114,2600);
        dbHandler.insertProductxPlace(8,115,1700);
        dbHandler.insertProductxPlace(8,117,2000);
        dbHandler.insertProductxPlace(8,118,1500);
        dbHandler.insertProductxPlace(8,116,1000);
        dbHandler.insertProductxPlace(9,104,600);
        dbHandler.insertProductxPlace(9,105,1100);
        dbHandler.insertProductxPlace(9,106,900);
        dbHandler.insertProductxPlace(9,107,1600);
        dbHandler.insertProductxPlace(9,126,1400);
        dbHandler.insertProductxPlace(9,108,700);
    }
}

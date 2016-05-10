package co.edu.udea.compumovil.proyectogr8.foodea;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Drinks.DrinkActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.Foods.FoodActivity;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.User;
import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
}

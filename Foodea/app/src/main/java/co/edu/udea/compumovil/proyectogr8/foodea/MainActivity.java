package co.edu.udea.compumovil.proyectogr8.foodea;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Product;

public class MainActivity extends AppCompatActivity {

    private DBAdapter dbHandler;
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the database handler
        dbHandler = new DBAdapter(this);

        //Open the connection
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



        //Launch the HomeActivity
        //Intent home = new Intent(MainActivity.this, HomeActivity.class);
        //startActivity(home);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_login:
                Intent home = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.btn_register:
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
                break;
        }
    }

    //Initialize the database in other thread
    private class InitializeDBTask extends AsyncTask<Void,Void,Void>{

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

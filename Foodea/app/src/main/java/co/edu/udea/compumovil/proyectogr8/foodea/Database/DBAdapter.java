package co.edu.udea.compumovil.proyectogr8.foodea.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Model.Product;


/**
 * Created by andres.ceballoss on 4/05/16.
 */
public class DBAdapter {
    private static final String TAG = DBHandler.class.getSimpleName();

    //Información de la base de datos
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Foodea.db";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";

    //Definicion de la tabla producto
    public static final String PRODUCT_TABLE = "Product";


    public class Producto implements BaseColumns {
        public static final String PRODUCT_ID = BaseColumns._ID;
        public static final String PRODUCT_NAME = "Name";
        public static final String PRODUCT_TYPE = "Type";
        public static final String PRODUCT_CATEGORY = "Category";
    }

    //Definicion de la tabla place
    public static final String PLACE_TABLE = "Place";
    public class Place implements BaseColumns{
        public static final String PLACE_ID =BaseColumns._ID;
        public static final String PLACE_NAME= "Name";
        public static final String PLACE_LATITUDE = "Latitude";
        public static final String PLACE_LONGITUDE = "Longitude";
        public static final String PLACE_DESCRIPTION = "Description";
    }

    //Definicion de la tabla productxplace
    public static final String PRODUCTXPLACE_TABLE = "ProductXPlace";
    public class ProductXPlace implements  BaseColumns{
        public static final String PLACE_ID ="idPlace";
        public static final String PRODUCT_ID= "idProduct";
        public static final String PRICE = "Price";
        public static final String RATING = "Rating";
    }

    //Sentencias para la creación de las tablas

    private static final String PRODUCT_TABLE_CREATE = String
            .format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL)",
                    PRODUCT_TABLE,
                    Producto.PRODUCT_ID,
                    Producto.PRODUCT_NAME,
                    Producto.PRODUCT_TYPE,
                    Producto.PRODUCT_CATEGORY);

    private static final String PLACE_TABLE_CREATE = String
            .format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s REAL NOT NULL, " +
                    "%s REAL NOT NULL," +
                    "%s TEXT)",
                    PLACE_TABLE,
                    Place.PLACE_ID,
                    Place.PLACE_NAME,
                    Place.PLACE_LATITUDE,
                    Place.PLACE_LONGITUDE,
                    Place.PLACE_DESCRIPTION);


    private static final String PRODUCTXPLACE_TABLE_CREATE = String
            .format("CREATE TABLE %s (" +
                    "%s INTEGER NOT NULL," +
                    "%s INTEGER NOT NULL," +
                    "%s REAL NOT NULL," +
                    "%s INTEGER," +
                    "FOREIGN KEY (%s) REFERENCES %s(%s)," +
                    "FOREIGN KEY (%s) REFERENCES %s(%s)," +
                    "PRIMARY KEY (%s,%s))",
                    PRODUCTXPLACE_TABLE,
                    ProductXPlace.PLACE_ID,
                    ProductXPlace.PRODUCT_ID,
                    ProductXPlace.PRICE,
                    ProductXPlace.RATING,
                    ProductXPlace.PLACE_ID,
                    PLACE_TABLE,
                    Place.PLACE_ID,
                    ProductXPlace.PRODUCT_ID,
                    PRODUCT_TABLE,
                    Producto.PRODUCT_ID,
                    ProductXPlace.PLACE_ID,
                    ProductXPlace.PRODUCT_ID
                    );

    private Context nContext;
    private SQLiteDatabase db;
    private DBHandler dbHandler; //Gestor de base de datos

    //Constructor de la Clase
    //Recibe como parámetro una variable de Tipo contexto
    // Esto debido a que Para acceder o crear la BD lo pide la Clase SQLiteOpenHelper
    public DBAdapter(Context context){
        this.nContext = context;
        this.dbHandler = new DBHandler(this.nContext);
    }


    //Abrir la conexion a la base de datos
    public void openConnection() {
        this.db=dbHandler.getWritableDatabase();
    }

    //Cerrar la conexión a la base de datos
    public void closeConnection(){
        this.db.close();
    }

    private static class DBHandler extends SQLiteOpenHelper {

        DBHandler(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PLACE_TABLE_CREATE);
            db.execSQL(PRODUCT_TABLE_CREATE);
            db.execSQL(PRODUCTXPLACE_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+PRODUCT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+PLACE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+PRODUCTXPLACE_TABLE);
            onCreate(db);
        }
    }

    //Insert product
    public void insertProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(Producto.PRODUCT_NAME,product.getName());
        values.put(Producto.PRODUCT_TYPE, product.getType());
        values.put(Producto.PRODUCT_CATEGORY, product.getCategory());
        db.insert(PRODUCT_TABLE,null,values);
    }

    public boolean isEmpty(){
        long numRows = DatabaseUtils.queryNumEntries(db,PRODUCT_TABLE);
        if(numRows==0){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<String> getAllFoodCategories(Context context){
        //Validar que los datos de login ingresados sean correctos
        String columns [] ={Producto.PRODUCT_ID,Producto.PRODUCT_CATEGORY};
        String selection = Producto.PRODUCT_TYPE+"=?";
        String selectionArgs[] = {"Comida"};

        Cursor cursor = db.query(true,PRODUCT_TABLE,columns,selection,selectionArgs,Producto.PRODUCT_CATEGORY,null,null,null);
        ArrayList<String> categories = new ArrayList<>();
        //Si el cursor esta vacio es porque no hay eventos

        if(cursor.moveToFirst()){
            do{
                categories.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {//Se cierra el cursor si no está cerrado ya
            cursor.close();
        }
        return categories;
    }

    //Traer todos los eventos de la base de datos
    public Cursor getAllProducts(Context context){
        //Validar que los datos de login ingresados sean correctos
        String columns [] =    {Producto.PRODUCT_NAME,
                Producto.PRODUCT_TYPE,
                Producto.PRODUCT_CATEGORY};

        //Cursor c1 = db.query(TABLA_USUARIOS,columns,selection,selectionArgs,null,null,null);
        Cursor c1 = db.query(PRODUCT_TABLE,columns,null,null,null,null,null);
        return c1;
        /*ArrayList<Product> listEvents = new ArrayList<>();


        //Si el cursor esta vacio es porque no hay eventos

        if(c1.moveToFirst()){
            do{
                Product currentEvent = new Product();
                currentEvent.setName(c1.getString(0));
                currentEvent.setType(c1.getString(1));
                currentEvent.setCategory(c1.getString(2));
                listEvents.add(currentEvent);
            }while (c1.moveToNext());
        }
        if (!c1.isClosed()) {//Se cierra el cursor si no está cerrado ya
            c1.close();
        }
        return listEvents;*/
    }


}

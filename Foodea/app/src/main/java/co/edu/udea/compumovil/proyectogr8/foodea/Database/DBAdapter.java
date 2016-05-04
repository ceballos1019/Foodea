package co.edu.udea.compumovil.proyectogr8.foodea.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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


    public class Product implements BaseColumns {
        public static final String PRODUCT_ID = "_id";
        public static final String PRODUCT_NAME = "Name";
        public static final String PRODUCT_TYPE = "Type";
        public static final String PRODUCT_CATEGORY = "Category";
    }

    //Definicion de la tabla place
    public static final String PLACE_TABLE = "Place";
    public class Place implements BaseColumns{
        public static final String PLACE_ID ="_id";
        public static final String PLACE_NAME= "Name";
        public static final String PLACE_LATITUDE = "Latitude";
        public static final String PLACE_LONGITUDE = "Longitude";
        public static final String PLACE_DESCRIPTION = "Description";
    }

    //Definicion de la tabla productxplace
    public static final String PRODUCTXPLACE_TABLE = "ProductXPlace";
    public class PoductXPlace implements  BaseColumns{
        public static final String PLACE_ID ="idPlace";
        public static final String PRODUCT_ID= "idProduct";
        public static final String PRICE = "Price";
        public static final String RATING = "Rating";
    }

    //Sentencias para la creación de las tablas

    private static final String PRODUCT_TABLE_CREATE = String
            .format("create table %s (%s integer primary key autoincrement, %s text not null, %s text not null, %s text not null)",
                    PRODUCT_TABLE,
                    Product.PRODUCT_ID,
                    Product.PRODUCT_NAME,
                    Product.PRODUCT_TYPE,
                    Product.PRODUCT_CATEGORY);

    private static final String PLACE_TABLE_CREATE = String
            .format("create table %s (%s integer primary key autoincrement, %s text not null, %s real not null, %s real not null, %s text)",
                    PLACE_TABLE,
                    Place.PLACE_ID,
                    Place.PLACE_NAME,
                    Place.PLACE_LATITUDE,
                    Place.PLACE_LONGITUDE,
                    Place.PLACE_DESCRIPTION);


    private static final String PRODUCTXPLACE_TABLE_CREATE = String
            .format("create table %s (%s integer, %s integer, %s real, %s integer)",
                    PRODUCTXPLACE_TABLE,
                    Login.LOGIN_CURRENT_USER_NAME);

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
    public void open() {
        this.db=dbHandler.getWritableDatabase();

    }

    //Cerrar la conexión a la base de datos
    public void close(){
        this.db.close();
    }

    private static class DBHandler extends SQLiteOpenHelper {

        DBHandler(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(USER_TABLE_CREATE);
            db.execSQL(EVENT_TABLE_CREATE);
            db.execSQL(LOGIN_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLA_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS "+TABLA_EVENTOS);
            onCreate(db);
        }
    }
}

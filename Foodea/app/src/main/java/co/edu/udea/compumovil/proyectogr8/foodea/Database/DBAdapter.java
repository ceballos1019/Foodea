package co.edu.udea.compumovil.proyectogr8.foodea.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;

import co.edu.udea.compumovil.proyectogr8.foodea.Model.Place;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Product;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.User;


/**
 * Created by andres.ceballoss on 4/05/16.
 */
public class DBAdapter {
    private static final String TAG = DBHandler.class.getSimpleName();

    //Información de la base de datos
    public static final int DATABASE_VERSION = 12;
    public static final String DATABASE_NAME = "Foodea.db";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";

    //Definicion de la tabla usuario
    public static final String USER_TABLE = "User";


    public class Usuario implements BaseColumns{
        public static final  String USER_ID=BaseColumns._ID;
        public static final  String USER_USER="User";
        public static final  String USER_PASSWORD="Password";
        public static final  String USER_EMAIL="Email";
    }


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
    public class Site implements BaseColumns{
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
    //Definicion de la tabla Login
    public static final String LOGIN_TABLE = "Login";
    public class Login implements  BaseColumns{
        public static final String LOGIN_CURRENT_USER_NAME= "User";
    }

//Sentencias para la creación de las tablas


    private static final String USER_TABLE_CREATE = String
            .format("CREATE TABLE %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL)",
                    USER_TABLE,
                    Usuario.USER_ID,
                    Usuario.USER_USER,
                    Usuario.USER_PASSWORD,
                    Usuario.USER_EMAIL);


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
                    Site.PLACE_ID,
                    Site.PLACE_NAME,
                    Site.PLACE_LATITUDE,
                    Site.PLACE_LONGITUDE,
                    Site.PLACE_DESCRIPTION);


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
                    Site.PLACE_ID,
                    ProductXPlace.PRODUCT_ID,
                    PRODUCT_TABLE,
                    Producto.PRODUCT_ID,
                    ProductXPlace.PLACE_ID,
                    ProductXPlace.PRODUCT_ID
                    );

    private static final String LOGIN_TABLE_CREATE = String
            .format("create table %s (%s text primary key)",
                    LOGIN_TABLE,
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
            db.execSQL(USER_TABLE_CREATE);
            db.execSQL(PLACE_TABLE_CREATE);
            db.execSQL(PRODUCT_TABLE_CREATE);
            db.execSQL(PRODUCTXPLACE_TABLE_CREATE);
            db.execSQL(LOGIN_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+LOGIN_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+PRODUCT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+PLACE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+PRODUCTXPLACE_TABLE);
            onCreate(db);
        }
    }
    //Insertar usuario en la base de datos
    public void insertUser(String name, String password, String email){
        ContentValues values= new ContentValues();
        values.put(Usuario.USER_USER,name);
        values.put(Usuario.USER_PASSWORD,password);
        values.put(Usuario.USER_EMAIL,email);
        db.insert(USER_TABLE, null, values);
    }
    //Obtener un usuario de la base de datos
    public boolean getUser(Context context,String username,String pass){
        //Validar que los datos de login ingresados sean correctos
        String columns [] = {Usuario.USER_USER,Usuario.USER_PASSWORD};
        String selection = "User=? and Password=?";
        String selectionArgs[] = {username,pass};

        Cursor c1 = db.query(USER_TABLE,columns,selection,selectionArgs,null,null,null);
        //Cursor c1 = db.query(TABLA_USUARIOS,columns,null,null,null,null,null);

        //Si el cursor esta vacio es porque el usuario o la contraseña es incorrecta
        if(!c1.moveToFirst()){
            Toast.makeText(context, "Usuario o contraseña incorrectas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //Inserta el usuario actualmente logeado en la tabla Login
    public void setCurrentLogin(String currentUser){
        ContentValues values= new ContentValues();
        values.put(Login.LOGIN_CURRENT_USER_NAME, currentUser);
        db.insert(LOGIN_TABLE, null, values);

    }
    //Retorna el usuario logeado actualmente
    public User getCurrentUser(){
        String columns [] = {Login.LOGIN_CURRENT_USER_NAME};
        Cursor c1 = db.query(LOGIN_TABLE,columns,null,null,null,null,null);
        //Si el cursor esta vacio es porque no hay usuarios logeados
        User currentUser = null;
        if(!c1.moveToFirst()){
            return null;
        }
        String nameCurrentUser = c1.getString(0);
        c1.close();
        String columnsQuery[] = {Usuario.USER_USER, Usuario.USER_EMAIL};
        String selection = "User=?";
        String selectionArgs[] = {nameCurrentUser};
        Cursor c2 = db.query(USER_TABLE,columnsQuery,selection,selectionArgs,null,null,null);
        if(c2.moveToFirst()){
            currentUser = new User();
            currentUser.setName(c2.getString(0));
            currentUser.setEmail(c2.getString(1));
        }
        c2.close();
        return currentUser;
    }

    //Elimina el registro de la tabla login (Implementado para deslogearse)
    public void deleteLogin() {
        db.delete(LOGIN_TABLE,null,null);
    }




    //Insert product
    public void insertProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(Producto.PRODUCT_NAME,product.getName());
        values.put(Producto.PRODUCT_TYPE, product.getType());
        values.put(Producto.PRODUCT_CATEGORY, product.getCategory());
        db.insert(PRODUCT_TABLE,null,values);
    }

    //Check if product table is empty
    public boolean isEmpty(){
        long numRows = DatabaseUtils.queryNumEntries(db,PRODUCT_TABLE);
        if(numRows==0){
            return true;
        }else{
            return false;
        }
    }

    //Get all distinct food categories
    public ArrayList<String> getAllFoodCategories(){

        //Setting up the parameters for the query
        String columns [] ={Producto.PRODUCT_ID,Producto.PRODUCT_CATEGORY};
        String selection = Producto.PRODUCT_TYPE+"=?";
        String selectionArgs[] = {"Comida"};

        Cursor cursor = db.query(true,PRODUCT_TABLE,columns,selection,selectionArgs,Producto.PRODUCT_CATEGORY,null,null,null);
        ArrayList<String> categories = new ArrayList<>();

        //If the cursor is empty, there are not categories
        if(cursor.moveToFirst()){
            do{
                categories.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }

        //Close the cursor
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return categories;
    }

    //get all distinct drink categories
    public ArrayList<String> getAllDrinkCategories(){

        //Setting up the parameters for the query
        String columns [] ={Producto.PRODUCT_ID,Producto.PRODUCT_CATEGORY};
        String selection = Producto.PRODUCT_TYPE+"=?";
        String selectionArgs[] = {"Bebida"};

        Cursor cursor = db.query(true,PRODUCT_TABLE,columns,selection,selectionArgs,Producto.PRODUCT_CATEGORY,null,null,null);
        ArrayList<String> categories = new ArrayList<>();

        //If the cursor is empty, there are not categories
        if(cursor.moveToFirst()){
            do{
                categories.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }

        //Close the cursor
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return categories;
    }



    //Traer todos los productos de la base de datos
    public ArrayList<Product> getAllProducts( ){

        //Setting up the parameters for the query
        String columns [] =    {Producto.PRODUCT_ID,
                Producto.PRODUCT_NAME,
                Producto.PRODUCT_TYPE,
                Producto.PRODUCT_CATEGORY};

        Cursor c1 = db.query(PRODUCT_TABLE,columns,null,null,null,null,null);
        ArrayList<Product> listEvents = new ArrayList<>();


        //If the cursor is empty, there are not products
        if(c1.moveToFirst()){
            do{
                Product currentEvent = new Product();
                currentEvent.setId(c1.getInt(0));
                currentEvent.setName(c1.getString(1));
                currentEvent.setType(c1.getString(2));
                currentEvent.setCategory(c1.getString(3));
                listEvents.add(currentEvent);
            }while (c1.moveToNext());
        }
        //Close the cursor
        if (!c1.isClosed()) {
            c1.close();
        }
        return listEvents;
    }

    //Get food products of an specified category
    public ArrayList<String> getFoodByCategory(String category){

        //Setting up the parameters for the query
        String columns [] ={Producto.PRODUCT_NAME};
        String selection = Producto.PRODUCT_CATEGORY+"=? and "+Producto.PRODUCT_TYPE+"=?";
        String selectionArgs[] = {category,"Comida"};

        Cursor cursor = db.query(PRODUCT_TABLE,columns,selection,selectionArgs,null,null,null);
        ArrayList<String> listFoodProducts = new ArrayList<>();

        //If the cursor is empty, there are not categories
        if(cursor.moveToFirst()){
            do{
                listFoodProducts.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }

        //Close the cursor
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return listFoodProducts;
    }


    //Get drink products of an specified category
    public ArrayList<String> getDrinkByCategory(String category){

        //Setting up the parameters for the query
        String columns [] ={Producto.PRODUCT_NAME};
        String selection = Producto.PRODUCT_CATEGORY+"=? and "+Producto.PRODUCT_TYPE+"=?";
        String selectionArgs[] = {category,"Bebida"};

        Cursor cursor = db.query(PRODUCT_TABLE,columns,selection,selectionArgs,null,null,null);
        ArrayList<String> listDrinkProducts = new ArrayList<>();

        //If the cursor is empty, there are not categories
        if(cursor.moveToFirst()){
            do{
                listDrinkProducts.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }

        //Close the cursor
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return listDrinkProducts;
    }

    //Insert a place
    public void insertPlace(co.edu.udea.compumovil.proyectogr8.foodea.Model.Place place){
        ContentValues values = new ContentValues();
        values.put(Site.PLACE_NAME,place.getName());
        values.put(Site.PLACE_LATITUDE, place.getLatitude());
        values.put(Site.PLACE_LONGITUDE, place.getLongitude());
        values.put(Site.PLACE_DESCRIPTION,place.getDescription());
        db.insert(PLACE_TABLE,null,values);
    }

    //Traer todos los eventos de la base de datos
    public ArrayList<Place> getAllPlaces( ){

        //Setting up the parameters for the query
        String columns [] =    {Site.PLACE_ID,Site.PLACE_NAME, Site.PLACE_LATITUDE, Site.PLACE_LONGITUDE,Site.PLACE_DESCRIPTION};

        Cursor c1 = db.query(PLACE_TABLE,columns,null,null,null,null,null);
        ArrayList<Place> listPlaces = new ArrayList<>();

       //If the cursor is empty, there are not products
        if(c1.moveToFirst()){
            do{
                Place currentPlace = new Place();
                currentPlace.setId(c1.getInt(0));
                currentPlace.setName(c1.getString(1));
                currentPlace.setLatitude(c1.getDouble(2));
                currentPlace.setLongitude(c1.getDouble(3));
                currentPlace.setDescription(c1.getString(4));
                listPlaces.add(currentPlace);
            }while (c1.moveToNext());
        }
        //Close the cursor
        if (!c1.isClosed()) {
            c1.close();
        }
        return listPlaces;
    }

    //Insert registers to the ProductXPlace table
    public void insertProductxPlace(int placeId, int productId, int price){
        ContentValues values = new ContentValues();
        values.put(ProductXPlace.PLACE_ID,placeId);
        values.put(ProductXPlace.PRODUCT_ID,productId);
        values.put(ProductXPlace.PRICE,price);
        db.insert(PRODUCTXPLACE_TABLE,null,values);
    }

    //Return the places where exist the specified product
    public ArrayList<Place> getPlacesByProduct(String productName){
        //Setting up the parameters for the first query
        String table = PRODUCT_TABLE + " INNER JOIN " + PRODUCTXPLACE_TABLE + " ON " + PRODUCT_TABLE+"."+Producto.PRODUCT_ID +"="+PRODUCTXPLACE_TABLE+"."+ProductXPlace.PRODUCT_ID
                + " INNER JOIN " + PLACE_TABLE + " ON " + PLACE_TABLE+"."+Site.PLACE_ID +"="+PRODUCTXPLACE_TABLE+"."+ProductXPlace.PLACE_ID;
        String columnsProduct [] = {PLACE_TABLE+"."+Site.PLACE_ID,PLACE_TABLE+"."+Site.PLACE_NAME, Site.PLACE_LATITUDE, Site.PLACE_LONGITUDE, Site.PLACE_DESCRIPTION};
        String selection = PRODUCT_TABLE+"."+Producto.PRODUCT_NAME+"=?";
        String selectionArgs[] = {productName};

        Cursor c1 = db.query(table,columnsProduct,selection,selectionArgs,null,null,null);
        ArrayList<Place> listPlaces = new ArrayList<>();
        if(c1.moveToFirst()){
            do{
                Place currentPlace = new Place();
                currentPlace.setId(c1.getInt(0));
                currentPlace.setName(c1.getString(1));
                currentPlace.setLatitude(c1.getDouble(2));
                currentPlace.setLongitude(c1.getDouble(3));
                currentPlace.setDescription(c1.getString(4));
                listPlaces.add(currentPlace);
            }while(c1.moveToNext());
        }

        //Close the cursor
        if (!c1.isClosed()) {
            c1.close();
        }

        return listPlaces;
    }

    public int getPrice(String productName, int placeId){

        String table = PRODUCT_TABLE + " INNER JOIN " + PRODUCTXPLACE_TABLE + " ON " + PRODUCT_TABLE+"."+Producto.PRODUCT_ID +"="+PRODUCTXPLACE_TABLE+"."+ProductXPlace.PRODUCT_ID;

        String columnsProduct [] = {PRODUCTXPLACE_TABLE+"."+ProductXPlace.PRICE};
        String selection = PRODUCT_TABLE+"."+Producto.PRODUCT_NAME+"=? and "+PRODUCTXPLACE_TABLE+"."+ProductXPlace.PLACE_ID+"=?";
        String selectionArgs[] = {productName, String.valueOf(placeId)};

        Cursor c1 = db.query(table,columnsProduct,selection,selectionArgs,null,null,null);

        if(c1.moveToFirst()){
            return c1.getInt(0);
        }

        return 0;
    }

    public ArrayList<Integer> getPrices(){
        String columns [] = {ProductXPlace.PRICE};
        Cursor c1 =db.query(PRODUCTXPLACE_TABLE,columns,null,null,null,null,null);
        ArrayList<Integer> listaPrecios = new ArrayList<>();
        if(c1.moveToFirst()){
            do{
                listaPrecios.add(c1.getInt(0));
            }while (c1.moveToNext());
        }
        //Close the cursor
        if (!c1.isClosed()) {
            c1.close();
        }

        return listaPrecios;
    }

    public Place getProduct(int idPlace){

        String columns [] = {Site.PLACE_ID, Site.PLACE_NAME, Site.PLACE_LATITUDE, Site.PLACE_LONGITUDE,Site.PLACE_DESCRIPTION};
        String selection = Site.PLACE_ID + "=?";
        String selectionArgs[] = {String.valueOf(idPlace)};

        Cursor c1 = db.query(PLACE_TABLE,columns,selection,selectionArgs,null,null,null);

        Place place = null;

        if(c1.moveToFirst()){
            place = new Place();
            place.setId(c1.getInt(0));
            place.setName(c1.getString(1));
            place.setLatitude(c1.getDouble(2));
            place.setLongitude(c1.getDouble(3));
            place.setDescription(c1.getString(4));
        }
        return place;
    }

    public ArrayList<Product> getProductsByPlace(int idPlace) {

        String table = PRODUCT_TABLE + " INNER JOIN " + PRODUCTXPLACE_TABLE + " ON " + Producto.PRODUCT_ID + "="+ProductXPlace.PRODUCT_ID;
        String columns [] = {Producto.PRODUCT_ID,Producto.PRODUCT_NAME, Producto.PRODUCT_TYPE,Producto.PRODUCT_CATEGORY};
        String selection = ProductXPlace.PLACE_ID+"=?";
        String selectionArgs [] = {String.valueOf(idPlace)};

        Cursor c1 = db.query(table, columns,selection, selectionArgs, null,null,null);

        ArrayList<Product> products = new ArrayList<>();
        Product currentProduct = null;

        if(c1.moveToFirst()){
            do{
                int idProduct = c1.getInt(0);
                String productName = c1.getString(1);
                String productType = c1.getString(2);
                String productCategory = c1.getString(3);
                currentProduct = new Product(idProduct, productName, productType,productCategory);
                products.add(currentProduct);
            }while(c1.moveToNext());
        }

        //Close the cursor
        if (!c1.isClosed()) {
            c1.close();
        }

        return products;
    }

    public ArrayList<String> getCategoriesByPlace(int idPlace){

        String table = PRODUCT_TABLE + " INNER JOIN " + PRODUCTXPLACE_TABLE + " ON " + Producto.PRODUCT_ID + "="+ProductXPlace.PRODUCT_ID;
        String columns [] = {Producto.PRODUCT_CATEGORY};
        String selection = ProductXPlace.PLACE_ID+"=?";
        String selectionArgs [] = {String.valueOf(idPlace)};

        Cursor c1 = db.query(true,table, columns,selection, selectionArgs, null,null,null,null);

        ArrayList<String> categories = new ArrayList<>();

        if(c1.moveToFirst()){
            do{
               categories.add(c1.getString(0));
            }while(c1.moveToNext());
        }

        //Close the cursor
        if (!c1.isClosed()) {
            c1.close();
        }

        return categories;
    }

}

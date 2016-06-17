package co.edu.udea.compumovil.proyectogr8.foodea.Places;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import co.edu.udea.compumovil.proyectogr8.foodea.Adapters.CustomListAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Product;
import co.edu.udea.compumovil.proyectogr8.foodea.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceProductsFragment extends ListFragment {

    public static final String TAB_TITLE = "Productos";
    public static final String KEY_PRODUCT_NAME = "Product_Name";
    public static final String KEY_PRODUCT_TYPE = "Product_Type";
    public static final String KEY_PRODUCT = "Product";
    public static final String KEY_PRODUCT_PRICE = "Product_Price";

    //private ArrayAdapter adapter;
    private  CustomListAdapter adapter;
    private MenuItem mSearchAction;
    private boolean mSearchOpened;
    private String mSearchQuery;
    private EditText mSearchEt;
    OnProductSelectedListener mCallback;
    private ArrayList<HashMap<String,Object>> products;
    private ArrayList<HashMap<String,String>> productsAdapter;


    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnProductSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected*/
        void onProductSelected(Product product);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        //Create the database handler
        DBAdapter dbHandler = new DBAdapter(getContext());
        //Open the connection with the database
        dbHandler.openConnection();
        //get the arguments
        Bundle args = getArguments();
        int idPlace=0;
        if(args!=null){
            idPlace = args.getInt(PlaceActivity.PLACE_PRODUCTS_KEY);
        }

        //query on the database
        products = dbHandler.getProductsByPlace(idPlace);
        //Close the connection with the database
        dbHandler.closeConnection();

        //Create the array with product names and produc types to show on the list view
        productsAdapter = new ArrayList<>();

        //Set the data
        for(HashMap<String,Object> productInformation: products){
            HashMap<String,String> map = new HashMap<>();
            Product product = (Product) productInformation.get(KEY_PRODUCT);
            String priceString = productInformation.get(KEY_PRODUCT_PRICE).toString();
            map.put(KEY_PRODUCT_NAME, product.getName());
            map.put(KEY_PRODUCT_TYPE, product.getType());
            map.put(KEY_PRODUCT_PRICE,priceString);
            productsAdapter.add(map);
        }

        //Create the custom adapter and set it to the list view
        adapter = new CustomListAdapter(getActivity(),productsAdapter);
        setListAdapter(adapter);

        //Initialize some variables
        mSearchOpened = false;
        mSearchQuery = "";

        //Allow the actions buttons to be showed in this fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText("No se encontraron productos");
        int[] colors = {0, 0xFFB0BEC5, 0}; // red for the example
        getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        getListView().setDividerHeight(5);
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        /*if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnProductSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProductSelectedListener");
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search){
            if (mSearchOpened) {
                closeSearchBar();
            } else {
                openSearchBar(mSearchQuery);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText) {

        // Set custom view on action bar.
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        // Search edit text field setup.
        mSearchEt = (EditText) actionBar.getCustomView()
                .findViewById(R.id.etSearch);
        mSearchEt.addTextChangedListener(new SearchWatcher());
        mSearchEt.setText(queryText);
        mSearchEt.requestFocus();

        // Change search icon accordingly.
        mSearchAction.setIcon(getActivity().getResources().getDrawable(R.mipmap.ic_close_black_24dp));
        mSearchOpened = true;

        //Show the soft keyboard
        InputMethodManager inputMgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(0, 0);

    }

    private void closeSearchBar() {

        View v = getActivity().getCurrentFocus();
        mSearchEt.setText("");
        //Hide the soft keyboard
        if(v!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Remove custom view.
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);

        // Change search icon accordingly.
        mSearchAction.setIcon(getActivity().getResources().getDrawable(R.mipmap.ic_search_black_24dp));
        mSearchOpened = false;
    }

    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
            Log.d("TESTING",mSearchEt.getText().toString());
            adapter.getFilter().filter(mSearchEt.getText().toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onProductSelected((Product)products.get(position).get(KEY_PRODUCT));
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }


}

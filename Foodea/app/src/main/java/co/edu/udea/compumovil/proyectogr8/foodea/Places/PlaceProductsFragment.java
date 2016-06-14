package co.edu.udea.compumovil.proyectogr8.foodea.Places;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import co.edu.udea.compumovil.proyectogr8.foodea.Database.DBAdapter;
import co.edu.udea.compumovil.proyectogr8.foodea.Model.Product;
import co.edu.udea.compumovil.proyectogr8.foodea.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceProductsFragment extends ListFragment {

    public static final String TAB_TITLE = "Productos";

    private ArrayAdapter adapter;
    private MenuItem mSearchAction;
    private boolean mSearchOpened;
    private String mSearchQuery;
    private EditText mSearchEt;
    private TextView tvEmptyMessage;
    OnProductSelectedListener mCallback;
    private ArrayList<Product> products;


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

        //Create the array with product names to show on the list view
        ArrayList<String> productNames = new ArrayList<>();
        for(Product product: products){
            productNames.add(product.getName());
        }


        //Create and set the adapter for the list view
        adapter = new ArrayAdapter<>(getContext(),layout,productNames);
        setListAdapter(adapter);

        mSearchOpened = false;
        mSearchQuery = "";

        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText("No se encontraron productos");
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

        }

        @Override
        public void afterTextChanged(Editable editable) {
            adapter.getFilter().filter(mSearchEt.getText().toString());
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onProductSelected(products.get(position));
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }


}

package co.edu.udea.compumovil.proyectogr8.foodea;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import co.edu.udea.compumovil.proyectogr8.foodea.Places.PlaceProductsFragment;

/**
 * Created by user on 15/06/2016.
 */
public class CustomListAdapter extends BaseAdapter implements Filterable {

    private static LayoutInflater inflater = null;
    private ArrayList<HashMap<String,String>> data;
    private ArrayList<HashMap<String,String>> filteredData;
    private ItemFilter mFilter = new ItemFilter();


    public CustomListAdapter(Activity activity, ArrayList<HashMap<String,String>> data){
        this.data = data;
        this.filteredData = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView==null){
            view = inflater.inflate(R.layout.list_row,null);
        }
        TextView tvProductName = (TextView)view.findViewById(R.id.product_name); // Product Name
        TextView tvProductPrice = (TextView)view.findViewById(R.id.product_price); //Product price
        ImageView ivProduct=(ImageView)view.findViewById(R.id.iv_product_type); // type product icon

        HashMap<String, String> product;
        product = filteredData.get(position);

        // Setting all values in listview
        NumberFormat formatCurrency = NumberFormat.getCurrencyInstance(Locale.US);
        formatCurrency.setMaximumFractionDigits(0);
        int price = Integer.parseInt(product.get(PlaceProductsFragment.KEY_PRODUCT_PRICE));
        tvProductName.setText(product.get(PlaceProductsFragment.KEY_PRODUCT_NAME));
        tvProductPrice.setText(formatCurrency.format(price));
        String productType = product.get(PlaceProductsFragment.KEY_PRODUCT_TYPE);
        if(productType.equals("Comida")) {
            ivProduct.setImageResource(R.drawable.ic_food);
        }else{
            ivProduct.setImageResource(R.drawable.ic_drink);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<HashMap<String,String>> list = data;

            int count = list.size();
            final ArrayList<HashMap<String,String>> filteredList = new ArrayList<>();

            String filterableString ;

            for (int i = 0; i < count; i++) {
                HashMap<String,String> currentItem = list.get(i);
                filterableString = currentItem.get(PlaceProductsFragment.KEY_PRODUCT_NAME);
                if (filterableString.toLowerCase().contains(filterString)) {
                    filteredList.add(currentItem);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<HashMap<String,String>>) results.values;
            notifyDataSetChanged();
        }

    }
}

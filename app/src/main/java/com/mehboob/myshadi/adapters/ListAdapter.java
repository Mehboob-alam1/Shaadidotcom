package com.mehboob.myshadi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mehboob.myshadi.json.Country;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter implements Filterable {
    private List<Country> countryList;
    private LayoutInflater inflater;
    private CountryFilter countryFilter;

    private List<Country> filteredList;

    public ListAdapter(Context context, List<Country> countryList) {
        this.countryList = countryList;
        this.filteredList = countryList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            holder = new ViewHolder();
            holder.countryNameTextView = convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Country country = countryList.get(position);
        holder.countryNameTextView.setText(country.getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (countryFilter == null) {
            countryFilter = new CountryFilter();
        }
        return countryFilter;
    }

    private static class ViewHolder {
        TextView countryNameTextView;
    }

    private class CountryFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Country> filteredCountries = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If the search bar is empty, show the original list
                filteredCountries.addAll(countryList);
            } else {
                // Filter the list based on the constraint
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Country country : countryList) {
                    if (country.getName().toLowerCase().contains(filterPattern)) {
                        filteredCountries.add(country);
                    }
                }
            }

            results.values = filteredCountries;
            results.count = filteredCountries.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Update the filtered list and notify the adapter
            filteredList = (List<Country>) results.values;
            notifyDataSetChanged();
        }
    }
}

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
import com.mehboob.myshadi.json.State;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends BaseAdapter implements Filterable {
    private List<State> countryList;
    private LayoutInflater inflater;
    private CountryFilter countryFilter;

    private List<State> filteredList;

    public StateAdapter(Context context, List<State> countryList) {
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

        State country = countryList.get(position);
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
            List<State> filteredCountries = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If the search bar is empty, show the original list
                filteredCountries.addAll(countryList);
            } else {
                // Filter the list based on the constraint
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (State country : countryList) {
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
            // Clear the current filtered list
            filteredList.clear();

            // Add the filtered results to the filtered list
            filteredList.addAll((List<State>) results.values);

            // Notify the adapter
            notifyDataSetChanged();
        }
    }

}

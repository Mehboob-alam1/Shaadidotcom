package com.mehboob.myshadi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.json.Countries;
import com.mehboob.myshadi.json.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountryViewHolder> implements Filterable {

    public OnItemClickListener onItemClickListener;
    private List<Countries> countryList; // Original list
    private List<Countries> countryListFull; // Full list to support filtering
    private final Object lock = new Object(); // Lock to avoid issues with concurrent modification

    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(Countries country, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public CountriesAdapter() {
        countryList = new ArrayList<>();
        countryListFull = new ArrayList<>();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_layout, parent, false);
        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Countries currentCountry = countryList.get(position);
        holder.bind(currentCountry);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }



    public void setCountries(List<Countries> countries) {
        synchronized (lock) {
            countryList = new ArrayList<>(countries);
            countryListFull = new ArrayList<>(countries);
            notifyDataSetChanged(); // Notify adapter of the change
        }
    }

    private final Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Countries> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                synchronized (lock) {
                    filteredList.addAll(countryListFull);
                }
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                synchronized (lock) {
                    for (Countries country : countryListFull) {
                        if (country.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(country);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countryList = (List<Countries>) results.values;
            notifyDataSetChanged(); // Notify adapter of the change
        }
    };

    public class CountryViewHolder extends RecyclerView.ViewHolder {
        private final TextView countryNameTextView;

        CountryViewHolder(View itemView) {
            super(itemView);
            countryNameTextView = itemView.findViewById(R.id.countryNameTextView);

            // Set click listener for the item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(countryList.get(position), position);
                }
            });
        }

        void bind(Countries country) {
            countryNameTextView.setText(country.getName());
        }
    }
}


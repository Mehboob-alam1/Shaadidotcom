package com.mehboob.myshadi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehboob.myshadi.R;
import com.mehboob.myshadi.json.Cities;
import com.mehboob.myshadi.json.States;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> implements Filterable {

    public CitiesAdapter.OnItemClickListener onItemClickListener;
    private List<Cities> citiesList; // Original list
    private List<Cities> citiesListFull; // Full list to support filtering
    private final Object lock = new Object(); // Lock to avoid issues with concurrent modification

    @Override
    public Filter getFilter() {
        return citiesFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(Cities states, int position);
    }

    public void setOnItemClickListener(CitiesAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public CitiesAdapter() {
        citiesList = new ArrayList<>();
        citiesListFull = new ArrayList<>();
    }

    @NonNull
    @Override
    public CitiesAdapter.CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_layout, parent, false);
        return new CitiesAdapter.CityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesAdapter.CityViewHolder holder, int position) {
        Cities currentCity = citiesList.get(position);
        holder.bind(currentCity);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }



    public void setCities(List<Cities> cities) {
        synchronized (lock) {
            citiesList = new ArrayList<>(cities);
            citiesListFull = new ArrayList<>(cities);
            notifyDataSetChanged(); // Notify adapter of the change
        }
    }

    private final Filter citiesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Cities> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                synchronized (lock) {
                    filteredList.addAll(citiesListFull);
                }
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                synchronized (lock) {
                    for (Cities city : citiesListFull) {
                        if (city.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(city);
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
            citiesList = (List<Cities>) results.values;
            notifyDataSetChanged(); // Notify adapter of the change
        }
    };

    public class CityViewHolder extends RecyclerView.ViewHolder {
        private final TextView cityTextName;

        CityViewHolder(View itemView) {
            super(itemView);
            cityTextName = itemView.findViewById(R.id.countryNameTextView);

            // Set click listener for the item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(citiesList.get(position), position);
                }
            });
        }

        void bind(Cities city) {
            cityTextName.setText(city.getName());
        }
    }
}

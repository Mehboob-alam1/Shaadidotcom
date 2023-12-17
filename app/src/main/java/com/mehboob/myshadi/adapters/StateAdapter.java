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
import com.mehboob.myshadi.json.States;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> implements Filterable {

    public StateAdapter.OnItemClickListener onItemClickListener;
    private List<States> statesList; // Original list
    private List<States> stateListFull; // Full list to support filtering
    private final Object lock = new Object(); // Lock to avoid issues with concurrent modification

    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(States states, int position);
    }

    public void setOnItemClickListener(StateAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public StateAdapter() {
        statesList = new ArrayList<>();
        stateListFull = new ArrayList<>();
    }

    @NonNull
    @Override
    public StateAdapter.StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_layout, parent, false);
        return new StateAdapter.StateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StateAdapter.StateViewHolder holder, int position) {
        States currentState = statesList.get(position);
        holder.bind(currentState);
    }

    @Override
    public int getItemCount() {
        return statesList.size();
    }



    public void setStates(List<States> states) {
        synchronized (lock) {
            statesList = new ArrayList<>(states);
            stateListFull = new ArrayList<>(states);
            notifyDataSetChanged(); // Notify adapter of the change
        }
    }

    private final Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<States> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                synchronized (lock) {
                    filteredList.addAll(stateListFull);
                }
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                synchronized (lock) {
                    for (States state : stateListFull) {
                        if (state.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(state);
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
            statesList = (List<States>) results.values;
            notifyDataSetChanged(); // Notify adapter of the change
        }
    };

    public class StateViewHolder extends RecyclerView.ViewHolder {
        private final TextView stateTextName;

        StateViewHolder(View itemView) {
            super(itemView);
            stateTextName = itemView.findViewById(R.id.countryNameTextView);

            // Set click listener for the item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(statesList.get(position), position);
                }
            });
        }

        void bind(States state) {
            stateTextName.setText(state.getName());
        }
    }
}

package com.example.eventbasedtracking.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbasedtracking.R;

import java.util.List;

public class LocationsViewAdapter extends RecyclerView.Adapter<LocationsViewHolder> {

    Context context;
    List<String> locationsName, locationsETA, locationsETD;

    public LocationsViewAdapter(Context context, List<String> locationsName, List<String> locationsETA, List<String> locationsETD) {
        this.context = context;
        this.locationsName = locationsName;
        this.locationsETA = locationsETA;
        this.locationsETD = locationsETD;
    }

    @NonNull
    @Override
    public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_locations, parent, false);
        return new LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsViewHolder holder, final int position) {
        String locationName = String.valueOf(locationsName.get(position));
        String locationETA = String.valueOf(locationsETA.get(position));
        String locationETD = String.valueOf(locationsETD.get(position));
        holder.txt_location_name.setText(locationName);
        holder.txt_location_eta.setText(locationETA);
        holder.txt_location_etd.setText(locationETD);
    }

    @Override
    public int getItemCount() {
        return locationsName.size();
    }
}

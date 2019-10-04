package com.example.eventbasedtracking.ViewAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbasedtracking.R;
import com.google.android.material.card.MaterialCardView;

public class LocationsViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView cardView;
    TextView txt_location_name, txt_location_eta, txt_location_etd;

    public LocationsViewHolder(View itemView) {
        super(itemView);

        // View
        cardView = itemView.findViewById(R.id.card);
        txt_location_name = itemView.findViewById(R.id.txt_location_name);
        txt_location_eta = itemView.findViewById(R.id.txt_location_eta);
        txt_location_etd = itemView.findViewById(R.id.txt_location_etd);
    }
}
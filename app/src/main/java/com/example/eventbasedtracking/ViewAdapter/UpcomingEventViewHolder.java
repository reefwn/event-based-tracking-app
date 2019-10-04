package com.example.eventbasedtracking.ViewAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbasedtracking.R;
import com.google.android.material.card.MaterialCardView;

public class UpcomingEventViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView cardView;
    TextView txt_event_name;
    TextView txt_event_code;
    TextView txt_event_status;

    public UpcomingEventViewHolder(View itemView) {
        super(itemView);

        // View
        cardView = itemView.findViewById(R.id.card);
        txt_event_name = itemView.findViewById(R.id.txt_event_name);
        txt_event_code = itemView.findViewById(R.id.txt_event_code);
        txt_event_status = itemView.findViewById(R.id.txt_event_status);
    }
}
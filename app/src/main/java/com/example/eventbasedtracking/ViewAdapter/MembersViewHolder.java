package com.example.eventbasedtracking.ViewAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbasedtracking.R;
import com.google.android.material.card.MaterialCardView;

public class MembersViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView cardView;
    TextView txt_member_name;

    public MembersViewHolder(View itemView) {
        super(itemView);

        // View
        cardView = itemView.findViewById(R.id.card);
        txt_member_name = itemView.findViewById(R.id.txt_member_name);
    }
}
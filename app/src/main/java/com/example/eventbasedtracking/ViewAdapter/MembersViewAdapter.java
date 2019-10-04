package com.example.eventbasedtracking.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbasedtracking.R;

import java.util.List;

public class MembersViewAdapter extends RecyclerView.Adapter<MembersViewHolder> {

    Context context;
    List<String> members;

    public MembersViewAdapter(Context context, List<String> members) {
        this.context = context;
        this.members = members;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_members, parent, false);
        return new MembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, final int position) {
        String memberName = String.valueOf(members.get(position));
        holder.txt_member_name.setText(memberName);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}

package com.example.eventbasedtracking.ViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbasedtracking.EventActivity;
import com.example.eventbasedtracking.Model.UpcomingEvent;
import com.example.eventbasedtracking.R;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventViewAdapter extends RecyclerView.Adapter<UpcomingEventViewHolder> {

    Context context;
    List<UpcomingEvent> upcomingEventList;

    public UpcomingEventViewAdapter(Context context, List<UpcomingEvent> upcomingEventList) {
        this.context = context;
        this.upcomingEventList = upcomingEventList;
    }

    @NonNull
    @Override
    public UpcomingEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_upcoming_events, parent, false);
        return new UpcomingEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingEventViewHolder holder, final int position) {
        final String eventName = String.valueOf(upcomingEventList.get(position).eventName);
        final String eventCode = String.valueOf(upcomingEventList.get(position).eventCode);
        final String createdBy = String.valueOf(upcomingEventList.get(position).createdBy);
        final String startDate = String.valueOf(upcomingEventList.get(position).eventStart.date);
        final String startTime = String.valueOf(upcomingEventList.get(position).eventStart.time);
        final String endDate = String.valueOf(upcomingEventList.get(position).eventEnd.date);
        final String endTime = String.valueOf(upcomingEventList.get(position).eventEnd.time);
        final ArrayList<String> members = new ArrayList<>();
        final ArrayList<String> locationsName = new ArrayList<>();
        final ArrayList<String> locationsETA = new ArrayList<>();
        final ArrayList<String> locationsETD = new ArrayList<>();

        if(upcomingEventList.get(position).members != null && upcomingEventList.get(position).members.isEmpty() == false)
        {
            for(int i=0; i<upcomingEventList.get(position).members.size(); i++) {
                members.add(String.valueOf(upcomingEventList.get(position).members.get(i).userName));
            }
        }

        if(upcomingEventList.get(position).eventLocations != null) {
            for(int i=0; i<upcomingEventList.get(position).eventLocations.size(); i++) {
                locationsName.add(String.valueOf(upcomingEventList.get(position).eventLocations.get(i).locationName));
                locationsETA.add(String.valueOf(upcomingEventList.get(position).eventLocations.get(i).locationETA));
                locationsETD.add(String.valueOf(upcomingEventList.get(position).eventLocations.get(i).locationETD));
            }
        }

        holder.txt_event_name.setText(eventName);
        holder.txt_event_code.setText(eventCode);

        // EVENT LISTENER
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("eventName", eventName);
                intent.putExtra("eventCode", eventCode);
                intent.putExtra("createdBy", createdBy);
                intent.putExtra("startDate", startDate);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endDate", endDate);
                intent.putExtra("endTime", endTime);
                intent.putStringArrayListExtra("members", members);
                intent.putStringArrayListExtra("locationsName", locationsName);
                intent.putStringArrayListExtra("locationsETA", locationsETA);
                intent.putStringArrayListExtra("locationsETD", locationsETD);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcomingEventList.size();
    }
}

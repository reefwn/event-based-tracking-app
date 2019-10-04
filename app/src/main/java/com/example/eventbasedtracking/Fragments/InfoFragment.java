package com.example.eventbasedtracking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventbasedtracking.R;

public class InfoFragment extends Fragment {

    TextView txt_event_id, txt_start_date, txt_start_time, txt_end_date, txt_end_time;
    String event_code, event_start_date, event_start_time, event_end_date, event_end_time;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event_code = getArguments().getString("eventCode");
            event_start_date = getArguments().getString("startDate");
            event_start_time = getArguments().getString("startTime");
            event_end_date = getArguments().getString("endDate");
            event_end_time = getArguments().getString("endTime");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        txt_event_id = view.findViewById(R.id.txt_event_id);
        txt_event_id.setText("Group ID: " + event_code);
        txt_start_date = view.findViewById(R.id.txt_start_date);
        txt_start_date.setText(event_start_date);
        txt_start_time = view.findViewById(R.id.txt_start_time);
        txt_start_time.setText(event_start_time);
        txt_end_date = view.findViewById(R.id.txt_end_date);
        txt_end_date.setText(event_end_date);
        txt_end_time = view.findViewById(R.id.txt_end_time);
        txt_end_time.setText(event_end_time);
        return view;
    }
}

package com.example.eventbasedtracking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventbasedtracking.R;
import com.example.eventbasedtracking.ViewAdapter.LocationsViewAdapter;

import java.util.ArrayList;

public class LocationsFragment extends Fragment {

    RecyclerView rcv_locations;
    ArrayList<String> locationsName, locationsETA, locationsETD;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationsName = getArguments().getStringArrayList("locationsName");
            locationsETA = getArguments().getStringArrayList("locationsETA");
            locationsETD = getArguments().getStringArrayList("locationsETD");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        rcv_locations = view.findViewById(R.id.rcv_locations);
        rcv_locations.setHasFixedSize(true);
        rcv_locations.setLayoutManager(new LinearLayoutManager(getActivity()));
        LocationsViewAdapter adapter = new LocationsViewAdapter(this.getContext(), locationsName, locationsETA, locationsETD);
        rcv_locations.setAdapter(adapter);
        rcv_locations.setItemAnimator(new DefaultItemAnimator());

        return view;
    }
}

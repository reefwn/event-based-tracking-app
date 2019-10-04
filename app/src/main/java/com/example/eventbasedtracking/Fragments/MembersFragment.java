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
import com.example.eventbasedtracking.ViewAdapter.MembersViewAdapter;

import java.util.ArrayList;

public class MembersFragment extends Fragment {

    RecyclerView rcv_members;
    ArrayList<String> members;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            members = getArguments().getStringArrayList("members");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members, container, false);

        rcv_members = view.findViewById(R.id.rcv_members);
        rcv_members.setHasFixedSize(true);
        rcv_members.setLayoutManager(new LinearLayoutManager(getActivity()));
        MembersViewAdapter adapter = new MembersViewAdapter(this.getContext(), members);
        rcv_members.setAdapter(adapter);
        rcv_members.setItemAnimator(new DefaultItemAnimator());

        return view;
    }
}

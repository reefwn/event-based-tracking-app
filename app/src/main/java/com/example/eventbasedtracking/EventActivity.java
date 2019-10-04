package com.example.eventbasedtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.eventbasedtracking.Fragments.LocationsFragment;
import com.example.eventbasedtracking.Fragments.MembersFragment;
import com.example.eventbasedtracking.Fragments.InfoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EventActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    Bundle bundle = new Bundle();
    String event_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Get Intent
        if(getIntent().hasExtra("eventName") && getIntent().hasExtra("eventCode")) {
            event_name = getIntent().getStringExtra("eventName");
            TextView event_title = findViewById(R.id.event_title);
            event_title.setText(event_name);

            bundle.putString("eventCode", getIntent().getStringExtra("eventCode"));
            bundle.putString("startDate", getIntent().getStringExtra("startDate"));
            bundle.putString("startTime", getIntent().getStringExtra("startTime"));
            bundle.putString("endDate", getIntent().getStringExtra("endDate"));
            bundle.putString("endTime", getIntent().getStringExtra("endTime"));
            bundle.putStringArrayList("members", getIntent().getStringArrayListExtra("members"));
            bundle.putStringArrayList("locationsName", getIntent().getStringArrayListExtra("locationsName"));
            bundle.putStringArrayList("locationsETA", getIntent().getStringArrayListExtra("locationsETA"));
            bundle.putStringArrayList("locationsETD", getIntent().getStringArrayListExtra("locationsETD"));
        }

        // SetOnclickListener for navigation
        BottomNavigationView eventNavigation = findViewById(R.id.event_navigation);
        eventNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                String fragmentName = "";
                switch (menuItem.getItemId()) {
                    case R.id.nav_info:
                        selectedFragment = new InfoFragment();
                        fragmentName = "info";
                        break;
                    case R.id.nav_members:
                        selectedFragment = new MembersFragment();
                        fragmentName = "member";
                        break;
                    case R.id.nav_locations:
                        selectedFragment = new LocationsFragment();
                        fragmentName = "location";
                        break;
                }

                selectedFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment, fragmentName).commit();
                return true;
            }
        });

        Fragment initialFragment = new InfoFragment();
        initialFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, initialFragment, "info").commit();
    }
}

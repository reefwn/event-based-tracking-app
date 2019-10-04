package com.example.eventbasedtracking;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventbasedtracking.ViewAdapter.UpcomingEventViewAdapter;
import com.example.eventbasedtracking.Model.UpcomingEvent;
import com.example.eventbasedtracking.Retrofit.API;
import com.example.eventbasedtracking.Retrofit.APIClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MyUpcomingEventActivity extends AppCompatActivity {

    final static int REQUEST_CODE = 100;

    API scalarAPI, gsonAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    EditText edt_join;
    MaterialButton btn_join;
    FloatingActionButton btn_add_event;
    RecyclerView rcv_upcoming_events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_upcoming_event);

        // Initial API
        Retrofit gsonRetrofit = APIClient.getInstance("gson");
        gsonAPI = gsonRetrofit.create(API.class);
        Retrofit scalarRetrofit = APIClient.getInstance("scalar");
        scalarAPI = scalarRetrofit.create(API.class);

        // Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
        final String userId = sharedPreferences.getString(getString(R.string.sp_user_id), "");
        final String userName = sharedPreferences.getString(getString(R.string.sp_user_name), "");

        // View
        edt_join = findViewById(R.id.edt_join);
        btn_join = findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_join.getText().toString() == "" || edt_join.getText().toString().isEmpty()) {
                    Toast.makeText(MyUpcomingEventActivity.this, "Please filled in Group ID", Toast.LENGTH_LONG).show();
                } else {
                    joinEvent(edt_join.getText().toString(), userId, userName);
                }
            }
        });
        btn_add_event = findViewById(R.id.btn_add_event);
        rcv_upcoming_events = findViewById(R.id.rcv_upcoming_events);
        rcv_upcoming_events.setHasFixedSize(true);
        rcv_upcoming_events.setLayoutManager(new LinearLayoutManager(this));
        fetchUpcomingEvents(userId);
        rcv_upcoming_events.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                String userId = sharedPreferences.getString(getString(R.string.sp_user_id), "");
                Log.e("USER ID", userId);
                fetchUpcomingEvents(userId);
            }
        }
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    // FUNCTIONS
    private void joinEvent(String eventCode, String userId, String userName) {
        compositeDisposable.add(scalarAPI.joinEvent(eventCode, userId, userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MyUpcomingEventActivity.this, "" + s, Toast.LENGTH_LONG).show();
                    }
                }));
        fetchUpcomingEvents(userId);
    }

    private void fetchUpcomingEvents(String userId) {
        compositeDisposable.add(gsonAPI.fetchUpcomingEvents(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UpcomingEvent>>() {
                    @Override
                    public void accept(List<UpcomingEvent> events) throws Exception {
                        displayData(events);
                    }
                }));
    }

    private void displayData(List<UpcomingEvent> events) {
        UpcomingEventViewAdapter adapter = new UpcomingEventViewAdapter(this, events);
        rcv_upcoming_events.setAdapter(adapter);
    }

    // EVENT LISTENER
    public void addEventClick(View view) {
        Intent addEvent = new Intent(MyUpcomingEventActivity.this, CreateEventActivity.class);
        startActivityForResult(addEvent, REQUEST_CODE);
    }
}

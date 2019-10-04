package com.example.eventbasedtracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.eventbasedtracking.Retrofit.API;
import com.example.eventbasedtracking.Retrofit.APIClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    API myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    FloatingActionButton btn_add_location, btn_remove_location;
    MaterialButton btn_event_add_submit;
    TextInputEditText edt_event_name, edt_start_date, edt_end_date, edt_start_time, edt_end_time, edt_event_location1, edt_event_location_eta1, edt_event_location_etd1,
            edt_event_location2, edt_event_location_eta2, edt_event_location_etd2, edt_event_location3, edt_event_location_eta3, edt_event_location_etd3,
            edt_event_location4, edt_event_location_eta4, edt_event_location_etd4, edt_event_location5, edt_event_location_eta5, edt_event_location_etd5;
    ConstraintLayout layout_start_date, layout_end_date, layout_start_time, layout_end_time;
    LinearLayout layout_locations;

    private int year, month, day, hour, minute, locationCount;
    private final static NumberFormat numberFormat = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        locationCount=1;

        // Initial API
        Retrofit retrofit = APIClient.getInstance("scalar");
        myAPI = retrofit.create(API.class);

        // Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        // Buttons
        btn_remove_location = findViewById(R.id.btn_remove_location);
        btn_add_location = findViewById(R.id.btn_add_location);
        btn_event_add_submit = findViewById(R.id.btn_event_add_submit);

        // EditTexts
        edt_event_name = findViewById(R.id.edt_event_name);
        edt_start_date = findViewById(R.id.edt_start_date);
        edt_end_date = findViewById(R.id.edt_end_date);
        edt_start_time = findViewById(R.id.edt_start_time);
        edt_end_time = findViewById(R.id.edt_end_time);
        edt_event_location1 = findViewById(R.id.edt_event_loaction);
        edt_event_location_eta1 = findViewById(R.id.edt_event_loaction_eta);
        edt_event_location_etd1 = findViewById(R.id.edt_event_loaction_etd);

        // LayOuts
        layout_locations = findViewById(R.id.layout_locations);
        layout_start_date = findViewById(R.id.layout_start_date);
        layout_end_date = findViewById(R.id.layout_end_date);
        layout_start_time = findViewById(R.id.layout_start_time);
        layout_end_time = findViewById(R.id.layout_end_time);

        // OnClicks
        layout_start_date.setOnClickListener(this);
        layout_end_date.setOnClickListener(this);
        layout_start_time.setOnClickListener(this);
        layout_end_time.setOnClickListener(this);

        // Hide Button
        btn_remove_location.hide();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    // EVENTS LISTENER
    @Override
    public void onClick(final View view) {

        if(view == layout_start_date || view == layout_end_date) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            if(view == layout_start_date) {
                                edt_start_date.setText(numberFormat.format(day) + "-" + numberFormat.format((month+1)) + "-" + year);
                            } else {
                                edt_end_date.setText(numberFormat.format(day) + "-" + numberFormat.format((month+1)) + "-" + year);
                            }
                        }
                    }, year, month, day
            );
            datePickerDialog.show();
        }
        if(view == layout_start_time || view == layout_end_time) {
            final Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            if(view == layout_start_time) {
                                edt_start_time.setText(numberFormat.format(hour) + ":" + numberFormat.format(minute));
                            } else {
                                edt_end_time.setText(numberFormat.format(hour) + ":" + numberFormat.format(minute));
                            }
                        }
                    }, hour, minute, true
            );
            timePickerDialog.show();
        }
    }

    public void createEventClick(View view) {
        boolean isNameFilled = validateName();
        if(isNameFilled == false) {
            Toast.makeText(this, "Please fill in the name of this event", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isStartDateSelected = validateStartDate();
        if(isStartDateSelected == false) {
            Toast.makeText(this, "Please select the starting date of this event", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isEndDateSelected = validateEndDate();
        if(isEndDateSelected == false) {
            Toast.makeText(this, "Please select the ending date of this event", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isStartTimeSelected = validateStartTime();
        if(isStartTimeSelected == false) {
            Toast.makeText(this, "Please select the starting time of this event", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isEndTimeSelected = validateEndTime();
        if(isEndTimeSelected == false) {
            Toast.makeText(this, "Please select the ending time of this event", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isLocationsFilled = validateLocations();
        if(isLocationsFilled == false) {
            Toast.makeText(this, "Please fill all the locations' name", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isLocationsTimeCorrect = validateLocationsTime();
        if(isLocationsTimeCorrect == false) {
            Toast.makeText(this, "Please use 24-hour time format in location section", Toast.LENGTH_LONG).show();
            return;
        }
        String id = sharedPreferences.getString(getString(R.string.sp_user_id), "");
        switch(locationCount){
            case 1:
                if(edt_event_location1.getText().toString().isEmpty()) {
                    createEvent(edt_event_name.getText().toString(), id, edt_start_date.getText().toString(),
                            edt_start_time.getText().toString(), edt_end_date.getText().toString(), edt_end_time.getText().toString(),
                            locationCount);
                }
                else
                {
                    createEvent(edt_event_name.getText().toString(), id, edt_start_date.getText().toString(),
                            edt_start_time.getText().toString(), edt_end_date.getText().toString(), edt_end_time.getText().toString(),
                            locationCount, edt_event_location1.getText().toString(), edt_event_location_eta1.getText().toString(), edt_event_location_etd1.getText().toString());
                }
                break;
            case 2:
                createEvent(edt_event_name.getText().toString(), id, edt_start_date.getText().toString(),
                        edt_start_time.getText().toString(), edt_end_date.getText().toString(), edt_end_time.getText().toString(),
                        locationCount, edt_event_location1.getText().toString(), edt_event_location_eta1.getText().toString(), edt_event_location_etd1.getText().toString(),
                        edt_event_location2.getText().toString(), edt_event_location_eta2.getText().toString(), edt_event_location_etd2.getText().toString());
                break;
            case 3:
                createEvent(edt_event_name.getText().toString(), id, edt_start_date.getText().toString(),
                        edt_start_time.getText().toString(), edt_end_date.getText().toString(), edt_end_time.getText().toString(),
                        locationCount, edt_event_location1.getText().toString(), edt_event_location_eta1.getText().toString(), edt_event_location_etd1.getText().toString(),
                        edt_event_location2.getText().toString(), edt_event_location_eta2.getText().toString(), edt_event_location_etd2.getText().toString(),
                        edt_event_location3.getText().toString(), edt_event_location_eta3.getText().toString(), edt_event_location_etd3.getText().toString());
                break;
            case 4:
                createEvent(edt_event_name.getText().toString(), id, edt_start_date.getText().toString(),
                        edt_start_time.getText().toString(), edt_end_date.getText().toString(), edt_end_time.getText().toString(),
                        locationCount, edt_event_location1.getText().toString(), edt_event_location_eta1.getText().toString(), edt_event_location_etd1.getText().toString(),
                        edt_event_location2.getText().toString(), edt_event_location_eta2.getText().toString(), edt_event_location_etd2.getText().toString(),
                        edt_event_location3.getText().toString(), edt_event_location_eta3.getText().toString(), edt_event_location_etd3.getText().toString(),
                        edt_event_location4.getText().toString(), edt_event_location_eta4.getText().toString(), edt_event_location_etd4.getText().toString());
                break;
            case 5:
                createEvent(edt_event_name.getText().toString(), id, edt_start_date.getText().toString(),
                        edt_start_time.getText().toString(), edt_end_date.getText().toString(), edt_end_time.getText().toString(),
                        locationCount, edt_event_location1.getText().toString(), edt_event_location_eta1.getText().toString(), edt_event_location_etd1.getText().toString(),
                        edt_event_location2.getText().toString(), edt_event_location_eta2.getText().toString(), edt_event_location_etd2.getText().toString(),
                        edt_event_location3.getText().toString(), edt_event_location_eta3.getText().toString(), edt_event_location_etd3.getText().toString(),
                        edt_event_location4.getText().toString(), edt_event_location_eta4.getText().toString(), edt_event_location_etd4.getText().toString(),
                        edt_event_location5.getText().toString(), edt_event_location_eta5.getText().toString(), edt_event_location_etd5.getText().toString());
                break;
        }
    }

    // VALIDATIONS
    public boolean validateName() {
        boolean result = true;
        if(edt_event_name.getText().toString().isEmpty()) {
            result = false;
        }
        return result;
    }
    public boolean validateStartDate() {
        boolean result = true;
        if(edt_start_date.getText().toString().isEmpty()) {
            result = false;
        }
        return result;
    }
    public boolean validateEndDate() {
        boolean result = true;
        if(edt_end_date.getText().toString().isEmpty()) {
            result = false;
        }
        return result;
    }
    public boolean validateStartTime() {
        boolean result = true;
        if(edt_start_time.getText().toString().isEmpty()) {
            result = false;
        }
        return result;
    }
    public boolean validateEndTime() {
        boolean result = true;
        if(edt_end_time.getText().toString().isEmpty()) {
            result = false;
        }
        return result;
    }
    public boolean validateLocations() {
        boolean result = true;
        switch(locationCount) {
            case 1:
                if(edt_event_location1.getText().toString().isEmpty() && !edt_event_location_eta1.getText().toString().isEmpty()) {
                    result = false;
                }
                else if(edt_event_location1.getText().toString().isEmpty() && !edt_event_location_etd1.getText().toString().isEmpty()) {
                    result = false;
                }
                break;
            case 2:
                if(edt_event_location1.getText().toString().isEmpty() || edt_event_location2.getText().toString().isEmpty()) {
                    result = false;
                }
                break;
            case 3:
                if(edt_event_location1.getText().toString().isEmpty() || edt_event_location2.getText().toString().isEmpty() || edt_event_location3.getText().toString().isEmpty()) {
                    result = false;
                }
                break;
            case 4:
                if(edt_event_location1.getText().toString().isEmpty() || edt_event_location2.getText().toString().isEmpty()
                        || edt_event_location3.getText().toString().isEmpty() || edt_event_location4.getText().toString().isEmpty()) {
                    result = false;
                }
                break;
            case 5:
                if(edt_event_location1.getText().toString().isEmpty() || edt_event_location2.getText().toString().isEmpty()
                        || edt_event_location3.getText().toString().isEmpty() || edt_event_location4.getText().toString().isEmpty()
                        || edt_event_location5.getText().toString().isEmpty()) {
                    result = false;
                }
                break;
        }
        return result;
    }
    public boolean validateLocationsTime() {
        boolean result = true;
        String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        switch(locationCount) {
            case 1:
                if(!edt_event_location_eta1.getText().toString().isEmpty()) {
                    result = edt_event_location_eta1.getText().toString().matches(regex);
                }
                if(!edt_event_location_etd1.getText().toString().isEmpty()) {
                    result = edt_event_location_etd1.getText().toString().matches(regex);
                }
                break;
            case 2:
                if(!edt_event_location_eta2.getText().toString().isEmpty()) {
                    result = edt_event_location_eta2.getText().toString().matches(regex);
                }
                if(!edt_event_location_etd2.getText().toString().isEmpty()) {
                    result = edt_event_location_etd2.getText().toString().matches(regex);
                }
                break;
            case 3:
                if(!edt_event_location_eta3.getText().toString().isEmpty()) {
                    result = edt_event_location_eta3.getText().toString().matches(regex);
                }
                if(!edt_event_location_etd3.getText().toString().isEmpty()) {
                    result = edt_event_location_etd3.getText().toString().matches(regex);
                }
                break;
            case 4:
                if(!edt_event_location_eta4.getText().toString().isEmpty()) {
                    result = edt_event_location_eta4.getText().toString().matches(regex);
                }
                if(!edt_event_location_etd4.getText().toString().isEmpty()) {
                    result = edt_event_location_etd4.getText().toString().matches(regex);
                }
                break;
            case 5:
                if(!edt_event_location_eta5.getText().toString().isEmpty()) {
                    result = edt_event_location_eta5.getText().toString().matches(regex);
                }
                if(!edt_event_location_etd5.getText().toString().isEmpty()) {
                    result = edt_event_location_etd5.getText().toString().matches(regex);
                }
                break;
        }
        return result;
    }
    public boolean addLocationValidation() {
        boolean result = true;
        switch(locationCount) {
            case 1:
                if(edt_event_location1.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please fill in the 1st location before adding a new", Toast.LENGTH_LONG).show();
                    result = false;
                }
                break;
            case 2:
                if(edt_event_location2.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please fill in the 2nd location before adding a new", Toast.LENGTH_LONG).show();
                    result = false;
                }
                break;
            case 3:
                if(edt_event_location3.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please fill in the 3rd location before adding a new", Toast.LENGTH_LONG).show();
                    result = false;
                }
                break;
            case 4:
                if(edt_event_location4.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please fill in the 4nd location before adding a new", Toast.LENGTH_LONG).show();
                    result = false;
                }
                break;
            case 5:
                Toast.makeText(this, "Locations are limited to 5", Toast.LENGTH_LONG).show();
                result = false;
                break;
        }
        return result;
    }

    // FUNCTIONS (add/delete locations)
    public void onAddField(View v) {
        boolean validation = addLocationValidation();
        if(validation == false) {
            return;
        }
        locationCount += 1;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.view_add_location_field, null);
        layout_locations.addView(rowView, layout_locations.getChildCount() - 2);
        if(locationCount == 2) {
            edt_event_location2 = rowView.findViewById(R.id.edt_event_loaction);
            edt_event_location_eta2 = rowView.findViewById(R.id.edt_event_loaction_eta);
            edt_event_location_etd2 = rowView.findViewById(R.id.edt_event_loaction_etd);
            btn_remove_location.show();
        }
        if(locationCount == 3) {
            edt_event_location3 = rowView.findViewById(R.id.edt_event_loaction);
            edt_event_location_eta3 = rowView.findViewById(R.id.edt_event_loaction_eta);
            edt_event_location_etd3 = rowView.findViewById(R.id.edt_event_loaction_etd);
        }
        if(locationCount == 4) {
            edt_event_location4 = rowView.findViewById(R.id.edt_event_loaction);
            edt_event_location_eta4 = rowView.findViewById(R.id.edt_event_loaction_eta);
            edt_event_location_etd4 = rowView.findViewById(R.id.edt_event_loaction_etd);
        }
        if(locationCount == 5) {
            edt_event_location5 = rowView.findViewById(R.id.edt_event_loaction);
            edt_event_location_eta5 = rowView.findViewById(R.id.edt_event_loaction_eta);
            edt_event_location_etd5 = rowView.findViewById(R.id.edt_event_loaction_etd);
        }
    }
    public void onDeleteField(View v) {
        switch (locationCount) {
            case 2:
                layout_locations.removeView((View) edt_event_location2.getParent());
                locationCount -= 1;
                btn_remove_location.hide();
                break;
            case 3:
                layout_locations.removeView((View) edt_event_location3.getParent());
                locationCount -= 1;
                break;
            case 4:
                layout_locations.removeView((View) edt_event_location4.getParent());
                locationCount -= 1;
                break;
            case 5:
                layout_locations.removeView((View) edt_event_location5.getParent());
                locationCount -= 1;
                break;
        }
    }

    // FUNCTIONS
    private void createEvent(String eventName, String createdBy, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int locationCount) {
        compositeDisposable.add(myAPI.createEvent(eventName, createdBy, eventStartDate, eventStartTime, eventEndDate, eventEndTime, locationCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("Event is created")) {
                            Toast.makeText(CreateEventActivity.this, ""+s, Toast.LENGTH_LONG).show();
                            Intent result = new Intent();
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else {
                            Toast.makeText(CreateEventActivity.this, "Error: cannot add an event", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }
    private void createEvent(String eventName, String createdBy, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int locationCount,
                             String eventLocation1, String eventLocationETA1, String eventLocationETD1) {
        compositeDisposable.add(myAPI.createEvent(eventName, createdBy, eventStartDate, eventStartTime, eventEndDate, eventEndTime, locationCount,
                eventLocation1, eventLocationETA1, eventLocationETD1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("Event is created")) {
                            Toast.makeText(CreateEventActivity.this, ""+s, Toast.LENGTH_LONG).show();
                            Intent result = new Intent();
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else {
                            Toast.makeText(CreateEventActivity.this, "Error: cannot add an event", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }
    private void createEvent(String eventName, String createdBy, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int locationCount,
                             String eventLocation1, String eventLocationETA1, String eventLocationETD1, String eventLocation2, String eventLocationETA2, String eventLocationETD2) {
        compositeDisposable.add(myAPI.createEvent(eventName, createdBy, eventStartDate, eventStartTime, eventEndDate, eventEndTime, locationCount,
                eventLocation1, eventLocationETA1, eventLocationETD1, eventLocation2, eventLocationETA2, eventLocationETD2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("Event is created")) {
                            Toast.makeText(CreateEventActivity.this, ""+s, Toast.LENGTH_LONG).show();
                            Intent result = new Intent();
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else {
                            Toast.makeText(CreateEventActivity.this, "Error: cannot add an event", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }
    private void createEvent(String eventName, String createdBy, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int locationCount,
                             String eventLocation1, String eventLocationETA1, String eventLocationETD1, String eventLocation2, String eventLocationETA2, String eventLocationETD2,
                             String eventLocation3, String eventLocationETA3, String eventLocationETD3) {
        compositeDisposable.add(myAPI.createEvent(eventName, createdBy, eventStartDate, eventStartTime, eventEndDate, eventEndTime, locationCount,
                eventLocation1, eventLocationETA1, eventLocationETD1, eventLocation2, eventLocationETA2, eventLocationETD2,
                eventLocation3, eventLocationETA3, eventLocationETD3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("Event is created")) {
                            Toast.makeText(CreateEventActivity.this, ""+s, Toast.LENGTH_LONG).show();
                            Intent result = new Intent();
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else {
                            Toast.makeText(CreateEventActivity.this, "Error: cannot add an event", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }
    private void createEvent(String eventName, String createdBy, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int locationCount,
                             String eventLocation1, String eventLocationETA1, String eventLocationETD1, String eventLocation2, String eventLocationETA2, String eventLocationETD2,
                             String eventLocation3, String eventLocationETA3, String eventLocationETD3, String eventLocation4, String eventLocationETA4, String eventLocationETD4) {
        compositeDisposable.add(myAPI.createEvent(eventName, createdBy, eventStartDate, eventStartTime, eventEndDate, eventEndTime, locationCount,
                eventLocation1, eventLocationETA1, eventLocationETD1, eventLocation2, eventLocationETA2, eventLocationETD2,
                eventLocation3, eventLocationETA3, eventLocationETD3, eventLocation4, eventLocationETA4, eventLocationETD4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("Event is created")) {
                            Toast.makeText(CreateEventActivity.this, ""+s, Toast.LENGTH_LONG).show();
                            Intent result = new Intent();
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else {
                            Toast.makeText(CreateEventActivity.this, "Error: cannot add an event", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }
    private void createEvent(String eventName, String createdBy, String eventStartDate, String eventStartTime, String eventEndDate, String eventEndTime, int locationCount,
                             String eventLocation1, String eventLocationETA1, String eventLocationETD1,
                             String eventLocation2, String eventLocationETA2, String eventLocationETD2,
                             String eventLocation3, String eventLocationETA3, String eventLocationETD3,
                             String eventLocation4, String eventLocationETA4, String eventLocationETD4,
                             String eventLocation5, String eventLocationETA5, String eventLocationETD5) {
        compositeDisposable.add(myAPI.createEvent(eventName, createdBy, eventStartDate, eventStartTime, eventEndDate, eventEndTime,
                locationCount, eventLocation1, eventLocationETA1, eventLocationETD1,
                eventLocation2, eventLocationETA2, eventLocationETD2,
                eventLocation3, eventLocationETA3, eventLocationETD3,
                eventLocation4, eventLocationETA4, eventLocationETD4,
                eventLocation5, eventLocationETA5, eventLocationETD5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("Event is created")) {
                            Toast.makeText(CreateEventActivity.this, ""+s, Toast.LENGTH_LONG).show();
                            Intent result = new Intent();
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        else {
                            Toast.makeText(CreateEventActivity.this, "Error: cannot add an event", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }
}
package com.example.eventbasedtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    TextInputEditText edt_email, edt_password;
    MaterialButton btn_register, btn_login;

    private boolean prefCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View
        btn_login = findViewById(R.id.btn_join);
        btn_register = findViewById(R.id.btn_join);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        // Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        prefCheck = checkSharedPreferences();
        if(prefCheck == true) {
            Intent upcomingEvents = new Intent(MainActivity.this, MyUpcomingEventActivity.class);
            finishAffinity();
            startActivity(upcomingEvents);
        }
    }

    // FUNCTIONS
    public boolean checkSharedPreferences() {
        String id = sharedPreferences.getString(getString(R.string.sp_user_id), "");
        String name = sharedPreferences.getString(getString(R.string.sp_user_name), "");
        String email = sharedPreferences.getString(getString(R.string.sp_user_email), "");
        if(!id.isEmpty() && !name.isEmpty() || !email.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    // EVENTS LISTENER
    public void gotoLogin(View view) {
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
    }

    public void gotoRegister(View view) {
        Intent register = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(register);
    }
}

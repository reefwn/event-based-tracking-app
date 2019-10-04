package com.example.eventbasedtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.eventbasedtracking.Retrofit.API;
import com.example.eventbasedtracking.Retrofit.APIClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    API myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    TextInputEditText edt_email, edt_password;
    MaterialButton btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initial API
        Retrofit retrofit = APIClient.getInstance("scalar");
        myAPI = retrofit.create(API.class);

        // View
        btn_login = findViewById(R.id.btn_join);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        // Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    // FUNCTIONS
    private void loginUser(String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("unique_id") || s.contains("_id")) {
                            JSONObject jsonObject = new JSONObject(s);
                            storeUserInfo(jsonObject.getString("_id"), jsonObject.getString("name"), jsonObject.getString("email"));
                            Intent myEvent = new Intent(LoginActivity.this, MyUpcomingEventActivity.class);
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                            finishAffinity();
                            startActivity(myEvent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, ""+s, Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }

    private void storeUserInfo(String id, String name, String email) {
        sharedPreferencesEditor.putString(getString(R.string.sp_user_id), id);
        sharedPreferencesEditor.putString(getString(R.string.sp_user_name), name);
        sharedPreferencesEditor.putString(getString(R.string.sp_user_email), email);
        sharedPreferencesEditor.commit();
    }

    // EVENT LISTENER
    public void loginClick(View view) {
        if(edt_email.getText().toString().isEmpty() || edt_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_LONG).show();
            return;
        }
        loginUser(edt_email.getText().toString(), edt_password.getText().toString());
    }
}

package com.example.eventbasedtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.eventbasedtracking.Retrofit.API;
import com.example.eventbasedtracking.Retrofit.APIClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    API myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    TextInputEditText edt_name, edt_email, edt_password, edt_confirm_password;
    MaterialButton btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initial API
        Retrofit retrofit = APIClient.getInstance("scalar");
        myAPI = retrofit.create(API.class);

        // View
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_register = findViewById(R.id.btn_add_event);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    // FUNCTIONS
    private void registerUser(String name, String email, final String password) {
        compositeDisposable.add(myAPI.registerUser(email, name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("Register success")) {
                            Toast.makeText(RegisterActivity.this, ""+s, Toast.LENGTH_LONG).show();
                            RegisterActivity.this.finish();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, ""+s, Toast.LENGTH_LONG).show();
                        }
                    }
                })
        );
    }

    // VALIDATIONS
    private boolean validateName(String name) {
        boolean result = true;
        int nameLength = name.length();
        if(nameLength <= 3 || name.isEmpty()) {
            result = false;
        }
        return result;
    }

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private boolean validatePassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }

    private boolean validatePasswordMatch(String password, String confirm_password) {
        boolean result = false;
        if(password.equals(confirm_password)) {
            result = true;
        }
        return result;
    }

    // EVENTS LISTENER
    public void registerClick(View view) {
        boolean isNameValid = validateName(edt_name.getText().toString());
        if(isNameValid == false) {
            Toast.makeText(this, "Name must longer than 3 characters", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isEmailValid = validateEmail(edt_email.getText().toString());
        if(isEmailValid == false) {
            Toast.makeText(this, "Email is invalid", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isPasswordValid = validatePassword(edt_password.getText().toString());
        if(isPasswordValid == false) {
            Toast.makeText(this, "Password has to be 8 characters long and contains characters and number", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isPasswordMatch = validatePasswordMatch(edt_password.getText().toString(), edt_confirm_password.getText().toString());
        if(isPasswordMatch == false) {
            Toast.makeText(this, "Passwords are not matched", Toast.LENGTH_LONG).show();
            return;
        }
        registerUser(edt_name.getText().toString(), edt_email.getText().toString(), edt_password.getText().toString());
    }
}

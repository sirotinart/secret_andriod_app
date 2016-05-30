package com.nullsoft.art.kuponchikru;

/**
 * Created by art on 01.04.16.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_last_name) EditText _lastNameText;
    @Bind(R.id.input_city) EditText _cityText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_password_repeat) EditText _passwordRepeatText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    private ProgressDialog progressDialog;

    public SignupActivity()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        loadCityList();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Ошибка регистрации");
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String lastName =_lastNameText.getText().toString();
        String city = _cityText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        SignupMsgHandler handler=new SignupMsgHandler(this);

        UserController.getController().create(email, name, lastName, city, password, handler);

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        progressDialog.dismiss();
        finish();
    }

    public void onSignupFailed(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String name = _nameText.getText().toString();
        String lastName=_lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String passwordRepeat =_passwordRepeatText.getText().toString();
        String city = _cityText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Имя должно быть длиннее 3 символов");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("Фамилия должна быть длиннее 3 символов");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Введите корректный e-mail");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Пароль должен быть длиной от 4 до 10 символов");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if(passwordRepeat.isEmpty() || !passwordRepeat.equals(password)) {
            _passwordRepeatText.setError("Пароли не совпадают");
            valid = false;
        } else {
            _passwordRepeatText.setError(null);
        }

        if(city.isEmpty()) {
            _cityText.setError("Введите город");
            valid = false;
        } else {
           _cityText.setError(null);
        }

        return valid;
    }

    public void loadCityList()  {
        ServerApi.CitiesList cityListService = RetrofitManager.getRetrofit().create(ServerApi.CitiesList.class);
        Call<ServerApi.CityList> call = cityListService.getCities();

        call.enqueue(new Callback<ServerApi.CityList>() {
            @Override
            public void onResponse(Call<ServerApi.CityList> call, Response<ServerApi.CityList> response) {
                AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.input_city);

                String[] cities = {};
                cities = response.body().cities.toArray(cities);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.city_list_item_profile, cities);

                try {
                    tv.setAdapter(adapter);
                } catch (NullPointerException e) {
                    Log.d("loadCityList() error:", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ServerApi.CityList> call, Throwable t) {
                Log.d("loadCityList() error:", "cannot get list of cities from server (network error)");
                Log.d("loadCityList() error:", t.getMessage());
            }
        });
    }
}
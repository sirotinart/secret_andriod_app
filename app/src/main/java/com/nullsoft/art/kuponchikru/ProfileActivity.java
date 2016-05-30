package com.nullsoft.art.kuponchikru;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by art on 03.04.16.
 */
public class ProfileActivity extends AppCompatActivity
{

    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_name) EditText firstNameText;
    @Bind(R.id.input_last_name) EditText lastNameText;
    @Bind(R.id.input_city) EditText cityText;
    @Bind(R.id.input_new_password) EditText newPasswordText;
    @Bind(R.id.input_new_password_repeat) EditText newPasswordRepeatText;
    @Bind(R.id.input_password) EditText passwordText;

    @Bind(R.id.btn_update) Button updateBtn;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.profile_screen_name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadCityList();

        User user=UserController.getController().get();

        emailText.setText(user.LOGIN);
        firstNameText.setText(user.FIRST_NAME);
        lastNameText.setText(user.LAST_NAME);
        cityText.setText(user.ADDRESS);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public User validate() {

        boolean valid = true;

        String name = firstNameText.getText().toString();
        String lastName=lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password=passwordText.getText().toString();
        String newPassword = newPasswordText.getText().toString();
        String newPasswordRepeat =newPasswordRepeatText.getText().toString();
        String city = cityText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            firstNameText.setError("Имя должно быть длиннее 3 символов");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            lastNameText.setError("Фамилия должна быть длиннее 3 символов");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Введите корректный e-mail");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (!newPassword.isEmpty() && (newPassword.length() < 4 || newPassword.length() > 10)) {
            newPasswordText.setError("Пароль должен быть длиной от 4 до 10 символов");
            valid = false;
        } else {
            newPasswordText.setError(null);
        }

        if(!newPasswordRepeat.isEmpty() && !newPasswordRepeat.equals(newPassword)) {
            newPasswordRepeatText.setError("Пароли не совпадают");
            valid = false;
        } else {
            newPasswordRepeatText.setError(null);
        }

        if(newPassword.isEmpty())
        {
            newPassword=password;
        }

        if(city.isEmpty()) {
            cityText.setError("Введите город");
            valid = false;
        } else {
            cityText.setError(null);
        }

        if(password.isEmpty()) {
            passwordText.setError("Введите текущий пароль");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if(valid)
        {
            return new User(0, email, newPassword, city, name, lastName);
        }
        return null;
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

    public void updateProfile()
    {
        User user=validate();
        if(user!=null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Обновление данных...");
            progressDialog.show();
            UserController.getController().update(user, passwordText.getText().toString(), new ProfileMsgHandler(this));
        }
    }

    public void updateProfileComplete(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }
}

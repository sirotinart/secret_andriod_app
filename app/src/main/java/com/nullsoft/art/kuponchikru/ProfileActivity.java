package com.nullsoft.art.kuponchikru;

import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.profile_screen_name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UserController.User user=UserController.getController().get();

        emailText.setText(user.LOGIN);
        firstNameText.setText(user.FIRST_NAME);
        lastNameText.setText(user.LAST_NAME);
        cityText.setText(user.ADDRESS);

        updateBtn.setOnClickListener(new UpdateBtnClickListener());
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




    class UpdateBtnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            UserController.User user=validate();
            if(user!=null)
            {
                UserController.getController().update(user, passwordText.getText().toString());
            }
        }
    }


    public UserController.User validate() {

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
            return new UserController.User(0, email, newPassword, city, name, lastName);
        }
        return null;
    }

}

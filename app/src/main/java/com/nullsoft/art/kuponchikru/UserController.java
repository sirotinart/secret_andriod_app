package com.nullsoft.art.kuponchikru;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by art on 24.04.16.
 */
public class UserController
{
    public static class User
    {
        public int USER_ID;
        public String LOGIN;
        public String PASSWORD;
        public String ADDRESS;
        public String FIRST_NAME;
        public String LAST_NAME;

        public User(int id, String login, String password, String address, String firstName, String lastName)
        {
            this.LOGIN=login;
            this.PASSWORD=password;
            this.ADDRESS=address;
            this.FIRST_NAME=firstName;
            this.LAST_NAME=lastName;
            this.USER_ID=id;
        }

    }

    private static UserController controller=new UserController();

    private UserController(){};

    public static UserController getController()
    {
        return controller;
    }

    public User get()
    {
        Cursor c=Database.getDb().query("user_info_table",null,null,null,null,null, null);

        if(c!=null && c.moveToFirst())
        {
            int id=c.getInt(c.getColumnIndex("_id"));
            String login=c.getString(c.getColumnIndex("login"));
            String password=c.getString(c.getColumnIndex("password"));
            String address=c.getString(c.getColumnIndex("address"));
            String firstName=c.getString(c.getColumnIndex("first_name"));
            String lastName=c.getString(c.getColumnIndex("last_name"));
            c.close();

            return new User(id, login, password, address, firstName, lastName);
        }

        return null;
    }

    public void insert(User user)
    {
        Database.getDb().delete("user_info_table", null,null);

        ContentValues values=new ContentValues();
        values.put("_id", user.USER_ID);
        values.put("login", user.LOGIN);
        values.put("password", user.PASSWORD);
        values.put("address", user.ADDRESS);
        values.put("first_name", user.FIRST_NAME);
        values.put("last_name", user.LAST_NAME);
        long insertId=Database.getDb().insert("user_info_table", null, values);

        if(insertId==-1)
        {
            Log.d("database error", "insertUserToDb: error while inserting new user");
        }
    }

    public void update(User newUser, String password)
    {
        User currentUser=get();

        newUser.USER_ID=currentUser.USER_ID;

        if(!password.equals(currentUser.PASSWORD))
        {
            Toast.makeText(Kuponchikru.getAppContext(),"Неверный текущий пароль", Toast.LENGTH_LONG).show();
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialog(Kuponchikru.getAppContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Обновление данных...");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.100:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ServerApi.UserInfoUpdateService service = retrofit.create(ServerApi.UserInfoUpdateService.class);

            Call<ServerApi.ServerResponse> call = service.updateUser(newUser.USER_ID, newUser.LOGIN, newUser.FIRST_NAME, newUser.LAST_NAME, newUser.ADDRESS, currentUser.PASSWORD, newUser.PASSWORD);
            call.enqueue(new Callback<ServerApi.ServerResponse>() {

                @Override
                public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                    Log.d("update() error:",t.getMessage());
                    progressDialog.dismiss();

                }

                @Override
                public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                    if(response.raw().code()!=200)
                    {

                    }

                    if(response.raw().code()==200 && response.body().success==false)
                    {
                        if(response.body().errorText!=null)
                        {

                        }
                    }

                    if(response.raw().code()==200 && response.body().success==true)
                    {

                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

}

package com.nullsoft.art.kuponchikru;


import android.database.Cursor;
import android.os.Message;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by art on 24.04.16.
 */
public class UserController
{


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
            User user=new User(c);

            c.close();

            return user;
        }

        return null;
    }

    public void insert(User user)
    {
        User user1=get();
        if(user1!=null && !user.LOGIN.equals(user1.LOGIN))
        {
            CouponController.getInstance().clearUserCache();
            CouponController.getInstance().clearFavoritesCache();

        }

        Database.getDb().delete("user_info_table", null, null);

        long insertId=Database.getDb().insert("user_info_table", null, user.toContentValues());

        if(insertId==-1)
        {
            Log.d("database error", "insertUserToDb: error while inserting new user");
        }
    }

    public void update(final User newUser, String password,final ProfileMsgHandler handler)
    {
        final Message message=Message.obtain(handler,0);

        User currentUser=get();

        newUser.USER_ID=currentUser.USER_ID;

        if(!password.equals(currentUser.PASSWORD))
        {
            message.obj="Неверный текущий пароль";
            handler.sendMessage(message);
        }
        else
        {
            Call<ServerApi.ServerResponse> call = ServerApi.getUserService().update(newUser.USER_ID, newUser.LOGIN, newUser.FIRST_NAME, newUser.LAST_NAME, newUser.ADDRESS, currentUser.PASSWORD, newUser.PASSWORD);
            call.enqueue(new Callback<ServerApi.ServerResponse>() {

                @Override
                public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                    Log.d("update() error:",t.getMessage());
                    message.obj=t.getMessage();
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                    if(response.raw().code()!=200)
                    {
                        Log.d("update() error:", "unknown error");
                        message.obj="Ошибка";
                    }

                    if(response.raw().code()==200 && !response.body().success)
                    {
                        if(response.body().errorText!=null)
                        {
                            Log.d("update() error:", response.body().errorText);
                            message.obj=response.body().errorText;
                        }
                        else
                        {
                            message.obj="Ошибка";
                        }
                    }

                    if(response.raw().code()==200 && response.body().success)
                    {
                        insert(newUser);
                        message.obj="Информация обновлена";
                    }

                    handler.sendMessage(message);
                }
            });
        }
    }

    public void create(String login, String firstName, String lastName, String city, String password, final SignupMsgHandler handler)
    {
        Call<ServerApi.ServerResponse> call = ServerApi.getUserService().register(login, firstName, lastName, city, password);

        final Message message=Message.obtain(handler,0);

        call.enqueue(new Callback<ServerApi.ServerResponse>() {


            @Override
            public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                Log.d("signup() error:",t.getMessage());

                message.obj="Ошибка регистрации";
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                if(response.raw().code()!=200)
                {
                    message.obj="Ошибка регистрации";
                    handler.sendMessage(message);
                }

                if(response.raw().code()==200 && !response.body().success)
                {
                    if(response.body().errorText!=null && response.body().errorText.length()!=0)
                    {
                        message.obj=response.body().errorText;
                        handler.sendMessage(message);

                    }
                    else
                    {
                        message.obj="Ошибка регистрации";
                        handler.sendMessage(message);
                    }
                }

                if(response.raw().code()==200 && response.body().success)
                {
                    message.obj=null;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public void logout()
    {
        Call<ServerApi.ServerResponse> call=ServerApi.getUserService().logout();
        call.enqueue(new Callback<ServerApi.ServerResponse>() {

            @Override
            public void onResponse(Call<ServerApi.ServerResponse> call, Response<ServerApi.ServerResponse> response) {
                if(response.body()!=null)
                {
                    if(response.body().success)
                    {
                        Database.getDb().delete("user_info_table", null, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerApi.ServerResponse> call, Throwable t) {
                Log.d("logout() error:", t.getMessage());
            }
        });
    }

    public String getUserName()
    {
        User user=get();

        return user.FIRST_NAME+' '+user.LAST_NAME;
    }


}

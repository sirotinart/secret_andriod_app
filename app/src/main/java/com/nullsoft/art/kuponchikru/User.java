package com.nullsoft.art.kuponchikru;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by art on 28.04.16.
 */
public class User
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

    public User(Cursor c)
    {
        USER_ID=c.getInt(c.getColumnIndex("USER_ID"));
        LOGIN=c.getString(c.getColumnIndex("LOGIN"));
        PASSWORD=c.getString(c.getColumnIndex("PASSWORD"));
        ADDRESS=c.getString(c.getColumnIndex("ADDRESS"));
        FIRST_NAME=c.getString(c.getColumnIndex("FIRST_NAME"));
        LAST_NAME=c.getString(c.getColumnIndex("LAST_NAME"));
    }

    public ContentValues toContentValues()
    {
        ContentValues cv=new ContentValues();

        cv.put("LOGIN", LOGIN);
        cv.put("PASSWORD", PASSWORD);
        cv.put("ADDRESS", ADDRESS);
        cv.put("FIRST_NAME", FIRST_NAME);
        cv.put("LAST_NAME", LAST_NAME);
        cv.put("USER_ID", USER_ID);

        return cv;
    }
}
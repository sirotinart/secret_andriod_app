package com.nullsoft.art.kuponchikru;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by art on 24.04.16.
 */
public class Database extends SQLiteOpenHelper
{
//    private static final String USER_INFO_TABLE="user_info";

    private static final int DATABASE_VERSION=1;

    private static Database db=new Database(Kuponchikru.getAppContext(),"kuponckik_db", null, DATABASE_VERSION);

    private Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDb()
    {
        return db.getReadableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table user_info_table (_id integer primary key, login text, password text, address text, first_name text, last_name text);");
        sqLiteDatabase.execSQL("create table user_coupons (_id integer primary key, unique_code text, name text, description text, " +
                "sell_date text, exp_date text, seller_name text, shop_address_list text, seller_id integer, price integer, full_price integer, discount integer);");
        sqLiteDatabase.execSQL("create table coupons_cache (_id integer primary key, name text, description text, " +
                "crt_date text, exp_date text, seller_name text, shop_address_list text, seller_id integer, price integer, full_price integer, discount integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exist user_info_table");
        onCreate(sqLiteDatabase);
    }



}

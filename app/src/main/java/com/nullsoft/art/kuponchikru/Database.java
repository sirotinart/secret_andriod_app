package com.nullsoft.art.kuponchikru;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by art on 24.04.16.
 */
public class Database extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION=18;

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
        sqLiteDatabase.execSQL("create table user_info_table (USER_ID integer primary key, LOGIN text, PASSWORD text, ADDRESS text, FIRST_NAME text, LAST_NAME text);");
        sqLiteDatabase.execSQL("create table user_coupons (COUPON_ID integer, DESCRIPTION text, DATE text, " +
                "CREATION_DATE text, EXPIRATION_DATE text, NAME text, SHOP_ADDRESS_LIST text, SUPPLIER_ID integer, PRICE integer, " +
                "FULL_PRICE integer, DISCOUNT integer, COUNT integer, SHORT_DESCRIPTION text, CATEGORY text, CITY text, CODE text primary key);");
        sqLiteDatabase.execSQL("create table coupons_cache (COUPON_ID integer primary key, DESCRIPTION text, " +
                "CREATION_DATE text, EXPIRATION_DATE text, NAME text, SHOP_ADDRESS_LIST text, SUPPLIER_ID integer, PRICE integer, " +
                "FULL_PRICE integer, DISCOUNT integer, COUNT integer, SHORT_DESCRIPTION text, CATEGORY text, CITY text);");
        sqLiteDatabase.execSQL("create table user_favorites (COUPON_ID integer primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists user_info_table");
        sqLiteDatabase.execSQL("drop table if exists user_coupons");
        sqLiteDatabase.execSQL("drop table if exists coupons_cache");
        sqLiteDatabase.execSQL("drop table if exists user_favorites");
        onCreate(sqLiteDatabase);
    }



}

package com.nullsoft.art.kuponchikru;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by art on 22.05.16.
 */
public class CouponWithCode extends Coupon
{
    public String CODE;
    public String DATE;

    public CouponWithCode(Cursor c)
    {
        super(c);

        CODE=c.getString(c.getColumnIndex("CODE"));
        DATE=c.getString(c.getColumnIndex("DATE"));
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv=super.toContentValues();

        cv.put("CODE", CODE);
        cv.put("DATE", DATE);

        return cv;
    }


}

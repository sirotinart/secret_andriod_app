package com.nullsoft.art.kuponchikru;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by art on 29.04.16.
 */
public class Coupon
{
    public int COUPON_ID;
    public int PRICE;
    public String CREATION_DATE;
    public String EXPIRATION_DATE;
    public int SUPPLIER_ID;
    public String DESCRIPTION;
    public int FULL_PRICE;
    public int DISCOUNT;
    public String SHORT_DESCRIPTION;
    public int COUNT;
    public String SHOP_ADDRESS_LIST;
    public String NAME;
    public String COUPON_IMAGE_URL;
    public String SELLER_IMAGE_URL;
    public String CATEGORY;
    public String CITY;

    public Coupon(Cursor c)
    {
        DESCRIPTION=c.getString(c.getColumnIndex("DESCRIPTION"));
        COUPON_ID=c.getInt(c.getColumnIndex("COUPON_ID"));
        COUNT=c.getInt(c.getColumnIndex("COUNT"));
        SHORT_DESCRIPTION=c.getString(c.getColumnIndex("SHORT_DESCRIPTION"));

        String tmp=c.getString(c.getColumnIndex("CREATION_DATE"));
        String[] date=tmp.split("-");
        CREATION_DATE=date[2]+"."+date[1]+"."+date[0];

        tmp=c.getString(c.getColumnIndex("EXPIRATION_DATE"));
        date=tmp.split("-");
        EXPIRATION_DATE=date[2]+"."+date[1]+"."+date[0];

        SHOP_ADDRESS_LIST=c.getString(c.getColumnIndex("SHOP_ADDRESS_LIST"));
        SUPPLIER_ID=c.getInt(c.getColumnIndex("SUPPLIER_ID"));
        PRICE=c.getInt(c.getColumnIndex("PRICE"));
        FULL_PRICE=c.getInt(c.getColumnIndex("FULL_PRICE"));
        NAME=c.getString(c.getColumnIndex("NAME"));
        DISCOUNT=c.getInt(c.getColumnIndex("DISCOUNT"));
        CATEGORY=c.getString(c.getColumnIndex("CATEGORY"));
        CITY=c.getString(c.getColumnIndex("CITY"));

        COUPON_IMAGE_URL=String.format(Kuponchikru.getAppContext().getString(R.string.coupon_img_url),SUPPLIER_ID, COUPON_ID);
        SELLER_IMAGE_URL=String.format(Kuponchikru.getAppContext().getString(R.string.seller_img_url),SUPPLIER_ID);
    }

    public ContentValues toContentValues()
    {
        ContentValues cv=new ContentValues();

        cv.put("COUPON_ID", COUPON_ID);
        cv.put("PRICE", PRICE);
        cv.put("CREATION_DATE", CREATION_DATE);
        cv.put("EXPIRATION_DATE", EXPIRATION_DATE);
        cv.put("SUPPLIER_ID", SUPPLIER_ID);
        cv.put("DESCRIPTION", DESCRIPTION);
        cv.put("FULL_PRICE", FULL_PRICE);
        cv.put("DISCOUNT", DISCOUNT);
        cv.put("SHORT_DESCRIPTION", SHORT_DESCRIPTION);
        cv.put("COUNT", COUNT);
        cv.put("SHOP_ADDRESS_LIST", SHOP_ADDRESS_LIST);
        cv.put("NAME", NAME);
        cv.put("CATEGORY", CATEGORY);
        cv.put("CITY", CITY);

        return cv;
    }

};

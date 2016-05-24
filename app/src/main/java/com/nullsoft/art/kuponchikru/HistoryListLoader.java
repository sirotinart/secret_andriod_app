package com.nullsoft.art.kuponchikru;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by art on 18.05.16.
 */
public class HistoryListLoader extends CouponListLoader
{
    public HistoryListLoader(Context context) {
        super(context);
        DataUpdateReceiver.getInstance().setLoader(this);
    }

    @Override
    public ArrayList<Coupon> loadInBackground() {
        ArrayList<Coupon> data=CouponController.getInstance().getHistoryListFromDb();
        return data;
    }
}

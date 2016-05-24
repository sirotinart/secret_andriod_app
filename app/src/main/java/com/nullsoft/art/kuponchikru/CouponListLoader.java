package com.nullsoft.art.kuponchikru;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by art on 29.04.16.
 */
public class CouponListLoader extends AsyncTaskLoader<ArrayList<Coupon>>
{

    public CouponListLoader(Context context)
    {
        super(context);
        DataUpdateReceiver.getInstance().setLoader(this);
    }

    @Override
    public void deliverResult(ArrayList<Coupon> data) {
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public ArrayList<Coupon> loadInBackground() {
        ArrayList<Coupon> data=CouponController.getInstance().getCouponsListFromDb();
        return data;
    }
}

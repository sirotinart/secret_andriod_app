package com.nullsoft.art.kuponchikru;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by art on 10.05.16.
 */
public class DataUpdateReceiver extends Handler
{
    CouponListLoader loader;

    private static final DataUpdateReceiver instance=new DataUpdateReceiver();

    private DataUpdateReceiver(){}

    public static DataUpdateReceiver getInstance()
    {
        return instance;
    }

    public void setLoader(CouponListLoader loader)
    {
        this.loader=loader;
    }

    @Override
    public void handleMessage(Message msg) {
        loader.onContentChanged();
    }
}

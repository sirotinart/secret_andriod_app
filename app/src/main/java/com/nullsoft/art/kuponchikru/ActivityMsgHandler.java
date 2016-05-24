package com.nullsoft.art.kuponchikru;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by art on 23.05.16.
 */
public class ActivityMsgHandler extends Handler
{
    private AppCompatActivity activity=null;

    private static final ActivityMsgHandler instance=new ActivityMsgHandler();

    private ActivityMsgHandler(){}

    public static ActivityMsgHandler getInstance(){return instance;}

    public void setActivity(AppCompatActivity activity)
    {
        this.activity=activity;
    }

    @Override
    public void handleMessage(Message msg) {
        if(activity!=null)
        {
            if(activity instanceof CouponActivity)
            {
                CouponActivity a=(CouponActivity)activity;
                a.handleMessage(msg);
            }
            if(activity instanceof MapActivity)
            {
                MapActivity a=(MapActivity)activity;
                a.handleMessage(msg);
            }
        }

    }
}

package com.nullsoft.art.kuponchikru;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * Created by art on 25.04.16.
 */
public class SignupMsgHandler extends Handler
{
    protected final WeakReference<AppCompatActivity> activity;

    public SignupMsgHandler(AppCompatActivity activity)
    {
        this.activity=new WeakReference<AppCompatActivity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        SignupActivity a=(SignupActivity) activity.get();
        if(a!=null)
        {
            if(msg.obj!=null)
            {
                a.onSignupFailed(msg.obj.toString());
            }
            else
            {
                a.onSignupSuccess();
            }
        }
    }
}

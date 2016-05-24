package com.nullsoft.art.kuponchikru;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by art on 25.04.16.
 */
public class ProfileMsgHandler extends SignupMsgHandler
{

    public ProfileMsgHandler(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        ProfileActivity a=(ProfileActivity) activity.get();
        if(a!=null)
        {
            if(msg.obj.toString()!=null)
            {
                a.updateProfileComplete(msg.obj.toString());
            }
        }
    }
}

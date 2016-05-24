package com.nullsoft.art.kuponchikru;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by art on 03.04.16.
 */
public class FragmentHistory extends FragmentMain
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar=(Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.history_screen_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CouponController.getInstance().clearUserCache();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<ArrayList<Coupon>> onCreateLoader(int id, Bundle args) {
        return new HistoryListLoader(getActivity());
    }


    @Override
    protected void openCouponActivity(Object object) {
        String couponCode=(String)object;

        Intent intent=new Intent(getContext(), CodeCouponActivity.class);
        intent.putExtra("unique_code", couponCode);
        startActivity(intent);
    }

    @Override
    public void loadData() {
        CouponController.getInstance().loadUserCoupons();
    }

    @Override
    protected CouponListItemAdapter getAdapter() {
        return new HistoryListItemAdapter(new MHandler());
    }


}

package com.nullsoft.art.kuponchikru;

//import android.app.Fragment;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by art on 02.04.16.
 */
public class FragmentMain extends Fragment implements PopupMenu.OnMenuItemClickListener, LoaderCallbacks<ArrayList<Coupon>>
{
    protected CouponListItemAdapter mAdapter;
    protected ProgressBar progressBar;
    protected RecyclerView mRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatActivity parent= (AppCompatActivity)getActivity();
        ActionBar toolbar=parent.getSupportActionBar();
        toolbar.setTitle(R.string.main_screen_name);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        CouponController.getInstance().clearCache();

        View layout=inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_XLARGE)
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else
        {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        mAdapter=getAdapter();

        mRecyclerView.setAdapter(mAdapter);

        loadData();

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar=(ProgressBar) getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public Loader<ArrayList<Coupon>> onCreateLoader(int id, Bundle args) {
        return new CouponListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Coupon>> loader, ArrayList<Coupon> data) {
        mAdapter.swapData(data);
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onLoaderReset(Loader<ArrayList<Coupon>> loader) {
        mAdapter.swapData(null);
    }

    protected void showProgressBar()
    {
        progressBar=(ProgressBar) getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void openCouponActivity(Object object)
    {
        int couponId=(int)object;

        Intent intent=new Intent(getContext(), CouponActivity.class);
        intent.putExtra("coupon_id", couponId);
        startActivity(intent);
    }

    public void loadData()
    {
        CouponController.getInstance().loadCoupons();
    }

    protected CouponListItemAdapter getAdapter()
    {
        return new CouponListItemAdapter(new MHandler());
    }

    class MHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                {
                    showProgressBar();
                    break;
                }
                case 1:
                {
                    openCouponActivity(msg.obj);
                    break;
                }
            }
        }
    }


}

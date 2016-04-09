package com.nullsoft.art.kuponchikru;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by art on 03.04.16.
 */
public class FragmentHistory extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);

        View layout=inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> myDataset = getDataSet();

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return layout;
    }


    private ArrayList<String> getDataSet() {

        ArrayList<String> mDataSet = new ArrayList();

        for (int i = 0; i < 100; i++) {
            mDataSet.add(i, "item" + i);
        }
        mDataSet.add(mDataSet.size(), "пункт" + mDataSet.size() + 1);
        return mDataSet;
    }
}

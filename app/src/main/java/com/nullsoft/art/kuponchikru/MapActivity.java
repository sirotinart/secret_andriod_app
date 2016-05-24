package com.nullsoft.art.kuponchikru;

import android.app.FragmentTransaction;
import android.bluetooth.le.ScanRecord;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by art on 14.05.16.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private int couponId;
    Coupon coupon;
    MapFragment mMapFragment;
    GoogleMap map=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.shops_list));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        couponId=getIntent().getExtras().getInt("coupon_id");

        AsyncTask load=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                CouponController.getInstance().getCoupon(couponId);
                return null;
            }
        };

        load.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityMsgHandler.getInstance().setActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        this.map=map;

        if(coupon!=null)
        {
            loadMarkers();
        }
    }

    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case 0:
            {
                coupon=(Coupon) msg.obj;
                if(coupon!=null)
                {
                    createMap();
                }
                break;
            }
        }
    }

    void loadMarkers()
    {
        ListView textView=(ListView) findViewById(R.id.adress_list);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels/3;

        LatLngBounds bounds=null;
        ArrayList<LatLng> startBounds = new ArrayList<LatLng>();

        if(coupon!=null)
        {
            String[] address=coupon.SHOP_ADDRESS_LIST.split("\r\n");
            ArrayAdapter<String> listAdapter=new ArrayAdapter<String>(this, R.layout.adress_list_item, address);
            textView.setAdapter(listAdapter);

            for (int i=0; i<address.length; i++)
            {

                List<Address> coordinates=null;
                Geocoder geocoder=new Geocoder(this);
                try {
                    coordinates=geocoder.getFromLocationName(address[i], 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                if(coordinates!=null && coordinates.size()!=0)
                {
                    LatLng marker = new LatLng(coordinates.get(0).getLatitude(), coordinates.get(0).getLongitude());
                    map.addMarker(new MarkerOptions().position(marker).title(address[i]));
                    if(address.length==1)
                    {
                        map.moveCamera(CameraUpdateFactory.zoomTo(13));
                        map.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    }

                    if(i<2)
                    {
                        startBounds.add(marker);
                        if(i==1)
                        {
                            try {
                                bounds=new LatLngBounds(startBounds.get(0), startBounds.get(1));
                            }
                            catch (IllegalArgumentException e){
                                bounds=new LatLngBounds(startBounds.get(1), startBounds.get(0));
                            }
                        }
                    }
                    else
                    {
                        try {
                            bounds=bounds.including(marker);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if(bounds!=null)
        {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,width,height,Kuponchikru.dpToPx(50)));
        }

        ProgressBar progress=(ProgressBar)findViewById(R.id.progressBar2);
        progress.setVisibility(View.INVISIBLE);

        FrameLayout layout=(FrameLayout)findViewById(R.id.fragment_container);
        layout.setVisibility(View.VISIBLE);

        CardView view=(CardView)findViewById(R.id.address_view);
        view.setVisibility(View.VISIBLE);

    }

    protected void createMap()
    {
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
    }
}

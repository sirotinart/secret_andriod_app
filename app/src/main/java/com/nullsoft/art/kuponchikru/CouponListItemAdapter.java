package com.nullsoft.art.kuponchikru;

/**
 * Created by art on 06.04.16.
 */
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class CouponListItemAdapter extends RecyclerView.Adapter<CouponListItemAdapter.ViewHolder>
{

    protected ArrayList<Coupon> mDataset;
    private boolean endOfList=false;
    protected final Context context;
    protected Handler handler;

    public CouponListItemAdapter(Handler handler) {
        context = Kuponchikru.getAppContext();
        this.handler=handler;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_description;
        public TextView item_discount;
        public TextView item_price;
        public TextView item_full_price;
        public ImageView card_img;
        public ImageButton add_to_favorites_btn;

        public ViewHolder(View v) {
            super(v);
            item_description = (TextView) v.findViewById(R.id.item_description);
            item_discount = (TextView) v.findViewById(R.id.item_discount);
            item_price = (TextView) v.findViewById(R.id.item_price);
            item_full_price = (TextView) v.findViewById(R.id.item_full_price);
            card_img = (ImageView) v.findViewById(R.id.card_img);
            item_description = (TextView) v.findViewById(R.id.item_description);
            add_to_favorites_btn = (ImageButton) v.findViewById(R.id.add_to_favorites_btn);
        }
    }

    @Override
    public CouponListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message m=new Message();
                m.what=1;
                m.obj=view.getTag();
                handler.sendMessage(m);
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.item_description.setText(mDataset.get(position).SHORT_DESCRIPTION);
        holder.item_discount.setText(String.format(context.getString(R.string.discount),mDataset.get(position).DISCOUNT));
        holder.item_price.setText(String.format(context.getString(R.string.price),mDataset.get(position).PRICE));
        holder.item_full_price.setText(String.format(context.getString(R.string.price),mDataset.get(position).FULL_PRICE));
        Picasso.with(context).load(mDataset.get(position).COUPON_IMAGE_URL)
                .into(holder.card_img);
        holder.itemView.setTag(mDataset.get(position).COUPON_ID);

        holder.add_to_favorites_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(view.getContext(),view);
                popup.inflate(R.menu.add_to_favorite);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.add_to_favorites:
                            {
                                CouponController.getInstance().saveToFavorites(mDataset.get(position).COUPON_ID);
                                return true;
                            }
                            default:
                            {
                                return false;
                            }
                        }
                    }
                });
                popup.show();
            }
        });

        if(position==mDataset.size()-2 && !endOfList)
        {
            endOfList=true;
            CouponController.getInstance().loadCoupons();
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataset!=null)
        {
            return mDataset.size();
        }
        else return 0;
    }

    public void setData(ArrayList<Coupon> data)
    {
        mDataset=data;
    }

    public void swapData(ArrayList<Coupon> data)
    {
        if(mDataset==null || (data!=null && mDataset.size()!=data.size()))
        {
            endOfList=false;
        }
        mDataset=data;
        notifyDataSetChanged();
    }

}
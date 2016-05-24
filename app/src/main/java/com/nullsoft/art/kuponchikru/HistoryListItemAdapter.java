package com.nullsoft.art.kuponchikru;

import android.os.Handler;
import android.view.View;

import com.squareup.picasso.Picasso;

/**
 * Created by art on 22.05.16.
 */
public class HistoryListItemAdapter extends CouponListItemAdapter
{

    public HistoryListItemAdapter(Handler handler) {
        super(handler);
    };

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_description.setText(mDataset.get(position).SHORT_DESCRIPTION);
        holder.item_discount.setText(String.format(context.getString(R.string.discount),mDataset.get(position).DISCOUNT));
        holder.item_price.setText(String.format(context.getString(R.string.price),mDataset.get(position).PRICE));
        holder.item_full_price.setText(String.format(context.getString(R.string.price),mDataset.get(position).FULL_PRICE));
        Picasso.with(context).load(mDataset.get(position).COUPON_IMAGE_URL)
                .placeholder(R.drawable.kdpv2).into(holder.card_img);

        CouponWithCode couponWithCode=(CouponWithCode)mDataset.get(position);
        holder.itemView.setTag(couponWithCode.CODE);

        holder.add_to_favorites_btn.setVisibility(View.INVISIBLE);
    }
}

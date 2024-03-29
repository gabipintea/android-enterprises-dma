package com.android_enterprises.discountcards.ui.cardslist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android_enterprises.discountcards.DBHelper;
import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.ShowDetails;
import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.shopType;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    //private final List<DummyItem> mValues;
    private final List<DiscountCard> mValues;
    DBHelper db;

    public MyItemRecyclerViewAdapter(List<DiscountCard> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cardslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final long shopId = mValues.get(position).getShopId();

        db = new DBHelper(holder.name.getContext());
        final Shop shop = db.getShop(shopId);

        holder.mItem = mValues.get(position);
        holder.name.setText(shop.getShopName());
        holder.discount.setText(String.valueOf(mValues.get(position).getDiscount()) + "%");
        holder.expiry.setText("EXP: " + String.valueOf(mValues.get(position).getExpiryDate()));

        //SHOP_LOGO
        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                //Log.d(TAG, "----------image received from thread------------");
                Bundle data = msg.getData();
                Bitmap image = data.getParcelable("image");
                holder.logo.setImageBitmap(image);
            }
        };

        //Log.d(TAG, "----------downloadImage method------------");

        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Log.d(TAG, "----------download content run------------");
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(shop.getLogoUrl());
                    URLConnection connection = url.openConnection();
                    if(connection instanceof HttpURLConnection)
                    {
                        httpURLConnection = (HttpURLConnection) connection;
                        httpURLConnection.connect();
                        int resultCode = httpURLConnection.getResponseCode();
                        if(resultCode == HTTP_OK)
                        {
                            InputStream is = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            Message message = handler.obtainMessage();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("image", bitmap);
                            message.setData(bundle);
                            handler.sendMessage(message);
                            //Log.d(TAG, "----------download finished with success------------");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        downloadThread.start();
        //Log.d(TAG, "----------download thread started------------");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public DiscountCard mItem;

        public final ImageView logo;
        public final TextView name, discount, expiry;
        public final LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.item_number);
//            mContentView = (TextView) view.findViewById(R.id.content);

            linearLayout = (LinearLayout) view.findViewById(R.id.cardContainer);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mView.getContext(), ShowDetails.class);
                    i.putExtra("selectedCard", mItem);
                    mView.getContext().startActivity(i);
                }
            });

            logo = (ImageView) view.findViewById(R.id.logoView);

            name = (TextView) view.findViewById(R.id.tvName);
            discount = (TextView) view.findViewById(R.id.tvDiscount);
            expiry = (TextView) view.findViewById(R.id.tvExpiryDate);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
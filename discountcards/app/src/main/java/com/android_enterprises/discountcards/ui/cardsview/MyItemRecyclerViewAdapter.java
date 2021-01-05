package com.android_enterprises.discountcards.ui.cardsview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android_enterprises.discountcards.AddActivity;
import com.android_enterprises.discountcards.DBHelper;
import com.android_enterprises.discountcards.MainActivity;
import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.shopType;
import com.android_enterprises.discountcards.ui.cardslist.dummy.DummyContent.DummyItem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    //private final List<DummyItem> mValues;
    private final List<DiscountCard> mValues;
    private static final String TAG = MyItemRecyclerViewAdapter.class.getSimpleName();
    DBHelper db;

    public MyItemRecyclerViewAdapter(List<DiscountCard> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cardsview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final long shopId = mValues.get(position).getShopId();

        db = new DBHelper(holder.name.getContext());
        //boolean registered = db.registerShop("Lidl", shopType.fromId(1), "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");


        final Shop shop = db.getShop(shopId);

        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(String.valueOf(mValues.get(position).getShopId()));
//        holder.mContentView.setText(String.valueOf(mValues.get(position).getDiscount()));
        holder.name.setText(shop.getShopName());
        //holder.discount.setText(String.valueOf(mValues.get(position).getDiscount()));
        //holder.expiry.setText(String.valueOf(mValues.get(position).getExpiryDate()));

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
        //        public final TextView mIdView;
//        public final TextView mContentView;
        public DiscountCard mItem;

        public final ImageView logo;
        public final TextView name, discount;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.item_number);
//            mContentView = (TextView) view.findViewById(R.id.content);

            logo = (ImageView) view.findViewById(R.id.logoView);
            name = (TextView) view.findViewById(R.id.tvName);
            discount = (TextView) view.findViewById(R.id.tvDiscount);
            //expiry = (TextView) view.findViewById(R.id.tvExpiryDate);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
package com.android_enterprises.discount_cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android_enterprises.discount_cards.model.Shop;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static java.net.HttpURLConnection.HTTP_OK;

public class ShopAdapter extends BaseAdapter {

    Map<Long, Shop> shopList;
    LayoutInflater layoutInflater;
    Context context;

    public ShopAdapter(Map<Long, Shop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return shopList.size();
    }

    @Override
    public Object getItem(int i) {
        Object[] objects = shopList.keySet().toArray();
        return shopList.get(objects[i]);
    }

    @Override
    public long getItemId(int i) {
        Object[] objects = shopList.keySet().toArray();
        return shopList.get(objects[i]).getShopId();
    }

    private static class ShopViewHolder
    {
        public ImageView logo;
        public TextView name, address;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final ShopViewHolder holder;

        if(view == null)
        {
            view = layoutInflater.inflate(R.layout.lv_shop_item, parent, false);
            holder = new ShopViewHolder();
            holder.logo = view.findViewById(R.id.logoView);
            holder.name = view.findViewById(R.id.tvName);
            holder.address = view.findViewById(R.id.tvAddress);
            view.setTag(holder);
        }
        else {
            holder = (ShopViewHolder) view.getTag();
        }
        final Shop shop = shopList.get(getItemId(i));
        holder.name.setText(shop.getShopName());
        holder.address.setText(shop.getAddress());
        //static
        //int logo = context.getResources().getIdentifier("logo_" + shop.getShopId(), "drawable", context.getPackageName());
        //holder.logo.setImageDrawable(context.getResources().getDrawable(logo));
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d(TAG, "----------image received from thread------------");
                Bundle data = msg.getData();
                Bitmap image = data.getParcelable("image");
                holder.logo.setImageBitmap(image);
            }
        };

        Log.d(TAG, "----------downloadImage method------------");

        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "----------download content run------------");
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
                            Log.d(TAG, "----------download finished with success------------");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        downloadThread.start();
        Log.d(TAG, "----------download thread started------------");

        return view;
    }
}

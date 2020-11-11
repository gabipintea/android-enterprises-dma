package com.android_enterprises.discount_cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android_enterprises.discount_cards.model.Shop;

import java.util.Map;

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
        ShopViewHolder holder;

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
        Shop shop = shopList.get(getItemId(i));
        holder.name.setText(shop.getShopName());
        holder.address.setText(shop.getAddress());
        int logo = context.getResources().getIdentifier("logo_" + shop.getShopId(), "drawable", context.getPackageName());
        holder.logo.setImageDrawable(context.getResources().getDrawable(logo));

        return view;
    }
}

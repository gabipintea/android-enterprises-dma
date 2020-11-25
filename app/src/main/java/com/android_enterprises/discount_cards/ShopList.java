package com.android_enterprises.discount_cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Spinner;

import com.android_enterprises.discount_cards.model.Shop;
import com.android_enterprises.discount_cards.model.shopType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopList extends AppCompatActivity {

    GridView gridView;
    private static List<Shop> shopList = new ArrayList<Shop>();
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        gridView = (GridView) findViewById(R.id.gvShops);
        Shop s1 = new Shop(100, "Kaufland", "Dorobanti", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
        Shop s2 = new Shop(200, "Lidl", "Pipera", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");
        Shop s3 = new Shop(300, "Carrefour", "Unirii", shopType.Food, "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Carrefour_logo_no_tag.svg/1024px-Carrefour_logo_no_tag.svg.png");


        Map<Long, Shop> shopMap = new HashMap<>();
        shopMap.put(s1.getShopId(), s1);
        shopMap.put(s2.getShopId(), s2);
        shopMap.put(s3.getShopId(), s3);



        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
        gridView.setAdapter(shopAdapter);

        shopList.add(s1);
        shopList.add(s2);
        shopList.add(s3);


        try {
            JSONObject jsonTBS = new JSONObject();
            JSONArray jsonShops = new JSONArray();
            for(int index=0; index<3;index++) {
                JSONObject jsonShop = new JSONObject();
                jsonShop.put("shopId", shopList.get(index).getShopId());
                jsonShop.put("shopName", shopList.get(index).getShopName());
                jsonShop.put("address", shopList.get(index).getAddress());
                jsonShop.put("logoUrl", shopList.get(index).getLogoUrl());
                jsonShops.put(jsonShop);
            }
            jsonTBS.put("Gabriel", jsonShops);

            UploadAsync uploadAsync = new UploadAsync()
            {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Log.d(TAG, "Value: " + s);

                }
            };
            uploadAsync.execute("http://167.99.143.42/upload", "gabriel", jsonTBS.toString());

            Log.d(TAG, jsonTBS.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
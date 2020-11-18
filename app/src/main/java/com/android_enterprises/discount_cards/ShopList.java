package com.android_enterprises.discount_cards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Spinner;

import com.android_enterprises.discount_cards.model.Shop;
import com.android_enterprises.discount_cards.model.shopType;

import java.util.HashMap;
import java.util.Map;

public class ShopList extends AppCompatActivity {

    GridView gridView;

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
    }
}
package com.android_enterprises.discountcards.ui.shopadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.android_enterprises.discountcards.DBHelper;
import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.ShopAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopAdminFragment extends Fragment {


    DBHelper db;
    List<Shop> shops = new ArrayList<Shop>();
    Spinner shopSpinner;


    private String mParam1;
    private String mParam2;

    public ShopAdminFragment() {
        // Required empty public constructor
    }



    public static ShopAdminFragment newInstance(String param1, String param2) {
        ShopAdminFragment fragment = new ShopAdminFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_admin, container, false);

        shopSpinner = (Spinner) view.findViewById(R.id.shopSpinner);
        db = new DBHelper(getActivity());
        shops.clear();
        shops = db.getShops();
        //Toast.makeText(this, String.valueOf(shops.size()), Toast.LENGTH_LONG).show();
        Map<Long, Shop> shopMap = new HashMap<>();
        for( Shop shop : shops) {
            shopMap.put(shop.getShopId(), shop);
        }

        ShopAdapter shopAdapter = new ShopAdapter(shopMap, getContext());
        shopSpinner.setAdapter(shopAdapter);

        return view;
    }
}
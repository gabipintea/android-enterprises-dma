package com.android_enterprises.discountcards.ui.shopadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android_enterprises.discountcards.DBHelper;
import com.android_enterprises.discountcards.MainActivity;
import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.model.Shop;
import com.android_enterprises.discountcards.model.ShopAdapter;
import com.android_enterprises.discountcards.model.shopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ShopAdminFragment extends Fragment {


    DBHelper db;
    Shop selectedShop;
    List<Shop> shops = new ArrayList<Shop>();
    Spinner shopSpinner;
    EditText editShopName, editShopType, editLogoURL;
    Button editShop, addShop;


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
        editShopName = (EditText) view.findViewById(R.id.editShopName);
        editShopType = (EditText) view.findViewById(R.id.editShopType);
        editLogoURL = (EditText) view.findViewById(R.id.editLogoURL);

        editShop = (Button) view.findViewById(R.id.editShop);
        addShop = (Button) view.findViewById(R.id.addShop);

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

        shopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //selectedShop = shopAdapter.shopList.get(shopSpinner.getSelectedItemId());
                selectedShop = shopAdapter.shopList.get(l);
                editShopName.setText(selectedShop.getShopName());
                editShopType.setText(String.valueOf(selectedShop.getType()));
                editLogoURL.setText(selectedShop.getLogoUrl());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Nothing yet

            }
        });

        editShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.editShop(selectedShop.getShopId(), editShopName.getText().toString(), shopType.valueOf(editShopType.getText().toString()), editLogoURL.getText().toString())) {
                    db.close();
                    Toast.makeText(getContext(), "Shop updated successfully", Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(getContext(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    }, 2000);
                }

            }
        });

        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
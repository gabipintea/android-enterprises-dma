package com.android_enterprises.discount_cards;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android_enterprises.discount_cards.model.DiscountCard;
import com.android_enterprises.discount_cards.model.Shop;
import com.android_enterprises.discount_cards.model.shopType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Spinner shopName;
    SeekBar discountValue;
    EditText expiryDate;
    ListView cardsList;
    GridView gridView;

    private static final String SHOP_NAME = "shopName";
    private static final String SHOP_ADDRESS = "shopAddress";
    private static final String SHOP_LOGO = "shopLogo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shopName = (Spinner) findViewById(R.id.shopSpinner);
        Shop s1 = new Shop(100, "Kaufland", "Dorobanti", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Kaufland_Logo.svg/1200px-Kaufland_Logo.svg.png");
        Shop s2 = new Shop(200, "Lidl", "Pipera", shopType.Food, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Lidl_logo.png/600px-Lidl_logo.png");
        Shop s3 = new Shop(300, "Carrefour", "Unirii", shopType.Food, "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Carrefour_logo_no_tag.svg/1024px-Carrefour_logo_no_tag.svg.png");
        Map<Long, Shop> shopMap = new HashMap<>();
        shopMap.put(s1.getShopId(), s1);
        shopMap.put(s2.getShopId(), s2);
        shopMap.put(s3.getShopId(), s3);

        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
        shopName.setAdapter(shopAdapter);

        //Button btnAdd = findViewById(R.id.btnAddCard);
        //btnAdd.setOnClickListener(this);
    }

    // Passing args between activities
    public void showDetails(View view) {

        Intent intent = new Intent(this, ShowDetails.class);

        shopName = (Spinner)findViewById(R.id.shopSpinner);
        discountValue = (SeekBar)findViewById(R.id.discountValue);
        expiryDate   = (EditText)findViewById(R.id.expiryDate);

        Bundle bundle = new Bundle();

        bundle.putLong("shopId", shopName.getSelectedItemId());
        bundle.putString("discountValue", String.valueOf(discountValue.getProgress()));
        bundle.putString("expiryDate", expiryDate.getText().toString());
        intent.putExtras(bundle);
//        intent.putExtra("param1", "Text from main");
        startActivity(intent);
        //startActivityForResult(intent, 100);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 100) {
//            if(resultCode == RESULT_OK) {
//                String result = data.getStringExtra("result");
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//            }
//        }
//    }



    //@Override
//    public void onClick(View view) {
//        Fragment fragment = null;
//
//        if (view.getId() == R.id.btnAddCard) {
//            shopName = (Spinner) findViewById(R.id.shopSpinner);
//
//            Shop shop = new Shop(shopName.getSelectedItemId(), shopName.getSelectedItem().toString(), "Dorobanti", shopType.Food);
//
//            discountValue = (SeekBar) findViewById(R.id.discountValue);
//            expiryDate = (EditText) findViewById(R.id.expiryDate);
//
//            Date expDate = null;
////                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy");
////                try {
////                    Date date = format.parse(expiryDate.toString());
////                    expDate = date;
////                } catch (ParseException e) {
////                    e.printStackTrace();
////                }
//            DiscountCard discountCard = new DiscountCard(shop, (float) discountValue.getProgress(), expDate);
//            fragment = (Fragment) CardsList.newInstance(discountCard);
//        }
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        // assert fragment != null;
//        fragmentTransaction.replace(R.id.cardsFragment, fragment);
//        fragmentTransaction.commit();
//    }

   // something triggered on fragment display
   //@Override
   // public void onViewClick(String p1) {
      //  Context context =null;
        //Toast.makeText(context,"",1).show();
        //Toast.makeText(this, p1, Toast.LENGTH_LONG).show();
       //cardsList = (ListView) findViewById(R.id.cardslist);
//       gridView = findViewById(R.id.cardslist);
//        Shop s1 = new Shop(100, "Kaufland", "Dorobanti", shopType.Food);
//        Shop s2 = new Shop(200, "Lidl", "Pipera", shopType.Food);
//        //Shop s3 = new Shop(300, "Carrefour", "Unirii", shopType.Food);
//
//        Map<Long, Shop> shopMap = new HashMap<>();
//        for(int i=0; i<4; i++) {
//            Shop shop1 = new Shop(s1, i);
//            Shop shop2 = new Shop(s2, i);
//
//            shopMap.put(shop1.getShopId(), shop1);
//            shopMap.put(shop2.getShopId(), shop2);
//        }
//        ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
//        gridView.setAdapter(shopAdapter);
//       gridView = (GridView) findViewById(R.id.cardslist);
//       Shop s1 = new Shop(100, "Kaufland", "Dorobanti", shopType.Food);
//       Shop s2 = new Shop(200, "Lidl", "Pipera", shopType.Food);
//       Shop s3 = new Shop(300, "Carrefour", "Unirii", shopType.Food);
//       Map<Long, Shop> shopMap = new HashMap<>();
//       shopMap.put(s1.getShopId(), s1);
//       shopMap.put(s2.getShopId(), s2);
//       shopMap.put(s3.getShopId(), s3);
//
//       ShopAdapter shopAdapter = new ShopAdapter(shopMap, this);
//       gridView.setAdapter(shopAdapter);
//
//    }
}
package com.android_enterprises.discount_cards;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.android_enterprises.discount_cards.model.DiscountCard;
import com.android_enterprises.discount_cards.model.Shop;
import com.android_enterprises.discount_cards.model.shopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardsList extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    GridView gridView;
    private static List<DiscountCard> cardsList = new ArrayList<DiscountCard>();


    private static View displayedView;


    private String mParam1;
    // private String mParam2;



    public CardsList() {
        // Required empty public constructor
    }


    // TODO: Make a list of cards to be shown in the listview/cardsview/gridview
    public static CardsList newInstance(DiscountCard discountCard) {
        CardsList fragment = new CardsList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, discountCard.toString());
        fragment.setArguments(args);
        cardsList.add(discountCard);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        displayedView = inflater.inflate(R.layout.fragment_cards_list, container, false);
        listener.onViewClick(mParam1); // triggering a method on display
//        gridView = getActivity().findViewById(R.id.cardslist);
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
//        ShopAdapter shopAdapter = new ShopAdapter(shopMap, getActivity());
//        gridView.setAdapter(shopAdapter);
        return displayedView;
    }

    // also needed for triggering something on display
    private OnFragmentInteraction listener;

    public interface OnFragmentInteraction
    {
        void onViewClick(String p1);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteraction)
            listener = (OnFragmentInteraction) context;
        else
            throw new ClassCastException(context.toString() + " must implement CardsList.OnFragmentInteraction");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
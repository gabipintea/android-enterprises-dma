package com.android_enterprises.discountcards.ui.cardslist;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.model.DiscountCard;
import com.android_enterprises.discountcards.ui.cardslist.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class CardslistFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    public static final List<DiscountCard> discountCards = new ArrayList<DiscountCard>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardslistFragment() {
        //TODO populate an array of DiscountCards from database based on the current User (from SharedPreferences)
        DiscountCard c1 = new DiscountCard(100, "gabi@gmail.com", 50, "2020-01-01");
        DiscountCard c2 = new DiscountCard(200, "gabi@gmail.com", 80, "2021-01-01");
        DiscountCard c3 = new DiscountCard(300, "gabi@gmail.com", 45, "2022-01-01");
        discountCards.add(c1);
        discountCards.add(c2);
        discountCards.add(c3);
    }


    @SuppressWarnings("unused")
    public static CardslistFragment newInstance(int columnCount) {
        CardslistFragment fragment = new CardslistFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardslist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(discountCards));
        }
        return view;
    }
}
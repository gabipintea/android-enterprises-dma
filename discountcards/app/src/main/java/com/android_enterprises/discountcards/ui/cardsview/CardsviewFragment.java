package com.android_enterprises.discountcards.ui.cardsview;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android_enterprises.discountcards.DBHelper;
import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.model.DiscountCard;


import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class CardsviewFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private String mEmail = "";

    public static List<DiscountCard> discountCards = new ArrayList<DiscountCard>();
    DBHelper db;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardsviewFragment() {
    }


    @SuppressWarnings("unused")
    public static CardsviewFragment newInstance(int columnCount) {
        CardsviewFragment fragment = new CardsviewFragment();
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
            mEmail = getArguments().getString("email");

            db = new DBHelper(this.getContext());
            discountCards.clear();
            discountCards = db.getUserCards(mEmail);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardsview_list, container, false);

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
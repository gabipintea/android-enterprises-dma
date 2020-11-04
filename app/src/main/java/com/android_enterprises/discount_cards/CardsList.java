package com.android_enterprises.discount_cards;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android_enterprises.discount_cards.model.DiscountCard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardsList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
   //  private static final String ARG_PARAM2 = "param2";
    private static View displayedView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    // private String mParam2;

    public CardsList() {
        // Required empty public constructor
    }


    // TODO: Make a list of cards to be shown in the listview/cardsview
    public static CardsList newInstance(DiscountCard discountCard) {
        CardsList fragment = new CardsList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, discountCard.toString());
        fragment.setArguments(args);
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
        return displayedView;   // TODO: Find out why this fragment does appear at the bottom
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
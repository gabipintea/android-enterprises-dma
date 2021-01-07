package com.android_enterprises.discountcards.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.android_enterprises.discountcards.R;

public class ShopDialog extends AppCompatDialogFragment {
    private EditText shopName;
    private EditText shopType;


    private ShopDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_shop_dialog, null);
        builder.setView(view)
                .setTitle("Add Shop")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String shopname = shopName.getText().toString();
                        String shoptype = shopType.getText().toString();

                        listener.applyShopTexts(shopname, shoptype);
                    }
                });
        shopName = view.findViewById(R.id.shopName);
        shopType = view.findViewById(R.id.shopType);


        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ShopDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ShopDialogListener");
        }
    }
    public interface ShopDialogListener {
        void applyShopTexts(String shopname, String shoptype);
    }
}

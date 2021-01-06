package com.android_enterprises.discountcards.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.ui.controls.MySeekBar;

public class CardDialog extends AppCompatDialogFragment {
    private MySeekBar discountValue;
    private EditText expiryDateField;

    private CardDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_card_dialog, null);
        builder.setView(view)
                .setTitle("Edit card")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int discount = discountValue.getProgress();
                        String expiryDate = String.valueOf(expiryDateField.getText());
                        listener.applyTexts(discount, expiryDate);
                    }
                });

        discountValue = (MySeekBar)view.findViewById(R.id.discountValue);
        discountValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        expiryDateField   = (EditText)view.findViewById(R.id.expiryDate);
        if(getArguments() != null) {
            discountValue.setProgress(getArguments().getInt("discount"));
            expiryDateField.setText(getArguments().getString("expirydate"));
        }

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CardDialogListener");
        }
    }
    public interface CardDialogListener {
        void applyTexts(int discount, String expiryDate);
    }
}

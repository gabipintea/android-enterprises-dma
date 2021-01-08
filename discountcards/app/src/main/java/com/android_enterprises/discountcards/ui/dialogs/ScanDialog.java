package com.android_enterprises.discountcards.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.android_enterprises.discountcards.R;

public class ScanDialog extends AppCompatDialogFragment {
    private ImageView qrCode;


    private ScanDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.scan_card_dialog, null);
        builder.setView(view)
                .setTitle("Scan QR Code")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String shopname = shopName.getText().toString();
//                        String shoptype = shopType.getText().toString();

                        listener.applyQRCode();
                    }
                });
        qrCode = view.findViewById(R.id.qrCode);


        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ScanDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ScanDialogListener");
        }
    }
    public interface ScanDialogListener {
        void applyQRCode();
    }
}

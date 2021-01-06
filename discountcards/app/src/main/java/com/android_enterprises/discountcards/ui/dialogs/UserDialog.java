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

public class UserDialog extends AppCompatDialogFragment {
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editBirthdate;

    private UserDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_user_dialog, null);
        builder.setView(view)
                .setTitle("Add User")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String firstname = editFirstName.getText().toString();
                        String lastname = editLastName.getText().toString();
                        String email = editEmail.getText().toString();
                        String birthdate = editBirthdate.getText().toString();
                        listener.applyTexts(firstname, lastname, email, birthdate);
                    }
                });
        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editEmail = view.findViewById(R.id.editEmail);
        editBirthdate = view.findViewById(R.id.editBirthdate);

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (UserDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface UserDialogListener {
        void applyTexts(String firstname, String lastname, String email, String birthdate);
    }
}

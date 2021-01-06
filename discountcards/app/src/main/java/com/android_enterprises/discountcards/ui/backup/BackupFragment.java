package com.android_enterprises.discountcards.ui.backup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android_enterprises.discountcards.R;
import com.android_enterprises.discountcards.ui.backup.BackupViewModel;

public class BackupFragment extends Fragment {

    private BackupViewModel mViewModel;

    public static BackupFragment newInstance() {
        return new BackupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        mViewModel =
//                new ViewModelProvider(this).get(BackupViewModel.class);
        View root = inflater.inflate(R.layout.backup_fragment, container, false);
//        final TextView textView = root.findViewById(R.id.text_backup);
//        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BackupViewModel.class);
    }

}
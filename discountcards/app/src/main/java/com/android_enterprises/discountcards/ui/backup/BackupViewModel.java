package com.android_enterprises.discountcards.ui.backup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BackupViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public BackupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is backup fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
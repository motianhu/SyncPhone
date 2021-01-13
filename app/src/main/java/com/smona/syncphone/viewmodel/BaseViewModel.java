package com.smona.syncphone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.smona.syncphone.model.BaseModel;

public class BaseViewModel<M extends BaseModel> extends AndroidViewModel {
    protected M model;

    public BaseViewModel(@NonNull Application application) {
        this(application, null);
    }

    public BaseViewModel(@NonNull Application application, M model) {
        super(application);
        this.model = model;
    }
}

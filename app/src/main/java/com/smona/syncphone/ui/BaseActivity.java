 package com.smona.syncphone.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

 public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends ViewModel> extends AppCompatActivity {
    private VDB dataBinding;
    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewModel = createViewModel(getViewModelClass());
        int viewModelId = getVariableId();
        dataBinding.setVariable(viewModelId, viewModel);
        dataBinding.setLifecycleOwner(this);
    }

    protected abstract int getLayoutId();
    protected abstract int getVariableId();

    protected abstract Class<VM> getViewModelClass();

    private VM createViewModel(@NonNull Class<VM> modelClass) {
        return getDefaultViewModelProviderFactory().create(modelClass);
    }
}

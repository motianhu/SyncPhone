package com.smona.syncphone.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.smona.syncphone.base.binding.command.BindingAction;
import com.smona.syncphone.base.binding.command.BindingCommand;
import com.smona.syncphone.base.http.BaseResponse;
import com.smona.syncphone.base.http.ResponseThrowable;
import com.smona.syncphone.base.http.util.RxUtils;
import com.smona.syncphone.bean.PhoneList;
import com.smona.syncphone.repository.PhoneRepository;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class PhoneViewModel extends BaseViewModel<PhoneRepository> {

    public ObservableField<PhoneList> phoneList = new ObservableField<>(null);

    public BindingCommand submitClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    public PhoneViewModel(@NonNull Application application) {
        super(application);
    }

    public void requestPhoneList(String phone) {
//        model.requestPhoneList(phone)
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                .doOnSubscribe(this)//请求与ViewModel周期同步
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//
//                    }
//                })
//                .subscribe(new DisposableObserver<BaseResponse<PhoneList>>() {
//                    @Override
//                    public void onNext(BaseResponse<PhoneList> response) {
//                        //清除列表
//                        phoneList.set(null);
//                        if(response.isOk()) {
//                            //双向绑定动态添加Item
//                            phoneList.set(response.getData());
//                        } else {
//                            //code错误时也可以定义Observable回调到View层去处理
//                        }
//
//                        //请求成功
//                        if (response.getCode() == 1) {
//                            for (DemoEntity.ItemsEntity entity : response.getResult().getItems()) {
//                                NetWorkItemViewModel itemViewModel = new NetWorkItemViewModel(NetWorkViewModel.this, entity);
//
//                                observableList.add(itemViewModel);
//                            }
//                        } else {
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        //关闭对话框
//                        //请求刷新完成收回
//                        if (throwable instanceof ResponseThrowable) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        //请求刷新完成收回
//                    }
//                });
    }
}

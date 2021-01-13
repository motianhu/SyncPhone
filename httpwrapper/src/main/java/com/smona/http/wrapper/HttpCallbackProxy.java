package com.smona.http.wrapper;

import com.smona.base.http.HttpCallBack;
import com.smona.http.business.BaseResponse;

public class HttpCallbackProxy<K> extends HttpCallBack<K> {
    private final OnResultListener<K> realListener;

    public HttpCallbackProxy(OnResultListener<K> real) {
        this.realListener = real;
    }

    @Override
    public void onSuccess(K data) {
        BaseResponse<?> response;
        if (data instanceof BaseResponse<?>) {
            response = (BaseResponse<?>) data;
            if (response.isOk()) {
                if (realListener != null) {
                    realListener.onSuccess(data);
                }
            } else {
                if (realListener != null) {
                    realListener.onError(response.code + "", response.msg);
                }
            }
        } else {
            if (realListener != null) {
                realListener.onSuccess(data);
            }
        }
    }

    @Override
    public void onError(String stateCode, String errorInfo) {
        if (realListener != null) {
            realListener.onError(stateCode, errorInfo);
        }
    }
}

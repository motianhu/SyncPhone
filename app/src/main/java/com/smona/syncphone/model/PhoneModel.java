package com.smona.syncphone.model;

import com.smona.http.business.BaseResponse;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.business.CommonBuilder;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;
import com.smona.syncphone.bean.PhoneList;
import com.smona.syncphone.bean.ReqPhone;

public class PhoneModel {
    public void requestPhoneList(ReqPhone phone, OnResultListener<BaseResponse<PhoneList>> listener) {
        HttpCallbackProxy<BaseResponse<PhoneList>> callbackProxy = new HttpCallbackProxy<BaseResponse<PhoneList>>(listener) {
        };
        new CommonBuilder<PhoneList>(CommonBuilder.REQUEST_POST, BusinessHttpService.PHONE_LIST).requestData(phone, callbackProxy);
    }
}

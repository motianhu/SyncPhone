package com.smona.syncphone.repository;

import com.smona.syncphone.model.BaseModel;
import com.smona.syncphone.repository.local.ILocalSource;
import com.smona.syncphone.repository.http.IHttpSource;

public class PhoneRepository  extends BaseModel implements IHttpSource, ILocalSource {
    private IHttpSource mHttpDataSource;
    private ILocalSource mLocalDataSource;

    public PhoneRepository() {
    }

    public void requestPhoneList(String phone) {
        
    }
}

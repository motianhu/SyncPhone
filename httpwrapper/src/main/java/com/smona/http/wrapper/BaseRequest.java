package com.smona.http.wrapper;

import com.smona.http.business.BusinessHttpService;

public abstract class BaseRequest<T> extends BaseBuilder<T> {
    private String path;

    BaseRequest(String path) {
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }

    @Override
    protected String getBaseUrl() {
        return BusinessHttpService.API_URL;
    }
}

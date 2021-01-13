package com.smona.http.business;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 3/25/19 1:59 PM
 */
public class BaseResponse<R> {
    public int code;
    public String msg;
    public R data;

    public boolean isOk() {
        return code == 1;
    }
}

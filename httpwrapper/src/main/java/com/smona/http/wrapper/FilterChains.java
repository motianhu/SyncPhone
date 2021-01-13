package com.smona.http.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/11/19 1:56 PM
 */
public class FilterChains {
    private Map<String, IFilter> mAspectFilter = new HashMap<>();

    private static class Holder {
        private final static FilterChains sFilterChain = new FilterChains();
    }

    public static FilterChains getInstance() {
        return Holder.sFilterChain;
    }

    public void addAspectRouter(String errCode, IFilter aspectRouter) {
        mAspectFilter.put(errCode, aspectRouter);
    }

    public void exeFilter(String errCode) {
        IFilter filter = mAspectFilter.get(errCode);
        if (filter != null) {
            filter.exeFilter();
        }
    }
}

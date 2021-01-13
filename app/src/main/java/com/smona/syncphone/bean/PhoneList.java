package com.smona.syncphone.bean;

import java.util.List;

public class PhoneList {
    private boolean hasNext;
    private List<PhoneBean> items;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<PhoneBean> getItems() {
        return items;
    }

    public void setItems(List<PhoneBean> items) {
        this.items = items;
    }
}

package com.smona.syncphone.bean;

public class PhoneBean {
    private String tel;
    private String name;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PhoneBean{" +
                "tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

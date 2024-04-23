package com.ssg.starroad.user.enums;

public enum Authority {
    STORE("매장"),
    MALL("쇼핑몰"),
    ADMIN("관리자");

    private String desc;

    Authority(String desc) {
        this.desc = desc;
    }
}

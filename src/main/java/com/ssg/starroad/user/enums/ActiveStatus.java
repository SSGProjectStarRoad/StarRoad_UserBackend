package com.ssg.starroad.user.enums;

import lombok.Getter;

@Getter
public enum ActiveStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    SUSPENDED("정지"),
    CLOSED("탈퇴"),
    WITHDRAW("탈퇴");

    private String desc;

    ActiveStatus(String desc) {
        this.desc = desc;
    }
}
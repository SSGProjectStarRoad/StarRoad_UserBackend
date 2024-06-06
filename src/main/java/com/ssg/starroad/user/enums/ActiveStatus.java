package com.ssg.starroad.user.enums;

import lombok.Getter;

@Getter
public enum ActiveStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    // 관리자에 의해 강제로 계정을 닫은 경우
    CLOSED("탈퇴"),
    // 유저가 자발적으로 탈퇴한 경우
    WITHDRAW("탈퇴");

    private String desc;

    ActiveStatus(String desc) {
        this.desc = desc;
    }
}
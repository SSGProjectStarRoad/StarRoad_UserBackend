package com.ssg.starroad.user.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남자"),
    FEMALE("여자");

    private String desc;

    Gender(String desc) {
        this.desc = desc;
    }
}

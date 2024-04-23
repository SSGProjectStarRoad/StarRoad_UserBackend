package com.ssg.starroad.review.enums;

import lombok.Getter;

@Getter
public enum ConfidenceType {
    POSITIVE("긍정"),
    NEGATIVE("부정"),
    NEUTRAL("중립");

    private String desc;

    ConfidenceType(String desc) {
        this.desc = desc;
    }
}

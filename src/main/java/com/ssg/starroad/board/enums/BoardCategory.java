package com.ssg.starroad.board.enums;

public enum BoardCategory {
    NOTICE("공지사항"),
    FAQ("FAQ"),
    QNA("Q&A"),
    EVENT("이벤트");

    private String desc;

    BoardCategory(String desc) {
        this.desc = desc;
    }
}

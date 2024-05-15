package com.ssg.starroad.shop.enums;

import lombok.Getter;

@Getter
public enum Floor {
    BASEMENT_FOUR("B4"),
    BASEMENT_THREE("B3"),
    BASEMENT_TWO("B2"),
    BASEMENT_ONE("B1"),
    FIRST("1"),
    SECOND("2"),
    THIRD("3"),
    FOURTH("4"),
    FIFTH("5"),
    SIXTH("6"),
    SEVENTH("7"),
    EIGHTH("8"),
    NINTH("9"),
    TENTH("10"),
    ELEVENTH("11"),
    TWELFTH("12"),
    THIRTEENTH("13"),
    FOURTEENTH("14");

    private final String floor;

    Floor(String floor) {
        this.floor = floor;
    }
}
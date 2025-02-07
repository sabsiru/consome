package com.consome.domain;

import lombok.Getter;

public enum BanType {
    NO(0),DAY(1),WEEK(7),MONTH(30),PERMANENT(36500);

    private final int days;
    BanType(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}

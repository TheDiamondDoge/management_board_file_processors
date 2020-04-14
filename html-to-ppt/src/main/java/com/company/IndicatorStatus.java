package com.company;

public enum IndicatorStatus {
    BLANK(0), RED(1), YELLOW(2), GREEN(3);
    private int value;

    IndicatorStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

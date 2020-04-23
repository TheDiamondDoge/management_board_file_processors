package com.aiksanov.enums;

public enum IndicatorStatus {
    BLANK(0), GREEN(1), YELLOW(2), RED(3);
    private int value;

    IndicatorStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static IndicatorStatus getStatus(int value) throws Exception {
        for (IndicatorStatus val : values()) {
            if (val.getValue() == value) {
                return val;
            }
        }

        throw new Exception(String.valueOf(value));
    }
}

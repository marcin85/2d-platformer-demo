package com.github.x6ud.ptoy.enums;

public enum HorizontalDirection {
    LEFT(-1), RIGHT(1);

    private int value;

    HorizontalDirection(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}

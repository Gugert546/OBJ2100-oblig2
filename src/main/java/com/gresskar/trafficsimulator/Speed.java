package com.gresskar.trafficsimulator;

public enum Speed {
    STOP(0), SLOW(0.5), HIGH(1);

    private final double value;

    Speed(double value) {
        if (value < 0) {
            this.value = 0;
        } else if (value < 3) {
            this.value = 3;
        } else {
            this.value = value;
        }
    }

    public double getValue() {
        return value;
    }
}

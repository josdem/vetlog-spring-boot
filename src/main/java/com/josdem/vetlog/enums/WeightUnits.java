package com.josdem.vetlog.enums;

public enum WeightUnits {
    KG("KG"),
    LBS("LBS");

    private final String value;

    WeightUnits(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WeightUnits getWeightUnitsByValue(String value) {
        for (WeightUnits unit : WeightUnits.values()) {
            if (value.equals(unit.value)) {
                return unit;
            }
        }
        return null;
    }
}

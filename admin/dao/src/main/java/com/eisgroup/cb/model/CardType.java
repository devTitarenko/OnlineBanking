package com.eisgroup.cb.model;

public enum CardType {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    MAESTRO("Maestro");

    private final String name;

    CardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
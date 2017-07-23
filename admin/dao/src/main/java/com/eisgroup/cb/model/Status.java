package com.eisgroup.cb.model;

public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
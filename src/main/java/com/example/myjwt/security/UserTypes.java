package com.example.myjwt.security;

public enum UserTypes {
    ADMIN("admin"), EMPLOYEE("employee");

    private final String type;

    UserTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

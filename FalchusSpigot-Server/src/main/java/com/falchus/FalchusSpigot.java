package com.falchus;

public class FalchusSpigot {

    private static FalchusSpigot instance;

    public static final String version = "1.0.0";

    public FalchusSpigot() {
        instance = this;
    }

    public static FalchusSpigot getInstance() {
        return instance;
    }
}

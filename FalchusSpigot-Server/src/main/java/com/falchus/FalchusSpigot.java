package com.falchus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FalchusSpigot {

    private static FalchusSpigot instance;
    public static final String version = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger();

    public FalchusSpigot() {
        instance = this;
    }

    public static FalchusSpigot getInstance() {
        return instance;
    }
}

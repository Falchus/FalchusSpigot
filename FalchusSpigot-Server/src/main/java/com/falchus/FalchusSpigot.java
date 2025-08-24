package com.falchus;

import com.falchus.listeners.JoinQuitListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FalchusSpigot {

    private static FalchusSpigot instance;
    public final JavaPlugin plugin = new JavaPlugin() {};
    public static final String version = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger();

    public FalchusSpigot() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), plugin);
    }

    public static FalchusSpigot getInstance() {
        return instance;
    }
}

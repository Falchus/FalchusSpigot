package com.falchus.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.falchus.FalchusSpigot;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.sugarcanemc.sugarcane.util.yaml.YamlCommenter;

import com.google.common.base.Throwables;

@SuppressWarnings("unused")
public class FalchusSpigotConfig {

    private static final Logger LOGGER = LogManager.getLogger(FalchusSpigotConfig.class);
    public static File CONFIG_FILE;
    protected static final YamlCommenter c = new YamlCommenter();
    private static final String HEADER = "This is the main configuration file for FalchusSpigot.\n"
            + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
            + "with caution, and make sure you know what each option does before configuring.\n";

    public static YamlConfiguration config;
    static int version;

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            FalchusSpigot.LOGGER.info("Loading FalchusSpigot config from " + configFile.getName());
            config.load(CONFIG_FILE);
        } catch (IOException ignored) {
        } catch (InvalidConfigurationException ex) {
            LOGGER.log(Level.ERROR, "Could not load falchusspigot.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().copyDefaults(true);

        int configVersion = 1; // Update this every new configuration update

        version = getInt("config-version", configVersion);
        set("config-version", configVersion);
        c.setHeader(HEADER);
        readConfig(FalchusSpigotConfig.class, null);

        try {
            config.save(CONFIG_FILE);
            loadComments();
            c.saveComments(CONFIG_FILE);
        } catch (Exception ex) {
            LOGGER.log(Level.ERROR, "Could not save " + CONFIG_FILE, ex);

            LOGGER.warn("Please regenerate your falchusspigot.yml file to prevent this issue! The server will run with the default config for now.");

            makeReadable();
        }

    }

    // Not private as the config is read by calling all private methods with 0 params
    static void makeReadable() {
        LOGGER.warn("Waiting for 10 seconds so this can be read...");

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        LOGGER.log(Level.ERROR, "Error invoking " + method, ex);
                    }
                }
            }
        }
    }

    static void loadComments() {
        c.addComment("config-version", "Configuration version, do NOT modify this!");

        c.addComment("settings.player.obfuscate.health", "Obfuscate player health to other players.");

        c.addComment("settings.player.elements.tablist", "Display integrated Tablist.");
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static float getFloat(String path, float def) {
        config.addDefault(path, def);
        return config.getFloat(path, config.getFloat(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    public static boolean playerObfuscateHealth;
    private static void playerObfuscate() {
        playerObfuscateHealth = getBoolean("settings.player.obfuscate.health", true);
    }

    public static boolean playerTablist;
    public static String playerTablistHeader;
    public static String playerTablistFooter;
    private static void playerTablist() {
        playerTablist = getBoolean("settings.player.elements.tablist", false);
        playerTablistHeader = getString("settings.player.elements.tablist.header", "§f§lFalchusSpigot");
        playerTablistFooter = getString("settings.player.elements.tablist.footer", "§f§lv" + FalchusSpigot.version);
    }
}

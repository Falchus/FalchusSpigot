package com.falchus.player.elements;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.falchus.FalchusSpigot;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class PlayerElement {

    protected final FalchusSpigot spigot = FalchusSpigot.getInstance();
    protected final Player player;

    private static final Map<Class<? extends PlayerElement>, Map<UUID, PlayerElement>> INSTANCES = new HashMap<>();
    private static final Map<Class<? extends PlayerElement>, Map<UUID, BukkitTask>> TASKS = new HashMap<>();

    protected PlayerElement(Player player) {
        this.player = player;
    }

    public void send() {}

    public void sendUpdating(long intervalTicks, Runnable runnable) {
        Map<UUID, BukkitTask> map = TASKS.computeIfAbsent(this.getClass(), c -> new HashMap<>());

        BukkitTask oldTask = map.get(player.getUniqueId());
        if (oldTask != null) {
            remove();
        }

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    remove();
                    return;
                }
                runnable.run();
            }
        }.runTaskTimer(spigot.plugin, 0, intervalTicks);

        map.put(player.getUniqueId(), task);
    }

    public void remove() {
        Map<UUID, BukkitTask> map = TASKS.get(this.getClass());
        if (map != null) {
            BukkitTask task = map.remove(player.getUniqueId());
            if (task != null) {
                task.cancel();
            }
        }

        Map<UUID, PlayerElement> isntMap = INSTANCES.get(this.getClass());
        if (isntMap != null) {
            isntMap.remove(player.getUniqueId());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends PlayerElement> T get(Class<T> clazz, Player player) {
        if (!PlayerElement.class.isAssignableFrom(clazz)) return null;

        Map<UUID, PlayerElement> map = INSTANCES.computeIfAbsent(clazz, c -> new HashMap<>());

        return (T) map.computeIfAbsent(player.getUniqueId(), id -> {
            try {
                Constructor<T> ctor = clazz.getDeclaredConstructor(Player.class);
                ctor.setAccessible(true);
                return ctor.newInstance(player);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}

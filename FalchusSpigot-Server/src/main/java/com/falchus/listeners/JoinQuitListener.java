package com.falchus.listeners;

import com.falchus.config.FalchusSpigotConfig;
import com.falchus.player.elements.PlayerElement;
import com.falchus.player.elements.impl.Tablist;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collections;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerElement.get(Tablist.class, player).sendUpdating(
            1,
            () -> Collections.singletonList(FalchusSpigotConfig.playerTablistHeader),
            () -> Collections.singletonList(FalchusSpigotConfig.playerTablistFooter),
            null
        );
    }
}

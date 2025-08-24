package com.falchus.player.elements.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Supplier;

import com.falchus.player.elements.PlayerElement;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Tablist extends PlayerElement {

    private Tablist(Player player) {
        super(player);
    }

    public void send(List<String> header, List<String> footer, String name) {
        String headerText = header != null ? String.join("\n", header) : "";
        String footerText = footer != null ? String.join("\n", footer) : "";
        name = name != null ? name : "";

        IChatBaseComponent headerComponent = new ChatComponentText(headerText);
        IChatBaseComponent footerComponent = new ChatComponentText(footerText);

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(headerComponent);

        try {
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
            b.set(packet, footerComponent);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return;
        }

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        player.setPlayerListName(name);
    }

    public void sendUpdating(long intervalTicks, Supplier<List<String>> headerSupplier, Supplier<List<String>> footerSupplier, Supplier<String> nameSupplier) {
        super.sendUpdating(intervalTicks, () -> {
            List<String> header = headerSupplier.get();
            List<String> footer = footerSupplier.get();
            String name = nameSupplier.get();
            send(header, footer, name);
        });
    }

    public void remove() {
        super.remove();

        send(null, null, player.getName());
    }
}

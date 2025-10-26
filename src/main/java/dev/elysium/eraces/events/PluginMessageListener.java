package dev.elysium.eraces.events;

import dev.elysium.eraces.ERaces;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("elysium:eraces_cast")) return;

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            long timestamp = in.readLong();
            String type = in.readUTF();
            String payload = in.readUTF();

            ERaces.getInstance().getLogger().info(player.getName() + " sent: " + timestamp + " / " + type + " / " + payload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package dev.elysium.eraces.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import dev.elysium.eraces.ERaces;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("elysium:eraces_cast")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        long timestamp = in.readLong();
        String type = in.readUTF();
        String payload = in.readUTF();
        ERaces.getInstance().getLogger().info(timestamp+", Игрок: "+player.getName() + " отправил: "+type+"/"+payload);
    }
}

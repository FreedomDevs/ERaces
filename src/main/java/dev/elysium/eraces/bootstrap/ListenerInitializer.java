package dev.elysium.eraces.bootstrap;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.exceptions.internal.InitFailedException;
import dev.elysium.eraces.listeners.GuiListener;
import dev.elysium.eraces.listeners.RaceSelectListener;
import dev.elysium.eraces.listeners.PluginMessageListener;
import org.bukkit.Bukkit;

public class ListenerInitializer implements IInitializer {
    @Override
    public void setup(ERaces plugin) {
        try {
            Bukkit.getPluginManager().registerEvents(RaceSelectListener.INSTANCE, plugin);
            plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "elysium:eraces_cast", new PluginMessageListener());

            GuiListener.INSTANCE.init(plugin);
            Bukkit.getPluginManager().registerEvents(GuiListener.INSTANCE, plugin);
        } catch (Exception e) {
            throw new InitFailedException("Ошибка при регистрации слушателей", e);
        }
    }
}

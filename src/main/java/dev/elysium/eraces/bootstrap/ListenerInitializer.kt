package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.exceptions.internal.InitFailedException
import dev.elysium.eraces.listeners.GuiListener
import dev.elysium.eraces.listeners.GuiListener.init
import dev.elysium.eraces.listeners.PluginMessageListener
import dev.elysium.eraces.listeners.RaceSelectListener
import org.bukkit.Bukkit

class ListenerInitializer : IInitializer {
    override fun setup(plugin: ERaces) {
        try {
            Bukkit.getPluginManager().registerEvents(RaceSelectListener, plugin)
            plugin.server.messenger
                .registerIncomingPluginChannel(plugin, "elysium:eraces_cast", PluginMessageListener())

            init(plugin)
            Bukkit.getPluginManager().registerEvents(GuiListener, plugin)
        } catch (e: Exception) {
            throw InitFailedException("Ошибка при регистрации слушателей", e)
        }
    }
}

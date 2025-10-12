package dev.elysium.eraces.utils.targetUtils

import dev.elysium.eraces.ERaces

object PluginAccessor {
    lateinit var plugin: ERaces
    fun init(plugin: ERaces) { this.plugin = plugin }
}

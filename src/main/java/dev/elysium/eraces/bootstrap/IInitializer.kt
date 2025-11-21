package dev.elysium.eraces.bootstrap

import dev.elysium.eraces.ERaces

interface IInitializer {
    fun setup(plugin: ERaces)
}

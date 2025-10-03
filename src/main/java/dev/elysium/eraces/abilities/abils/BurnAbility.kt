package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class BurnAbility: IAbility {
    override val id: String = "burn"
    private var cooldown: Int = 10
    private var maxDistance: Double = 50.0

    override fun activate(player: Player) {
        TODO("Not yet implemented")
    }

    override fun loadParams(cfg: YamlConfiguration) {
        TODO("Not yet implemented")
    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        TODO("Not yet implemented")
    }
}
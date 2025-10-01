package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BlockAbility : IAbility {
    override val id: String = "block"
    private var cooldown: Int = 5
    private var duration: Int = 10
    private var level: Int = 3
    override fun activate(player: Player) {
        val resistance = PotionEffect(
            PotionEffectType.RESISTANCE,
            duration * 20,
            level,
            false,
            true
        )
        player.addPotionEffect(resistance)
    }

    override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getInt("cooldown", 5)
        duration = cfg.getInt("duration", 10)
        level = cfg.getInt("level", 3)
    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", 5)
        cfg.set("duration", 10)
        cfg.set("level", 3)
    }
}
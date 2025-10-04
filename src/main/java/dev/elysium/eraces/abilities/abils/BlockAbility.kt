package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.abilities.IAbility
import dev.elysium.eraces.abilities.ICooldownAbility
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BlockAbility : IAbility, ICooldownAbility {
    override val id: String = "block"
    private var cooldown: String = "5m"
    private var duration: String = "10s"
    private var level: Int = 3
    override fun activate(player: Player) {
        val resistance = PotionEffect(
            PotionEffectType.RESISTANCE,
            TimeParser.parseToTicks(duration).toInt(),
            level,
            false,
            true
        )
        player.addPotionEffect(resistance)
    }

    override fun loadParams(cfg: YamlConfiguration) {
        cooldown = cfg.getString("cooldown", cooldown)!!
        duration = cfg.getString("duration", "10s").toString()
        level = cfg.getInt("level", 3)
    }

    override fun writeDefaultParams(cfg: YamlConfiguration) {
        cfg.set("cooldown", cooldown)
        cfg.set("duration", "10s")
        cfg.set("level", 3)
    }

    override fun getCooldown(): Long {
        return TimeParser.parseToSeconds(cooldown)
    }
}
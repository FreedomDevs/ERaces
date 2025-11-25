package dev.elysium.eraces.abilities.abils.buffs.mana

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.ConfigHelper.read
import dev.elysium.eraces.abilities.ConfigHelper.write
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

@RegisterAbility
@Suppress("unused")


class TheMagicRadiusAbility : BaseCooldownAbility(
    id = "themagiceadius", defaultCooldown = "45s"
) {
    private var radius: Double = 3.0
    private var duration: String = "30s"
    private var manaRestorePercent: Double = .10

    override fun onActivate(player: Player) {
        Target.from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius)

            .execute { target ->
                if (target is Player) {
                    val manaManager = ERaces.getInstance().context.manaManager

                    val maxMana = manaManager.getMaxMana(target)
                    val manaToRestore = maxMana * (manaRestorePercent)

                    val currentMana = manaManager.getMana(target)
                    val newMana = (currentMana + manaToRestore).coerceAtMost(maxMana)

                    manaManager.setMana(target, newMana)

                }
            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radius", ::radius)
            read("duration", ::duration)
            read("manaRestorePercent", ::manaRestorePercent)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radius)
            write("duration", duration)
            write("manaRestorePercent", manaRestorePercent)
        }
    }
}


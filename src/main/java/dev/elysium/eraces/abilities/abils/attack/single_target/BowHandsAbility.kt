package dev.elysium.eraces.abilities.abils.attack.single_target

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

@RegisterAbility
@Suppress("unused")
class BowHandsAbility : BaseCooldownAbility(id = "bowhands", defaultCooldown = "1m") {

    var minArrows = 5
    var maxArrows = 8
    var arrowDamage = 5.0
    var arrowFireIntervalTicks = 10

    override fun onActivate(player: Player) {

        val arrowsAmount = (minArrows..maxArrows).random();

        // нам нужен таск, потому что стрелять будем с интервалом
        val task = object : BukkitRunnable() {
            //считаем количество выпущеных стрел
            var arrowsFired = 0
            override fun run() {
                val arrow = player.launchProjectile(Arrow::class.java)
                arrow.damage = arrowDamage
                arrowsFired++

                //отменяем когда количество выпущенных стрел достигает arrowsAmount
                if (arrowsFired == arrowsAmount)
                    cancel()

            }
        }

        task.runTaskTimer(ERaces.getInstance(), 0, arrowFireIntervalTicks.toLong())

    }


    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("minArrows", minArrows)
            write("maxArrows", maxArrows)
            write("arrowDamage", arrowDamage)
            write("arrowFireIntervalTicks", arrowFireIntervalTicks)
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("minArrows", ::minArrows)
            read("maxArrows", ::maxArrows)
            read("arrowDamage", ::arrowDamage)
            read("arrowFireIntervalTicks", ::arrowFireIntervalTicks)
        }
    }
}
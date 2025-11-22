package dev.elysium.eraces.abilities.abils.buffs.self_buffs

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

class HolyShieldAbility : BaseCooldownAbility(id = "holyshield", defaultCooldown = "6m"), Listener {

    var minDamageBlockedPercent = 50;
    var maxDamageBlockedPercent = 80;

    val abilityActivatedByPlayer: MutableMap<UUID, Boolean> = mutableMapOf()

    override fun onActivate(player: Player) {
        abilityActivatedByPlayer.put(player.uniqueId, true)
    }

    @EventHandler
    private fun onPlayerTakeDamage(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player)
            return
        val player = event.entity as Player

        if (event.damager !is LivingEntity)
            return
        val target = event.damager as LivingEntity

        // если способность не активирована
        if (abilityActivatedByPlayer.get(player.uniqueId) != true)
            return


        // расчитываем процент
        // сначала находим случайное число между 50 и 80, а потом умножаем его на 0.01, чтобы получить процент
        val reflectedDamagePercent = (minDamageBlockedPercent..maxDamageBlockedPercent).random().toDouble() * 0.01;
        val reflectedDamage = event.damage * reflectedDamagePercent

        // отменяем атаку по игроку
        event.isCancelled = true

        // наносим урон врагу
        target.damage(reflectedDamage)

        // выключаем способность
        abilityActivatedByPlayer.put(player.uniqueId, false)
    }

    // очищаем мапу когда игрок выходит с сервера
    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        abilityActivatedByPlayer.remove(event.player.uniqueId)
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("minDamageBlockedPercent", ::minDamageBlockedPercent)
            read("maxDamageBlockedPercent", ::maxDamageBlockedPercent)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
           write("minDamageBlockedPercent", minDamageBlockedPercent)
           write("maxDamageBlockedPercent", maxDamageBlockedPercent)
        }
    }
}
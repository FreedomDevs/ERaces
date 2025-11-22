package dev.elysium.eraces.abilities.abils.attack.single_target

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

@RegisterAbility
@Suppress("unused")
class CursedTouchAbility : BaseCooldownAbility("cursedtouch", defaultCooldown = "2m"), Listener {

    // трекаем у кого активировалась абилка
    val abilityActivatedByPlayer: MutableMap<UUID, Boolean> = mutableMapOf()
    var killChance = 10.0
    // помечаем игрока как активировавшего абилку
    override fun onActivate(player: Player) {
        abilityActivatedByPlayer.put(player.uniqueId, true);
    }

    @EventHandler
    fun onPlayerHit(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player)
            return;
        val player = event.damager as Player;

        if (event.entity !is LivingEntity)
            return;
        val victim = event.entity as LivingEntity;

        val activated = abilityActivatedByPlayer.get(player.uniqueId) ?: false;
        if (activated) {
            val chance = (1..100).random();
            // убиваем сущность, по которой был нанесен удар если выпало число от 1 до killChance
            if (chance <= killChance)
                victim.health = 0.0;

            // абилка сработала, поэтому деактивируем способность
            abilityActivatedByPlayer.put(player.uniqueId, false)
        }
    }


    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // чистим мапу, когда игрок выходит с сервера
        abilityActivatedByPlayer.remove(event.player.uniqueId)
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("killChance", ::killChance)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        cfg.addDefault("killChance", killChance)
    }
}
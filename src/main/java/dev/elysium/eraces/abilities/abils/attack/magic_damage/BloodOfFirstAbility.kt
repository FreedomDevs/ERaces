package dev.elysium.eraces.abilities.abils.attack.magic_damage

import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.targetUtils.target.TargetTrail
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffectType
import java.util.UUID

@RegisterAbility
@Suppress("unused")
class BloodOfFirstAbility : Listener, BaseEffectsAbility(
    id = "blood_of_first", defaultCooldown = "30m",

    defaultEffects = linkedMapOf(
        "speed" to EffectData("2m", 3, PotionEffectType.SPEED),
    )
) {
    private var duration: String = "2m"
    private var clickCooldownMs: Long = 75
    private var damage: Double = 1.0
    private var maxDistance: Double = 25.0
    private var step: Double = 0.25

    data class WaitingPlayer(var lastActivateTime: Long, var lastClickTime: Long)

    private val playersWaiting: MutableMap<UUID, WaitingPlayer> = mutableMapOf()

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_AIR) return

        val player = event.player
        val uuid = player.uniqueId
        if (!playersWaiting.containsKey(uuid)) return

        val waitingPlayer = playersWaiting[uuid]!!
        val durationMs: Long = TimeParser.parseToMilliseconds(duration)
        val curtime = System.currentTimeMillis()
        if (waitingPlayer.lastActivateTime + durationMs < curtime) return
        if (waitingPlayer.lastClickTime + clickCooldownMs > curtime) return

        waitingPlayer.lastClickTime = curtime
        Target.Companion.from(player)
            .filter(*arrayOf(TargetFilter.ENTITIES, TargetFilter.FIRST_ENTITY))
            .trail(
                TargetTrail.Config(
                distance = maxDistance,
                step = step,
                particle = Particle.ELECTRIC_SPARK,
                stopAtBlock = true,
                onStep = { loc -> },
                onHit = { entity -> run {entity.damage(damage, player) }}
            ))
    }

    override fun customActivate(player: Player) {
        playersWaiting[player.uniqueId] = WaitingPlayer(System.currentTimeMillis(), 0)
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        super.loadCustomParams(cfg)
        ConfigHelper.with(cfg) {
            read("duration", ::duration)
            read("click_cooldown_ms", ::clickCooldownMs)
            read("damage", ::damage)
            read("max_distance", ::maxDistance)
            read("step", ::step)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        super.writeCustomDefaults(cfg)
        ConfigHelper.with(cfg) {
            write("duration", duration)
            write("click_cooldown_ms", clickCooldownMs)
            write("damage", damage)
            write("max_distance", maxDistance)
            write("step", step)
        }
    }
}
package dev.elysium.eraces.abilities.abils.control.stasis

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.EffectUtils
import dev.elysium.eraces.utils.TimeParser
import org.bukkit.event.Listener
import java.util.UUID
import dev.elysium.eraces.utils.targetUtils.Target
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

@RegisterAbility
@Suppress("unused")
class ForestAidAbility : BaseCooldownAbility(
    id = "forest_aid", defaultCooldown = "1m"
), Listener {
    private var duration: String = "5s"
    private var radius: Double = 40.0
    private var poisonLevel: Int = 1
    private var poisonDuration: String = "5s"
    private var particleCount: Int = 8
    private var fallingBlocks: Boolean = true
    private var fallingBlockMaterial: String = "OAK_WOOD"
    private var fallingBlockCount: Int = 3

    private val activePlayers = mutableMapOf<UUID, Long>()

    private val plugin = ERaces.getInstance()

    override fun onActivate(player: Player) {
        Target.from(player)
            .inRadius(radius)
            .excludeCaster()
            .execute { target ->
                if (target !is Player) return@execute
                val uuid = target.uniqueId
                activePlayers[uuid] = System.currentTimeMillis()

                EffectUtils.applyEffects(
                    target,
                    mapOf("minecraft:poison" to poisonLevel),
                    TimeParser.parseToTicks(poisonDuration).toInt()
                )

                AbilityUtils.runRepeatingForDuration(plugin, duration, 2L) { _ ->
                    val loc = target.location.clone().add(0.0, 1.0, 0.0)
                    for (i in 0 until particleCount) {
                        val angle = i * 2 * Math.PI / particleCount
                        val x = cos(angle)
                        val z = sin(angle)
                        target.world.spawnParticle(
                            Particle.WITCH,
                            loc.clone().add(x, i * 0.25, z),
                            1, 0.0, 0.0, 0.0, 0.0
                        )
                    }
                }

                if (fallingBlocks) {
                    val material = Material.matchMaterial(fallingBlockMaterial) ?: Material.OAK_WOOD
                    AbilityUtils.runRepeatingForDuration(plugin, duration, 4L) { _ ->
                        val loc = target.location.clone().add(0.0, 5.0, 0.0)
                        repeat(fallingBlockCount) {
                            val block = loc.world?.spawnFallingBlock(
                                loc.clone().add(Math.random() - 0.5, 0.0, Math.random() - 0.5),
                                material.createBlockData()
                            )
                            block?.velocity = Vector(0.0, -0.5, 0.0)
                        }
                    }
                }
            }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radius", ::radius)
            read("duration", ::duration)
            read("poison_level", ::poisonLevel)
            read("poison_duration", ::poisonDuration)
            read("particle_count", ::particleCount)
            read("falling_blocks", ::fallingBlocks)
            read("falling_block_material", ::fallingBlockMaterial)
            read("falling_block_count", ::fallingBlockCount)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radius)
            write("duration", duration)
            write("poison_level", poisonLevel)
            write("poison_duration", poisonDuration)
            write("particle_count", particleCount)
            write("falling_blocks", fallingBlocks)
            write("falling_block_material", fallingBlockMaterial)
            write("falling_block_count", fallingBlockCount)
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val uuid = event.player.uniqueId
        val start = activePlayers[uuid] ?: return
        val elapsed = System.currentTimeMillis() - start
        if (elapsed < TimeParser.parseToMilliseconds(duration)) {
            event.isCancelled = true
        } else {
            activePlayers.remove(uuid)
        }
    }

    @EventHandler
    fun onPlayerAttack(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val uuid = event.damager.uniqueId
        val start = activePlayers[uuid] ?: return
        val elapsed = System.currentTimeMillis() - start
        if (elapsed < TimeParser.parseToMilliseconds(duration)) {
            event.isCancelled = true
        } else {
            activePlayers.remove(uuid)
        }
    }
}
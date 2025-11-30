package dev.elysium.eraces.abilities.abils.attack.aoe

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.AbilityUtils
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.utils.TimeParser
import dev.elysium.eraces.utils.targetUtils.Target
import dev.elysium.eraces.utils.targetUtils.effects.EffectsTarget
import dev.elysium.eraces.utils.targetUtils.effects.Executor
import dev.elysium.eraces.utils.targetUtils.target.TargetFilter
import dev.elysium.eraces.utils.vectors.RadiusFillBuilder
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Registry
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import java.util.UUID

@RegisterAbility
@Suppress("unused")
class ForestChildAbility : BaseCooldownAbility(id = "forestchild", defaultCooldown = "2m"), Listener {
    private var radius: Double = 40.0
    private var targetAmount: Int = 5
    private var damage: Double = 10.0
    private var stunDuration: String = "5s"

    private var effectDuration: String = "9s"
    private var effect: String = "blindness"

    private val bukkitEffect = Registry.POTION_EFFECT_TYPE.get(NamespacedKey(ERaces.getInstance(), effect))

    override fun onActivate(player: Player) {
        require(bukkitEffect != null) { "Нет такого эффекта $effect" }

        // берем все сущности в радиусе
        val entities =Target
            .from(player)
            .filter(TargetFilter.ENTITIES)
            .inRadius(radius, useRaycast = false)
            .getEntities()

        //берем 5 ближайших сущностей
        val firstFiveClosestEntities = entities
            .sortedBy { player.location.distance(it.location) }
            .take(targetAmount)

        //для каждой сущности выполняем действия
        for (entity in firstFiveClosestEntities) {

            Target.from(entity)
                .execute { it.addPotionEffects(listOf(PotionEffect(bukkitEffect, TimeParser.parseToTicks(effectDuration).toInt(), 0))) }
                .execute { it.damage(damage) }
                .execute {
                    //добавляем сущность в оглушенные и через время убираем
                    stunnedEntities[it.uniqueId] = it
                    AbilityUtils.runLater(ERaces.getInstance(), stunDuration) {
                        stunnedEntities.remove( it.uniqueId)
                    }
                }
                .executeEffects(
                    //партиклы оглушения
                    EffectsTarget()
                        .from(Executor.PLAYER(player))
                        .dust(Particle.DustOptions(Color.ORANGE, 1.0f))
                        .duration(TimeParser.parseToTicks(stunDuration).toInt())
                        .math(
                            RadiusFillBuilder()
                                .circle(1.2)
                                .filled(false)
                                .step(0.1)
                                .interpolationFactor(2)
                        )
                )
        }
    }

    private val stunnedEntities = mutableMapOf<UUID, LivingEntity>()

    @EventHandler
    private fun onEntityMove(event: EntityMoveEvent) {
        if (stunnedEntities[event.entity.uniqueId] != null) {
            event.isCancelled = true
        }
    }
    @EventHandler
    private fun onPlayerMove(event: PlayerMoveEvent) {
        if (stunnedEntities[event.player.uniqueId] != null) {
            event.isCancelled = true
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("radius", radius)
            write("targetAmount", targetAmount)
            write("damage", damage)
            write("stunDuration", stunDuration)
            write("effectDuration", effectDuration)
            write("effect", effect)
        }
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("radius", ::radius)
            read("targetAmount", ::targetAmount)
            read("damage", ::damage)
            read("stunDuration", ::stunDuration)
            read("effectDuration", ::effectDuration)
            read("effect", ::effect)
        }
    }
}
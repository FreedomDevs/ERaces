package dev.elysium.eraces.abilities.abils

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.abilities.ConfigHelper
import dev.elysium.eraces.abilities.RegisterAbility
import dev.elysium.eraces.abilities.abils.base.BaseCooldownAbility
import dev.elysium.eraces.abilities.abils.base.BaseEffectsAbility
import dev.elysium.eraces.utils.EffectUtils
import dev.elysium.eraces.utils.TimeParser
import io.papermc.paper.registry.RegistryAccess
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.sqrt

@RegisterAbility
@Suppress("unused")
class ThroughSkiesAbility : BaseCooldownAbility("throughskies", defaultCooldown = "30s", comboKey = "3535") {

    private var verticalDistance = 10.0
    private var teleportDistance = 20.0
    private var lineThickness = 6.0

    private var damage = 7.0

    private var effectLevel = 10
    private var effectDurarion = "2s"
    private var effect = "slowness"

    private val bukkitEffect = Registry.POTION_EFFECT_TYPE.get(NamespacedKey(ERaces.getInstance(), effect))

    override fun onActivate(player: Player) {
        // применяем вертикальное ускорение по оси Y
        player.velocity = Vector(0.0, calculateLaunchVelocity(verticalDistance), 0.0)

        val task = object : BukkitRunnable() {
            //после того, как игрок пролетит 20 блоков, нам нужно выполнить этот код,
            //игрок телепортируется по направлению прицела
            //другие игроки получают урон и замедление на линии телепортации
            override fun run() {
                val playerInitialLocation = player.location.clone()
                val playerTeleportLocation = getTeleportLocation(player)

                player.teleport(playerTeleportLocation);

                val entities =
                    getAllEntitiesOnLineIgnoringY(playerInitialLocation, playerTeleportLocation, lineThickness)

                applyEffectsToEntities(entities, player)
            }
        }

        // телепортируем игрока через время полёта
        task.runTaskLater(ERaces.getInstance(), calculateLaunchTimeTicks(teleportDistance))
    }

    private fun applyEffectsToEntities(entities: List<LivingEntity>, damageSource: Entity) {
        //применяем эффекты к сущностям
        for (entity in entities) {
            if (entity.uniqueId == damageSource.uniqueId) return

            entity.damage(damage, damageSource)
            require(bukkitEffect != null) { "Нет такого эффекта $effect" }
            entity.addPotionEffect(
                PotionEffect(
                    bukkitEffect,
                    TimeParser.parseToTicks(effectDurarion).toInt(),
                    effectLevel - 1
                )
            )
        }
    }

    private fun getTeleportLocation(player: Player): Location {
        val loc = player.location.clone();
        val dir = loc.direction.normalize();

        var lastLocation: Location = loc;
        for (i in 1..teleportDistance.toInt()) {
            val checkLoc = loc.clone().add(dir.clone().multiply(i));

            if (checkLoc.world.getBlockAt(checkLoc).type.isSolid())
                break;
            else
                lastLocation = checkLoc;

        }
        return lastLocation;
    }

    // deepSeek сказал, что так можно расчитать скорость, чтобы подлететь ровно на какое-то количество блоков
    private fun calculateLaunchVelocity(targetHeight: Double): Double {
        val gravity = 0.08 // Гравитация в Minecraft (блоков/тик²)
        val velocity = sqrt(2 * gravity * targetHeight)

        return velocity
    }

    //считаем, сколько времени занимает полёт
    //может быть лучше надо слушать PlayerMoveEvent и ждать, пока вертикальная скорость не упадет до 0
    private fun calculateLaunchTimeTicks(targetHeight: Double): Long {
        val velocity = calculateLaunchVelocity(targetHeight)

        val timeTicks = targetHeight / velocity
        //округляем потому что timeTicks скорее всего дробное, а тики это целое число
        val timeTicksRounded = Math.floor(timeTicks)

        return timeTicksRounded.toLong()
    }


    //получаем список живых сущностей на линии
    //код написан  deepSeek
    private fun getAllEntitiesOnLineIgnoringY(
        start: Location,
        end: Location,
        lineThickness: Double
    ): List<LivingEntity> {
        val world = start.world ?: return emptyList()

        // Создаем направляющий вектор линии (игнорируем Y)
        val direction = Vector(
            end.x - start.x,
            0.0, // Игнорируем Y
            end.z - start.z
        ).normalize()

        // Получаем все живые сущности в радиусе от линии
        val maxDistance = start.distance(end)
        val nearbyEntities = world.getNearbyEntities(
            start,
            maxDistance + lineThickness,
            world.maxHeight.toDouble(), // Большая высота для поиска по вертикали
            maxDistance + lineThickness
        )

        return nearbyEntities.filterIsInstance<LivingEntity>().filter { entity ->
            isEntityOnLine(entity, start, direction, maxDistance, lineThickness)
        }
    }

    //проверяем, что сущность находится на линии
    //код написан  deepSeek
    private fun isEntityOnLine(
        entity: LivingEntity,
        lineStart: Location,
        lineDirection: Vector,
        lineLength: Double,
        thickness: Double
    ): Boolean {
        val entityLoc = entity.location

        // Вектор от начала линии к сущности (игнорируем Y)
        val toEntity = Vector(
            entityLoc.x - lineStart.x,
            0.0,
            entityLoc.z - lineStart.z
        )

        // Проекция вектора toEntity на направление линии
        val dotProduct = toEntity.dot(lineDirection)

        // Проверяем, что сущность находится в пределах длины линии
        if (dotProduct < 0 || dotProduct > lineLength) {
            return false
        }

        // Вычисляем перпендикулярное расстояние от сущности до линии
        val projection = lineDirection.clone().multiply(dotProduct)
        val perpendicular = toEntity.clone().subtract(projection)
        val distanceToLine = perpendicular.length()

        // Проверяем, что сущность находится в пределах толщины линии
        return distanceToLine <= thickness
    }

    override fun loadCustomParams(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            read("damage", ::damage)
            read("effect", ::effect)
            read("effectLevel", ::effectLevel)
            read("effectDuration", ::effectDurarion)
            read("verticalDistance", ::verticalDistance)
            read("teleportDistance", ::teleportDistance)
            read("lineThickness", ::lineThickness)
        }
    }

    override fun writeCustomDefaults(cfg: YamlConfiguration) {
        ConfigHelper.with(cfg) {
            write("damage", damage)
            write("effect", effect)
            write("effectLevel", effectLevel)
            write("effectDuration", effectDurarion)
            write("verticalDistance", verticalDistance)
            write("teleportDistance", teleportDistance)
            write("lineThickness", lineThickness)
        }
    }
}

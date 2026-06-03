package dev.elysium.eraces.updaters

import dev.elysium.eraces.datatypes.Race
import dev.elysium.eraces.updaters.base.IDisableable
import dev.elysium.eraces.updaters.base.IUnloadable
import dev.elysium.eraces.updaters.base.IUpdater
import dev.elysium.eraces.updaters.damage.*
import dev.elysium.eraces.updaters.speed.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

object UpdatersReloader {
    private val updaters: List<Any> = listOf(
        // Атрибуты
        MaxHealthUpdater(),
        AdditionalArmorUpdater(),

        HealthRegenUpdater(),
        ShieldUsageUpdater(),
        ForbiddenFoodsUpdater(),
        HandDamageUpdater(),
        AntiKnockbackLevelUpdater(),
        AxeDamageUpdater(),
        SwordDamageUpdater(),
        BowDamageUpdater(),
        PeacefulMobsAfraidUpdater(),
        BowSpeedUpdater(),
        MaceDamageUpdater(),
        DualWeaponDamageUpdater(),
        DamageUpdater(),
        AntiKnockbackLevelWithIronArmorAndMoreUpdater(),
        DamageAdditionalWithLowerThanIronArmorUpdater(),
        BlockReachUpdater(),
        DamageWithWolfsNearUpdater(),
        SaturationUpdater(),
        SlownessWithIronAndMoreArmorListener(),
        EffectsUpdater(),
        NeutralMobsUpdater(),
        ClumsinessUpdater(),
        AdditionalFireDamage(),
        FallDamageUpdater(),
        EffectsTargetingUpdater(),
        SecondLifeUpdater(),
        OxygenBonusUpdater(),
        AttackSpeedUpdater(),
        MoveSpeedUpdater(),
        BaseDamageUpdater(),
        AdditionalScaleUpdater(),
        ManaRegenerationUpdater(),
        ChanceResurrectionUpdater(),
        MissingChanceUpdater(),
    )

    fun reloadUpdatersForPlayer(player: Player, race: Race) {
        for (obj in updaters)
            if (obj is IUpdater)
                obj.update(race, player)
    }

    fun registerUpdatersListeners(plugin: JavaPlugin) {
        for (obj in updaters)
            if (obj is Listener)
                Bukkit.getPluginManager().registerEvents(obj, plugin)
    }

    fun unloadPlayerDataFromUpdaters(player: Player) {
        for (obj in updaters)
            if (obj is IUnloadable)
                obj.unload(player)
    }

    fun disableUpdatersForPlayers(player: Player) {
        for (obj in updaters)
            if (obj is IDisableable)
                obj.disable(player)
    }
}
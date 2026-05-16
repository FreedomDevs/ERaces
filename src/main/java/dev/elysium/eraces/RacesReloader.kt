package dev.elysium.eraces

import dev.elysium.eraces.ERaces.Companion.getInstance
import dev.elysium.eraces.items.RaceChangePotion
import dev.elysium.eraces.listeners.*
import dev.elysium.eraces.listeners.PluginMessageListener.Companion.sendAbilities
import dev.elysium.eraces.updaters.*
import dev.elysium.eraces.updaters.base.IUnloadable
import dev.elysium.eraces.updaters.base.IUpdater
import dev.elysium.eraces.updaters.damage.*
import dev.elysium.eraces.updaters.speed.SlownessWithIronAndMoreArmorListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

object RacesReloader : Listener {
    private val updaters: List<Any> = listOf(
        HealthUpdater(),
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
    )

    private val listeners: List<Listener> = listOf(
        PlayerJoinListener(),
        PlayerQuitListener(),
        PlayerRespawnListener(),
        RaceChangeGuiListener(),
        RaceChangePotion(),
        PlayerShootBowEventListener(),
        ManaRegenerationListener(),
        ChanceResurrectionListener(),
        MissingChanceListener(),
    )

    fun reloadRaceForPlayer(player: Player) {
        val race = getInstance().context.playerDataManager.getPlayerRace(player) ?: return

        for (obj in updaters)
            if (obj is IUpdater)
                obj.update(race, player)

        sendAbilities(player)
    }

    fun reloadRaceForAllPlayers() {
        for (i in Bukkit.getOnlinePlayers())
            reloadRaceForPlayer(i)
    }

    // Включает все листенеры
    fun startListeners(plugin: JavaPlugin) {
        for (obj in updaters)
            if (obj is Listener)
                Bukkit.getPluginManager().registerEvents(obj, plugin)

        for (obj in listeners)
            Bukkit.getPluginManager().registerEvents(obj, plugin)
    }

    // Чистит какой-то мусор, TODO надо потом разобратся с ним
    fun unloadPlayerData(player: Player) {
        for (obj in updaters)
            if (obj is IUnloadable)
                obj.unload(player)
    }
}
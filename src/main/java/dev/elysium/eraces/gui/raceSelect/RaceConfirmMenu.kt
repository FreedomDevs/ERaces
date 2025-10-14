package dev.elysium.eraces.gui.raceSelect

import dev.elysium.eraces.ERaces
import dev.elysium.eraces.RacesReloader
import dev.elysium.eraces.VisualsManager
import dev.elysium.eraces.gui.core.GuiBase
import dev.elysium.eraces.gui.core.GuiButton
import dev.elysium.eraces.gui.core.GuiManager
import dev.elysium.eraces.utils.ChatUtil
import org.bukkit.Material
import org.bukkit.entity.Player

class RaceConfirmMenu(
    player: Player,
    private val race: RacePage
) : GuiBase(player, "Подтверждение выбора", 27) {

    init {
        preventClose = true
        closeMessage = "<red>Ты должен выбрать расу, прежде чем продолжить!"

    }

    override fun setup() {
        setButton(11, GuiButton.of(Material.LIME_WOOL, "§aДа, выбрать ${race.displayName}") {
            GuiManager.getOpenMenu(player)?.preventClose = false
            GuiManager.close(player)
            player.closeInventory()
            ChatUtil.sendAction(player, "<green>Ты выбрал расу: <gold>${race.displayName}")
            ERaces.getInstance().context.playerDataManager.setPlayerRace(player, race.id)
            RacesReloader.reloadRaceForPlayer(player)
            VisualsManager.updateVisualsForPlayer(player)
        })

        setButton(15, GuiButton.of(Material.RED_WOOL, "§cНет, вернуться") {
            GuiManager.getOpenMenu(player)?.preventClose = false
            GuiManager.close(player)
            player.closeInventory()
            RaceSelectMenu(player).open()
        })
    }
}

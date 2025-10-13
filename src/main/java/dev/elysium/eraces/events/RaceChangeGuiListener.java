package dev.elysium.eraces.events;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.RacesReloader;
import dev.elysium.eraces.VisualsManager;
import dev.elysium.eraces.gui.RaceChangeGui;
import dev.elysium.eraces.items.RaceItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RaceChangeGuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof RaceChangeGui)) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        PersistentDataContainer pdc = clicked.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(RaceItems.RACE_GUI_ITEM_KEY, PersistentDataType.STRING)) return;

        String raceKey = pdc.get(RaceItems.RACE_GUI_ITEM_KEY, PersistentDataType.STRING);
        Player player = (Player) event.getWhoClicked();

        var context = ERaces.getInstance().getContext();
        context.getPlayerDataManager().setPlayerRace(player, raceKey);

        RacesReloader.reloadRaceForPlayer(player);
        VisualsManager.updateVisualsForPlayer(player);

        player.sendMessage("§aВы выбрали расу: §f" + raceKey);
        player.closeInventory();
    }
}

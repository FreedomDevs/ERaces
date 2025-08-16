package dev.fdp.races.gui;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;
import dev.fdp.races.RacesReloader;
import dev.fdp.races.items.RaceItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RaceChangeGUI implements Listener {
    private static final Map<String, String> raceKeyByName = new HashMap<>();
    private static final Set<Player> selectedPlayers = new HashSet<>();

    static {
        for (Race i : FDP_Races.getInstance().getRacesConfigManager().getRaces().values())
            raceKeyByName.put(i.getRaceGuiConfig().getName(), i.getId());
    }

    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, centerTitle("§5Выбор Расы"));

        String currentRace = FDP_Races.getInstance().getPlayerDataManager().getPlayerRace(player.getName());

        gui.setItem(10, RaceItems.elf(currentRace));
        gui.setItem(12, RaceItems.human(currentRace));
        gui.setItem(14, RaceItems.bearBeastman(currentRace));
        gui.setItem(16, RaceItems.wolfBeastman(currentRace));
        gui.setItem(28, RaceItems.tigerBeastman(currentRace));
        gui.setItem(30, RaceItems.ork(currentRace));

        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) { // Подтупливает чуток
        String rawTitle = event.getView().getTitle().replaceAll(" ", "");
        if (!rawTitle.contains("§5ВыборРасы"))
            return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        selectedPlayers.add(player);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())
            return;

        String displayName = clicked.getItemMeta().getDisplayName();

        if (raceKeyByName.containsKey(displayName)) {
            String raceKey = raceKeyByName.get(displayName);

            FDP_Races.getInstance().getPlayerDataManager().setPlayerRace(player.getName(), raceKey);
            RacesReloader.reloadRaceForPlayer(player);
            player.sendMessage("§aВы выбрали расу: §f" + displayName);

            player.closeInventory();
        }
    }

    public static String centerTitle(String title) {
        int maxLength = 32;
        int spaceWidth = 2;
        int titleLength = title.replaceAll("§.", "").length();
        int spacesToAdd = (maxLength - titleLength) / spaceWidth;
        return " ".repeat(Math.max(0, spacesToAdd)) + title;
    }
}

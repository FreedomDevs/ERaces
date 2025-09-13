package dev.elysium.eraces.gui;

import dev.elysium.eraces.ERaces;
import dev.elysium.eraces.datatypes.Race;
import dev.elysium.eraces.items.RaceItems;
import dev.elysium.eraces.utils.ChatUtil;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class RaceChangeGui implements InventoryHolder {
    private final Inventory inventory;

    public RaceChangeGui(Player player) {
        int[] ids = {10, 12, 14, 16, 28, 30, 32, 34};
        this.inventory = Bukkit.createInventory(this, 54, ChatUtil.text(centerTitle("Выбор Расы"), NamedTextColor.DARK_PURPLE));

        Race currentRace = ERaces.getPlayerMng().getPlayerRace(player);

        int i = 0;
        for (Race r : ERaces.getRacesMng().getRaces().values()) {
            boolean b = r == currentRace;
            this.inventory.setItem(ids[i], RaceItems.getItem(r, b));
            i++;
        }
    }

    public static void open(Player player) {
        player.openInventory(new RaceChangeGui(player).getInventory());
    }

    public static String centerTitle(String title) {
        int maxLength = 32;
        int spaceWidth = 2;
        int titleLength = title.replaceAll("§.", "").length();
        int spacesToAdd = (maxLength - titleLength) / spaceWidth;
        return " ".repeat(Math.max(0, spacesToAdd)) + title;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}

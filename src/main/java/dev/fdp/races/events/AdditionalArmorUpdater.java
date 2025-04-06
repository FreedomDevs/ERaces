package dev.fdp.races.events;

import dev.fdp.races.Race;
import dev.fdp.races.RaceManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AdditionalArmorUpdater implements Listener {
    private final RaceManager raceManager;

    public AdditionalArmorUpdater(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    public static void updateAdditionalArmor(RaceManager raceManager, Player player) {
        String playerRace = raceManager.getPlayerRace(player.getName());
        Race race = raceManager.getRaces().get(playerRace);
        double additionalArmor = race.getAdditionalArmor();

        AttributeInstance attibute = player.getAttribute(Attribute.GENERIC_ARMOR);
        if  (attibute != null) {
            double baseAttribute = attibute.getBaseValue();
            attibute.setBaseValue(baseAttribute + additionalArmor);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateAdditionalArmor(raceManager, event.getPlayer());
    }
}


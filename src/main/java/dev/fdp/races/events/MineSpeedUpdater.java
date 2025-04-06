package dev.fdp.races.events;

import dev.fdp.races.Race;
import dev.fdp.races.RaceManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MineSpeedUpdater implements Listener {
    private final RaceManager raceManager;

    public MineSpeedUpdater(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    public static void updateMineSpeed(RaceManager raceManager, Player player) {
        String playerRace = raceManager.getPlayerRace(player.getName());

        Race race = raceManager.getRaces().get(playerRace);

        double mineSpeed = race.getMineSpeed();

        AttributeInstance attibute = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if  (attibute != null) {
            double baseAttibute = attibute.getBaseValue();
            attibute.setBaseValue(baseAttibute * mineSpeed);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateMineSpeed(raceManager, event.getPlayer());
    }

}

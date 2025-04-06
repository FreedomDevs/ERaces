package dev.fdp.races.events;

import dev.fdp.races.Race;
import dev.fdp.races.RaceManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AttackRangeUpdater implements Listener {
    private final RaceManager raceManager;

    public AttackRangeUpdater(RaceManager raceManager) {
        this.raceManager = raceManager;
    }

    public static void updateAttackRange(RaceManager raceManager, Player player) {
        String playerRace = raceManager.getPlayerRace(player.getName());
        Race race = raceManager.getRaces().get(playerRace);
        int attackRange = race.getHandDistanceBonus();

        AttributeInstance attibute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if  (attibute != null) {
            double baseAttribute = attibute.getBaseValue();
            attibute.setBaseValue(baseAttribute + attackRange);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateAttackRange(raceManager, event.getPlayer());
    }
}

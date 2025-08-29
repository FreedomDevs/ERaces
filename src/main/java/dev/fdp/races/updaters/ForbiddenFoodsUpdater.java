package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.RaceAttributeMapStorage;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;
import java.util.Map;

public class ForbiddenFoodsUpdater extends RaceAttributeMapStorage<List<String>> implements Listener {
    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        List<String> param = getParam(player);
        if (param == null || !param.contains(event.getItem().getType().toString())) return;

        player.sendActionBar(ChatUtil.format(FDP_Races.getMsgMng().getString("forbidden_foods"), Map.of()));
        player.playSound(
                player.getLocation(),
                Sound.ENTITY_VILLAGER_NO,
                1.0f,
                1.0f);
        player.spawnParticle(Particle.ANGRY_VILLAGER, player.getLocation(), 100, 0.5, 0.7, 0.5, 0.4);
        event.setCancelled(true);
    }

    @Override
    protected List<String> getAttribute(Race race) {
        return race.getForbiddenFoods();
    }

    @Override
    protected boolean putCheck(List<String> att) {
        return !att.isEmpty();
    }
}

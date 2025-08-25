package dev.fdp.races.updaters;

import dev.fdp.races.datatypes.Race;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class DamageWithWolfsNearUpdater implements Listener, IUpdater, IUnloadable {
    private static final HashMap<String, Double> damageNicknames = new HashMap<>();
    private static final double nearby_radius = 15d;

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(damageNicknames.containsKey(player.getName()))) return;

        int wolvesNearby = 0;
        for (Entity entity : player.getNearbyEntities(nearby_radius, nearby_radius, nearby_radius)) {
            if (entity.getType() == EntityType.WOLF && entity.getLocation().distance(player.getLocation()) <= nearby_radius)
                wolvesNearby++;
        }

        // доп дамаг f(x) в зависимости от количества волков неподалёку x выражается по формуле f(x) = a * 2x/(x+1),
        // где a - базовый дополнительный урон. Таким образом, когда около игрока 1 волк, то доп. урон равен a.
        // При этом при x → ∞ f(x) → 2*a, то есть максимальный дополнительный урон равен 2*a.
        double baseDamage = damageNicknames.get(player.getName());
        double additionalDamage = baseDamage * 2 * wolvesNearby / (wolvesNearby + 1);

        event.setDamage(event.getDamage() + additionalDamage);
    }

    @Override
    public void update(Race race, Player player) {
        double base_group_damage = race.getWeaponProficiency().getDamageAdditionalWithWolfsNear();
        if (base_group_damage != 0)
            damageNicknames.put(player.getName(), base_group_damage);
        else
            damageNicknames.remove(player.getName());
    }


    @Override
    public void unload(Player player) {
        damageNicknames.remove(player.getName());
    }
}

package dev.fdp.races.updaters.damage;

import dev.fdp.races.datatypes.Race;
import dev.fdp.races.updaters.base.BaseDamageUpdater;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DamageWithWolfsNearUpdater extends BaseDamageUpdater {
    private static final double nearby_radius = 15d;

    {
        OP = (a, param, player) -> {
            int wolvesNearby = 0;
            for (Entity entity : player.getNearbyEntities(nearby_radius, nearby_radius, nearby_radius)) {
                if (entity.getType() == EntityType.WOLF && entity.getLocation().distance(player.getLocation()) <= nearby_radius)
                    wolvesNearby++;
            }

            // доп дамаг f(x) в зависимости от количества волков неподалёку x выражается по формуле f(x) = a * 2x/(x+1),
            // где a - базовый дополнительный урон. Таким образом, когда около игрока 1 волк, то доп. урон равен a.
            // При этом при x → ∞ f(x) → 2*a, то есть максимальный дополнительный урон равен 2*a.
            double additionalDamage = param * 2 * wolvesNearby / (wolvesNearby + 1);
            return a + additionalDamage;
        };
    }

    @Override
    protected boolean playerCheck(Player player) {
        return true;
    }

    @Override
    protected Double getAttribute(Race race) {
        return race.getWeaponProficiency().getDamageAdditionalWithWolfsNear();
    }

    @Override
    protected boolean putCheck(Double att) {
        return att != 0;
    }
}

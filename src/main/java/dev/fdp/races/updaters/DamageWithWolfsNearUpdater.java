package dev.fdp.races.updaters;

import dev.fdp.races.Race;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Objects;

public class DamageWithWolfsNearUpdater implements Listener, IUpdater, IUnloadable {
    private static final HashMap<String, Double> damageNicknames = new HashMap<>();
    private static final double nearby_radius = 15d;



    public static double Q_rsqrt(double x) {
        double xhalf = 0.5d * x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5d - xhalf * x * x);
        return x;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(damageNicknames.containsKey(player.getName()))) return;

        int wolvesNearby = 0;

        for(String nickname : damageNicknames.keySet()) {
            if (nickname.equals(player.getName()))
                continue;

            Player xyiZnaet = Bukkit.getServer().getPlayer(nickname);
            if (xyiZnaet != null && xyiZnaet.isOnline() && xyiZnaet.getLocation().distance(player.getLocation()) <= nearby_radius) {
                wolvesNearby++;
            }
        }

        double baseDamage = damageNicknames.get(player.getName());
        double additionalDamage;
        // доп дамаг f(x) в зависимости от количества волков неподалёку x выражается по формуле f(x) = (2-1/(√x)) * a,
        // где а - базовый дополнительный урон. Таким образом, когда около игрока 1 волк, то доп. урон равен a.
        // При этом при x → ∞ f(x) → 2*a, то есть максимальный дополнительный урон равен 2*a.

        if (wolvesNearby == 0) {
            additionalDamage = baseDamage;
        } else {
            additionalDamage = (2 - Q_rsqrt(wolvesNearby)) * baseDamage;
        }


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
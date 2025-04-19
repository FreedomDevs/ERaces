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



    private static float rsqrt(float x) { // это буквально 1/√x
        final float x2 = 0.5f * x;

        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);          //тест на программиста xDD
        float y = Float.intBitsToFloat(i);
        y *= (1.5f - x2 * y * y);
        return y;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(damageNicknames.containsKey(player.getName()))) return;

        int wolvesNearby = 0;

        for(String nickname : damageNicknames.keySet()) {
            if (nickname.equals(player.getName()))
                continue;

            if(Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).getLocation().distance(player.getLocation()) <= nearby_radius)
                wolvesNearby++;
        }

        double additionalDamage = (2 - rsqrt(wolvesNearby)) * damageNicknames.get(player.getName());
        // доп дамаг f(x) в зависимости от количества волков неподалёку x выражается по формуле f(x) = (2-1/(√x)) * a,
        // где а - базовый дополнительный урон. Таким образом, когда около игрока 1 волк, то доп. урон равен a.
        // При этом при x → ∞ f(x) → 2*a, то есть максимальный дополнительный урон равен 2*a.

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
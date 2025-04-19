package dev.fdp.races.updaters;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.Race;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.Objects;

public class DamageWithWolfsNearUpdater implements Listener, IUpdater, IUnloadable {
    private static final ArrayList<String> wolvesOnline = new ArrayList<>();
    private static final double nearby_radius = 15d;
    private static final double base_group_damage = 2d; // урон, когда 1 волк рядом


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
        if (!(wolvesOnline.contains(player.getName()))) return;

        int wolvesNearby = 0;

        for(String nickname : wolvesOnline) {
            if (nickname.equals(player.getName()))
                continue;

            if(Objects.requireNonNull(Bukkit.getServer().getPlayer(nickname)).getLocation().distance(player.getLocation()) <= nearby_radius)
                wolvesNearby++;
        }

        double additionalDamage = (2 - rsqrt(wolvesNearby)) * base_group_damage;
        // доп дамаг f(x) в зависимости от количества волков неподалёку x выражается по формуле f(x) = (2-1/(√x)) * a,
        // где а - базовый дополнительный урон. Таким образом, когда около игрока 1 волк, то доп. урон равен a.
        // При этом при x → ∞ f(x) → 2*a, то есть максимальный дополнительный урон равен 2*a.

        event.setDamage(event.getDamage() + additionalDamage);
    }

    @Override
    public void update(Race race, Player player) {

        if (race.getId().equals("wolf_beastman"))
            wolvesOnline.add(player.getName());
        else
            wolvesOnline.remove(player.getName());

    }


    @Override
    public void unload(Player player) {
        wolvesOnline.remove(player.getName());
    }
}
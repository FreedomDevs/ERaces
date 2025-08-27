package dev.fdp.races.modifiers;

import org.bukkit.entity.Player;

import java.util.UUID;

public class ModifierAdapter<K> {
    UUID key;
    Modifier<K> target;

    ModifierAdapter(UUID key, Modifier<K> target) {
        this.key = key;
        this.target = target;
    }

    public void set(Player player, K value) {
        target.data.get(key).put(player.getName(), value);
        target.apply(player);
    }

    public K get(Player player) {
        return target.data.get(key).get(player.getName());
    }
}

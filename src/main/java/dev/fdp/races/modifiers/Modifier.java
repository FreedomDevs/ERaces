package dev.fdp.races.modifiers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Modifier<K> {
    final Map<UUID, Map<String, K>> data = new HashMap<>();

    public ModifierAdapter<K> register() {
        UUID key = UUID.randomUUID();
        data.put(key, new HashMap<>());
        return new ModifierAdapter<>(key, this);
    }

    abstract public K collectData(Player player);

    abstract public void apply(Player player);
}

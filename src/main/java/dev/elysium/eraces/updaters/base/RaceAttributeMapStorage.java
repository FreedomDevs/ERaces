package dev.elysium.eraces.updaters.base;

import dev.elysium.eraces.datatypes.Race;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class RaceAttributeMapStorage<K> implements IUpdater, IUnloadable {
    protected final Map<UUID, K> map = new HashMap<>();

    abstract protected K getAttribute(Race race);

    abstract protected boolean putCheck(K att);

    protected K getParam(Player player) {
        return map.get(player.getUniqueId());
    }

    @Override
    public void update(Race race, Player player) {
        K att = getAttribute(race);
        UUID uuid = player.getUniqueId();
        if (putCheck(att))
            map.put(uuid, att);
        else
            map.remove(uuid);
    }

    @Override
    public void unload(Player player) {
        map.remove(player.getUniqueId());
    }
}

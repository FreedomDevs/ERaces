package dev.elysium.eraces.modifiers;

import dev.elysium.eraces.utils.MovementUtils;
import org.bukkit.entity.Player;

public class SpeedModifier extends Modifier<Integer> {
    @Override
    public Integer collectData(Player player) {
        String name = player.getName();
        return data.values().stream().map(map -> map.getOrDefault(name, 0)).reduce(0, Integer::sum);
    }

    @Override
    public void apply(Player player) {
        MovementUtils.setSpeedAttributeLevel(player, collectData(player));
    }
}

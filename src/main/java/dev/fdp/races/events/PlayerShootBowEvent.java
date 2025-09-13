package dev.fdp.races.events;

import lombok.Getter;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerShootBowEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final AbstractArrow arrow;

    public PlayerShootBowEvent(Player player, AbstractArrow arrow) {
        this.player = player;
        this.arrow = arrow;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}

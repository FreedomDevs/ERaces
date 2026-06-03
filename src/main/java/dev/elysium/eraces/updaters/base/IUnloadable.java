package dev.elysium.eraces.updaters.base;

import org.bukkit.entity.Player;

// Отвечает за выгрузку лишнего мусора из памяти
// Вызывается когда игрок ливает с сервера
public interface IUnloadable {
    void unload(Player player);
}

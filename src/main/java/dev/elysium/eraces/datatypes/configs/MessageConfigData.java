package dev.elysium.eraces.datatypes.configs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageConfigData {
    @ConfigsProperty(path = "plugin.enabled", defaultString = "Плагин включен!")
    public String pluginEnabled;

    @ConfigsProperty(path = "plugin.disabled", defaultString = "Плагин выключен")
    public String pluginDisabled;

    @ConfigsProperty(path = "commands.get_player_race.race_check_success", defaultString = "<green>У игрока <yellow>{player} <green>раса <yellow>{race}")
    public String getPlayerRaceSuccess;

    @ConfigsProperty(path = "commands.get_player_race.race_check_success_me", defaultString = "<green>Ваша раса <yellow>{race}")
    public String getPlayerRaceSuccessMe;

    @ConfigsProperty(path = "commands.regenerate_player_race.regenerate_success", defaultString = "<green>Раса игрока <yellow>{player} <green>установлена на: <yellow>{race}")
    public String regenerateSuccess;

    @ConfigsProperty(path = "commands.set_player_race.race_not_found", defaultString = "<red>Такая раса не существует")
    public String setPlayerRaceNotFound;

    @ConfigsProperty(path = "commands.set_player_race.set_success", defaultString = "<green>Раса игрока <yellow>{player} <green>установлена на: <yellow>{race}")
    public String setPlayerRaceSuccess;

    @ConfigsProperty(path = "commands.reload.reload_success", defaultString = "<green>Конфиг перезагружен")
    public String reloadSuccess;

    @ConfigsProperty(path = "commands.myrace.race_not_selected", defaultString = "<red>У вас не выбрана раса")
    public String raceNotSelected;

    @ConfigsProperty(path = "commands.get_xp.result", defaultString = "У игрока <aqua>{player}</aqua>: <gold>{count}</gold> XP")
    public String getXpResult;

    @ConfigsProperty(path = "commands.add_xp.response", defaultString = "Игроку {player} успешно добавлено {count} XP")
    public String addXpResponse;

    @ConfigsProperty(path = "shield_block", defaultString = "<red>Ваша раса не умеет использовать щиты")
    public String shieldBlock;

    @ConfigsProperty(path = "forbidden_foods", defaultString = "<red>Ваша раса не приемлет данную еду")
    public String forbiddenFoods;

    @ConfigsProperty(path = "player_not_found", defaultString = "<red>Указанный игрок не найден")
    public String playerNotFound;

    @ConfigsProperty(path = "multiple_players_selected", defaultString = "<red>Ожидалось получить одного игрока, но получено: {count}")
    public String multiplePlayersSelected;

    public MessageConfigData() {}
}

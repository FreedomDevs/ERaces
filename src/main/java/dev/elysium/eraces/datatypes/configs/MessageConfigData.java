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

    @ConfigsProperty(path = "commands.get_player_race.race_check_error", defaultString = "<red>Вы не указали игрока для получения расы")
    public String getPlayerRaceError;

    @ConfigsProperty(path = "commands.get_player_race.race_check_success", defaultString = "<green>У игрока <yellow>{player} <green>раса <yellow>{race}")
    public String getPlayerRaceSuccess;

    @ConfigsProperty(path = "commands.get_player_race.race_check_success_me", defaultString = "<green>Ваша раса <yellow>{race}")
    public String getPlayerRaceSuccessMe;

    @ConfigsProperty(path = "commands.races.subcommand_error", defaultString = "<red>Не указана подкоманда")
    public String racesSubcommandError;

    @ConfigsProperty(path = "commands.races.subcommand_is_null", defaultString = "<red>Подкоманда: <yellow>{arg} <red>не существует")
    public String racesSubcommandIsNull;

    @ConfigsProperty(path = "commands.regenerate_player_race.console_usage", defaultString = "<red>Использование из консоли: /command <ник игрока>")
    public String regenerateConsoleUsage;

    @ConfigsProperty(path = "commands.regenerate_player_race.nick_error", defaultString = "<red>Неверно указан ник игрока")
    public String regenerateNickError;

    @ConfigsProperty(path = "commands.regenerate_player_race.regenerate_success", defaultString = "<green>Раса игрока <yellow>{player} <green>установлена на: <yellow>{race}")
    public String regenerateSuccess;

    @ConfigsProperty(path = "commands.set_player_race.small_args", defaultString = "<red>Мало аргументов")
    public String setPlayerRaceSmallArgs;

    @ConfigsProperty(path = "commands.set_player_race.race_not_found", defaultString = "<red>Такая раса не существует")
    public String setPlayerRaceNotFound;

    @ConfigsProperty(path = "commands.set_player_race.nick_null", defaultString = "<red>Неверно указан ник игрока")
    public String setPlayerRaceNickNull;

    @ConfigsProperty(path = "commands.set_player_race.set_success", defaultString = "<green>Раса игрока <yellow>{player} <green>установлена на: <yellow>{race}")
    public String setPlayerRaceSuccess;

    @ConfigsProperty(path = "commands.reload.reload_success", defaultString = "<green>Конфиг перезагружен")
    public String reloadSuccess;

    @ConfigsProperty(path = "commands.myrace.race_not_selected", defaultString = "<red>У вас не выбрана раса")
    public String raceNotSelected;

    @ConfigsProperty(path = "commands.get_xp.result", defaultString = "У игрока <aqua>{player}</aqua>: <gold>{count}</gold> XP")
    public String getXpResult;

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

package dev.elysium.eraces.datatypes.configs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageConfigData {
    @ConfigsProperty(path = "plugin.enabled", defaultString = "Плагин включен!")
    private String pluginEnabled;

    @ConfigsProperty(path = "plugin.disabled", defaultString = "Плагин выключен")
    private String pluginDisabled;

    @ConfigsProperty(path = "commands.get_player_race.race_check_error", defaultString = "<red>Вы не указали игрока для получения расы")
    private String getPlayerRaceError;

    @ConfigsProperty(path = "commands.get_player_race.race_check_success", defaultString = "<green>У игрока <yellow>{player} <green>раса <yellow>{race}")
    private String getPlayerRaceSuccess;

    @ConfigsProperty(path = "commands.get_player_race.race_check_success_me", defaultString = "<green>Ваша раса <yellow>{race}")
    private String getPlayerRaceSuccessMe;

    @ConfigsProperty(path = "commands.races.subcommand_error", defaultString = "<red>Не указана подкоманда")
    private String racesSubcommandError;

    @ConfigsProperty(path = "commands.races.subcommand_is_null", defaultString = "<red>Подкоманда: <yellow>{arg} <red>не существует")
    private String racesSubcommandIsNull;

    @ConfigsProperty(path = "commands.regenerate_player_race.console_usage", defaultString = "<red>Использование из консоли: /command <ник игрока>")
    private String regenerateConsoleUsage;

    @ConfigsProperty(path = "commands.regenerate_player_race.nick_error", defaultString = "<red>Неверно указан ник игрока")
    private String regenerateNickError;

    @ConfigsProperty(path = "commands.regenerate_player_race.regenerate_success", defaultString = "<green>Раса игрока <yellow>{player} <green>установлена на: <yellow>{race}")
    private String regenerateSuccess;

    @ConfigsProperty(path = "commands.set_player_race.small_args", defaultString = "<red>Мало аргументов")
    private String setPlayerRaceSmallArgs;

    @ConfigsProperty(path = "commands.set_player_race.race_not_found", defaultString = "<red>Такая раса не существует")
    private String setPlayerRaceNotFound;

    @ConfigsProperty(path = "commands.set_player_race.nick_null", defaultString = "<red>Неверно указан ник игрока")
    private String setPlayerRaceNickNull;

    @ConfigsProperty(path = "commands.set_player_race.set_success", defaultString = "<green>Раса игрока <yellow>{player} <green>установлена на: <yellow>{race}")
    private String setPlayerRaceSuccess;

    @ConfigsProperty(path = "commands.reload.reload_success", defaultString = "<green>Конфиг перезагружен")
    private String reloadSuccess;

    @ConfigsProperty(path = "shield_block", defaultString = "<red>Ваша раса не умеет использовать щиты")
    private String shieldBlock;

    @ConfigsProperty(path = "forbidden_foods", defaultString = "<red>Ваша раса не приемлет данную еду")
    private String forbiddenFoods;

    public MessageConfigData() {}
}

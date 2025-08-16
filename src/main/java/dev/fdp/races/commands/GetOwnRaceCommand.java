package dev.fdp.races.commands;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class GetOwnRaceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.message(sender, "Эту команду может использовать только игрок.");
            return true;
        }

        String playerRace = FDP_Races.getInstance().getPlayerDataManager().getPlayerRace(player.getName());

        if (playerRace == null || playerRace.isEmpty()) {
            ChatUtil.message(player, "У вас не выбрана раса.");
            return true;
        }

        String message = FDP_Races.getInstance().getMessageManager().getString("commands.get_player_race.race_check_success_me");
        ChatUtil.message(player, message, Map.of("{race}", playerRace));
        return true;
    }
}

package dev.fdp.races.commands;

import dev.fdp.races.FDP_Races;
import dev.fdp.races.RacesReloader;
import dev.fdp.races.VisualsManager;
import dev.fdp.races.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends AbstractSubCommand {
    public void onCommandExecute(CommandSender sender, String[] args) {
        FDP_Races.getInstance().getRacesConfigManager().reloadConfig();
        FDP_Races.getInstance().getPlayerDataManager().loadData();
        RacesReloader.reloadRaceForAllPlayers();
        VisualsManager.reloadVisualsForAllPlayer();
        String message = FDP_Races.getInstance().getMessageManager().getString("commands.reload.reload_success");
        ChatUtil.message(sender, message);
    }
}

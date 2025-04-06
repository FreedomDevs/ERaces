package dev.fdp.races.commands;

import org.bukkit.command.CommandSender;

public abstract class AbstractSubCommand {
  public abstract void onCommandExecute(CommandSender sender, String[] args);
}

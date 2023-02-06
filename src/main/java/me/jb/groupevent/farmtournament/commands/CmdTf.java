/*
 * Copyright (c) 2022 Jb
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.jb.groupevent.farmtournament.commands;

import me.jb.groupevent.configuration.FileHandler;
import me.jb.groupevent.enums.Permission;
import me.jb.groupevent.enums.config.Message;
import me.jb.groupevent.utils.MessageUtils;
import me.jb.groupevent.farmtournament.Tournament;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdTf implements CommandExecutor {

  private final Tournament tournament;
  private final FileHandler fileHandler;

  public CmdTf(@NotNull Tournament tournament, @NotNull FileHandler fileHandler) {
    this.tournament = tournament;
    this.fileHandler = fileHandler;
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {

    FileConfiguration messageConfig = this.fileHandler.getMessageConfigFile().getConfig();

    if (!(commandSender.hasPermission(Permission.TS_USE.getPermission())
        || commandSender.hasPermission(Permission.TS_ADMIN.getPermission()))) {
      String noPermMess = messageConfig.getString(Message.NO_PERM.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(noPermMess));
      return true;
    }

    if (args.length == 0) {
      this.onInfo(commandSender, messageConfig);
    }

    if (args.length == 1)
      switch (args[0]) {
        case "info":
        case "infos":
          this.onInfo(commandSender, messageConfig);
          return true;
        case "reload":
          this.onReload(commandSender, messageConfig);
          return true;
        case "forcestart":
        case "start":
          this.onStart(commandSender, messageConfig);
          return true;
        case "stop":
        case "forcestop":
          this.onForceStop(commandSender, messageConfig);
          return true;
        case "help":
          this.onHelp(commandSender, messageConfig);
          return true;
        default:
          String errorMessage = messageConfig.getString(Message.TOURNAMENT_ERROR.getKey());
          commandSender.sendMessage(MessageUtils.setColorsMessage(errorMessage));
          return true;
      }
    return true;
  }

  private void onInfo(CommandSender commandSender, FileConfiguration messageConfig) {

    if (commandSender instanceof Player) {

      if (!this.tournament.getTournamentModel().hasActiveTournament()) {
        String noActiveEventMess =
            messageConfig.getString(Message.TOURNAMENT_NOACTIVEEVENT.getKey());
        commandSender.sendMessage(MessageUtils.setColorsMessage(noActiveEventMess));
        return;
      }

      String infoMessage = messageConfig.getString(Message.TOURNAMENT_INFO.getKey());
      commandSender.sendMessage(MessageUtils.setupMessage((Player) commandSender, infoMessage));
    } else {
      String onlyPlayerMessage = messageConfig.getString(Message.ONLY_PLAYER.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(onlyPlayerMessage));
    }
  }

  private void onReload(CommandSender commandSender, FileConfiguration messageConfig) {
    if (!(commandSender.hasPermission(Permission.RELOAD.getPermission())
        || commandSender.hasPermission(Permission.TS_ADMIN.getPermission()))) {
      String noPermMess = messageConfig.getString(Message.NO_PERM.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(noPermMess));
      return;
    }

    this.tournament.reloadPlugin();

    FileConfiguration messageConfigReloaded = this.fileHandler.getMessageConfigFile().getConfig();

    String reloadMess = messageConfigReloaded.getString(Message.RELOAD.getKey());
    commandSender.sendMessage(MessageUtils.setColorsMessage(reloadMess));
  }

  private void onStart(CommandSender commandSender, FileConfiguration messageConfig) {
    if (!(commandSender.hasPermission(Permission.TS_START.getPermission())
        || commandSender.hasPermission(Permission.TS_ADMIN.getPermission()))) {
      String noPermMess = messageConfig.getString(Message.NO_PERM.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(noPermMess));
      return;
    }

    TournamentModel tournamentModel = this.tournament.getTournamentModel();

    if (tournamentModel.hasActiveTournament()) {
      String alreadyActiveMess =
          messageConfig.getString(Message.TOURNAMENT_ALREADYACTIVEEVENT.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(alreadyActiveMess));
      return;
    }

    TournamentData tournamentData = tournamentModel.getClosestTournament();
    this.tournament.getTournamentController().startEvent(tournamentData, true);

    String forceStartMess = messageConfig.getString(Message.TOURNAMENT_FORCESTART_SENDER.getKey());
    commandSender.sendMessage(MessageUtils.setColorsMessage(forceStartMess));
  }

  private void onForceStop(CommandSender commandSender, FileConfiguration messageConfig) {
    if (!(commandSender.hasPermission(Permission.TS_FORCESTOP.getPermission())
        || commandSender.hasPermission(Permission.TS_ADMIN.getPermission()))) {
      String noPermMess = messageConfig.getString(Message.NO_PERM.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(noPermMess));
      return;
    }

    if (!this.tournament.getTournamentModel().hasActiveTournament()) {
      String noActiveEventMess = messageConfig.getString(Message.TOURNAMENT_NOACTIVEEVENT.getKey());
      commandSender.sendMessage(MessageUtils.setColorsMessage(noActiveEventMess));
      return;
    }

    this.tournament.getTournamentController().stopEvent(true);

    String forceStopMess = messageConfig.getString(Message.TOURNAMENT_FORCESTOP_SENDER.getKey());
    commandSender.sendMessage(MessageUtils.setColorsMessage(forceStopMess));
  }

  private void onHelp(CommandSender commandSender, FileConfiguration messageConfig) {
    String helpMess = messageConfig.getString(Message.TOURNAMENT_HELP.getKey());
    commandSender.sendMessage(MessageUtils.setColorsMessage(helpMess));
  }
}

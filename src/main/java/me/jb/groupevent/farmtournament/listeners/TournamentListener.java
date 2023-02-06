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

package me.jb.groupevent.farmtournament.listeners;

import me.jb.groupevent.configuration.FileHandler;
import me.jb.groupevent.enums.config.Message;
import me.jb.groupevent.farmtournament.customevent.TournamentStartEvent;
import me.jb.groupevent.farmtournament.customevent.TournamentStopEvent;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import me.jb.groupevent.utils.MessageUtils;
import me.jb.groupevent.farmtournament.missions.Mission;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class TournamentListener implements Listener {

  private final FileHandler fileHandler;
  private final TournamentModel tournamentModel;

  public TournamentListener(
      @NotNull FileHandler fileHandler, @NotNull TournamentModel tournamentModel) {
    this.fileHandler = fileHandler;
    this.tournamentModel = tournamentModel;
  }

  @EventHandler
  public void onTournamentStart(TournamentStartEvent event) {
    FileConfiguration messageConfig = this.fileHandler.getMessageConfigFile().getConfig();
    String message;

    if (event.isForcedStart())
      message = messageConfig.getString(Message.TOURNAMENT_FORCESTART_PLAYERS.getKey());
    else message = messageConfig.getString(Message.TOURNAMENT_START.getKey());

    Mission<? extends Keyed> activeMission =
        tournamentModel.getActiveTournamentData().get().getActiveTournamentMission();
    String replacedMessage = message.replace("%tf_item%", activeMission.getName());

    Bukkit.getOnlinePlayers()
        .forEach(player -> player.sendMessage(MessageUtils.setupMessage(player, replacedMessage)));
  }

  @EventHandler
  public void onTournamentStop(TournamentStopEvent event) {
    FileConfiguration messageConfig = this.fileHandler.getMessageConfigFile().getConfig();
    String message;

    if (event.isForcedStart()) {
      message = messageConfig.getString(Message.TOURNAMENT_FORCESTOP_PLAYERS.getKey());
    } else {
      message = messageConfig.getString(Message.TOURNAMENT_STOP.getKey());
    }

    Bukkit.getOnlinePlayers()
        .forEach(player -> player.sendMessage(MessageUtils.setupMessage(player, message)));
  }
}

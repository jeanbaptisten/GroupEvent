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

package me.jb.groupevent.farmtournament.tournament.controller;

import me.jb.groupevent.farmtournament.customevent.TournamentStartEvent;
import me.jb.groupevent.farmtournament.customevent.TournamentStopEvent;
import me.jb.groupevent.farmtournament.missions.Mission;
import me.jb.groupevent.farmtournament.missions.config.MissionModel;
import me.jb.groupevent.farmtournament.playerdata.PlayerTournamentData;
import me.jb.groupevent.farmtournament.rewards.config.RewardsService;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.data.active.ActiveTournamentData;
import me.jb.groupevent.farmtournament.tournament.data.active.DefaultActiveTournamentData;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import me.jb.groupevent.farmtournament.tournament.task.TournamentStartTask;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DefaultTournamentController implements TournamentController {

  private final Plugin plugin;
  private final TournamentModel tournamentModel;
  private final MissionModel missionModel;
  private final RewardsService rewardsService;
  private Timer tournamentStartTimer;
  private BukkitTask stopTask;

  public DefaultTournamentController(
      @NotNull Plugin plugin,
      @NotNull TournamentModel tournamentModel,
      @NotNull MissionModel missionModel,
      @NotNull RewardsService rewardsService) {
    this.plugin = plugin;
    this.tournamentModel = tournamentModel;
    this.missionModel = missionModel;
    this.rewardsService = rewardsService;
  }

  public void startEvent(@NotNull TournamentData tournamentData, boolean forced) {
    if (this.tournamentModel.hasActiveTournament()) this.tournamentModel.stopActiveTournament();

    if (forced && this.tournamentStartTimer != null) this.tournamentStartTimer.cancel();
    if (forced && this.stopTask != null) this.stopTask.cancel();

    // Starting task.
    if (forced)
      this.stopTask =
          Bukkit.getScheduler()
              .runTaskLater(
                  this.plugin, () -> this.stopEvent(false), tournamentModel.getForcedTime() * 20L);
    else {
      long beginHour = tournamentData.getBeginningHour();
      long endHour = tournamentData.getEndingHour();
      this.stopTask =
          Bukkit.getScheduler()
              .runTaskLater(this.plugin, () -> this.stopEvent(false), (endHour - beginHour) * 20L);
    }


    // Getting random Tournament.
    Mission<? extends Keyed> randomMission = this.missionModel.getRandomMission();

    // Setting TournamentData.
    ActiveTournamentData newActiveVoteEvent =
        new DefaultActiveTournamentData(
            this.plugin,
            tournamentData,
            randomMission,
            this.tournamentModel.getActionBarManager(),
            this.tournamentModel.getActionBarFormat(),
            this.tournamentModel.getActionBarDelay());

    this.tournamentModel.setActiveTournamentData(newActiveVoteEvent);

    // Appel de l'event
    TournamentStartEvent tournamentStartEvent = new TournamentStartEvent(forced);
    Bukkit.getPluginManager().callEvent(tournamentStartEvent);

    this.plugin
        .getLogger()
        .log(Level.INFO, "Event started ! (" + tournamentData.getBeginningHour() + ")");
  }

  public void loadNextEvent() {
    List<TournamentData> tournamentDataList = this.tournamentModel.getTournamentDataList();

    if (tournamentDataList.isEmpty())
      throw new IllegalStateException("Tournament data list is empty !");

    this.tournamentModel.refreshNextTournamentData();
    TournamentData fastestTournament = this.tournamentModel.getClosestTournament();

    // Get different time between
    long differentTime = fastestTournament.getBeginningHour() - LocalTime.now().toSecondOfDay();
    if (differentTime < 0)
      differentTime =
          86400 - LocalTime.now().toSecondOfDay() + fastestTournament.getBeginningHour();

    this.plugin
        .getLogger()
        .log(
            Level.INFO,
            "New event task initiated ! (" + fastestTournament.getBeginningHour() + ")");

    this.tournamentStartTimer = new Timer();
    this.tournamentStartTimer.schedule(
        new TournamentStartTask(this.plugin, this, fastestTournament), differentTime * 1000L);
  }

  public void stopEvent(boolean forced) {

    if (!this.tournamentModel.hasActiveTournament())
      throw new IllegalStateException("There is no active event !");

    // Appel de l'event
    TournamentStopEvent tournamentStopEvent = new TournamentStopEvent(forced);
    Bukkit.getPluginManager().callEvent(tournamentStopEvent);

    // Give rewards
    if (!forced) this.giveRewards();

    // Stop event then load next event
    this.tournamentModel.stopActiveTournament();
    this.stopTask.cancel();

    this.plugin.getLogger().log(Level.INFO, "Event Task stopped !");

    this.loadNextEvent();
  }

  @Override
  public void reload() {
    if (this.tournamentModel.hasActiveTournament()) this.tournamentModel.stopActiveTournament();
    if (this.tournamentStartTimer != null) this.tournamentStartTimer.cancel();
    if (this.stopTask != null) this.stopTask.cancel();

    this.loadNextEvent();
  }

  private void giveRewards() {
    Collection<PlayerTournamentData> playerData =
        this.tournamentModel.getPlayerDataModel().getDataMap().values();

    List<UUID> playersSortedList =
        playerData.stream()
            .sorted(
                Comparator.comparing(PlayerTournamentData::getPoints, Comparator.reverseOrder()))
            .map(PlayerTournamentData::getPlayerUUID)
            .collect(Collectors.toList());

    this.rewardsService.giveRewards(playersSortedList);
  }
}

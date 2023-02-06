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

package me.jb.groupevent.farmtournament.tournament.data.active;

import me.jb.groupevent.farmtournament.tournament.actionbar.manager.DefaultActionBarManager;
import me.jb.groupevent.farmtournament.missions.Mission;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.task.ActionBarTask;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class DefaultActiveTournamentData implements ActiveTournamentData {

  private final Plugin plugin;
  private final TournamentData tournamentData;
  private final Mission<? extends Keyed> activeTournamentMission;
  private final BukkitTask actionBarTask;
  private final DefaultActionBarManager actionBarManager;
  private final String actionBarFormat;
  private int duration = 0;

  public DefaultActiveTournamentData(
      @NotNull Plugin plugin,
      @NotNull TournamentData tournamentData,
      @NotNull Mission<? extends Keyed> activeTournamentMission,
      @NotNull DefaultActionBarManager actionBarManager,
      @NotNull String actionBarFormat,
      int delay) {
    this.plugin = plugin;
    this.tournamentData = tournamentData;
    this.activeTournamentMission = activeTournamentMission;
    this.actionBarManager = actionBarManager;
    this.actionBarFormat = actionBarFormat;

    this.actionBarTask =
        Bukkit.getScheduler()
            .runTaskTimer(
                this.plugin,
                new ActionBarTask(this.actionBarManager, this.actionBarFormat),
                delay * 20L,
                delay * 20L);
  }

  @Override
  public TournamentData getTournamentData() {
    return tournamentData;
  }

  @Override
  public Mission<? extends Keyed> getActiveTournamentMission() {
    return activeTournamentMission;
  }

  @Override
  public int getDuration() {
    return this.duration;
  }

  @Override
  public void incrementDuration() {
    this.duration++;
  }

  @Override
  public void stopEvent() {
    this.actionBarTask.cancel();
  }

  @Override
  public String toString() {
    return "DefaultActiveTournamentData{"
        + "tournamentData="
        + tournamentData
        + ", activeTournamentMission="
        + activeTournamentMission
        + ", duration="
        + duration
        + '}';
  }
}

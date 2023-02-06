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

package me.jb.groupevent.farmtournament.tournament.task;

import me.jb.groupevent.farmtournament.tournament.controller.TournamentController;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.TimerTask;

public class TournamentStartTask extends TimerTask {

  private final Plugin plugin;
  private final TournamentController tournamentController;
  private final TournamentData tournamentData;

  public TournamentStartTask(
      @NotNull Plugin plugin,
      @NotNull TournamentController tournamentController,
      @NotNull TournamentData tournamentData) {
    this.plugin = plugin;
    this.tournamentController = tournamentController;
    this.tournamentData = tournamentData;
  }

  @Override
  public void run() {
    Bukkit.getScheduler()
        .runTask(
            this.plugin,
            () -> {
              this.tournamentController.startEvent(tournamentData, false);
            });
  }
}

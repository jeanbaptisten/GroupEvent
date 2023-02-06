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

import me.jb.groupevent.utils.MessageUtils;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.data.active.ActiveTournamentData;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;

public class TournamentMessageTask implements Runnable {

  private final TournamentModel tournamentModel;

  public TournamentMessageTask(@NotNull TournamentModel tournamentModel) {
    this.tournamentModel = tournamentModel;
  }

  @Override
  public void run() {

    // Case active tournament : Use messages through tournament.
    if (this.tournamentModel.hasActiveTournament()) {

      ActiveTournamentData activeTournamentData =
          this.tournamentModel.getActiveTournamentData().get();
      int tournamentDuration = activeTournamentData.getDuration();

      this.tournamentModel
          .getMessagesThoughTournament()
          .forEach(
              tournamentMessage -> {
                if (tournamentMessage.getDelay() == tournamentDuration)
                  Bukkit.getOnlinePlayers()
                      .forEach(
                          player -> {
                            String messageReplaced =
                                MessageUtils.setupMessage(player, tournamentMessage.getMessage());
                            player.sendMessage(messageReplaced);
                          });
              });

      activeTournamentData.incrementDuration();
      return;
    }

    // Case no active tournament: Use messages before next tournament.
    TournamentData tournamentData = this.tournamentModel.getClosestTournament();
    long differentTime = tournamentData.getBeginningHour() - LocalTime.now().toSecondOfDay();

    this.tournamentModel
        .getMessagesBeforeTournament()
        .forEach(
            tournamentMessage -> {
              if (tournamentMessage.getDelay() == differentTime) {
                Bukkit.getOnlinePlayers()
                    .forEach(
                        player -> {
                          String messageReplaced =
                              MessageUtils.setupMessage(player, tournamentMessage.getMessage());
                          player.sendMessage(messageReplaced);
                        });
              }
            });
  }
}

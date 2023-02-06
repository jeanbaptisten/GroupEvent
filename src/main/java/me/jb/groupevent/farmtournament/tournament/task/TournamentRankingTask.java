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

import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import me.jb.groupevent.placeholderexpansion.FarmTournamentExpansion;
import me.jb.groupevent.farmtournament.playerdata.PlayerTournamentData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Collectors;

public class TournamentRankingTask implements Runnable {

  private final FarmTournamentExpansion farmTournamentExpansion;
  private final TournamentModel tournamentModel;

  public TournamentRankingTask(
      @NotNull FarmTournamentExpansion farmTournamentExpansion,
      @NotNull TournamentModel tournamentModel) {
    this.farmTournamentExpansion = farmTournamentExpansion;
    this.tournamentModel = tournamentModel;
  }

  @Override
  public void run() {
    @Unmodifiable
    Map<UUID, PlayerTournamentData> playerDataMap =
        this.tournamentModel.getPlayerDataModel().getDataMap();

    if (playerDataMap.isEmpty()) {
      this.farmTournamentExpansion.setTop(new LinkedList<>());
      return;
    }

    if (!this.tournamentModel.hasActiveTournament()) return;

    Collection<PlayerTournamentData> playerDataList = playerDataMap.values();

    LinkedList<PlayerTournamentData> playersSortedList =
        playerDataList.stream()
            .sorted(
                Comparator.comparing(PlayerTournamentData::getPoints, Comparator.reverseOrder()))
            .collect(Collectors.toCollection(LinkedList::new));

    this.farmTournamentExpansion.setTop(playersSortedList);
  }
}

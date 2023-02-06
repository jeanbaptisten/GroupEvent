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

package me.jb.groupevent.farmtournament.tournament.model;

import me.jb.groupevent.farmtournament.tournament.actionbar.manager.DefaultActionBarManager;
import me.jb.groupevent.farmtournament.playerdata.model.PlayerDataModel;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.data.active.ActiveTournamentData;
import me.jb.groupevent.farmtournament.tournament.messages.TournamentMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

public interface TournamentModel {

  void setActiveTournamentData(ActiveTournamentData activeTournament);

  void setMessagesBeforeTournament(@NotNull List<TournamentMessage> messageList);

  void setTournamentDataList(@NotNull List<TournamentData> tournamentDataList);

  void setMessagesThoughTournament(@NotNull List<TournamentMessage> messageList);

    void setActionBarFormat(String actionBarFormat);

  void setActionBarDelay(int actionBarDelay);

  void setForcedTime(@Range(from = 0, to = Integer.MAX_VALUE) int forcedTime);

  @Unmodifiable
  List<TournamentMessage> getMessagesBeforeTournament();

  @Unmodifiable
  List<TournamentMessage> getMessagesThoughTournament();

  @Unmodifiable
  List<TournamentData> getTournamentDataList();

  void stopActiveTournament();

  PlayerDataModel getPlayerDataModel();

  boolean hasActiveTournament();

  Optional<ActiveTournamentData> getActiveTournamentData();

  DefaultActionBarManager getActionBarManager();

  String getActionBarFormat();

  int getActionBarDelay();

  void clear();

  void refreshNextTournamentData();

  TournamentData getClosestTournament();

  int getForcedTime();
}

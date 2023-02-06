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
import me.jb.groupevent.farmtournament.playerdata.model.DefaultPlayerDataModel;
import me.jb.groupevent.farmtournament.playerdata.model.PlayerDataModel;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.data.active.ActiveTournamentData;
import me.jb.groupevent.farmtournament.tournament.messages.TournamentMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TournamentModelProvider implements TournamentModel {

  private final PlayerDataModel playerDataModel = new DefaultPlayerDataModel();
  private final DefaultActionBarManager actionBarManager = new DefaultActionBarManager();
  private ActiveTournamentData activeTournamentData;
  private List<TournamentMessage> messagesBeforeTournament, messagesThoughTournament;
  private List<TournamentData> tournamentDataList;
  private TournamentData nextTournamentData;
  private String actionBarFormat;
  private int forcedTime, actionBarDelay;

  @Override
  public void setActiveTournamentData(ActiveTournamentData activeTournamentData) {
    this.activeTournamentData = activeTournamentData;
  }

  @Override
  public void setMessagesBeforeTournament(@NotNull List<TournamentMessage> messageList) {
    if (messageList.isEmpty()) throw new IllegalArgumentException("Message list cannot be empty.");

    this.messagesBeforeTournament = messageList;
  }

  @Override
  public void setTournamentDataList(@NotNull List<TournamentData> tournamentDataList) {
    if (tournamentDataList.isEmpty())
      throw new IllegalArgumentException("Tournament data list is empty.");

    this.tournamentDataList = tournamentDataList;
  }

  @Override
  public void setMessagesThoughTournament(@NotNull List<TournamentMessage> messageList) {
    if (messageList.isEmpty()) throw new IllegalArgumentException("Message list is empty.");

    this.messagesThoughTournament = messageList;
  }

  @Override
  public void setActionBarFormat(String actionBarFormat) {
    this.actionBarFormat = actionBarFormat;
  }

  @Override
  public void setActionBarDelay(int actionBarDelay) {
    this.actionBarDelay = actionBarDelay;
  }

  @Override
  public void setForcedTime(@Range(from = 0, to = Integer.MAX_VALUE) int forcedTime) {
    this.forcedTime = forcedTime;
  }

  @Override
  public @Unmodifiable List<TournamentMessage> getMessagesBeforeTournament() {
    return this.messagesBeforeTournament;
  }

  @Override
  public @Unmodifiable List<TournamentMessage> getMessagesThoughTournament() {
    return this.messagesThoughTournament;
  }

  @Override
  public @Unmodifiable List<TournamentData> getTournamentDataList() {
    return this.tournamentDataList;
  }

  @Override
  public void stopActiveTournament() {
    this.activeTournamentData.stopEvent();
    this.activeTournamentData = null;
    this.playerDataModel.clear();
  }

  @Override
  public PlayerDataModel getPlayerDataModel() {
    return playerDataModel;
  }

  @Override
  public boolean hasActiveTournament() {
    return this.activeTournamentData != null;
  }

  @Override
  public Optional<ActiveTournamentData> getActiveTournamentData() {
    return Optional.ofNullable(this.activeTournamentData);
  }

  @Override
  public DefaultActionBarManager getActionBarManager() {
    return actionBarManager;
  }

  @Override
  public String getActionBarFormat() {
    return actionBarFormat;
  }

  @Override
  public int getActionBarDelay() {
    return actionBarDelay;
  }

  @Override
  public void clear() {
    this.messagesBeforeTournament.clear();
    this.messagesThoughTournament.clear();
    this.tournamentDataList.clear();
    this.playerDataModel.clear();
    this.forcedTime = 0;
  }

  @Override
  public void refreshNextTournamentData() {
    long nowTime = LocalTime.now().toSecondOfDay();

    this.nextTournamentData =
        this.tournamentDataList.stream()
            .filter(data -> data.getBeginningHour() >= nowTime)
            .min(Comparator.comparingLong(data -> data.getBeginningHour() - nowTime))
            .orElse(tournamentDataList.size() > 0 ? tournamentDataList.get(0) : null);
  }

  @Override
  public TournamentData getClosestTournament() {
    return this.nextTournamentData;
  }

  @Override
  public int getForcedTime() {
    return this.forcedTime;
  }

  @Override
  public String toString() {
    return "TournamentModelProvider{"
        + "activeTournamentData="
        + activeTournamentData
        + ", messagesBeforeTournament="
        + messagesBeforeTournament
        + ", messagesThoughTournament="
        + messagesThoughTournament
        + ", tournamentDataList="
        + tournamentDataList
        + ", playerDataModel="
        + playerDataModel
        + '}';
  }
}

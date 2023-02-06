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

package me.jb.groupevent.farmtournament.tournament.service;

import me.jb.groupevent.farmtournament.tournament.dao.TournamentDAO;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.messages.TournamentMessage;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultTournamentService implements TournamentService {

  private final TournamentDAO tournamentDAO;
  private final TournamentModel tournamentModel;

  public DefaultTournamentService(
      @NotNull TournamentDAO tournamentDAO, @NotNull TournamentModel tournamentModel) {
    this.tournamentDAO = tournamentDAO;
    this.tournamentModel = tournamentModel;
  }

  @Override
  public void load() {
    List<TournamentMessage> beforeTournamentMessageList = this.tournamentDAO.loadMessages("before");
    List<TournamentMessage> thoughTournamentMessageList = this.tournamentDAO.loadMessages("during");
    List<TournamentData> tournamentDataList = this.tournamentDAO.loadTournementDatas();
    String actionBarFormat = this.tournamentDAO.getActionBarFormat();
    int forcedTime = this.tournamentDAO.getForcedTime();
    int actionBarDelay = this.tournamentDAO.getActionBarDelay();

    this.tournamentModel.setMessagesBeforeTournament(beforeTournamentMessageList);
    this.tournamentModel.setMessagesThoughTournament(thoughTournamentMessageList);
    this.tournamentModel.setTournamentDataList(tournamentDataList);
    this.tournamentModel.setForcedTime(forcedTime);
    this.tournamentModel.setActionBarDelay(actionBarDelay);
    this.tournamentModel.setActionBarFormat(actionBarFormat);
  }

  @Override
  public void reload() {
    this.tournamentModel.clear();
    this.load();
  }
}

/*
 * Copyright (c) 2022 Niz
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

package me.jb.groupevent.placeholderexpansion;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Optional;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jb.groupevent.utils.IntegerUtils;
import me.jb.groupevent.farmtournament.Tournament;
import me.jb.groupevent.farmtournament.missions.Mission;
import me.jb.groupevent.farmtournament.playerdata.PlayerTournamentData;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.data.active.ActiveTournamentData;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import org.bukkit.Keyed;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class FarmTournamentExpansion extends PlaceholderExpansion {

  private final Tournament tournament;
  private LinkedList<PlayerTournamentData> top = new LinkedList<>();

  public FarmTournamentExpansion(@NotNull Tournament tournament) {
    this.tournament = tournament;
  }

  @Override
  public @NotNull String getIdentifier() {
    return "tf";
  }

  @Override
  public @NotNull String getAuthor() {
    return "Niz";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0.0";
  }

  @Override
  public String onRequest(OfflinePlayer player, @NotNull String params) {
    if (params.equals("started")) {
      if (this.tournament.getTournamentModel().hasActiveTournament()) return "yes";
      else return "no";
    }

    if (params.equals("item")) {
      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (tournamentModel.hasActiveTournament()) {
        Mission<? extends Keyed> activeMission =
            tournamentModel.getActiveTournamentData().get().getActiveTournamentMission();
        return activeMission.getName();
      } else return "L’event n’a pas encore commencé.";
    }

    if (params.equals("timer_start")) {
      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (tournamentModel.hasActiveTournament()) return "PROGRESS";

      TournamentData tournamentData = tournamentModel.getClosestTournament();
      return String.valueOf(tournamentData.getBeginningHour() - LocalTime.now().toSecondOfDay());
    }

    if (params.equals("timer_start_formatted")) {
      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (tournamentModel.hasActiveTournament()) return "PROGRESS";

      TournamentData tournamentData = tournamentModel.getClosestTournament();
      return IntegerUtils.parseTime((int) (tournamentData.getBeginningHour() - LocalTime.now().toSecondOfDay()));
    }

    if (params.equals("timer_end")) {
      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (!tournamentModel.hasActiveTournament()) return "L’event n’a pas encore commencé.";

      ActiveTournamentData activeTournamentData = tournamentModel.getActiveTournamentData().get();

      long totalDuration =
          activeTournamentData.getTournamentData().getEndingHour()
              - activeTournamentData.getTournamentData().getBeginningHour();

      return String.valueOf(totalDuration - activeTournamentData.getDuration());
    }

    if (params.equals("timer_end_formatted")) {
      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (!tournamentModel.hasActiveTournament()) return "L’event n’a pas encore commencé.";

      ActiveTournamentData activeTournamentData = tournamentModel.getActiveTournamentData().get();

      long totalDuration =
              activeTournamentData.getTournamentData().getEndingHour()
                      - activeTournamentData.getTournamentData().getBeginningHour();

      return IntegerUtils.parseTime((int) (totalDuration - activeTournamentData.getDuration()));
    }

    if (params.equals("linkedplaceholder")) {
      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (!tournamentModel.hasActiveTournament()) return "L’event n’a pas encore commencé.";

      ActiveTournamentData activeTournamentData = tournamentModel.getActiveTournamentData().get();

      return activeTournamentData.getActiveTournamentMission().getLinkedPlaceholder();
    }

    if (params.equals("leaderboard_rank")) {
      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (tournamentModel.hasActiveTournament()) {
        Optional<PlayerTournamentData> jobsPlayerOptional =
            this.top.stream()
                .filter(playerData -> playerData.getPlayerUUID().equals(player.getUniqueId()))
                .findAny();

        if (!jobsPlayerOptional.isPresent()) return "✘";

        PlayerTournamentData playerTournamentData = jobsPlayerOptional.get();

        return String.valueOf(this.top.indexOf(playerTournamentData) + 1);
      } else return "✘";
    }

    if (params.startsWith("leaderboard_name_")) {

      TournamentModel tournamentModel = this.tournament.getTournamentModel();
      if (!tournamentModel.hasActiveTournament()) {
        return "L’event n’a pas encore commencé.";
      }
      String[] paramsSplit = params.split("leaderboard_name_");

      if (paramsSplit.length == 2) {

        if (!IntegerUtils.isNumeric(paramsSplit[1])) return null;

        int rank = Integer.parseInt(paramsSplit[1]);

        if (this.top.size() + 1 <= rank) return "✘";

        return this.top.get(rank - 1).getOfflinePlayer().getName();
      }
      return null;
    }

    if (params.startsWith("leaderboard_points_")) {

      String[] paramsSplit = params.split("leaderboard_points_");

      if (paramsSplit.length == 2) {
        TournamentModel tournamentModel = this.tournament.getTournamentModel();

        if (paramsSplit[1].equals("self")) {
          PlayerTournamentData playerTournamentData =
              tournamentModel.getPlayerDataModel().getPlayerData(player.getUniqueId());
          if (playerTournamentData == null) return "✘";
          else return String.valueOf(playerTournamentData.getPoints());
        }

        if (!tournamentModel.hasActiveTournament()) {
          return "L’event n’a pas encore commencé.";
        }

        if (!IntegerUtils.isNumeric(paramsSplit[1])) return null;

        int rank = Integer.parseInt(paramsSplit[1]);

        if (this.top.size() + 1 <= rank) return "✘";

        return String.valueOf(this.top.get(rank - 1).getPoints());
      }
      return null;
    }

    return null;
  }

  public void setTop(@NotNull LinkedList<PlayerTournamentData> top) {
    this.top = top;
  }
}

// TODO : Start/End

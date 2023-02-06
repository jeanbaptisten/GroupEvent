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

package me.jb.groupevent.farmtournament.tournament.dao;

import me.jb.groupevent.configuration.FileHandler;
import me.jb.groupevent.farmtournament.tournament.data.DefaultTournamentData;
import me.jb.groupevent.farmtournament.tournament.data.TournamentData;
import me.jb.groupevent.farmtournament.tournament.messages.DefaultTournamentMessage;
import me.jb.groupevent.farmtournament.tournament.messages.TournamentMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class YamlTournamentDAO implements TournamentDAO {

  private final FileHandler fileHandler;

  public YamlTournamentDAO(FileHandler fileHandler) {
    this.fileHandler = fileHandler;
  }

  @Override
  public List<TournamentMessage> loadMessages(String path) {

    FileConfiguration tournamentConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    ConfigurationSection selectedMessageListSection =
        tournamentConfig.getConfigurationSection("messages." + path);

    List<TournamentMessage> tournamentMessageList = new ArrayList<>();

    selectedMessageListSection
        .getKeys(false)
        .forEach(
            messageId -> {
              ConfigurationSection selectedTournamentMessageSection =
                  selectedMessageListSection.getConfigurationSection(messageId);

              String messageContent = selectedTournamentMessageSection.getString("content");
              int delay = selectedTournamentMessageSection.getInt("delay");

              tournamentMessageList.add(new DefaultTournamentMessage(messageContent, delay));
            });

    return tournamentMessageList;
  }

  @Override
  public List<TournamentData> loadTournementDatas() {

    FileConfiguration tournamentConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    ConfigurationSection tournamentsSection =
        tournamentConfig.getConfigurationSection("tournaments");

    List<TournamentData> tournamentDataList = new ArrayList<>();

    tournamentsSection
        .getKeys(false)
        .forEach(
            tournamentId -> {
              ConfigurationSection selectedTournamentSection =
                  tournamentsSection.getConfigurationSection(tournamentId);

              String beginningDateString = selectedTournamentSection.getString("begin");
              String endingDateString = selectedTournamentSection.getString("end");

              assert beginningDateString != null && endingDateString != null;

              long beginningDateLong = this.parseString(beginningDateString);
              long endingDateLong = this.parseString(endingDateString);

              tournamentDataList.add(
                  new DefaultTournamentData(tournamentId, beginningDateLong, endingDateLong));
            });

    return tournamentDataList;
  }

  @Override
  public int getForcedTime() {
    FileConfiguration tournamentConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    return tournamentConfig.getInt("forcedTournamentTime");
  }

  @Override
  public int getTaskDelay() {
    FileConfiguration tournamentConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    return tournamentConfig.getInt("forcedTournamentTime");
  }

  @Override
  public int getActionBarDelay() {
    FileConfiguration tournamentConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    return tournamentConfig.getInt("actionBar.delay");
  }

  @Override
  public String getActionBarFormat() {
    FileConfiguration tournamentConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    return tournamentConfig.getString("actionBar.format");
  }

  private long parseString(@NotNull String date) {
    String[] dateSplit = date.split("h");
    long dateHH, dateMM;
    if (dateSplit.length == 1) {
      dateHH = Long.parseLong(dateSplit[0]) * 3600;
      dateMM = 0;
    } else {
      dateHH = Long.parseLong(dateSplit[0]) * 3600;
      dateMM = Long.parseLong(dateSplit[1]) * 60;
    }

    return dateHH + dateMM;
  }
}

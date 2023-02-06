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

package me.jb.groupevent.farmtournament.tournament.actionbar.model;

import me.jb.groupevent.farmtournament.playerdata.PlayerTournamentData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultActionBarDataModel implements ActionBarDataModel {

  private final Map<UUID, PlayerTournamentData> actionBarDataMap = new HashMap<>();

  @Override
  public void addPlayer(Player player, PlayerTournamentData playerTournamentData) {
    if (this.containsPlayer(player.getUniqueId()))
      throw new IllegalArgumentException(
          String.format("Player data already exist for player %s !", player.getName()));

    this.actionBarDataMap.put(player.getUniqueId(), playerTournamentData);
  }

  @Override
  public boolean containsPlayer(UUID playerUUID) {
    return this.actionBarDataMap.containsKey(playerUUID);
  }

  @Override
  public Map<UUID, PlayerTournamentData> getActionBarDataMap() {
    return new HashMap<>(this.actionBarDataMap);
  }

  @Override
  public PlayerTournamentData getPlayerTournamentData(UUID playerUUID) {
    return this.actionBarDataMap.get(playerUUID);
  }

  @Override
  public void clear() {
    this.actionBarDataMap.clear();
  }

  @Override
  public String toString() {
    return "DefaultActionBarDataModel{" + "actionBarDataMap=" + actionBarDataMap + '}';
  }
}

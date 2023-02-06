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

package me.jb.groupevent.farmtournament.playerdata.model;

import me.jb.groupevent.farmtournament.playerdata.PlayerTournamentData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultPlayerDataModel implements PlayerDataModel {

  private final Map<UUID, PlayerTournamentData> uuidPlayerTDataMap = new HashMap<>();

  @Override
  public @Unmodifiable Map<UUID, PlayerTournamentData> getDataMap() {
    return this.uuidPlayerTDataMap;
  }

  @Override
  public void addPlayer(Player player, PlayerTournamentData playerTData) {
    if (this.containsPlayer(player.getUniqueId()))
      throw new IllegalArgumentException(
          String.format("Player data already exist for player %s !", player.getName()));

    this.uuidPlayerTDataMap.put(player.getUniqueId(), playerTData);
  }

  @Override
  public PlayerTournamentData getPlayerData(UUID playerUUID) {
    return this.uuidPlayerTDataMap.get(playerUUID);
  }

  @Override
  public boolean containsPlayer(UUID playerUUID) {
    return this.uuidPlayerTDataMap.containsKey(playerUUID);
  }

  @Override
  public void clear() {
    this.uuidPlayerTDataMap.clear();
  }

  @Override
  public String toString() {
    return "DefaultPlayerDataModel{" + "uuidPlayerTDataMap=" + uuidPlayerTDataMap + '}';
  }
}

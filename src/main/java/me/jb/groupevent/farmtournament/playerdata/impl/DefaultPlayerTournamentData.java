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

package me.jb.groupevent.farmtournament.playerdata.impl;

import me.jb.groupevent.farmtournament.playerdata.PlayerTournamentData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.UUID;

public class DefaultPlayerTournamentData implements PlayerTournamentData {

  private final UUID playerUUID;
  private int points;

  public DefaultPlayerTournamentData(
      @NotNull UUID playerUUID, @Range(from = 0, to = Integer.MAX_VALUE) int points) {
    this.playerUUID = playerUUID;
    this.points = points;
  }

  public DefaultPlayerTournamentData(@NotNull UUID playerUUID) {
    this(playerUUID, 0);
  }

  @Override
  public UUID getPlayerUUID() {
    return this.playerUUID;
  }

  @Override
  public Player getPlayer() {
    return (this.playerUUID != null) ? Bukkit.getPlayer(this.playerUUID) : null;
  }

  @Override
  public OfflinePlayer getOfflinePlayer() {
    return (this.playerUUID != null) ? Bukkit.getOfflinePlayer(this.playerUUID) : null;
  }

  @Override
  public int getPoints() {
    return this.points;
  }

  @Override
  public void addPoints(@Range(from = 0, to = Integer.MAX_VALUE) int addedPoints) {
    this.points += addedPoints;
  }

  @Override
  public String toString() {
    return "DefaultPlayerTournamentData{" + "playerUUID=" + playerUUID + ", points=" + points + '}';
  }
}

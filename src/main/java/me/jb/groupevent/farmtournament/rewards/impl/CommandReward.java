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

package me.jb.groupevent.farmtournament.rewards.impl;

import me.jb.groupevent.farmtournament.rewards.Reward;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class CommandReward implements Reward {

  private final String commandReward;

  public CommandReward(String commandReward) {
    this.commandReward = commandReward;
  }

  @Override
  public void give(UUID playerUUID) {
    OfflinePlayer selectedPlayer = Bukkit.getOfflinePlayer(playerUUID);
    String playerName = selectedPlayer.getName();

    if (playerName == null)
      throw new IllegalStateException(
          "Error while giving reward to player UUID \"" + playerUUID + ". Player name is null.");

    String finalCommandReward = this.commandReward.replace("%player_name%", playerName);
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommandReward);
  }

  @Override
  public String toString() {
    return "CommandReward{" + "commandReward='" + commandReward + '\'' + '}';
  }
}

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

package me.jb.groupevent.farmtournament.rewards.config.impl;

import me.jb.groupevent.farmtournament.rewards.Reward;
import me.jb.groupevent.farmtournament.rewards.config.RewardsDAO;
import me.jb.groupevent.farmtournament.rewards.config.RewardsModel;
import me.jb.groupevent.farmtournament.rewards.config.RewardsService;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class DefaultRewardsService implements RewardsService {

  private final RewardsModel rewardsModel;
  private final Plugin plugin;
  private final RewardsDAO rewardsDAO;

  public DefaultRewardsService(
      @NotNull Plugin plugin, @NotNull RewardsModel rewardsModel, @NotNull RewardsDAO rewardsDAO) {
    this.rewardsModel = rewardsModel;
    this.rewardsDAO = rewardsDAO;
    this.plugin = plugin;
  }

  @Override
  public void load() {
    HashMap<Integer, List<Reward>> rewardsMap = this.rewardsDAO.loadRewards();
    rewardsMap.forEach(this.rewardsModel::addRewardsToRank);
  }

  @Override
  public void giveRewards(List<UUID> playerCollection) {
    Map<Integer, List<Reward>> rewardsMap = this.rewardsModel.getRewardsMap();
    int playerSize = playerCollection.size();

    rewardsMap.forEach(
        (rank, rewards) -> {
          if (playerSize < rank) {
            this.plugin
                .getLogger()
                .log(Level.INFO, "Cannot give rewards for rank " + rank + ": No player found.");
            return;
          }
          UUID selectedPlayerUUID = playerCollection.get(rank - 1);
          rewards.forEach(reward -> reward.give(selectedPlayerUUID));
        });
  }

  @Override
  public void reload() {
    this.rewardsModel.clearCache();
    this.load();
  }
}

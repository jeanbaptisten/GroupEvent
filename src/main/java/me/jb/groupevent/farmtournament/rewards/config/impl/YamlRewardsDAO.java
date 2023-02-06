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

import me.jb.groupevent.configuration.FileHandler;
import me.jb.groupevent.utils.IntegerUtils;
import me.jb.groupevent.farmtournament.rewards.Reward;
import me.jb.groupevent.farmtournament.rewards.RewardFactory;
import me.jb.groupevent.farmtournament.rewards.config.RewardsDAO;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class YamlRewardsDAO implements RewardsDAO {

  private final FileHandler fileHandler;

  public YamlRewardsDAO(FileHandler fileHandler) {
    this.fileHandler = fileHandler;
  }

  @Override
  public HashMap<Integer, List<Reward>> loadRewards() {
    HashMap<Integer, List<Reward>> rewardsMap = new HashMap<>();

    FileConfiguration mainConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    ConfigurationSection ranksConfigurationSection = mainConfig.getConfigurationSection("rewards");

    Set<String> ranksKey = ranksConfigurationSection.getKeys(false);
    ranksKey.forEach(
        rank -> {
          if (!IntegerUtils.isNumeric(rank))
            throw new IllegalStateException("Rank " + rank + " isn't numeric !");

          int rankInt = Integer.parseInt(rank);

          if (rankInt < 1)
            throw new IllegalStateException("Rank " + rank + " is negative or zero !");

          List<Reward> rewardList = this.getRewardsList(ranksConfigurationSection, rankInt);
          rewardsMap.put(rankInt, rewardList);
        });

    return rewardsMap;
  }

  private List<Reward> getRewardsList(ConfigurationSection ranksConfigurationSection, int rank) {
    List<Reward> rewardList = new ArrayList<>();
    ConfigurationSection rewardsFromRankConfigSection =
        ranksConfigurationSection.getConfigurationSection(String.valueOf(rank));

    Set<String> rewardsList = rewardsFromRankConfigSection.getKeys(false);
    rewardsList.forEach(
        rewardName -> {
          Optional<Reward> optionalReward =
              RewardFactory.getReward(rewardsFromRankConfigSection, rewardName);

          if (!optionalReward.isPresent())
            throw new IllegalStateException("Reward " + rewardName + " has an incorrect type.");

          Reward reward = optionalReward.get();
          rewardList.add(reward);
        });

    return rewardList;
  }
}

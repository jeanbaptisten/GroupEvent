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
import me.jb.groupevent.farmtournament.rewards.config.RewardsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRewardsModel implements RewardsModel {

  private final HashMap<Integer, List<Reward>> rewardsMap = new HashMap<>();

  @Override
  public Map<Integer, List<Reward>> getRewardsMap() {
    return new HashMap<>(rewardsMap);
  }

  @Override
  public List<Reward> getRewardListFromRank(Integer rank) {
    if (!this.rewardsMap.containsKey(rank))
      throw new IllegalStateException("Rank " + rank + " doesn't have rewards.");

    return new ArrayList<>(this.rewardsMap.get(rank));
  }

  @Override
  public void addRewardsToRank(Integer rank, List<Reward> rewardsList) {
    if (this.rewardsMap.containsKey(rank))
      throw new IllegalStateException("Rank " + rank + "already exist and cannot be added.");

    this.rewardsMap.put(rank, rewardsList);
  }

  @Override
  public void removeRewardsToRank(Integer rank) {
    if (!this.rewardsMap.containsKey(rank))
      throw new IllegalStateException("Rank " + rank + " doesn't exist and cannot be removed.");

    this.rewardsMap.remove(rank);
  }

  @Override
  public void clearCache() {
    this.rewardsMap.clear();
  }

  @Override
  public String toString() {
    return "DefaultRewardsModel{" + "rewardsMap=" + rewardsMap + '}';
  }
}

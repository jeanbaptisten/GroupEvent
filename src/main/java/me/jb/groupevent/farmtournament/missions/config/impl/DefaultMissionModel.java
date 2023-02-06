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

package me.jb.groupevent.farmtournament.missions.config.impl;

import me.jb.groupevent.farmtournament.missions.Mission;
import me.jb.groupevent.farmtournament.missions.config.MissionModel;
import org.bukkit.Keyed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultMissionModel implements MissionModel {

  private final List<Mission<? extends Keyed>> missionList = new ArrayList<>();
  private Mission<? extends Keyed> lastMission;

  @Override
  public void addMission(Mission<? extends Keyed> mission) {
    if (this.missionList.contains(mission))
      throw new IllegalArgumentException(
          String.format("Mission %s already exist !", mission.getElementMission()));

    this.missionList.add(mission);
  }

  @Override
  public Mission<? extends Keyed> getRandomMission() {
    Random random = new Random();
    int missionListSize = this.missionList.size();
    Mission<? extends Keyed> randomMission = this.missionList.get(random.nextInt(missionListSize));

    if (missionListSize != 1)
      while (randomMission == this.lastMission) {
        randomMission = this.missionList.get(random.nextInt(this.missionList.size()));
      }

    this.lastMission = randomMission;

    return randomMission;
  }

  @Override
  public void clearCache() {
    this.missionList.clear();
  }

  @Override
  public String toString() {
    return "DefaultMissionModel{" + "missionList=" + missionList + '}';
  }
}

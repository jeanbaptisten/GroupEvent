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

package me.jb.groupevent.farmtournament.missions.impl;

import me.jb.groupevent.farmtournament.missions.Mission;
import me.jb.groupevent.farmtournament.missions.type.MissionType;
import org.bukkit.Keyed;

public class DefaultMission<T extends Keyed> implements Mission<T> {

  private final MissionType missionType;
  private final String name;
  private final String linkedPlaceholder;
  private final int points;
  private final T elementMission;

  public DefaultMission(MissionType missionType, T elementMission, String name, String linkedPlaceholder, int points) {
    this.missionType = missionType;
    this.name = name;
    this.elementMission = elementMission;
    this.linkedPlaceholder = linkedPlaceholder;
    this.points = points;
  }

  @Override
  public String getLinkedPlaceholder() {
    return linkedPlaceholder;
  }

  @Override
  public MissionType getType() {
    return this.missionType;
  }

  @Override
  public T getElementMission() {
    return this.elementMission;
  }

  @Override
  public int getPoints() {
    return this.points;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    return "DefaultMission{"
        + "missionType="
        + missionType
        + ", name='"
        + name
        + '\''
        + ", points="
        + points
        + ", elementMission="
        + elementMission
        + '}';
  }
}

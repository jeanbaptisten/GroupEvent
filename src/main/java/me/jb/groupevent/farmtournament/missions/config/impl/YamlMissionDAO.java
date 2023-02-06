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

import java.util.ArrayList;
import java.util.List;

import me.jb.groupevent.configuration.FileHandler;
import me.jb.groupevent.farmtournament.missions.Mission;
import me.jb.groupevent.farmtournament.missions.config.MissionDAO;
import me.jb.groupevent.farmtournament.missions.impl.DefaultMission;
import me.jb.groupevent.farmtournament.missions.type.MissionType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class YamlMissionDAO implements MissionDAO {

  private final FileHandler fileHandler;

  public YamlMissionDAO(@NotNull FileHandler fileHandler) {
    this.fileHandler = fileHandler;
  }

  @Override
  public List<Mission<EntityType>> loadEntityMissions(MissionType missionType, String mission) {
    FileConfiguration mainConfig = this.fileHandler.getMissionConfigFile().getConfig();

    ConfigurationSection selectedMissionSection = mainConfig.getConfigurationSection(mission);
    if (selectedMissionSection == null) return new ArrayList<>();

    List<Mission<EntityType>> missionList = new ArrayList<>();

    selectedMissionSection
        .getKeys(false)
        .forEach(
            stringEntity -> {
              EntityType entityType;

              try {
                entityType = EntityType.valueOf(stringEntity);
              } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    String.format("EntityType %s doesn't exist.", stringEntity), e);
              }

              String name = selectedMissionSection.getString(stringEntity + ".name");
              int points = selectedMissionSection.getInt(stringEntity + ".points");
              String linkedPlaceholder =
                  selectedMissionSection.getString(stringEntity + ".linkedPlaceholder");

              Mission<EntityType> materialMission =
                  new DefaultMission<>(missionType, entityType, name, linkedPlaceholder, points);
              missionList.add(materialMission);
            });

    return missionList;
  }

  @Override
  public List<Mission<Material>> loadMaterialMissions(MissionType missionType, String mission) {
    FileConfiguration mainConfig = this.fileHandler.getMissionConfigFile().getConfig();

    ConfigurationSection selectedMissionSection = mainConfig.getConfigurationSection(mission);
    if (selectedMissionSection == null) return new ArrayList<>();

    List<Mission<Material>> missionList = new ArrayList<>();

    selectedMissionSection
        .getKeys(false)
        .forEach(
            stringMat -> {
              Material mat;

              try {
                mat = Material.valueOf(stringMat);
              } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    String.format("Material %s doesn't exist.", stringMat), e);
              }

              String name = selectedMissionSection.getString(stringMat + ".name");
              String linkedPlaceholder =
                  selectedMissionSection.getString(stringMat + ".linkedPlaceholder");
              int points = selectedMissionSection.getInt(stringMat + ".points");

              Mission<Material> materialMission =
                  new DefaultMission<>(missionType, mat, name, linkedPlaceholder, points);
              missionList.add(materialMission);
            });

    return missionList;
  }
}

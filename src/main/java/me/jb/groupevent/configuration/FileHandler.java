/*
 * Copyright (c) 2022 Niz
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

package me.jb.groupevent.configuration;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileHandler {

  private CustomConfig mainConfig, messageConfig;
  private CustomConfig missionConfig, tournamentConfig;

  private final Plugin plugin;

  public FileHandler(@NotNull Plugin plugin) {
    this.plugin = plugin;

    this.loadFiles();
  }

  @NotNull
  public CustomConfig getMainConfig() {
    return this.mainConfig;
  }

  @NotNull
  public CustomConfig getMissionConfigFile() {
    return this.missionConfig;
  }

  @NotNull
  public CustomConfig getMessageConfigFile() {
    return this.messageConfig;
  }

  @NotNull
  public CustomConfig getTournamentConfigFile() {
    return this.tournamentConfig;
  }

  public void loadFiles() {

    // Initialize main configuration file.
    this.setupMainConfigFile();

    // Initialize mission configuration file.
    this.setupMissionConfigFile();

    // Initialize tournament configuration file.
    this.setupTournamentConfigFile();

    // Initialize message configuration file.
    this.setupMessageConfigFile();

    // Initialise README file.
    this.setupReadMeFile();
  }

  private void setupMainConfigFile() {
    File mainConfigFile = FileEngine.fileCreator(this.plugin, "config.yml");
    this.mainConfig = new CustomConfig(mainConfigFile);
  }

  private void setupReadMeFile() {
    FileEngine.fileCreator(this.plugin, "README.txt");
  }

  private void setupMissionConfigFile() {
    File missionConfigFile = FileEngine.fileCreator(this.plugin, "tournament", "missions.yml");
    this.missionConfig = new CustomConfig(missionConfigFile);
  }

  private void setupTournamentConfigFile() {
    File tournamentConfigFile = FileEngine.fileCreator(this.plugin, "tournament", "tournament.yml");
    this.tournamentConfig = new CustomConfig(tournamentConfigFile);
  }

  private void setupMessageConfigFile() {
    File messageConfigFile = FileEngine.fileCreator(this.plugin, "messages.yml");
    this.messageConfig = new CustomConfig(messageConfigFile);
  }
}

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

package me.jb.groupevent.farmtournament;

import me.jb.groupevent.configuration.FileHandler;
import me.jb.groupevent.farmtournament.commands.CmdTf;
import me.jb.groupevent.farmtournament.listeners.PlayerListener;
import me.jb.groupevent.farmtournament.tournament.controller.DefaultTournamentController;
import me.jb.groupevent.farmtournament.tournament.controller.TournamentController;
import me.jb.groupevent.farmtournament.tournament.dao.YamlTournamentDAO;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModelProvider;
import me.jb.groupevent.farmtournament.tournament.service.DefaultTournamentService;
import me.jb.groupevent.farmtournament.tournament.service.TournamentService;
import me.jb.groupevent.farmtournament.tournament.task.TournamentMessageTask;
import me.jb.groupevent.placeholderexpansion.FarmTournamentExpansion;
import me.jb.groupevent.farmtournament.commands.tabcompleter.TBTf;
import me.jb.groupevent.farmtournament.listeners.TournamentListener;
import me.jb.groupevent.farmtournament.missions.config.MissionModel;
import me.jb.groupevent.farmtournament.missions.config.MissionService;
import me.jb.groupevent.farmtournament.missions.config.impl.DefaultMissionModel;
import me.jb.groupevent.farmtournament.missions.config.impl.DefaultMissionService;
import me.jb.groupevent.farmtournament.missions.config.impl.YamlMissionDAO;
import me.jb.groupevent.farmtournament.rewards.config.RewardsDAO;
import me.jb.groupevent.farmtournament.rewards.config.RewardsModel;
import me.jb.groupevent.farmtournament.rewards.config.RewardsService;
import me.jb.groupevent.farmtournament.rewards.config.impl.DefaultRewardsModel;
import me.jb.groupevent.farmtournament.rewards.config.impl.DefaultRewardsService;
import me.jb.groupevent.farmtournament.rewards.config.impl.YamlRewardsDAO;
import me.jb.groupevent.farmtournament.tournament.task.TournamentRankingTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class Tournament {

  private final JavaPlugin plugin;
  private final FileHandler fileHandler;

  private RewardsService rewardsService;
  private RewardsModel rewardsModel;

  private MissionService missionService;
  private MissionModel missionModel;

  private TournamentController tournamentController;
  private TournamentService tournamentService;
  private TournamentModel tournamentModel;

  private BukkitTask tournamentRankingTask, tournamentMessageTask;
  private FarmTournamentExpansion farmTournamentExpansion;

  public Tournament(@NotNull JavaPlugin plugin, @NotNull FileHandler fileHandler) {
    this.plugin = plugin;
    this.fileHandler = fileHandler;
  }

  public void enable() {
    this.setupRewards();
    this.setupMission();
    this.setupTournamentSystem();

    this.registerListeners();
    this.registerCommands();

    this.registerPlaceholders();
    this.setupTournamentRankingTask();
    this.setupTournamentMessageTask();
  }

  private void setupTournamentMessageTask() {
    if (this.tournamentMessageTask != null) this.tournamentMessageTask.cancel();

    this.tournamentMessageTask =
        Bukkit.getScheduler()
            .runTaskTimer(this.plugin, new TournamentMessageTask(this.tournamentModel), 20L, 20L);
  }

  private void setupTournamentRankingTask() {
    if (this.tournamentRankingTask != null) this.tournamentRankingTask.cancel();

    FileConfiguration tournamentConfig = this.fileHandler.getTournamentConfigFile().getConfig();
    long period = 20L * tournamentConfig.getLong("topRefreshPeriod");
    this.tournamentRankingTask =
        Bukkit.getScheduler()
            .runTaskTimer(
                this.plugin,
                new TournamentRankingTask(this.farmTournamentExpansion, this.tournamentModel),
                20L,
                period);
  }

  public void disable() {}

  private void registerPlaceholders() {
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      this.farmTournamentExpansion = new FarmTournamentExpansion(this);
      this.farmTournamentExpansion.register();
    }
  }

  private void registerListeners() {
    Bukkit.getPluginManager()
        .registerEvents(
            new PlayerListener(
                this.plugin,
                this.tournamentModel,
                this.tournamentModel.getActionBarManager().getActionBarDataModel()),
            this.plugin);
    Bukkit.getPluginManager()
        .registerEvents(new TournamentListener(this.fileHandler, tournamentModel), this.plugin);
  }

  private void registerCommands() {
    this.plugin.getCommand("ts").setExecutor(new CmdTf(this, this.fileHandler));
    this.plugin.getCommand("ts").setTabCompleter(new TBTf());
  }

  private void setupRewards() {
    RewardsDAO rewardsDAO = new YamlRewardsDAO(this.fileHandler);
    this.rewardsModel = new DefaultRewardsModel();
    this.rewardsService = new DefaultRewardsService(this.plugin, rewardsModel, rewardsDAO);
    this.rewardsService.load();
  }

  private void setupMission() {
    this.missionModel = new DefaultMissionModel();
    this.missionService =
        new DefaultMissionService(this.missionModel, new YamlMissionDAO(this.fileHandler));
    this.missionService.load();
  }

  private void setupTournamentSystem() {
    // Setup model
    this.tournamentModel = new TournamentModelProvider();

    // Setup service
    this.tournamentService =
        new DefaultTournamentService(new YamlTournamentDAO(this.fileHandler), this.tournamentModel);
    this.tournamentService.load();

    // Setup controller
    this.tournamentController =
        new DefaultTournamentController(
            this.plugin, this.tournamentModel, this.missionModel, this.rewardsService);
    this.tournamentController.loadNextEvent();
  }

  public void reloadPlugin() {
    this.fileHandler.loadFiles();
    this.rewardsService.reload();
    this.missionService.reload();
    this.tournamentService.reload();
    this.tournamentController.reload();
    this.setupTournamentRankingTask();
    this.setupTournamentMessageTask();
  }

  public MissionService getMissionService() {
    return this.missionService;
  }

  public MissionModel getMissionModel() {
    return this.missionModel;
  }

  public TournamentController getTournamentController() {
    return this.tournamentController;
  }

  public TournamentService getTournamentService() {
    return this.tournamentService;
  }

  public TournamentModel getTournamentModel() {
    return this.tournamentModel;
  }

  public RewardsService getRewardsService() {
    return this.rewardsService;
  }

  public RewardsModel getRewardsModel() {
    return this.rewardsModel;
  }
}

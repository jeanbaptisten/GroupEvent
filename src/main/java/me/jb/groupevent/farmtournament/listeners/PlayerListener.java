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

package me.jb.groupevent.farmtournament.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jb.groupevent.farmtournament.tournament.actionbar.model.ActionBarDataModel;
import me.jb.groupevent.farmtournament.missions.Mission;
import me.jb.groupevent.farmtournament.missions.type.MissionType;
import me.jb.groupevent.farmtournament.playerdata.impl.DefaultPlayerTournamentData;
import me.jb.groupevent.farmtournament.playerdata.model.PlayerDataModel;
import me.jb.groupevent.farmtournament.tournament.data.active.ActiveTournamentData;
import me.jb.groupevent.farmtournament.tournament.model.TournamentModel;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener {

  private final JavaPlugin javaPlugin;
  private final TournamentModel tournamentModel;
  private final ActionBarDataModel actionBarDataModel;

  public PlayerListener(
      @NotNull JavaPlugin javaPlugin,
      @NotNull TournamentModel tournamentModel,
      @NotNull ActionBarDataModel actionBarDataModel) {
    this.javaPlugin = javaPlugin;
    this.tournamentModel = tournamentModel;
    this.actionBarDataModel = actionBarDataModel;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockBreak(BlockBreakEvent event) {

    if (!this.tournamentModel.hasActiveTournament()) return;

    if (event.isCancelled()) return;

    ActiveTournamentData activeTournamentData =
        this.tournamentModel.getActiveTournamentData().get();
    Mission<? extends Keyed> activeMission = activeTournamentData.getActiveTournamentMission();
    MissionType activeMissionType = activeMission.getType();

    if (!(activeMissionType == MissionType.FARMING || activeMissionType == MissionType.MINING))
      return;

    // Crops with full age ?
    BlockData blockData = event.getBlock().getBlockData();
    if (blockData instanceof Ageable)
      if (((Ageable) blockData).getAge() != ((Ageable) blockData).getMaximumAge()) return;

    // Right mat ?
    Material missionMat = (Material) activeMission.getElementMission();
    if (!event.getBlock().getType().name().equals(missionMat.name())) return;

    Player player = event.getPlayer();

    this.addPoints(player, activeMission, 1);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityKilled(EntityDeathEvent event) {

    if (!this.tournamentModel.hasActiveTournament()) return;

    Player player = event.getEntity().getKiller();

    if (player == null) return;

    ActiveTournamentData activeTournamentData =
        this.tournamentModel.getActiveTournamentData().get();
    Mission<? extends Keyed> activeMission = activeTournamentData.getActiveTournamentMission();
    MissionType activeMissionType = activeMission.getType();

    if (!(activeMissionType == MissionType.KILL)) return;

    // Right mat ?
    EntityType missionEntity = (EntityType) activeMission.getElementMission();
    if (!event.getEntity().getType().name().equals(missionEntity.name())) return;

    this.addPoints(player, activeMission, 1);
  }

  @EventHandler
  public void onItemCraft(CraftItemEvent event) {

    if (!this.tournamentModel.hasActiveTournament()) return;

    if (event.isCancelled()) return;

    InventoryAction eventAction = event.getAction();
    if (eventAction == InventoryAction.DROP_ALL_SLOT
        || eventAction == InventoryAction.DROP_ONE_SLOT
        || eventAction == InventoryAction.NOTHING) return;

    ActiveTournamentData activeTournamentData =
        this.tournamentModel.getActiveTournamentData().get();
    Mission<? extends Keyed> activeMission = activeTournamentData.getActiveTournamentMission();
    MissionType activeMissionType = activeMission.getType();

    if (!(activeMissionType == MissionType.CRAFTING)) return;

    // Right craft ?
    Material missionMat = (Material) activeMission.getElementMission();
    ItemStack resultItem = event.getRecipe().getResult();

    if (!resultItem.getType().name().equals(missionMat.name())) return;

    Player player = (Player) event.getWhoClicked();

    if (event.isShiftClick()) {
      final int smallestAmountBefore = this.getSmallestAmountFromMatrix(event);

      List<ItemStack> firstMatrix = new ArrayList<>();
      Arrays.stream(event.getInventory().getMatrix())
          .forEach(
              itemStack -> {
                if (itemStack != null) firstMatrix.add(new ItemStack(itemStack));
              });

      Bukkit.getScheduler()
          .runTaskLater(
              this.javaPlugin,
              () -> {
                int smallestAmountAfter;
                List<ItemStack> secondMatrix = new ArrayList<>();
                System.out.println(Arrays.toString(event.getInventory().getMatrix()));
                Arrays.stream(event.getInventory().getMatrix())
                    .forEach(
                        itemStack -> {
                          if (itemStack != null) secondMatrix.add(new ItemStack(itemStack));
                        });
                System.out.println(
                    "Same ? "
                        + this.sameMaterialInMatrix(firstMatrix, secondMatrix));
                if (this.sameMaterialInMatrix(firstMatrix, secondMatrix))
                  smallestAmountAfter = this.getSmallestAmountFromMatrix(event);
                else smallestAmountAfter = 0;

                int recipesTriggered = smallestAmountBefore - smallestAmountAfter;

                System.out.println("Recipes: " + recipesTriggered);
                this.addPoints(player, activeMission, recipesTriggered);
              },
              1);
      return;
    }
    System.out.println("Fuck ?");
    final int amountItems = resultItem.getAmount();

    this.addPoints(player, activeMission, amountItems);
  }

  private void addPoints(
      Player player, Mission<? extends Keyed> activeMission, int recipesTriggered) {
    PlayerDataModel playerDataModel = this.tournamentModel.getPlayerDataModel();
    int points = recipesTriggered * activeMission.getPoints();

    if (playerDataModel.containsPlayer(player.getUniqueId()))
      playerDataModel.getPlayerData(player.getUniqueId()).addPoints(points);
    else
      playerDataModel.addPlayer(
          player, new DefaultPlayerTournamentData(player.getUniqueId(), points));

    if (this.actionBarDataModel.containsPlayer(player.getUniqueId()))
      this.actionBarDataModel.getPlayerTournamentData(player.getUniqueId()).addPoints(points);
    else
      this.actionBarDataModel.addPlayer(
          player, new DefaultPlayerTournamentData(player.getUniqueId(), points));
  }

  private int getSmallestAmountFromMatrix(CraftItemEvent event) {
    int smallestAmount = -1;
    for (ItemStack item : event.getInventory().getMatrix()) {
      if (item != null && !item.getType().equals(Material.AIR)) {
        final int amount = item.getAmount();
        if (amount < smallestAmount || smallestAmount == -1) {
          smallestAmount = amount;
        }
      }
    }
    return smallestAmount != -1 ? smallestAmount : 0;
  }

  private boolean sameMaterialInMatrix(List<ItemStack> firstMatrix, List<ItemStack> secondMatrix) {
    System.out.println("First list " + firstMatrix);
    System.out.println("Second list " + secondMatrix);

    List<ItemStack> endedIS = new ArrayList<>(firstMatrix);

    firstMatrix.forEach(
        itemStack -> {
          Material itemMat = itemStack.getType();
          endedIS.remove(itemMat);
        });
    return secondMatrix.isEmpty();
  }

  private ItemStack getCraftedItem(CraftItemEvent event) {
    if (event.isShiftClick()) {
      final ItemStack recipeResult = event.getRecipe().getResult();
      final int resultAmt = recipeResult.getAmount(); // Bread = 1, Cookie = 8, etc.
      int leastIngredient = -1;
      for (ItemStack item : event.getInventory().getMatrix()) {
        if (item != null && !item.getType().equals(Material.AIR)) {
          final int re = item.getAmount() * resultAmt;
          if (leastIngredient == -1 || re < leastIngredient) {
            leastIngredient = item.getAmount() * resultAmt;
          }
        }
      }
      return new ItemStack(recipeResult.getType(), leastIngredient);
    }
    return event.getCurrentItem();
  }
}

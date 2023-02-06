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

package me.jb.groupevent.farmtournament.tournament.actionbar.manager;

import me.jb.groupevent.utils.MessageUtils;
import me.jb.groupevent.farmtournament.tournament.actionbar.model.ActionBarDataModel;
import me.jb.groupevent.farmtournament.tournament.actionbar.model.DefaultActionBarDataModel;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DefaultActionBarManager implements ActionBarManager {

  private final ActionBarDataModel actionBarDataModel = new DefaultActionBarDataModel();

  public void showActionBarToPlayers(String format) {
    this.actionBarDataModel
        .getActionBarDataMap()
        .values()
        .forEach(
            playerTournamentData -> {
              Player player = Bukkit.getPlayer(playerTournamentData.getPlayerUUID());
              if (player == null) return;

              String message =
                  MessageUtils.setupMessage(
                      player,
                      format.replace("%points%", String.valueOf(playerTournamentData.getPoints())));
              TextComponent textComponent = new TextComponent(message);

              player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            });

    this.actionBarDataModel.clear();
  }

  public ActionBarDataModel getActionBarDataModel() {
    return this.actionBarDataModel;
  }
}

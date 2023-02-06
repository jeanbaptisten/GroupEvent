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

package me.jb.groupevent;

import me.jb.groupevent.configuration.FileHandler;
import me.jb.groupevent.farmtournament.Tournament;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class GroupEvent extends JavaPlugin {

  private final FileHandler fileHandler = new FileHandler(this);

  private static Tournament tournament;

  @Override
  public void onEnable() {
    FileConfiguration mainConfig = this.fileHandler.getMainConfig().getConfig();

    if (mainConfig.getBoolean("farmTournament")) {
      tournament = new Tournament(this, fileHandler);
      tournament.enable();
      this.getLogger().log(Level.INFO, ChatColor.GREEN + "Farm tournament loaded.");
    }
  }

  @Override
  public void onDisable() {
    tournament.disable();
  }

  public static Tournament getFarmTournamentModule() {
    return tournament;
  }
}

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

package me.jb.groupevent.farmtournament.commands.tabcompleter;

import me.jb.groupevent.enums.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TBTf implements TabCompleter {

  private static final List<String> ALL_CMD_LIST =
      Arrays.asList("info", "infos", "reload", "start", "forcestop");

  @Nullable
  @Override
  public List<String> onTabComplete(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String alias,
      @NotNull String[] args) {
    List<String> cmdList = new ArrayList<>();

    if (args.length == 1) {
      if (sender.hasPermission(Permission.TS_ADMIN.getPermission())) return ALL_CMD_LIST;

      if (sender.hasPermission(Permission.TS_USE.getPermission())) {
        cmdList.add("info");
        cmdList.add("infos");
      }
      if (sender.hasPermission(Permission.RELOAD.getPermission())) cmdList.add("reload");
      if (sender.hasPermission(Permission.TS_START.getPermission())) cmdList.add("start");
      if (sender.hasPermission(Permission.TS_FORCESTOP.getPermission())) cmdList.add("forcestop");
      return cmdList;
    }

    return cmdList;
  }
}

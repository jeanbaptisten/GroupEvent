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

package me.jb.groupevent.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

  private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

  @NotNull
  public static String setColorsMessage(String string) {
    return translateHexColorCodes(ChatColor.translateAlternateColorCodes('&', string));
  }

  @NotNull
  public static String setupMessage(Player player, String message) {
    return translateHexColorCodes(PlaceholderAPI.setPlaceholders(player, message));
  }

  private static String translateHexColorCodes(String message) {
    Matcher matcher = HEX_PATTERN.matcher(message);
    StringBuffer buffer = new StringBuffer();

    while (matcher.find()) {
      matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
    }

    return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
  }
}

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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class FileEngine {

  @NotNull
  public static File fileCreator(@NotNull Plugin plugin, String path, @NotNull String fileName) {
    File file = new File(plugin.getDataFolder().getPath() + File.separator + path, fileName);
    return fileCreator(plugin, file);
  }

  @NotNull
  public static File fileCreator(@NotNull Plugin plugin, @NotNull String fileName) {
    File file = new File(plugin.getDataFolder().getPath(), fileName);
    return fileCreator(plugin, file);
  }

  @NotNull
  public static File fileCreator(@NotNull Plugin plugin, @NotNull File file) {

    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

    if (!file.exists())
      try (InputStream in = plugin.getResource(file.getName())) {
        Files.copy(in, file.toPath());
      } catch (IOException e) {
        plugin.getLogger().log(Level.SEVERE, "Error generating the plugin file: " + file.getName());
        e.printStackTrace();
      }

    return file;
  }

  @NotNull
  public static FileConfiguration fileConfigurationLoader(File file) {
    return YamlConfiguration.loadConfiguration(file);
  }
}

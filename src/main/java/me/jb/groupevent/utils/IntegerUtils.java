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

public class IntegerUtils {

  public static boolean isNumeric(String string) {
    if (string.trim().equals("")) return false;

    try {
      Integer.parseInt(string.trim());
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static String parseTime(Integer totalSecs) {
    int h = totalSecs / 3600;
    int m = (totalSecs % 3600) / 60;
    int s = totalSecs % 60;
    String sh = (h > 0 ? h + " " + "h" : "");
    String sm =
            (m < 10 && m > 0 && h > 0 ? "0" : "")
                    + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : m + "m") : "");
    String ss =
            (s == 0 && (h > 0 || m > 0)
                    ? ""
                    : (s < 10 && (h > 0 || m > 0) ? "0" : "") + s + "s");
    return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
  }
}

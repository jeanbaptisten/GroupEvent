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

package me.jb.groupevent.farmtournament.tournament.data;

import org.jetbrains.annotations.NotNull;

public class DefaultTournamentData implements TournamentData {

  private final String id;
  private long beginningHour;
  private long endingHour;

  public DefaultTournamentData(@NotNull String id, long beginningHour, long endingHour) {
    this.id = id;
    this.setBeginningHour(beginningHour);
    this.setEndingHour(endingHour);
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public long getBeginningHour() {
    return this.beginningHour;
  }

  @Override
  public long getEndingHour() {
    return this.endingHour;
  }

  private void setBeginningHour(long beginningHour) {
    if (beginningHour < 0) throw new IllegalArgumentException("Beginning hour can't be negative!");
    if (beginningHour >= 86400)
      throw new IllegalArgumentException("Beginning hour can't be out of day!");

    this.beginningHour = beginningHour;
  }

  private void setEndingHour(long endingHour) {
    if (endingHour < 0) throw new IllegalArgumentException("Beginning hour can't be negative!");
    if (endingHour >= 86400)
      throw new IllegalArgumentException("Beginning hour can't be out of day!");

    if (endingHour <= beginningHour)
      throw new IllegalArgumentException("Ending hour cannot be lower than beginning hour !");

    this.endingHour = endingHour;
  }

  @Override
  public String toString() {
    return "DefaultTournamentData{"
        + "id='"
        + id
        + '\''
        + ", beginningHour="
        + beginningHour
        + ", endingHour="
        + endingHour
        + '}';
  }
}

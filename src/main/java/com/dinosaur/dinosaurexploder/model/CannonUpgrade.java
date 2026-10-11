/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.model;

import java.util.List;

/**
 * Describes how the player's cannon fires at each upgrade level. Level 1 is a single shot, level 2
 * a double cannon and level 3 a triple spread. Each cannon pickup raises the level by one, up to
 * {@link #MAX_LEVEL}, for the rest of the current game.
 */
public final class CannonUpgrade {

  public static final int MIN_LEVEL = 1;
  public static final int MAX_LEVEL = 3;

  /** Distance in pixels between the two side barrels. */
  private static final double BARREL_SPACING = 16;

  /** Angle in degrees of the two outer barrels of the triple cannon. */
  private static final double SPREAD_ANGLE = 12;

  /**
   * One barrel of the cannon.
   *
   * @param sideOffset pixels to the right of the ship's center, perpendicular to the aim
   * @param angleOffset degrees added to the ship's aim direction
   */
  public record Barrel(double sideOffset, double angleOffset) {}

  private static final List<Barrel> SINGLE = List.of(new Barrel(0, 0));
  private static final List<Barrel> DOUBLE =
      List.of(new Barrel(-BARREL_SPACING / 2, 0), new Barrel(BARREL_SPACING / 2, 0));
  private static final List<Barrel> TRIPLE =
      List.of(
          new Barrel(0, 0),
          new Barrel(-BARREL_SPACING / 2, -SPREAD_ANGLE),
          new Barrel(BARREL_SPACING / 2, SPREAD_ANGLE));

  private CannonUpgrade() {}

  /** Returns the barrels that fire together on each shot at the given cannon level. */
  public static List<Barrel> barrels(int cannonLevel) {
    return switch (clamp(cannonLevel)) {
      case 3 -> TRIPLE;
      case 2 -> DOUBLE;
      default -> SINGLE;
    };
  }

  /** Returns the cannon level after one more pickup, never above {@link #MAX_LEVEL}. */
  public static int next(int cannonLevel) {
    return clamp(cannonLevel + 1);
  }

  public static boolean isMaxed(int cannonLevel) {
    return cannonLevel >= MAX_LEVEL;
  }

  private static int clamp(int cannonLevel) {
    return Math.clamp(cannonLevel, MIN_LEVEL, MAX_LEVEL);
  }
}

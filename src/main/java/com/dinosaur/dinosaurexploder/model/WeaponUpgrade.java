/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.model;

/**
 * Describes the player's weapon tiers. A game starts at the weapon picked in the weapon menu, and
 * each weapon pickup raises it by one tier, up to {@link #MAX_TIER}, for the rest of that game.
 * Higher tiers fire faster projectiles that deal more damage to bosses and asteroids.
 */
public final class WeaponUpgrade {

  public static final int MIN_TIER = 1;
  public static final int MAX_TIER = 3;

  private static final int BASE_PROJECTILE_SPEED = 600;

  private WeaponUpgrade() {}

  /** Returns the weapon tier after one more pickup, never above {@link #MAX_TIER}. */
  public static int next(int tier) {
    return clamp(tier + 1);
  }

  public static boolean isMaxed(int tier) {
    return tier >= MAX_TIER;
  }

  /** Damage one projectile deals to a boss or an asteroid. */
  public static int damage(int tier) {
    return clamp(tier);
  }

  public static int projectileSpeed(int tier) {
    return BASE_PROJECTILE_SPEED * clamp(tier);
  }

  /** Classpath path (with leading slash) of the projectile texture for a ship and weapon tier. */
  public static String projectileImagePath(int ship, int tier) {
    return "/assets/textures/projectiles/projectile" + ship + "_" + clamp(tier) + ".png";
  }

  private static int clamp(int tier) {
    return Math.clamp(tier, MIN_TIER, MAX_TIER);
  }
}

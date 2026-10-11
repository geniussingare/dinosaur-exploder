/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.model;

import static org.junit.jupiter.api.Assertions.*;

import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.Test;

class WeaponUpgradeTest {

  @Test
  void nextStopsAtMaxTier() {
    assertEquals(2, WeaponUpgrade.next(1));
    assertEquals(3, WeaponUpgrade.next(2));
    assertEquals(WeaponUpgrade.MAX_TIER, WeaponUpgrade.next(WeaponUpgrade.MAX_TIER));
    assertFalse(WeaponUpgrade.isMaxed(2));
    assertTrue(WeaponUpgrade.isMaxed(3));
  }

  @Test
  void higherTiersHitHarderAndFaster() {
    assertEquals(1, WeaponUpgrade.damage(1));
    assertEquals(2, WeaponUpgrade.damage(2));
    assertEquals(3, WeaponUpgrade.damage(3));
    assertEquals(600, WeaponUpgrade.projectileSpeed(1));
    assertEquals(1800, WeaponUpgrade.projectileSpeed(3));
  }

  @Test
  void projectileImageExistsForEveryShipAndTier() {
    for (int ship = 1; ship <= 8; ship++) {
      for (int tier = WeaponUpgrade.MIN_TIER; tier <= WeaponUpgrade.MAX_TIER; tier++) {
        String path = WeaponUpgrade.projectileImagePath(ship, tier);
        assertNotNull(getClass().getResource(path), path);
      }
    }
  }

  @Test
  void projectileDamageDefaultsToOne() {
    Entity allyProjectile = new Entity();
    assertEquals(1, CollisionHandler.projectileDamage(allyProjectile));

    Entity playerProjectile = new Entity();
    playerProjectile.setProperty(GameEntityFactory.PROJECTILE_DAMAGE, 3);
    assertEquals(3, CollisionHandler.projectileDamage(playerProjectile));
  }
}

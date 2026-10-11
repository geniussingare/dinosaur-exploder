/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class CannonUpgradeTest {

  @Test
  void eachLevelAddsABarrel() {
    assertEquals(1, CannonUpgrade.barrels(1).size());
    assertEquals(2, CannonUpgrade.barrels(2).size());
    assertEquals(3, CannonUpgrade.barrels(3).size());
  }

  @Test
  void levelsOutsideTheRangeAreClamped() {
    assertEquals(1, CannonUpgrade.barrels(0).size());
    assertEquals(3, CannonUpgrade.barrels(10).size());
  }

  @Test
  void nextStopsAtMaxLevel() {
    assertEquals(2, CannonUpgrade.next(1));
    assertEquals(3, CannonUpgrade.next(2));
    assertEquals(CannonUpgrade.MAX_LEVEL, CannonUpgrade.next(CannonUpgrade.MAX_LEVEL));
    assertFalse(CannonUpgrade.isMaxed(2));
    assertTrue(CannonUpgrade.isMaxed(3));
  }

  @Test
  void barrelsAreSymmetric() {
    for (int level = CannonUpgrade.MIN_LEVEL; level <= CannonUpgrade.MAX_LEVEL; level++) {
      List<CannonUpgrade.Barrel> barrels = CannonUpgrade.barrels(level);
      assertEquals(0, barrels.stream().mapToDouble(CannonUpgrade.Barrel::sideOffset).sum(), 1e-9);
      assertEquals(0, barrels.stream().mapToDouble(CannonUpgrade.Barrel::angleOffset).sum(), 1e-9);
    }
  }
}

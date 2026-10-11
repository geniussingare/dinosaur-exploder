/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.components;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dinosaur.dinosaurexploder.model.CannonUpgrade;
import com.dinosaur.dinosaurexploder.utils.MockGameTimer;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerAutoFireTest {

  private static final double FRAME = 1.0 / 60;

  private MockGameTimer shootTimer;
  private PlayerComponent player;

  @BeforeEach
  void setUp() {
    shootTimer = new MockGameTimer();
    player = spy(new PlayerComponent(shootTimer));
    // shoot() needs a running game, so stand in for it: a real shot restarts the shot timer
    doAnswer(
            invocation -> {
              shootTimer.capture();
              return null;
            })
        .when(player)
        .shoot();
  }

  /** Advances the game by the given number of seconds, one frame at a time. */
  private void hold(double seconds) {
    for (double t = 0; t < seconds - 1e-9; t += FRAME) {
      shootTimer.advance(Duration.seconds(FRAME));
      player.updateAutoFire(FRAME);
    }
  }

  @Test
  void tapFiresExactlyOneShot() {
    player.pressTrigger();
    hold(0.1);
    player.releaseTrigger();
    hold(1.0);

    verify(player, times(1)).shoot();
    assertFalse(player.isTriggerHeld());
  }

  @Test
  void holdingKeepsFiring() {
    player.pressTrigger();
    hold(1.0);

    // One shot on press, then about one every 0.15 s once the 0.25 s hold delay has passed
    verify(player, atLeast(5)).shoot();
    verify(player, atMost(7)).shoot();
  }

  @Test
  void releasingStopsAutoFire() {
    player.pressTrigger();
    hold(1.0);
    player.releaseTrigger();
    clearInvocations(player);

    hold(1.0);

    verify(player, never()).shoot();
  }

  @Test
  void cannonUpgradesStopAtMaxLevel() {
    assertEquals(CannonUpgrade.MIN_LEVEL, player.getCannonLevel());
    for (int i = 0; i < 5; i++) {
      player.upgradeCannon();
    }
    assertEquals(CannonUpgrade.MAX_LEVEL, player.getCannonLevel());
    assertTrue(player.isCannonMaxed());
  }
}

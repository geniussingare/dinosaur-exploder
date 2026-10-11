/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.components;

import com.almasb.fxgl.entity.component.Component;
import com.dinosaur.dinosaurexploder.constants.Direction;

/**
 * Summary : This handles the behaviour of a dropped power-up (cannon or weapon upgrade). It drifts
 * the same way as the dinosaur that dropped it until the player collects it.
 */
public class PowerUpDropComponent extends Component {
  private static final double DROP_SPEED = 100.0;
  private double verticalSpeed = DROP_SPEED;
  private double horizontalSpeed = 0;

  public void updateDirection(Direction direction) {
    switch (direction) {
      case DOWN -> {
        verticalSpeed = -DROP_SPEED;
        horizontalSpeed = 0;
      }
      case LEFT -> {
        verticalSpeed = 0;
        horizontalSpeed = DROP_SPEED;
      }
      case RIGHT -> {
        verticalSpeed = 0;
        horizontalSpeed = -DROP_SPEED;
      }
      default -> {
        verticalSpeed = DROP_SPEED;
        horizontalSpeed = 0;
      }
    }
  }

  @Override
  public void onUpdate(double tpf) {
    entity.translateX(horizontalSpeed * tpf);
    entity.translateY(verticalSpeed * tpf);
  }
}

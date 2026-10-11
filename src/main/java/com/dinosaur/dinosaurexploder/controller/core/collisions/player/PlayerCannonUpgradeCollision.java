/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.controller.core.collisions.player;

import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;

import com.dinosaur.dinosaurexploder.components.PlayerComponent;
import com.dinosaur.dinosaurexploder.constants.EntityType;
import com.dinosaur.dinosaurexploder.constants.GameConstants;
import com.dinosaur.dinosaurexploder.controller.core.GameActions;
import com.dinosaur.dinosaurexploder.controller.core.collisions.CollisionHandlerInterface;
import com.dinosaur.dinosaurexploder.utils.AudioManager;

public class PlayerCannonUpgradeCollision implements CollisionHandlerInterface {
  private final GameActions gameActions;

  public PlayerCannonUpgradeCollision(GameActions gameActions) {
    this.gameActions = gameActions;
  }

  @Override
  public void register() {
    onCollisionBegin(
        EntityType.PLAYER,
        EntityType.CANNON_UPGRADE,
        (playerEntity, cannonUpgrade) -> {
          cannonUpgrade.removeFromWorld();
          PlayerComponent player = playerEntity.getComponent(PlayerComponent.class);
          if (player.isCannonMaxed()) {
            return;
          }
          AudioManager.getInstance().playSound(GameConstants.HEART_HIT_SOUND);
          player.upgradeCannon();
          gameActions.showPowerUpMessage("cannon_upgraded");
        });
  }
}

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

public class PlayerWeaponUpgradeCollision implements CollisionHandlerInterface {
  private final GameActions gameActions;

  public PlayerWeaponUpgradeCollision(GameActions gameActions) {
    this.gameActions = gameActions;
  }

  @Override
  public void register() {
    onCollisionBegin(
        EntityType.PLAYER,
        EntityType.WEAPON_UPGRADE,
        (playerEntity, weaponUpgrade) -> {
          weaponUpgrade.removeFromWorld();
          PlayerComponent player = playerEntity.getComponent(PlayerComponent.class);
          if (player.isWeaponMaxed()) {
            return;
          }
          AudioManager.getInstance().playSound(GameConstants.HEART_HIT_SOUND);
          player.upgradeWeapon();
          gameActions.showPowerUpMessage("weapon_upgraded");
        });
  }
}

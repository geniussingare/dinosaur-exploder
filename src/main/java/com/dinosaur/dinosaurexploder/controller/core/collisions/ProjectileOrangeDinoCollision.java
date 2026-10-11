/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.controller.core.collisions;

import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.random;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

import com.almasb.fxgl.entity.Entity;
import com.dinosaur.dinosaurexploder.components.LevelProgressBarComponent;
import com.dinosaur.dinosaurexploder.components.OrangeDinoComponent;
import com.dinosaur.dinosaurexploder.components.ScoreComponent;
import com.dinosaur.dinosaurexploder.constants.Direction;
import com.dinosaur.dinosaurexploder.constants.EntityType;
import com.dinosaur.dinosaurexploder.constants.GameConstants;
import com.dinosaur.dinosaurexploder.controller.BossSpawner;
import com.dinosaur.dinosaurexploder.controller.core.GameActions;
import com.dinosaur.dinosaurexploder.controller.core.GameInitializer;
import com.dinosaur.dinosaurexploder.model.CollisionHandler;
import com.dinosaur.dinosaurexploder.utils.AudioManager;
import com.dinosaur.dinosaurexploder.utils.LevelManager;
import java.util.logging.Logger;

public class ProjectileOrangeDinoCollision implements CollisionHandlerInterface {

  private final GameActions gameActions;

  private final CollisionHandler collisionHandler;
  private final LevelManager levelManager;
  private final BossSpawner bossSpawner;
  private final Entity score;
  private final Entity levelProgressBar;
  private Logger logger = Logger.getLogger(getClass().getName());

  public ProjectileOrangeDinoCollision(GameInitializer gameInitializer, GameActions gameActions) {
    this.gameActions = gameActions;
    this.collisionHandler = gameInitializer.getCollisionHandler();
    this.levelManager = gameInitializer.getLevelManager();
    this.bossSpawner = gameInitializer.getBossSpawner();
    this.score = gameInitializer.getScore();
    this.levelProgressBar = gameInitializer.getLevelProgressBar();
  }

  @Override
  public void register() {
    onCollisionBegin(
        EntityType.PROJECTILE,
        EntityType.ORANGE_DINO,
        (projectile, orangeDino) -> {
          OrangeDinoComponent boss = orangeDino.getComponent(OrangeDinoComponent.class);
          projectile.removeFromWorld();
          if (boss.getLives() <= 0) {
            // another projectile already defeated the boss this frame
            return;
          }
          spawn("explosion", orangeDino.getX() - 25, orangeDino.getY() - 30);
          AudioManager.getInstance().playSound(GameConstants.ENEMY_EXPLODE_SOUND);
          // stronger weapons deal more damage, but lives never drop below zero
          collisionHandler.handleHitBoss(
              boss, Math.min(CollisionHandler.projectileDamage(projectile), boss.getLives()));

          if (orangeDino.getComponent(OrangeDinoComponent.class).getLives() == 0) {
            // if the boss is defeated it drops 100% a heart
            spawn("heart", orangeDino.getX(), orangeDino.getY());
            // like the heart, the boss always drops a weapon upgrade until the weapon is maxed out
            gameActions.dropWeaponUpgrade(orangeDino.getX() - 40, orangeDino.getY(), Direction.UP);
            // if the boss dino is defeated it drops twice as many coins as the current level
            for (int i = 0; i < levelManager.getCurrentLevel() * 2; i++) {
              spawn(
                  "coin", orangeDino.getX() + random(-25, 25), orangeDino.getY() + random(-25, 25));
            }
            bossSpawner.removeBossEntity(orangeDino);

            if (collisionHandler.isLevelUpAfterBossDefeat(
                score.getComponent(ScoreComponent.class),
                levelProgressBar.getComponent(LevelProgressBarComponent.class))) {
              gameActions.showLevelMessage();
              logger.info("Level up!");
            }
          } else {
            orangeDino.getComponent(OrangeDinoComponent.class).getHealthBar().updateBar();
          }
        });
  }
}

/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */

package com.dinosaur.dinosaurexploder.controller.core.collisions;

import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

import com.dinosaur.dinosaurexploder.components.AsteroidsComponent;
import com.dinosaur.dinosaurexploder.constants.EntityType;
import com.dinosaur.dinosaurexploder.controller.core.GameInitializer;
import com.dinosaur.dinosaurexploder.model.CollisionHandler;

public class ProjectileAsteroidsCollision implements CollisionHandlerInterface {

  private final CollisionHandler collisionHandler;

  public ProjectileAsteroidsCollision(GameInitializer gameInitializer) {
    this.collisionHandler = gameInitializer.getCollisionHandler();
  }

  @Override
  public void register() {
    onCollisionBegin(
        EntityType.PROJECTILE,
        EntityType.ASTEROIDS,
        (projectile, asteroids) -> {
          AsteroidsComponent asteroid = asteroids.getComponent(AsteroidsComponent.class);
          projectile.removeFromWorld();
          if (asteroid.getLives() <= 0) {
            // another projectile already destroyed this asteroid this frame
            return;
          }
          spawn("explosion", projectile.getX(), projectile.getY());

          // stronger weapons deal more damage, but lives never drop below zero
          collisionHandler.handleHitAsteroids(
              asteroid,
              Math.min(CollisionHandler.projectileDamage(projectile), asteroid.getLives()));
          if (asteroids.getComponent(AsteroidsComponent.class).getLives() == 0) {
            asteroids.removeFromWorld();
          }
        });
  }
}

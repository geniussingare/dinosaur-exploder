/*
 * SPDX-FileCopyrightText: 2026 jvondermarck
 * SPDX-License-Identifier: MIT
 */
package com.dinosaur.dinosaurexploder.constants

import com.almasb.fxgl.core.math.FXGLMath.random

/**
 * Movement direction for enemies, coins and drops.
 *
 * This file is Kotlin and is compiled by the kotlin-maven-plugin, so run the game through Maven
 * (`mvn clean javafx:run`). IDE launchers that only compile Java (such as VS Code's Run/Debug
 * button) can't resolve this class, and the game crashes on Start. See the README's Java setup.
 */
enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    companion object {
        @JvmStatic
        fun modeDirection(mode: GameMode): Direction {
            return if (mode == GameMode.EXPERT) randomDirection() else UP
        }

        @JvmStatic
        fun randomDirection(): Direction {
            val values = values()
            val index = random(0, values.size - 1)
            return values[index]
        }
    }
}
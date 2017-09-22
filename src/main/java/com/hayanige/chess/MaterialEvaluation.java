/*
 * Copyright (C) 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Color.opposite;

import org.jetbrains.annotations.NotNull;

final class MaterialEvaluation extends Evaluation {

  /**
   * Evaluates the position.
   *
   * @param position the position.
   * @return the evaluation value in centipawns.
   */
  int evaluate(@NotNull Position position) {
    // Initialize
    int myColor = position.activeColor;
    int oppositeColor = opposite(myColor);
    int value = 0;

    // Evaluate material
    int materialScore = (evaluateMaterial(myColor, position)
        - evaluateMaterial(oppositeColor, position));
    value += materialScore;

    return value;
  }

  private int evaluateMaterial(int color, @NotNull Position position) {
    int material = position.material[color];
    return material;
  }
}

/*
 * Copyright (C) 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import java.util.Random;
import org.jetbrains.annotations.NotNull;

final class RandomEvaluation extends Evaluation {

  static Random rand = new Random();

  /**
   * Evaluates the position.
   *
   * @param position the position.
   * @return the evaluation value in centipawns.
   */
  int evaluate(@NotNull Position position) {
    return rand.nextInt(200) - 100;
  }
}

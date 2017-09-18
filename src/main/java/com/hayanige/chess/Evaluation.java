/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import org.jetbrains.annotations.NotNull;

abstract class Evaluation {

  /**
   * Evaluates the position.
   *
   * @param position the position.
   * @return the evaluation value in centipawns.
   */
  abstract int evaluate(@NotNull Position position);
}

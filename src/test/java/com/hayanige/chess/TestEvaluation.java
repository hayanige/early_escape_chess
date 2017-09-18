/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestEvaluation {

  @Test
  public void testEvaluate() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);
    Evaluation evaluation = new PulseEvaluation();

    assertEquals(evaluation.evaluate(position), PulseEvaluation.TEMPO);
  }

}

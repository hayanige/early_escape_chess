/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static org.junit.Assert.assertEquals;

import com.hayanige.chess.MoveList.MoveEntry;
import org.junit.Test;

public class TestMoveList {

  @Test
  public void test() {
    MoveList<MoveEntry> moveList = new MoveList<MoveEntry>(MoveEntry.class);

    assertEquals(moveList.size, 0);

    moveList.entries[moveList.size++].move = 1;
    assertEquals(moveList.size, 1);
  }
}

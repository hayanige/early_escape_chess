/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import com.hayanige.chess.MoveList.RootEntry;

interface Protocol {

  void sendBestMove(int bestMove, int ponderMove);

  void sendStatus(int currentDepth, int currentMaxDepth, long totalNodes,
      int currentMove, int currentMoveNumber);

  void sendStatus(boolean force, int currentDepth, int currentMaxDepth,
      long totalNodes, int currentMove, int currentMoveNumber);

  void sendMove(RootEntry entry, int currentDepth, int currentMaxDepth,
      long totalNodes);

}

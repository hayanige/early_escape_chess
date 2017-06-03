/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Color.opposite;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.hayanige.chess.MoveList.MoveEntry;

final class Perft {

  private static final int MAX_DEPTH = 6;

  private final MoveGenerator[] moveGenerators = new MoveGenerator[MAX_DEPTH];

  void run() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);
    int depth = MAX_DEPTH;

    for (int i = 0; i < MAX_DEPTH; ++i) {
      moveGenerators[i] = new MoveGenerator();
    }

    System.out.format("Testing %s at depth %d%n",
        Notation.fromPosition(position), depth);

    long startTime = currentTimeMillis();
    long result = miniMax(depth, position, 0);
    long endTime = currentTimeMillis();

    long duration = endTime - startTime;

    System.out.format(
        "Nodes: %d%nDuration: %02d:%02d:%02d.%03d%n",
        result,
        MILLISECONDS.toHours(duration),
        MILLISECONDS.toMinutes(duration)
            - HOURS.toMinutes(MILLISECONDS.toHours(duration)),
        MILLISECONDS.toSeconds(duration)
            - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)),
        duration - SECONDS.toMillis(MILLISECONDS.toSeconds(duration))
    );

    System.out.format("n/ms: %d%n", result / duration);
  }

  private long miniMax(int depth, Position position, int ply) {
    if (depth == 0) {
      return 1;
    }

    int totalNodes = 0;

    boolean isCheck = position.isCheck();
    MoveGenerator moveGenerator = moveGenerators[ply];
    MoveList<MoveEntry> moves = moveGenerator.getMoves(position, depth,
        isCheck);
    for (int i = 0; i < moves.size; ++i) {
      int move = moves.entries[i].move;

      position.makeMove(move);
      if (!position.isCheck(opposite(position.activeColor))) {
        totalNodes += miniMax(depth - 1, position, ply + 1);
      }
      position.undoMove(move);
    }

    return totalNodes;
  }
}

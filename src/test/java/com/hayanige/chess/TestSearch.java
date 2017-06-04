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

import static com.hayanige.chess.Move.NOMOVE;
import static com.hayanige.chess.Square.a6;
import static com.hayanige.chess.Square.a7;
import static com.hayanige.chess.Square.a8;
import static com.hayanige.chess.Square.b6;
import static com.hayanige.chess.Square.h7;
import static com.hayanige.chess.Square.h8;
import static com.hayanige.chess.Value.CHECKMATE;
import static com.hayanige.chess.Value.CHECKMATE_THRESHOLD;
import static com.hayanige.chess.Value.NOVALUE;
import static java.lang.Integer.signum;
import static java.lang.Math.abs;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;

import com.hayanige.chess.MoveList.RootEntry;
import java.util.concurrent.Semaphore;
import org.junit.Test;

public class TestSearch {

  @Test
  public void testMate() throws InterruptedException {
    final int[] currentBestMove = {NOMOVE};
    final int[] currentPonderMove = {NOMOVE};

    final Semaphore semaphore = new Semaphore(0);

    Search search = new Search(
        new Protocol() {
          @Override
          public void sendBestMove(int bestMove, int ponderMove) {
            currentBestMove[0] = bestMove;
            currentPonderMove[0] = ponderMove;

            semaphore.release();
          }

          @Override
          public void sendStatus(int currentDepth, int currentMaxDepth,
              long totalNodes, int currentMove, int currentMoveNumber) {
          }

          @Override
          public void sendStatus(boolean force, int currentDepth,
              int currentMaxDepth, long totalNodes, int currentMove,
              int currentMoveNumber) {
          }

          @Override
          public void sendMove(RootEntry entry, int currentDepth,
              int currentMaxDepth, long totalNodes) {
          }
        });
    search.newDepthSearch(Notation.toPosition("3K3r/8/3k4/8/8/8/8/8 w - - 0 1"),
        1);
    search.start();

    assertEquals(semaphore.tryAcquire(10000, MILLISECONDS), true);

    assertEquals(currentBestMove[0], NOMOVE);
    assertEquals(currentPonderMove[0], NOMOVE);
  }

  @Test
  public void testMate1() throws InterruptedException {
    final int[] currentBestMove = {NOMOVE};
    final int[] currentPonderMove = {NOMOVE};
    final int[] mate = {NOVALUE};

    final Semaphore semaphore = new Semaphore(0);

    Search search = new Search(
        new Protocol() {
          @Override
          public void sendBestMove(int bestMove, int ponderMove) {
            currentBestMove[0] = bestMove;
            currentPonderMove[0] = ponderMove;

            semaphore.release();
          }

          @Override
          public void sendStatus(int currentDepth, int currentMaxDepth,
              long totalNodes, int currentMove, int currentMoveNumber) {
          }

          @Override
          public void sendStatus(boolean force, int currentDepth,
              int currentMaxDepth, long totalNodes, int currentMove,
              int currentMoveNumber) {
          }

          @Override
          public void sendMove(RootEntry entry, int currentDepth,
              int currentMaxDepth, long totalNodes) {
            if (abs(entry.value) >= CHECKMATE_THRESHOLD) {
              // Calculate mate distance
              int mateDepth = CHECKMATE - abs(entry.value);
              mate[0] = signum(entry.value) * (mateDepth + 1) / 2;
            }
          }
        });
    search.newDepthSearch(
        Notation.toPosition("8/8/1R1P4/2B2p2/k1K2P2/4P3/8/8 w - - 3 101"), 2);
    search.start();

    assertEquals(semaphore.tryAcquire(10000, MILLISECONDS), true);

    assertEquals(Move.getOriginSquare(currentBestMove[0]), b6);
    assertEquals(Move.getTargetSquare(currentBestMove[0]), a6);
    assertEquals(currentPonderMove[0], NOMOVE);
    assertEquals(mate[0], 1);
  }

  @Test
  public void testStalemate() throws InterruptedException {
    final int[] currentBestMove = {NOMOVE};
    final int[] currentPonderMove = {NOMOVE};

    final Semaphore semaphore = new Semaphore(0);

    Search search = new Search(
        new Protocol() {
          @Override
          public void sendBestMove(int bestMove, int ponderMove) {
            currentBestMove[0] = bestMove;
            currentPonderMove[0] = ponderMove;

            semaphore.release();
          }

          @Override
          public void sendStatus(int currentDepth, int currentMaxDepth,
              long totalNodes, int currentMove, int currentMoveNumber) {
          }

          @Override
          public void sendStatus(boolean force, int currentDepth,
              int currentMaxDepth, long totalNodes, int currentMove,
              int currentMoveNumber) {
          }

          @Override
          public void sendMove(RootEntry entry, int currentDepth,
              int currentMaxDepth, long totalNodes) {
          }
        });
    search.newDepthSearch(Notation.toPosition("7k/5K2/6Q1/8/8/8/8/8 b - - 1 1"),
        1);
    search.start();

    assertEquals(semaphore.tryAcquire(10000, MILLISECONDS), true);

    assertEquals(currentBestMove[0], NOMOVE);
    assertEquals(currentPonderMove[0], NOMOVE);
  }

  @Test
  public void testMateStopCondition() throws InterruptedException {
    final int[] currentBestMove = {NOMOVE};

    final Semaphore semaphore = new Semaphore(0);

    Search search = new Search(
        new Protocol() {
          @Override
          public void sendBestMove(int bestMove, int ponderMove) {
            currentBestMove[0] = bestMove;

            semaphore.release();
          }

          @Override
          public void sendStatus(int currentDepth, int currentMaxDepth,
              long totalNodes, int currentMove, int currentMoveNumber) {
          }

          @Override
          public void sendStatus(boolean force, int currentDepth,
              int currentMaxDepth, long totalNodes, int currentMove,
              int currentMoveNumber) {
          }

          @Override
          public void sendMove(RootEntry entry, int currentDepth,
              int currentMaxDepth, long totalNodes) {
          }
        });
    search.newClockSearch(Notation.toPosition("3K4/7r/3k4/8/8/8/8/8 b - - 0 1"),
        10000, 0, 10000, 0, 40);
    search.start();

    assertEquals(semaphore.tryAcquire(10000, MILLISECONDS), true);

    assertEquals(Move.getOriginSquare(currentBestMove[0]), h7);
    assertEquals(Move.getTargetSquare(currentBestMove[0]), h8);
  }

  @Test
  public void testOneMoveStopCondition() throws InterruptedException {
    final int[] currentBestMove = {NOMOVE};

    final Semaphore semaphore = new Semaphore(0);

    Search search = new Search(
        new Protocol() {
          @Override
          public void sendBestMove(int bestMove, int ponderMove) {
            currentBestMove[0] = bestMove;

            semaphore.release();
          }

          @Override
          public void sendStatus(int currentDepth, int currentMaxDepth,
              long totalNodes, int currentMove, int currentMoveNumber) {
          }

          @Override
          public void sendStatus(boolean force, int currentDepth,
              int currentMaxDepth, long totalNodes, int currentMove,
              int currentMoveNumber) {
          }

          @Override
          public void sendMove(RootEntry entry, int currentDepth,
              int currentMaxDepth, long totalNodes) {
          }
        });
    search.newClockSearch(Notation.toPosition("K1k5/8/8/8/8/8/8/8 w - - 0 1"),
        10000, 0, 10000, 0, 40);
    search.start();

    assertEquals(semaphore.tryAcquire(10000, MILLISECONDS), true);

    assertEquals(Move.getOriginSquare(currentBestMove[0]), a8);
    assertEquals(Move.getTargetSquare(currentBestMove[0]), a7);
  }

}

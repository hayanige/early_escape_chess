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

import static com.hayanige.chess.Depth.MAX_PLY;
import static com.hayanige.chess.Move.NOMOVE;
import static com.hayanige.chess.PieceType.KING_VALUE;
import static com.hayanige.chess.Value.NOVALUE;

import java.lang.reflect.Array;

/**
 * This class stores our moves for a specific position. For the root node we
 * the root node we will populate pv for every root move.
 */
final class MoveList<T extends MoveList.MoveEntry> {

  private static final int MAX_MOVES = 256;

  final T[] entries;
  int size = 0;

  static final class MoveVariation {

    final int[] moves = new int[MAX_PLY];
    int size = 0;
  }

  static class MoveEntry {

    int move = NOMOVE;
    int value = NOVALUE;
  }

  static final class RootEntry extends MoveEntry {

    final MoveVariation pv = new MoveVariation();
  }

  MoveList(Class<T> clazz) {
    final T[] entries = (T[]) Array.newInstance(clazz, MAX_MOVES);
    this.entries = entries;
    try {
      for (int i = 0; i < entries.length; ++i) {
        entries[i] = clazz.newInstance();
      }
    } catch (InstantiationException e) {
      throw new IllegalStateException(e);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Sorts the move list using a stable insertion sort.
   */
  void sort() {
    for (int i = 1; i < size; ++i) {
      T entry = entries[i];

      int j = i;
      while ((j > 0) && (entries[j - 1].value < entry.value)) {
        entries[j] = entries[j - 1];
        --j;
      }

      entries[j] = entry;
    }
  }

  /**
   * Rates the moves in the list according to "Most Valuable Victim - Least
   * Valuable Aggressor".
   */
  void rateFromMVVLVA() {
    for (int i = 0; i < size; i++) {
      int move = entries[i].move;
      int value = 0;

      int pieceTypeValue = PieceType.getValue(Piece.getType(
          Move.getOriginPiece(move)));
      value += KING_VALUE / pieceTypeValue;

      int target = Move.getTargetPiece(move);
      if (Piece.isValid(target)) {
        value += 10 * PieceType.getValue(Piece.getType(target));
      }

      entries[i].value = value;
    }
  }
}

/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
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

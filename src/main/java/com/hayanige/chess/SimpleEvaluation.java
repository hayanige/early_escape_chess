/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Bitboard.next;
import static com.hayanige.chess.Bitboard.remainder;
import static com.hayanige.chess.Color.opposite;
import static com.hayanige.chess.Piece.NOPIECE;
import static com.hayanige.chess.PieceType.BISHOP;
import static com.hayanige.chess.PieceType.KNIGHT;
import static com.hayanige.chess.PieceType.QUEEN;
import static com.hayanige.chess.PieceType.ROOK;
import static com.hayanige.chess.PieceType.isSliding;
import static com.hayanige.chess.Square.bishopDirections;
import static com.hayanige.chess.Square.knightDirections;
import static com.hayanige.chess.Square.queenDirections;
import static com.hayanige.chess.Square.rookDirections;

import org.jetbrains.annotations.NotNull;

final class SimpleEvaluation extends Evaluation {

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

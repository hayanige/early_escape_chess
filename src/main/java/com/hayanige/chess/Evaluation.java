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

final class Evaluation {

  static final int TEMPO = 1;

  static int materialWeight = 100;
  static int mobilityWeight = 80;
  private static final int MAX_WEIGHT = 100;


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
        - evaluateMaterial(oppositeColor, position))
        * materialWeight / MAX_WEIGHT;
    value += materialScore;

    // Evaluate mobility
    int mobilityScore = (evaluateMobility(myColor, position)
        - evaluateMobility(oppositeColor, position))
        * mobilityWeight / MAX_WEIGHT;
    value += mobilityScore;

    // Add Tempo
    value += TEMPO;

    return value;
  }

  private int evaluateMaterial(int color, @NotNull Position position) {
    int material = position.material[color];

    // Add bonus for bishop pair
    if (position.pieces[color][BISHOP].size() >= 2) {
      material += 50;
    }

    return material;
  }

  private int evaluateMobility(int color, @NotNull Position position) {
    int knightMobility = 0;
    for (long squares = position.pieces[color][KNIGHT].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      knightMobility += evaluateMobility(color, position, square,
          knightDirections);
    }

    int bishopMobility = 0;
    for (long squares = position.pieces[color][BISHOP].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      bishopMobility += evaluateMobility(color, position, square,
          bishopDirections);
    }

    int rookMobility = 0;
    for (long squares = position.pieces[color][ROOK].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      rookMobility += evaluateMobility(color, position, square,
          rookDirections);
    }

    int queenMobility = 0;
    for (long squares = position.pieces[color][QUEEN].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      queenMobility += evaluateMobility(color, position, square,
          queenDirections);
    }

    return knightMobility * 4
        + bishopMobility * 5
        + rookMobility * 2
        + queenMobility;
  }

  private int evaluateMobility(int color, @NotNull Position position,
      int square, @NotNull int[] directions) {
    int mobility = 0;
    boolean sliding = isSliding(Piece.getType(position.board[square]));

    for (int direction : directions) {
      int targetSquare = square + direction;

      while (Square.isValid(targetSquare)) {
        ++mobility;

        if (sliding && position.board[targetSquare] == NOPIECE) {
          targetSquare += direction;
        } else {
          break;
        }
      }
    }

    return mobility;
  }
}

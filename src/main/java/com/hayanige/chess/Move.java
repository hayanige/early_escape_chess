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

import static com.hayanige.chess.MoveType.NOMOVETYPE;
import static com.hayanige.chess.Piece.NOPIECE;
import static com.hayanige.chess.PieceType.NOPIECETYPE;
import static com.hayanige.chess.Square.NOSQUARE;

/**
 * This class represents a move as a int value. The fields are represented by
 * the following bits.
 *
 * 0 -  2 : type (required)
 * 3 -  9 : origin square (required)
 * 10 - 16 : target square (required)
 * 17 - 21 : origin piece (reuqired)
 * 22 - 26 : target piece (optional)
 * 27 - 29 : promotion type (optional)
 */
final class Move {

  // These are our bit masks
  private static final int TYPE_SHIFT = 0;
  private static final int TYPE_MASK = MoveType.MASK << TYPE_SHIFT;
  private static final int ORIGIN_SQUARE_SHIFT = 3;
  private static final int ORIGIN_SQUARE_MASK =
      Square.MASK << ORIGIN_SQUARE_SHIFT;
  private static final int TARGET_SQUARE_SHIFT = 10;
  private static final int TARGET_SQUARE_MASK =
      Square.MASK << TARGET_SQUARE_SHIFT;
  private static final int ORIGIN_PIECE_SHIFT = 17;
  private static final int ORIGIN_PIECE_MASK = Piece.MASK << ORIGIN_PIECE_SHIFT;
  private static final int TARGET_PIECE_SHIFT = 22;
  private static final int TARGET_PIECE_MASK = Piece.MASK << TARGET_PIECE_SHIFT;
  private static final int PROMOTION_SHIFT = 27;
  private static final int PROMOTION_MASK = PieceType.MASK << PROMOTION_SHIFT;

  // We don't use 0 as a null value to protect against errors.
  static final int NOMOVE = (NOMOVETYPE << TYPE_SHIFT)
      | (NOSQUARE << ORIGIN_SQUARE_SHIFT)
      | (NOSQUARE << TARGET_SQUARE_SHIFT)
      | (NOPIECE << ORIGIN_PIECE_SHIFT)
      | (NOPIECE << TARGET_PIECE_SHIFT)
      | (NOPIECETYPE << PROMOTION_SHIFT);

  private Move() {
  }

  static int valueOf(int type, int originSquare, int targetSquare,
      int originPiece, int targetPiece, int promotion) {
    int move = 0;

    // Encode type
    move |= type << TYPE_SHIFT;

    // Encode origin square
    move |= originSquare << ORIGIN_SQUARE_SHIFT;

    // Encode target square
    move |= targetSquare << TARGET_SQUARE_SHIFT;

    // Encode origin piece
    move |= originPiece << ORIGIN_PIECE_SHIFT;

    // Encode target piece
    move |= targetPiece << TARGET_PIECE_SHIFT;

    // Encode promotion
    move |= promotion << PROMOTION_SHIFT;

    return move;
  }

  static int getType(int move) {
    return (move & TYPE_MASK) >>> TYPE_SHIFT;
  }

  static int getOriginSquare(int move) {
    return (move & ORIGIN_SQUARE_MASK) >>> ORIGIN_SQUARE_SHIFT;
  }

  static int getTargetSquare(int move) {
    return (move & TARGET_SQUARE_MASK) >>> TARGET_SQUARE_SHIFT;
  }

  static int getOriginPiece(int move) {
    return (move & ORIGIN_PIECE_MASK) >>> ORIGIN_PIECE_SHIFT;
  }

  static int getTargetPiece(int move) {
    return (move & TARGET_PIECE_MASK) >>> TARGET_PIECE_SHIFT;
  }

  static int getPromotion(int move) {
    return (move & PROMOTION_MASK) >>> PROMOTION_SHIFT;
  }
}

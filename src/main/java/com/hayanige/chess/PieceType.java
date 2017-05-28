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

final class PieceType {

  static final int MASK = 0x7;

  static final int PAWN = 0;
  static final int KNIGHT = 1;
  static final int BISHOP = 2;
  static final int ROOK = 3;
  static final int QUEEN = 4;
  static final int KING = 5;

  static final int NOPIECETYPE = 6;

  static final int[] values = {
      PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
  };

  // Piece values as defined by Larry Kaufman
  static final int PAWN_VALUE = 100;
  static final int KNIGHT_VALUE = 325;
  static final int BISHOP_VALUE = 325;
  static final int ROOK_VALUE = 500;
  static final int QUEEN_VALUE = 975;
  static final int KING_VALUE = 20000;

  private PieceType() {
  }

  static boolean isValid(int piecetype) {
    switch (piecetype) {
      case PAWN:
      case KNIGHT:
      case BISHOP:
      case ROOK:
      case QUEEN:
      case KING:
        return true;
      default:
        return false;
    }
  }

  static boolean isValidPromotion(int piecetype) {
    switch (piecetype) {
      case KNIGHT:
      case BISHOP:
      case ROOK:
      case QUEEN:
        return true;
      default:
        return false;
    }
  }

  static boolean isSliding(int piecetype) {
    switch (piecetype) {
      case BISHOP:
      case ROOK:
      case QUEEN:
        return true;
      case PAWN:
      case KNIGHT:
      case KING:
        return false;
      default:
        throw new IllegalArgumentException();
    }
  }

  static int getValue(int piecetype) {
    switch (piecetype) {
      case PAWN:
        return PAWN_VALUE;
      case KNIGHT:
        return KNIGHT_VALUE;
      case BISHOP:
        return BISHOP_VALUE;
      case ROOK:
        return ROOK_VALUE;
      case QUEEN:
        return QUEEN_VALUE;
      case KING:
        return KING_VALUE;
      default:
        throw new IllegalArgumentException();
    }
  }
}

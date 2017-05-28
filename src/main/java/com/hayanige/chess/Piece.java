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

import static com.hayanige.chess.Color.BLACK;
import static com.hayanige.chess.Color.WHITE;
import static com.hayanige.chess.PieceType.BISHOP;
import static com.hayanige.chess.PieceType.KING;
import static com.hayanige.chess.PieceType.KNIGHT;
import static com.hayanige.chess.PieceType.PAWN;
import static com.hayanige.chess.PieceType.QUEEN;
import static com.hayanige.chess.PieceType.ROOK;

final class Piece {

  static final int MASK = 0x1F;

  static final int WHITE_PAWN = 0;
  static final int WHITE_KNIGHT = 1;
  static final int WHITE_BISHOP = 2;
  static final int WHITE_ROOK = 3;
  static final int WHITE_QUEEN = 4;
  static final int WHITE_KING = 5;
  static final int BLACK_PAWN = 6;
  static final int BLACK_KNIGHT = 7;
  static final int BLACK_BISHOP = 8;
  static final int BLACK_ROOK = 9;
  static final int BLACK_QUEEN = 10;
  static final int BLACK_KING = 11;

  static final int NOPIECE = 12;

  static final int[] values = {
      WHITE_PAWN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN,
      WHITE_KING,
      BLACK_PAWN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN,
      BLACK_KING
  };

  private Piece() {
  }

  static boolean isValid(int piece) {
    switch (piece) {
      case WHITE_PAWN:
      case WHITE_KNIGHT:
      case WHITE_BISHOP:
      case WHITE_ROOK:
      case WHITE_QUEEN:
      case WHITE_KING:
      case BLACK_PAWN:
      case BLACK_KNIGHT:
      case BLACK_BISHOP:
      case BLACK_ROOK:
      case BLACK_QUEEN:
      case BLACK_KING:
        return true;
      default:
        return false;
    }
  }

  static int valueOf(int color, int piecetype) {
    switch (color) {
      case WHITE:
        switch (piecetype) {
          case PAWN:
            return WHITE_PAWN;
          case KNIGHT:
            return WHITE_KNIGHT;
          case BISHOP:
            return WHITE_BISHOP;
          case ROOK:
            return WHITE_ROOK;
          case QUEEN:
            return WHITE_QUEEN;
          case KING:
            return WHITE_KING;
          default:
            throw new IllegalArgumentException();
        }
      case BLACK:
        switch (piecetype) {
          case PAWN:
            return BLACK_PAWN;
          case KNIGHT:
            return BLACK_KNIGHT;
          case BISHOP:
            return BLACK_BISHOP;
          case ROOK:
            return BLACK_ROOK;
          case QUEEN:
            return BLACK_QUEEN;
          case KING:
            return BLACK_KING;
          default:
            throw new IllegalArgumentException();
        }
      default:
        throw new IllegalArgumentException();
    }
  }

  static int getType(int piece) {
    switch (piece) {
      case WHITE_PAWN:
      case BLACK_PAWN:
        return PAWN;
      case WHITE_KNIGHT:
      case BLACK_KNIGHT:
        return KNIGHT;
      case WHITE_BISHOP:
      case BLACK_BISHOP:
        return BISHOP;
      case WHITE_ROOK:
      case BLACK_ROOK:
        return ROOK;
      case WHITE_QUEEN:
      case BLACK_QUEEN:
        return QUEEN;
      case WHITE_KING:
      case BLACK_KING:
        return KING;
      default:
        throw new IllegalArgumentException();
    }
  }

  static int getColor(int piece) {
    switch (piece) {
      case WHITE_PAWN:
      case WHITE_KNIGHT:
      case WHITE_BISHOP:
      case WHITE_ROOK:
      case WHITE_QUEEN:
      case WHITE_KING:
        return WHITE;
      case BLACK_PAWN:
      case BLACK_KNIGHT:
      case BLACK_BISHOP:
      case BLACK_ROOK:
      case BLACK_QUEEN:
      case BLACK_KING:
        return BLACK;
      default:
        throw new IllegalArgumentException();
    }
  }
}

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

import static com.hayanige.chess.Castling.BLACK_KINGSIDE;
import static com.hayanige.chess.Castling.BLACK_QUEENSIDE;
import static com.hayanige.chess.Castling.NOCASTLING;
import static com.hayanige.chess.Castling.WHITE_KINGSIDE;
import static com.hayanige.chess.Castling.WHITE_QUEENSIDE;
import static com.hayanige.chess.Color.WHITE;
import static com.hayanige.chess.Depth.MAX_PLY;
import static com.hayanige.chess.Piece.NOPIECE;
import static com.hayanige.chess.Square.NOSQUARE;

import java.security.SecureRandom;

final class Position {

  private static final int MAX_MOVES = MAX_PLY + 1024;

  final int[] board = new int[Square.VALUES_LENGTH];

  final Bitboard[][] pieces =
      new Bitboard[Color.values.length][PieceType.values.length];

  final int[] material = new int[Color.values.length];

  int castlingRights = NOCASTLING;
  int enPassantSquare = NOSQUARE;
  int activeColor = WHITE;
  int halfmoveClock = 0;
  private int halfmoveNumber = 2;

  long zobristKey = 0;

  // We will save same position parameters in a State before making a move.
  // Later we will restore them before undoing a move.

  private final State[] states = new State[MAX_MOVES];
  private int statesSize = 0;

  private static final class Zobrist {

    private static final SecureRandom random = new SecureRandom();

    static final long[][] board = new long[Piece.values.length][Square.VALUES_LENGTH];
    static final long[] castlingRights = new long[Castling.VALUES_LENGTH];
    static final long[] enPassantSquare = new long[Square.VALUES_LENGTH];
    static final long activeColor = next();

    // Initialize the zobrist keys
    static {
      for (int piece : Piece.values) {
        for (int i = 0; i < Square.VALUES_LENGTH; ++i) {
          board[piece][i] = next();
        }
      }

      castlingRights[WHITE_KINGSIDE] = next();
      castlingRights[WHITE_QUEENSIDE] = next();
      castlingRights[BLACK_KINGSIDE] = next();
      castlingRights[BLACK_QUEENSIDE] = next();
      castlingRights[WHITE_KINGSIDE | WHITE_QUEENSIDE] =
          castlingRights[WHITE_KINGSIDE] ^ castlingRights[WHITE_QUEENSIDE];
      castlingRights[BLACK_KINGSIDE | BLACK_QUEENSIDE] =
          castlingRights[BLACK_KINGSIDE] ^ castlingRights[BLACK_QUEENSIDE];

      for (int i = 0; i < Square.VALUES_LENGTH; ++i) {
        enPassantSquare[i] = next();
      }
    }

    private static long next() {
      byte[] bytes = new byte[16];
      random.nextBytes(bytes);

      long hash = 0L;
      for (int i = 0; i < bytes.length; ++i) {
        hash ^= ((long) (bytes[i] & 0xFF)) << ((i * 8) % 64);
      }

      return hash;
    }
  }

  private static final class State {

    private long zobristKey = 0;
    private int castlingRights = NOCASTLING;
    private int enPassantSquare = NOSQUARE;
    private int halfmoveClock = 0;
  }

  Position() {
    // Initialize board
    for (int square : Square.values) {
      board[square] = NOPIECE;
    }

    // Initialize piece type lists
    for (int color : Color.values) {
      for (int pieceType : PieceType.values) {
        pieces[color][pieceType] = new Bitboard();
      }
    }

    // Initialize states
    for (int i = 0; i < states.length; ++i) {
      states[i] = new State();
    }
  }
}

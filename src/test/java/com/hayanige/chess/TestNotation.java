/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Castling.BLACK_KINGSIDE;
import static com.hayanige.chess.Castling.BLACK_QUEENSIDE;
import static com.hayanige.chess.Castling.NOCASTLING;
import static com.hayanige.chess.Castling.WHITE_KINGSIDE;
import static com.hayanige.chess.Castling.WHITE_QUEENSIDE;
import static com.hayanige.chess.Color.BLACK;
import static com.hayanige.chess.Color.WHITE;
import static com.hayanige.chess.Piece.BLACK_BISHOP;
import static com.hayanige.chess.Piece.BLACK_KING;
import static com.hayanige.chess.Piece.BLACK_KNIGHT;
import static com.hayanige.chess.Piece.BLACK_PAWN;
import static com.hayanige.chess.Piece.BLACK_QUEEN;
import static com.hayanige.chess.Piece.BLACK_ROOK;
import static com.hayanige.chess.Piece.WHITE_BISHOP;
import static com.hayanige.chess.Piece.WHITE_KING;
import static com.hayanige.chess.Piece.WHITE_KNIGHT;
import static com.hayanige.chess.Piece.WHITE_PAWN;
import static com.hayanige.chess.Piece.WHITE_QUEEN;
import static com.hayanige.chess.Piece.WHITE_ROOK;
import static com.hayanige.chess.PieceType.BISHOP_VALUE;
import static com.hayanige.chess.PieceType.KING_VALUE;
import static com.hayanige.chess.PieceType.KNIGHT_VALUE;
import static com.hayanige.chess.PieceType.PAWN_VALUE;
import static com.hayanige.chess.PieceType.QUEEN_VALUE;
import static com.hayanige.chess.PieceType.ROOK_VALUE;
import static com.hayanige.chess.Rank.r2;
import static com.hayanige.chess.Rank.r7;
import static com.hayanige.chess.Square.NOSQUARE;
import static com.hayanige.chess.Square.a1;
import static com.hayanige.chess.Square.a8;
import static com.hayanige.chess.Square.b1;
import static com.hayanige.chess.Square.b8;
import static com.hayanige.chess.Square.c1;
import static com.hayanige.chess.Square.c8;
import static com.hayanige.chess.Square.d1;
import static com.hayanige.chess.Square.d8;
import static com.hayanige.chess.Square.e1;
import static com.hayanige.chess.Square.e8;
import static com.hayanige.chess.Square.f1;
import static com.hayanige.chess.Square.f8;
import static com.hayanige.chess.Square.g1;
import static com.hayanige.chess.Square.g8;
import static com.hayanige.chess.Square.h1;
import static com.hayanige.chess.Square.h8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TestNotation {

  @Test
  public void testStandardPosition() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);

    // Test pawns
    for (int file : File.values) {
      assertEquals(position.board[Square.valueOf(file, r2)], WHITE_PAWN);
      assertEquals(position.board[Square.valueOf(file, r7)], BLACK_PAWN);
    }

    // Test knights
    assertEquals(position.board[b1], WHITE_KNIGHT);
    assertEquals(position.board[g1], WHITE_KNIGHT);
    assertEquals(position.board[b8], BLACK_KNIGHT);
    assertEquals(position.board[g8], BLACK_KNIGHT);

    // Test bishops
    assertEquals(position.board[c1], WHITE_BISHOP);
    assertEquals(position.board[f1], WHITE_BISHOP);
    assertEquals(position.board[c8], BLACK_BISHOP);
    assertEquals(position.board[f8], BLACK_BISHOP);

    // Test rooks
    assertEquals(position.board[a1], WHITE_ROOK);
    assertEquals(position.board[h1], WHITE_ROOK);
    assertEquals(position.board[a8], BLACK_ROOK);
    assertEquals(position.board[h8], BLACK_ROOK);

    // Test queens
    assertEquals(position.board[d1], WHITE_QUEEN);
    assertEquals(position.board[d8], BLACK_QUEEN);

    // Test kings
    assertEquals(position.board[e1], WHITE_KING);
    assertEquals(position.board[e8], BLACK_KING);

    assertEquals(position.material[WHITE], (8 * PAWN_VALUE)
        + (2 * KNIGHT_VALUE)
        + (2 * BISHOP_VALUE)
        + (2 * ROOK_VALUE)
        + QUEEN_VALUE
        + KING_VALUE);
    assertEquals(position.material[BLACK], (8 * PAWN_VALUE)
        + (2 * KNIGHT_VALUE)
        + (2 * BISHOP_VALUE)
        + (2 * ROOK_VALUE)
        + QUEEN_VALUE
        + KING_VALUE);

    // Test castling
    assertNotEquals(position.castlingRights & WHITE_KINGSIDE, NOCASTLING);
    assertNotEquals(position.castlingRights & WHITE_QUEENSIDE, NOCASTLING);
    assertNotEquals(position.castlingRights & BLACK_KINGSIDE, NOCASTLING);
    assertNotEquals(position.castlingRights & BLACK_QUEENSIDE, NOCASTLING);

    // Test en passant
    assertEquals(position.enPassantSquare, NOSQUARE);

    // Test active color
    assertEquals(position.activeColor, WHITE);

    // Test half move clock
    assertEquals(position.halfmoveClock, 0);

    // Test full move number
    assertEquals(position.getFullmoveNumber(), 1);
  }
}

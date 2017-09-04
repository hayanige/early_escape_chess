/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Castling.NOCASTLING;
import static com.hayanige.chess.Castling.WHITE_KINGSIDE;
import static com.hayanige.chess.Castling.WHITE_QUEENSIDE;
import static com.hayanige.chess.Color.BLACK;
import static com.hayanige.chess.Color.WHITE;
import static com.hayanige.chess.MoveType.CASTLING;
import static com.hayanige.chess.MoveType.ENPASSANT;
import static com.hayanige.chess.MoveType.NORMAL;
import static com.hayanige.chess.MoveType.PAWNDOUBLE;
import static com.hayanige.chess.MoveType.PAWNPROMOTION;
import static com.hayanige.chess.Piece.BLACK_KNIGHT;
import static com.hayanige.chess.Piece.BLACK_PAWN;
import static com.hayanige.chess.Piece.NOPIECE;
import static com.hayanige.chess.Piece.WHITE_KING;
import static com.hayanige.chess.Piece.WHITE_KNIGHT;
import static com.hayanige.chess.Piece.WHITE_PAWN;
import static com.hayanige.chess.Piece.WHITE_QUEEN;
import static com.hayanige.chess.PieceType.NOPIECETYPE;
import static com.hayanige.chess.PieceType.QUEEN;
import static com.hayanige.chess.Square.NOSQUARE;
import static com.hayanige.chess.Square.a2;
import static com.hayanige.chess.Square.a3;
import static com.hayanige.chess.Square.a4;
import static com.hayanige.chess.Square.a7;
import static com.hayanige.chess.Square.a8;
import static com.hayanige.chess.Square.b1;
import static com.hayanige.chess.Square.b6;
import static com.hayanige.chess.Square.b7;
import static com.hayanige.chess.Square.b8;
import static com.hayanige.chess.Square.c1;
import static com.hayanige.chess.Square.c3;
import static com.hayanige.chess.Square.c6;
import static com.hayanige.chess.Square.d3;
import static com.hayanige.chess.Square.d4;
import static com.hayanige.chess.Square.e1;
import static com.hayanige.chess.Square.e4;
import static com.hayanige.chess.Square.f3;
import static com.hayanige.chess.Square.g1;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestPosition {

  @Test
  public void testActiveColor() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);

    // Move white pawn
    int move = Move.valueOf(NORMAL, a2, a3, WHITE_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);
    assertEquals(position.activeColor, BLACK);

    // Move black pawn
    move = Move.valueOf(NORMAL, b7, b6, BLACK_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);
    assertEquals(position.activeColor, WHITE);
  }

  @Test
  public void testHalfMoveClock() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);

    // Move white pawn
    int move = Move.valueOf(NORMAL, a2, a3, WHITE_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);
    assertEquals(position.halfmoveClock, 0);

    // Move black pawn
    move = Move.valueOf(NORMAL, b7, b6, BLACK_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    // Move white knight
    move = Move.valueOf(NORMAL, b1, c3, WHITE_KNIGHT, NOPIECE, NOPIECETYPE);
    position.makeMove(move);
    assertEquals(position.halfmoveClock, 1);
  }

  @Test
  public void testFullMoveNumber() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);

    // Move white pawn
    int move = Move.valueOf(NORMAL, a2, a3, WHITE_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);
    assertEquals(position.getFullmoveNumber(), 1);

    // Move black pawn
    move = Move.valueOf(NORMAL, b7, b6, BLACK_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);
    assertEquals(position.getFullmoveNumber(), 2);
  }

  @Test
  public void testIsRepetition() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);

    // Move white knight
    int move = Move.valueOf(NORMAL, b1, c3, WHITE_KNIGHT, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    // Move black knight
    move = Move.valueOf(NORMAL, b8, c6, BLACK_KNIGHT, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    // Move white knight
    move = Move.valueOf(NORMAL, g1, f3, WHITE_KNIGHT, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    // Move black knight
    move = Move.valueOf(NORMAL, c6, b8, BLACK_KNIGHT, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    // Move white knight
    move = Move.valueOf(NORMAL, f3, g1, WHITE_KNIGHT, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    assertEquals(position.isRepetition(), true);
  }

  @Test
  public void testHasInsufficientMaterial() {
    Position position = Notation.toPosition("8/4k3/8/8/8/8/2K5/8 w - - 0 1");
    assertEquals(position.hasInsufficientMaterial(), true);

    position = Notation.toPosition("8/4k3/8/2B5/8/8/2K5/8 b - - 0 1");
    assertEquals(position.hasInsufficientMaterial(), true);

    position = Notation.toPosition("8/4k3/8/2B3n1/8/8/2K5/8 b - - 0 1");
    assertEquals(position.hasInsufficientMaterial(), true);
  }

  @Test
  public void testNormalMove() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);
    long zobristKey = position.zobristKey;

    int move = Move.valueOf(NORMAL, a2, a3, WHITE_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);
    position.undoMove(move);

    assertEquals(Notation.fromPosition(position), Notation.STANDARDPOSITION);
    assertEquals(position.zobristKey, zobristKey);
  }

  @Test
  public void testPawnDoubleMove() {
    Position position = Notation.toPosition(Notation.STANDARDPOSITION);
    long zobristKey = position.zobristKey;

    int move = Move
        .valueOf(PAWNDOUBLE, a2, a4, WHITE_PAWN, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    assertEquals(position.enPassantSquare, a3);

    position.undoMove(move);

    assertEquals(Notation.fromPosition(position), Notation.STANDARDPOSITION);
    assertEquals(position.zobristKey, zobristKey);
  }

  @Test
  public void testPawnPromotionMove() {
    Position position = Notation.toPosition("8/P5k1/8/8/2K5/8/8/8 w - - 0 1");
    long zobristKey = position.zobristKey;

    int move = Move.valueOf(PAWNPROMOTION, a7, a8, WHITE_PAWN, NOPIECE, QUEEN);
    position.makeMove(move);

    assertEquals(position.board[a8], WHITE_QUEEN);

    position.undoMove(move);

    assertEquals(Notation.fromPosition(position),
        "8/P5k1/8/8/2K5/8/8/8 w - - 0 1");
    assertEquals(position.zobristKey, zobristKey);
  }

  @Test
  public void testEnPassantMove() {
    Position position = Notation
        .toPosition("5k2/8/8/8/3Pp3/8/8/3K4 b - d3 0 1");
    long zobristKey = position.zobristKey;

    // Make en passant move
    int move = Move
        .valueOf(ENPASSANT, e4, d3, BLACK_PAWN, WHITE_PAWN, NOPIECETYPE);
    position.makeMove(move);

    assertEquals(position.board[d4], NOPIECE);
    assertEquals(position.board[d3], BLACK_PAWN);
    assertEquals(position.enPassantSquare, NOSQUARE);

    position.undoMove(move);

    assertEquals(Notation.fromPosition(position),
        "5k2/8/8/8/3Pp3/8/8/3K4 b - d3 0 1");
    assertEquals(position.zobristKey, zobristKey);
  }

  @Test
  public void testCastlingMove() {
    Position position = Notation
        .toPosition("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    long zobristKey = position.zobristKey;

    int move = Move.valueOf(CASTLING, e1, c1, WHITE_KING, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    assertEquals(position.castlingRights & WHITE_QUEENSIDE, NOCASTLING);

    position.undoMove(move);

    assertEquals(Notation.fromPosition(position),
        "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    assertEquals(position.zobristKey, zobristKey);

    position = Notation.toPosition("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    zobristKey = position.zobristKey;

    move = Move.valueOf(CASTLING, e1, g1, WHITE_KING, NOPIECE, NOPIECETYPE);
    position.makeMove(move);

    assertEquals(position.castlingRights & WHITE_KINGSIDE, NOCASTLING);

    position.undoMove(move);

    assertEquals(Notation.fromPosition(position),
        "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    assertEquals(position.zobristKey, zobristKey);
  }
}

/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.MoveType.PAWNPROMOTION;
import static com.hayanige.chess.Piece.BLACK_QUEEN;
import static com.hayanige.chess.Piece.WHITE_PAWN;
import static com.hayanige.chess.PieceType.KNIGHT;
import static com.hayanige.chess.Square.a7;
import static com.hayanige.chess.Square.b8;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMove {

  @Test
  public void testCreation() {
    int move = Move.valueOf(PAWNPROMOTION, a7, b8, WHITE_PAWN, BLACK_QUEEN,
        KNIGHT);

    assertEquals(Move.getType(move), PAWNPROMOTION);
    assertEquals(Move.getOriginSquare(move), a7);
    assertEquals(Move.getTargetSquare(move), b8);
    assertEquals(Move.getOriginPiece(move), WHITE_PAWN);
    assertEquals(Move.getTargetPiece(move), BLACK_QUEEN);
    assertEquals(Move.getPromotion(move), KNIGHT);
  }
}

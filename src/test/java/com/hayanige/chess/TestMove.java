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

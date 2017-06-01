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
import static com.hayanige.chess.Castling.BLACK_KINGSIDE;
import static com.hayanige.chess.Castling.BLACK_QUEENSIDE;
import static com.hayanige.chess.Castling.NOCASTLING;
import static com.hayanige.chess.Castling.WHITE_KINGSIDE;
import static com.hayanige.chess.Castling.WHITE_QUEENSIDE;
import static com.hayanige.chess.Color.BLACK;
import static com.hayanige.chess.Color.WHITE;
import static com.hayanige.chess.Color.opposite;
import static com.hayanige.chess.MoveType.CASTLING;
import static com.hayanige.chess.MoveType.ENPASSANT;
import static com.hayanige.chess.MoveType.NORMAL;
import static com.hayanige.chess.MoveType.PAWNDOUBLE;
import static com.hayanige.chess.MoveType.PAWNPROMOTION;
import static com.hayanige.chess.Piece.NOPIECE;
import static com.hayanige.chess.PieceType.BISHOP;
import static com.hayanige.chess.PieceType.KING;
import static com.hayanige.chess.PieceType.KNIGHT;
import static com.hayanige.chess.PieceType.NOPIECETYPE;
import static com.hayanige.chess.PieceType.PAWN;
import static com.hayanige.chess.PieceType.QUEEN;
import static com.hayanige.chess.PieceType.ROOK;
import static com.hayanige.chess.PieceType.isSliding;
import static com.hayanige.chess.Rank.r1;
import static com.hayanige.chess.Rank.r4;
import static com.hayanige.chess.Rank.r5;
import static com.hayanige.chess.Rank.r8;
import static com.hayanige.chess.Square.N;
import static com.hayanige.chess.Square.S;
import static com.hayanige.chess.Square.b1;
import static com.hayanige.chess.Square.b8;
import static com.hayanige.chess.Square.bishopDirections;
import static com.hayanige.chess.Square.c1;
import static com.hayanige.chess.Square.c8;
import static com.hayanige.chess.Square.d1;
import static com.hayanige.chess.Square.d8;
import static com.hayanige.chess.Square.f1;
import static com.hayanige.chess.Square.f8;
import static com.hayanige.chess.Square.g1;
import static com.hayanige.chess.Square.g8;
import static com.hayanige.chess.Square.kingDirections;
import static com.hayanige.chess.Square.knightDirections;
import static com.hayanige.chess.Square.pawnDirections;
import static com.hayanige.chess.Square.queenDirections;
import static com.hayanige.chess.Square.rookDirections;

import com.hayanige.chess.MoveList.MoveEntry;
import com.sun.istack.internal.NotNull;

final class MoveGenerator {

  private final MoveList<MoveEntry> moves
      = new MoveList<MoveEntry>(MoveEntry.class);

  MoveList<MoveEntry> getLegalMoves(@NotNull Position position, int depth,
      boolean isCheck) {

    MoveList<MoveEntry> legalMoves = getMoves(position, depth, isCheck);

    int size = legalMoves.size;
    legalMoves.size = 0;
    for (int i = 0; i < size; ++i) {
      int move = legalMoves.entries[i].move;
      position.makeMove(move);
      if (!position.isCheck(opposite(position.activeColor))) {
        legalMoves.entries[legalMoves.size++].move = move;
      }
      position.undoMove(move);
    }

    return legalMoves;
  }

  MoveList<MoveEntry> getMoves(@NotNull Position position, int depth,
      boolean isCheck) {
    moves.size = 0;

    if (depth > 0) {
      // Generate main moves

      addMoves(moves, position);

      if (!isCheck) {
        int square = next(position.pieces[position.activeColor][KING].squares);
        addCastlingMoves(moves, square, position);
      }
    } else {
      // Generate quiescent moves

      addMoves(moves, position);

      if (!isCheck) {
        int size = moves.size;
        moves.size = 0;
        for (int i = 0; i < size; ++i) {
          if (Move.getTargetPiece(moves.entries[i].move) != NOPIECE) {
            // Add only capturing moves
            moves.entries[moves.size++].move = moves.entries[i].move;
          }
        }
      }
    }

    moves.rateFromMVVLVA();
    moves.sort();

    return moves;
  }

  private void addMoves(@NotNull MoveList<MoveEntry> list,
      @NotNull Position position) {
    int activeColor = position.activeColor;

    for (long squares = position.pieces[activeColor][PAWN].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      addPawnMoves(list, square, position);
    }

    for (long squares = position.pieces[activeColor][KNIGHT].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      addMoves(list, square, knightDirections, position);
    }

    for (long squares = position.pieces[activeColor][BISHOP].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      addMoves(list, square, bishopDirections, position);
    }

    for (long squares = position.pieces[activeColor][ROOK].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      addMoves(list, square, rookDirections, position);
    }

    for (long squares = position.pieces[activeColor][QUEEN].squares;
        squares != 0; squares = remainder(squares)) {
      int square = next(squares);
      addMoves(list, square, queenDirections, position);
    }

    int square = next(position.pieces[activeColor][KING].squares);
    addMoves(list, square, kingDirections, position);
  }

  private void addMoves(@NotNull MoveList<MoveEntry> list, int originSquare,
      @NotNull int[] directions, @NotNull Position position) {
    int originPiece = position.board[originSquare];
    boolean sliding = isSliding(Piece.getType(originPiece));
    int oppositeColor = opposite(Piece.getColor(originPiece));

    // Go through all move directions for this piece
    for (int direction : directions) {
      int targetSquare = originSquare + direction;

      // Check if we're still on the board
      while (Square.isValid(targetSquare)) {
        int targetPiece = position.board[targetSquare];

        if (targetPiece == NOPIECE) {
          // quiet move
          list.entries[list.size++].move = Move.valueOf(NORMAL, originSquare,
              targetSquare, originPiece, NOPIECE, NOPIECETYPE);

          if (!sliding) {
            break;
          }
          targetSquare += direction;
        } else {
          if (Piece.getColor(targetPiece) == oppositeColor) {
            // capturing move
            list.entries[list.size++].move = Move.valueOf(NORMAL, originSquare,
                targetSquare, originPiece, targetPiece, NOPIECETYPE);
          }

          break;
        }
      }
    }
  }

  private void addPawnMoves(@NotNull MoveList<MoveEntry> list, int pawnSquare,
      @NotNull Position position) {
    int pawnPiece = position.board[pawnSquare];
    int pawnColor = Piece.getColor(pawnPiece);

    // Generate only capturing moves first (i = 1)
    for (int i = 1; i < pawnDirections[pawnColor].length; ++i) {
      int direction = pawnDirections[pawnColor][i];

      int targetSquare = pawnSquare + direction;
      if (Square.isValid(targetSquare)) {
        int targetPiece = position.board[targetSquare];

        if (targetPiece != NOPIECE) {
          if (Piece.getColor(targetPiece) == opposite(pawnColor)) {
            // Capturing move

            if ((pawnColor == WHITE && Square.getRank(targetSquare) == r8)
                || (pawnColor == BLACK && Square.getRank(targetSquare) == r1)) {
              // Pawn promotion capturing move
              list.entries[list.size++].move = Move.valueOf(PAWNPROMOTION,
                  pawnSquare, targetSquare, pawnPiece, targetPiece, QUEEN);
              list.entries[list.size++].move = Move.valueOf(PAWNPROMOTION,
                  pawnSquare, targetSquare, pawnPiece, targetPiece, ROOK);
              list.entries[list.size++].move = Move.valueOf(PAWNPROMOTION,
                  pawnSquare, targetSquare, pawnPiece, targetPiece, BISHOP);
              list.entries[list.size++].move = Move.valueOf(PAWNPROMOTION,
                  pawnSquare, targetSquare, pawnPiece, targetPiece, KNIGHT);
            } else {
              // Normal capturing move

              list.entries[list.size++].move = Move.valueOf(NORMAL, pawnSquare,
                  targetSquare, pawnPiece, targetPiece, NOPIECETYPE);
            }
          }
        } else if (targetSquare == position.enPassantSquare) {
          // En passant move
          int captureSquare = targetSquare + (pawnColor == WHITE ? S : N);
          targetPiece = position.board[captureSquare];

          list.entries[list.size++].move = Move.valueOf(ENPASSANT, pawnSquare,
              targetSquare, pawnPiece, targetPiece, NOPIECETYPE);
        }
      }
    }

    // Generate non-capturing moves
    int direction = pawnDirections[pawnColor][0];

    // Move one rank forward
    int targetSquare = pawnSquare + direction;
    if (Square.isValid(targetSquare)
        && position.board[targetSquare] == NOPIECE) {
      if ((pawnColor == WHITE && Square.getRank(targetSquare) == r8)
          || (pawnColor == BLACK && Square.getRank(targetSquare) == r1)) {
        // Pawn promotion move

        list.entries[list.size++].move = Move.valueOf(
            PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, NOPIECE, QUEEN);
        list.entries[list.size++].move = Move.valueOf(
            PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, NOPIECE, ROOK);
        list.entries[list.size++].move = Move.valueOf(
            PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, NOPIECE,
            BISHOP);
        list.entries[list.size++].move = Move.valueOf(
            PAWNPROMOTION, pawnSquare, targetSquare, pawnPiece, NOPIECE,
            KNIGHT);
      } else {
        // Nomal move

        list.entries[list.size++].move = Move.valueOf(
            NORMAL, pawnSquare, targetSquare, pawnPiece, NOPIECE, NOPIECETYPE);

        // Move another rank forward
        targetSquare += direction;
        if (Square.isValid(targetSquare)
            && position.board[targetSquare] == NOPIECE) {
          if ((pawnColor == WHITE && Square.getRank(targetSquare) == r4)
              || (pawnColor == BLACK && Square.getRank(targetSquare) == r5)) {
            // Pawn double move

            list.entries[list.size++].move = Move.valueOf(PAWNDOUBLE,
                pawnSquare, targetSquare, pawnPiece, NOPIECE, NOPIECETYPE);
          }
        }
      }
    }
  }

  private void addCastlingMoves(@NotNull MoveList<MoveEntry> list,
      int kingSquare, @NotNull Position position) {
    int kingPiece = position.board[kingSquare];

    if (Piece.getColor(kingPiece) == WHITE) {
      // Do not test g1 whether it is attacked as we will test it in isLegal()
      if ((position.castlingRights & WHITE_KINGSIDE) != NOCASTLING
          && position.board[f1] == NOPIECE
          && position.board[g1] == NOPIECE
          && !position.isAttacked(f1, BLACK)) {
        list.entries[list.size++].move = Move.valueOf(CASTLING, kingSquare, g1,
            kingPiece, NOPIECE, NOPIECETYPE);
      }
      // Do not test c1 whether it is attacked as we will test it in isLegal()
      if ((position.castlingRights & WHITE_QUEENSIDE) != NOCASTLING
          && position.board[b1] == NOPIECE
          && position.board[c1] == NOPIECE
          && position.board[d1] == NOPIECE
          && !position.isAttacked(d1, BLACK)) {
        list.entries[list.size++].move = Move.valueOf(CASTLING, kingSquare, c1,
            kingPiece, NOPIECE, NOPIECETYPE);
      }
    } else {
      // Do not test g8 whether it is attacked as we will test it in isLegal()
      if ((position.castlingRights & BLACK_KINGSIDE) != NOCASTLING
          && position.board[f8] == NOPIECE
          && position.board[g8] == NOPIECE
          && !position.isAttacked(f8, WHITE)) {
        list.entries[list.size++].move = Move.valueOf(CASTLING, kingSquare, g8,
            kingPiece, NOPIECE, NOPIECETYPE);
      }
      // Do not test c8 whether it is attacked as we will test it in isLegal()
      if ((position.castlingRights & BLACK_QUEENSIDE) != NOCASTLING
          && position.board[b8] == NOPIECE
          && position.board[c8] == NOPIECE
          && position.board[d8] == NOPIECE
          && !position.isAttacked(d8, WHITE)) {
        list.entries[list.size++].move = Move.valueOf(CASTLING, kingSquare, c8,
            kingPiece, NOPIECE, NOPIECETYPE);
      }
    }
  }
}

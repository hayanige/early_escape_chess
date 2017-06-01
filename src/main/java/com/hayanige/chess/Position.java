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
import static com.hayanige.chess.Castling.BLACK_KINGSIDE;
import static com.hayanige.chess.Castling.BLACK_QUEENSIDE;
import static com.hayanige.chess.Castling.NOCASTLING;
import static com.hayanige.chess.Castling.WHITE_KINGSIDE;
import static com.hayanige.chess.Castling.WHITE_QUEENSIDE;
import static com.hayanige.chess.Color.BLACK;
import static com.hayanige.chess.Color.WHITE;
import static com.hayanige.chess.Color.opposite;
import static com.hayanige.chess.Depth.MAX_PLY;
import static com.hayanige.chess.MoveType.CASTLING;
import static com.hayanige.chess.MoveType.ENPASSANT;
import static com.hayanige.chess.MoveType.PAWNDOUBLE;
import static com.hayanige.chess.MoveType.PAWNPROMOTION;
import static com.hayanige.chess.Piece.NOPIECE;
import static com.hayanige.chess.PieceType.BISHOP;
import static com.hayanige.chess.PieceType.KING;
import static com.hayanige.chess.PieceType.KNIGHT;
import static com.hayanige.chess.PieceType.PAWN;
import static com.hayanige.chess.PieceType.QUEEN;
import static com.hayanige.chess.PieceType.ROOK;
import static com.hayanige.chess.Square.N;
import static com.hayanige.chess.Square.NOSQUARE;
import static com.hayanige.chess.Square.S;
import static com.hayanige.chess.Square.a1;
import static com.hayanige.chess.Square.a8;
import static com.hayanige.chess.Square.bishopDirections;
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
import static com.hayanige.chess.Square.kingDirections;
import static com.hayanige.chess.Square.knightDirections;
import static com.hayanige.chess.Square.pawnDirections;
import static com.hayanige.chess.Square.rookDirections;
import static java.lang.Math.max;

import com.sun.istack.internal.NotNull;
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

  void setActiveColor(int activeColor) {
    if (this.activeColor != activeColor) {
      this.activeColor = activeColor;
      zobristKey ^= Zobrist.activeColor;
    }
  }

  void setCastlingRight(int castling) {
    if ((castlingRights & castling) == NOCASTLING) {
      castlingRights |= castling;
      zobristKey ^= Zobrist.castlingRights[castling];
    }
  }

  void setEnPassantSquare(int enPassantSquare) {
    if (this.enPassantSquare != NOSQUARE) {
      zobristKey ^= Zobrist.enPassantSquare[this.enPassantSquare];
    }
    if (enPassantSquare != NOSQUARE) {
      zobristKey ^= Zobrist.enPassantSquare[enPassantSquare];
    }
    this.enPassantSquare = enPassantSquare;
  }

  void setHalfmoveClock(int halfmoveClock) {
    this.halfmoveClock = halfmoveClock;
  }

  int getFullmovenumber() {
    return halfmoveNumber / 2;
  }

  void setFullmoveNumber(int fullmoveNumber) {
    halfmoveNumber = fullmoveNumber * 2;
    if (activeColor == BLACK) {
      ++halfmoveNumber;
    }
  }

  boolean isRepetition() {
    // Search back until the last halfmoveClock reset
    int j = max(0, statesSize - halfmoveClock);
    for (int i = statesSize - 2; i >= j; i -= 2) {
      if (zobristKey == states[i].zobristKey) {
        return true;
      }
    }

    return false;
  }

  boolean hasInsufficientMaterial() {
    // If there is only one minor left, we are unable to checkmate
    return pieces[WHITE][PAWN].size() == 0 && pieces[BLACK][PAWN].size() == 0
        && pieces[WHITE][ROOK].size() == 0 && pieces[BLACK][ROOK].size() == 0
        && pieces[WHITE][QUEEN].size() == 0 && pieces[BLACK][QUEEN].size() == 0
        && (pieces[WHITE][KNIGHT].size() + pieces[WHITE][BISHOP].size() <= 1)
        && (pieces[BLACK][KNIGHT].size() + pieces[BLACK][BISHOP].size() <= 1);
  }

  /**
   * Puts a piece at the square. We need to update our board and the
   * appropriate.
   *
   * @param piece the Piece.
   * @param square the Square.
   */
  void put(int piece, int square) {
    int pieceType = Piece.getType(piece);
    int color = Piece.getColor(piece);

    board[square] = piece;
    pieces[color][pieceType].add(square);
    material[color] += PieceType.getValue(pieceType);
    zobristKey ^= Zobrist.board[piece][square];
  }

  /**
   * Removes a piece from the square. We need to update our board and the
   * appropriate piece type list.
   */
  int remove(int square) {
    int piece = board[square];

    int pieceType = Piece.getType(piece);
    int color = Piece.getColor(piece);

    board[square] = NOPIECE;
    pieces[color][pieceType].remove(square);
    material[color] -= PieceType.getValue(pieceType);

    zobristKey ^= Zobrist.board[piece][square];

    return piece;
  }

  void makeMove(int move) {
    // Save state
    State entry = states[statesSize];
    entry.zobristKey = zobristKey;
    entry.castlingRights = castlingRights;
    entry.enPassantSquare = enPassantSquare;
    entry.halfmoveClock = halfmoveClock;

    ++statesSize;

    // Get variables
    int type = Move.getType(move);
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int originPiece = Move.getOriginPiece(move);
    int originColor = Piece.getColor(originPiece);
    int targetPiece = Move.getTargetPiece(move);

    // Remove target piece and update castling rights
    if (targetPiece != NOPIECE) {
      int captureSquare = targetSquare;
      if (type == ENPASSANT) {
        captureSquare += (originColor == WHITE ? S : N);
      }
      remove(captureSquare);

      clearCastling(captureSquare);
    }

    // Move piece
    remove(originSquare);
    if (type == PAWNPROMOTION) {
      put(Piece.valueOf(originColor, Move.getPromotion(move)), targetSquare);
    } else {
      put(originPiece, targetSquare);
    }

    // Move rook and update castling rights
    if (type == CASTLING) {
      int rookOriginSquare;
      int rookTargetSquare;
      switch (targetSquare) {
        case g1:
          rookOriginSquare = h1;
          rookTargetSquare = f1;
          break;
        case c1:
          rookOriginSquare = a1;
          rookTargetSquare = d1;
          break;
        case g8:
          rookOriginSquare = h8;
          rookTargetSquare = f8;
          break;
        case c8:
          rookOriginSquare = a8;
          rookTargetSquare = d8;
          break;
        default:
          throw new IllegalArgumentException();
      }

      int rookPiece = remove(rookOriginSquare);
      put(rookPiece, rookTargetSquare);
    }

    // Update castling
    clearCastling(originSquare);

    // Update enPassantSquare
    if (enPassantSquare != NOSQUARE) {
      zobristKey ^= Zobrist.enPassantSquare[enPassantSquare];
    }
    if (type == PAWNDOUBLE) {
      enPassantSquare = targetSquare + (originColor == WHITE ? S : N);
      zobristKey ^= Zobrist.enPassantSquare[enPassantSquare];
    } else {
      enPassantSquare = NOSQUARE;
    }

    // Update activeColor
    activeColor = opposite(activeColor);
    zobristKey ^= Zobrist.activeColor;

    // Update halfmoveClock
    if (Piece.getType(originPiece) == PAWN || targetPiece != NOPIECE) {
      halfmoveClock = 0;
    } else {
      ++halfmoveClock;
    }

    // Update fullMoveNumber
    ++halfmoveNumber;
  }

  void undoMove(int move) {
    // Get variables
    int type = Move.getType(move);
    int originSquare = Move.getOriginSquare(move);
    int targetSquare = Move.getTargetSquare(move);
    int originPiece = Move.getOriginPiece(move);
    int originColor = Piece.getColor(originPiece);
    int targetPiece = Move.getTargetPiece(move);

    // Update fullMoveNumber
    --halfmoveNumber;

    // Update activeColor
    activeColor = opposite(activeColor);

    // Undo move rook
    if (type == CASTLING) {
      int rookOriginSquare;
      int rookTargetSquare;
      switch (targetSquare) {
        case g1:
          rookOriginSquare = h1;
          rookTargetSquare = f1;
          break;
        case c1:
          rookOriginSquare = a1;
          rookTargetSquare = d1;
          break;
        case g8:
          rookOriginSquare = h8;
          rookTargetSquare = f8;
          break;
        case c8:
          rookOriginSquare = a8;
          rookTargetSquare = d8;
          break;
        default:
          throw new IllegalArgumentException();
      }

      int rookPiece = remove(rookTargetSquare);
      put(rookPiece, rookOriginSquare);
    }

    // Undo move piece
    remove(targetSquare);
    put(originPiece, originSquare);

    // Restore target piece
    if (targetPiece != NOPIECE) {
      int captureSquare = targetSquare;
      if (type == ENPASSANT) {
        captureSquare += (originColor == WHITE ? S : N);
      }
      put(targetPiece, captureSquare);
    }

    // Restore state
    --statesSize;

    State entry = states[statesSize];
    halfmoveClock = entry.halfmoveClock;
    enPassantSquare = entry.enPassantSquare;
    castlingRights = entry.castlingRights;
    zobristKey = entry.zobristKey;
  }

  private void clearCastling(int square) {
    int newCastlingRights = castlingRights;

    switch (square) {
      case a1:
        newCastlingRights &= ~WHITE_QUEENSIDE;
        break;
      case a8:
        newCastlingRights &= ~BLACK_QUEENSIDE;
        break;
      case h1:
        newCastlingRights &= ~WHITE_KINGSIDE;
        break;
      case h8:
        newCastlingRights &= ~BLACK_KINGSIDE;
        break;
      case e1:
        newCastlingRights &= ~(WHITE_KINGSIDE | WHITE_QUEENSIDE);
        break;
      case e8:
        newCastlingRights &= ~(BLACK_KINGSIDE | BLACK_QUEENSIDE);
        break;
      default:
        return;
    }

    if (newCastlingRights != castlingRights) {
      castlingRights = newCastlingRights;
      zobristKey ^= Zobrist.castlingRights[newCastlingRights ^ castlingRights];
    }
  }

  boolean isCheck() {
    // Check whether our king is attacked by any opponet piece
    return isAttacked(next(pieces[activeColor][KING].squares),
        opposite(activeColor));
  }

  boolean isCheck(int color) {
    // Check whether the king for color is attacked by any opponet piece
    return isAttacked(next(pieces[color][KING].squares), opposite(color));
  }

  /**
   * Returns whether the targetSquare is attacked by any piece from the
   * attackerColor. We will backtrack from the targetSquare to find the piece.
   *
   * @param targetSquare the target Square.
   * @param attackerColor the attacker Color.
   * @return whether the targetSquare is attacked.
   */
  boolean isAttacked(int targetSquare, int attackerColor) {
    // Pawn attacks
    int pawnPiece = Piece.valueOf(attackerColor, PAWN);
    for (int i = 1; i < pawnDirections[attackerColor].length; ++i) {
      int attackerSquare = targetSquare - pawnDirections[attackerColor][i];
      if (Square.isValid(attackerSquare)) {
        int attackerPawn = board[attackerSquare];
        if (attackerPawn == pawnPiece) {
          return true;
        }
      }
    }

    return isAttacked(targetSquare, Piece.valueOf(attackerColor, KNIGHT),
        knightDirections)

        // The queen moves like a bishop, so check both piece types
        || isAttacked(targetSquare,
        Piece.valueOf(attackerColor, BISHOP),
        Piece.valueOf(attackerColor, QUEEN),
        bishopDirections)

        // The queen moves like a rook, so check both piece types
        || isAttacked(targetSquare,
        Piece.valueOf(attackerColor, ROOK),
        Piece.valueOf(attackerColor, QUEEN),
        rookDirections)

        || isAttacked(targetSquare,
        Piece.valueOf(attackerColor, KING),
        kingDirections);
  }

  /**
   * Returns whether the targetSquare is attacked by a non-sliding piece.
   */
  private boolean isAttacked(int targetSquare, int attackerPiece,
      @NotNull int[] directions) {
    for (int direction : directions) {
      int attackerSquare = targetSquare + direction;

      if (Square.isValid(attackerSquare) && board[attackerSquare]
          == attackerPiece) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns whether the targetSquare is attacked by a sliding piece.
   */
  private boolean isAttacked(int targetSquare, int attackerPiece,
      int queenPiece, @NotNull int[] directions) {
    for (int direction : directions) {
      int attackerSquare = targetSquare + direction;
      while (Square.isValid(attackerSquare)) {
        int piece = board[attackerSquare];

        if (Piece.isValid(piece)) {
          if (piece == attackerPiece || piece == queenPiece) {
            return true;
          }

          break;
        } else {
          attackerSquare += direction;
        }
      }
    }

    return false;
  }
}

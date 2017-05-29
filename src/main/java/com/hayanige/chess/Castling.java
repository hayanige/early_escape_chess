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

import static com.hayanige.chess.CastlingType.KINGSIDE;
import static com.hayanige.chess.CastlingType.QUEENSIDE;
import static com.hayanige.chess.Color.BLACK;
import static com.hayanige.chess.Color.WHITE;

final class Castling {

  static final int WHITE_KINGSIDE = 1 << 0;
  static final int WHITE_QUEENSIDE = 1 << 1;
  static final int BLACK_KINGSIDE = 1 << 2;
  static final int BLACK_QUEENSIDE = 1 << 3;

  static final int NOCASTLING = 0;

  static final int VALUES_LENGTH = 16;

  private Castling() {
  }

  static boolean isValid(int castling) {
    switch (castling) {
      case WHITE_KINGSIDE:
      case WHITE_QUEENSIDE:
      case BLACK_KINGSIDE:
      case BLACK_QUEENSIDE:
        return true;
      default:
        return false;
    }
  }

  static int valueOf(int color, int castlingtype) {
    switch (color) {
      case WHITE:
        switch (castlingtype) {
          case KINGSIDE:
            return WHITE_KINGSIDE;
          case QUEENSIDE:
            return WHITE_QUEENSIDE;
          default:
            throw new IllegalArgumentException();
        }
      case BLACK:
        switch (castlingtype) {
          case KINGSIDE:
            return BLACK_KINGSIDE;
          case QUEENSIDE:
            return BLACK_QUEENSIDE;
          default:
            throw new IllegalArgumentException();
        }
      default:
        throw new IllegalArgumentException();
    }
  }
}

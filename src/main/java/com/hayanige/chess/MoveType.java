/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

final class MoveType {

  static final int MASK = 0x7;

  static final int NORMAL = 0;
  static final int PAWNDOUBLE = 1;
  static final int PAWNPROMOTION = 2;
  static final int ENPASSANT = 3;
  static final int CASTLING = 4;

  static final int NOMOVETYPE = 5;

  private MoveType() {
  }

  static boolean isValid(int type) {
    switch (type) {
      case NORMAL:
      case PAWNDOUBLE:
      case PAWNPROMOTION:
      case ENPASSANT:
      case CASTLING:
        return true;
      default:
        return false;
    }
  }
}

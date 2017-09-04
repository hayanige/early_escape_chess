/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static java.lang.Long.bitCount;
import static java.lang.Long.numberOfTrailingZeros;

final class Bitboard {

  long squares = 0;

  static int next(long squares) {
    return toX88Square(numberOfTrailingZeros(squares));
  }

  static long remainder(long squares) {
    return squares & (squares - 1);
  }

  private static int toX88Square(int square) {
    return ((square & ~7) << 1) | (square & 7);
  }

  private static int toBitSquare(int square) {
    return ((square & ~7) >>> 1) | (square & 7);
  }

  int size() {
    return bitCount(squares);
  }

  void add(int square) {
    squares |= 1L << toBitSquare(square);
  }

  void remove(int square) {
    squares &= ~(1L << toBitSquare(square));
  }
}

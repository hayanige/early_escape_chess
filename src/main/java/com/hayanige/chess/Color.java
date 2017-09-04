/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

final class Color {

  static final int WHITE = 0;
  static final int BLACK = 1;

  static final int NOCOLOR = 2;

  static final int[] values = {
      WHITE, BLACK
  };

  private Color() {
  }

  static boolean isValid(int color) {
    switch (color) {
      case WHITE:
      case BLACK:
        return true;
      default:
        return false;
    }
  }

  static int opposite(int color) {
    switch (color) {
      case WHITE:
        return BLACK;
      case BLACK:
        return WHITE;
      default:
        throw new IllegalArgumentException();
    }
  }
}

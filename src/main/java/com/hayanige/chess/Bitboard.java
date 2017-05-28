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

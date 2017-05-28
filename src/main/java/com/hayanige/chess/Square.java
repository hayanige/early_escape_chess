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

final class Square {

  static final int MASK = 0x7F;

  static final int a1 = 0;
  static final int b1 = 1;
  static final int c1 = 2;
  static final int d1 = 3;
  static final int e1 = 4;
  static final int f1 = 5;
  static final int g1 = 6;
  static final int h1 = 7;

  static final int a2 = 16;
  static final int b2 = 17;
  static final int c2 = 18;
  static final int d2 = 19;
  static final int e2 = 20;
  static final int f2 = 21;
  static final int g2 = 22;
  static final int h2 = 23;

  static final int a3 = 32;
  static final int b3 = 33;
  static final int c3 = 34;
  static final int d3 = 35;
  static final int e3 = 36;
  static final int f3 = 37;
  static final int g3 = 38;
  static final int h3 = 39;

  static final int a4 = 48;
  static final int b4 = 49;
  static final int c4 = 50;
  static final int d4 = 51;
  static final int e4 = 52;
  static final int f4 = 53;
  static final int g4 = 54;
  static final int h4 = 55;

  static final int a5 = 64;
  static final int b5 = 65;
  static final int c5 = 66;
  static final int d5 = 67;
  static final int e5 = 68;
  static final int f5 = 69;
  static final int g5 = 70;
  static final int h5 = 71;

  static final int a6 = 80;
  static final int b6 = 81;
  static final int c6 = 82;
  static final int d6 = 83;
  static final int e6 = 84;
  static final int f6 = 85;
  static final int g6 = 86;
  static final int h6 = 87;

  static final int a7 = 96;
  static final int b7 = 97;
  static final int c7 = 98;
  static final int d7 = 99;
  static final int e7 = 100;
  static final int f7 = 101;
  static final int g7 = 102;
  static final int h7 = 103;

  static final int a8 = 112;
  static final int b8 = 113;
  static final int c8 = 114;
  static final int d8 = 115;
  static final int e8 = 116;
  static final int f8 = 117;
  static final int g8 = 118;
  static final int h8 = 119;

  static final int NOSQUARE = 127;

  static final int VALUES_LENGTH = 128;
  static final int[] values = {
      a1, b1, c1, d1, e1, f1, g1, h1,
      a2, b2, c2, d2, e2, f2, g2, h2,
      a3, b3, c3, d3, e3, f3, g3, h3,
      a4, b4, c4, d4, e4, f4, g4, h4,
      a5, b5, c5, d5, e5, f5, g5, h5,
      a6, b6, c6, d6, e6, f6, g6, h6,
      a7, b7, c7, d7, e7, f7, g7, h7,
      a8, b8, c8, d8, e8, f8, g8, h8
  };

  static final int N = 16;
  static final int E = 1;
  static final int S = -16;
  static final int W = -1;
  static final int NE = N + E;
  static final int SE = S + E;
  static final int SW = S + W;
  static final int NW = N + W;

  static final int[][] pawnDirections = {
      {N, NE, NW},
      {S, SE, SW}
  };

  static final int[] knightDirections = {
      N + N + E,
      N + N + W,
      N + E + E,
      N + W + W,
      S + S + E,
      S + S + W,
      S + E + E,
      S + W + W
  };

  static final int[] bishopDirections = {
      NE, NW, SE, SW
  };

  static final int[] rookDirections = {
      N, E, S, W
  };

  static final int[] queenDirections = {
      N, E, S, W,
      NE, NW, SE, SW
  };

  static final int[] kingDirections = {
      N, E, S, W,
      NE, NW, SE, SW
  };

  private Square() {
  }

  static boolean isValid(int square) {
    return (square & 0x88) == 0;
  }

  static int valueOf(int file, int rank) {
    return (rank << 4) + file;
  }

  static int getFile(int square) {
    return square & 0xF;
  }

  static int getRank(int square) {
    return square >>> 4;
  }
}

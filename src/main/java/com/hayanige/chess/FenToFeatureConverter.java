/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Notation.toPosition;
import static com.hayanige.chess.Square.a1;
import static com.hayanige.chess.Square.b1;
import static com.hayanige.chess.Square.c1;
import static com.hayanige.chess.Square.d1;
import static com.hayanige.chess.Square.e1;
import static com.hayanige.chess.Square.f1;
import static com.hayanige.chess.Square.g1;
import static com.hayanige.chess.Square.h1;
import static com.hayanige.chess.Square.a2;
import static com.hayanige.chess.Square.b2;
import static com.hayanige.chess.Square.c2;
import static com.hayanige.chess.Square.d2;
import static com.hayanige.chess.Square.e2;
import static com.hayanige.chess.Square.f2;
import static com.hayanige.chess.Square.g2;
import static com.hayanige.chess.Square.h2;
import static com.hayanige.chess.Square.a3;
import static com.hayanige.chess.Square.b3;
import static com.hayanige.chess.Square.c3;
import static com.hayanige.chess.Square.d3;
import static com.hayanige.chess.Square.e3;
import static com.hayanige.chess.Square.f3;
import static com.hayanige.chess.Square.g3;
import static com.hayanige.chess.Square.h3;
import static com.hayanige.chess.Square.a4;
import static com.hayanige.chess.Square.b4;
import static com.hayanige.chess.Square.c4;
import static com.hayanige.chess.Square.d4;
import static com.hayanige.chess.Square.e4;
import static com.hayanige.chess.Square.f4;
import static com.hayanige.chess.Square.g4;
import static com.hayanige.chess.Square.h4;
import static com.hayanige.chess.Square.a5;
import static com.hayanige.chess.Square.b5;
import static com.hayanige.chess.Square.c5;
import static com.hayanige.chess.Square.d5;
import static com.hayanige.chess.Square.e5;
import static com.hayanige.chess.Square.f5;
import static com.hayanige.chess.Square.g5;
import static com.hayanige.chess.Square.h5;
import static com.hayanige.chess.Square.a6;
import static com.hayanige.chess.Square.b6;
import static com.hayanige.chess.Square.c6;
import static com.hayanige.chess.Square.d6;
import static com.hayanige.chess.Square.e6;
import static com.hayanige.chess.Square.f6;
import static com.hayanige.chess.Square.g6;
import static com.hayanige.chess.Square.h6;
import static com.hayanige.chess.Square.a7;
import static com.hayanige.chess.Square.b7;
import static com.hayanige.chess.Square.c7;
import static com.hayanige.chess.Square.d7;
import static com.hayanige.chess.Square.e7;
import static com.hayanige.chess.Square.f7;
import static com.hayanige.chess.Square.g7;
import static com.hayanige.chess.Square.h7;
import static com.hayanige.chess.Square.a8;
import static com.hayanige.chess.Square.b8;
import static com.hayanige.chess.Square.c8;
import static com.hayanige.chess.Square.d8;
import static com.hayanige.chess.Square.e8;
import static com.hayanige.chess.Square.f8;
import static com.hayanige.chess.Square.g8;
import static com.hayanige.chess.Square.h8;

import java.util.Scanner;

public class FenToFeatureConverter {

  public static void main(String[] args) throws Exception {

    Scanner scanner = new Scanner(System.in);
    StringBuilder sb;
    String line;
    Position position;

    while (scanner.hasNext()) {
      sb = new StringBuilder();

      // read FEN
      line = scanner.nextLine();
      position = toPosition(line);

      // add board features
      sb.append(position.board[a1]).append(",");
      sb.append(position.board[b1]).append(",");
      sb.append(position.board[c1]).append(",");
      sb.append(position.board[d1]).append(",");
      sb.append(position.board[e1]).append(",");
      sb.append(position.board[f1]).append(",");
      sb.append(position.board[g1]).append(",");
      sb.append(position.board[h1]).append(",");
      sb.append(position.board[a2]).append(",");
      sb.append(position.board[b2]).append(",");
      sb.append(position.board[c2]).append(",");
      sb.append(position.board[d2]).append(",");
      sb.append(position.board[e2]).append(",");
      sb.append(position.board[f2]).append(",");
      sb.append(position.board[g2]).append(",");
      sb.append(position.board[h2]).append(",");
      sb.append(position.board[a3]).append(",");
      sb.append(position.board[b3]).append(",");
      sb.append(position.board[c3]).append(",");
      sb.append(position.board[d3]).append(",");
      sb.append(position.board[e3]).append(",");
      sb.append(position.board[f3]).append(",");
      sb.append(position.board[g3]).append(",");
      sb.append(position.board[h3]).append(",");
      sb.append(position.board[a4]).append(",");
      sb.append(position.board[b4]).append(",");
      sb.append(position.board[c4]).append(",");
      sb.append(position.board[d4]).append(",");
      sb.append(position.board[e4]).append(",");
      sb.append(position.board[f4]).append(",");
      sb.append(position.board[g4]).append(",");
      sb.append(position.board[h4]).append(",");
      sb.append(position.board[a5]).append(",");
      sb.append(position.board[b5]).append(",");
      sb.append(position.board[c5]).append(",");
      sb.append(position.board[d5]).append(",");
      sb.append(position.board[e5]).append(",");
      sb.append(position.board[f5]).append(",");
      sb.append(position.board[g5]).append(",");
      sb.append(position.board[h5]).append(",");
      sb.append(position.board[a6]).append(",");
      sb.append(position.board[b6]).append(",");
      sb.append(position.board[c6]).append(",");
      sb.append(position.board[d6]).append(",");
      sb.append(position.board[e6]).append(",");
      sb.append(position.board[f6]).append(",");
      sb.append(position.board[g6]).append(",");
      sb.append(position.board[h6]).append(",");
      sb.append(position.board[a7]).append(",");
      sb.append(position.board[b7]).append(",");
      sb.append(position.board[c7]).append(",");
      sb.append(position.board[d7]).append(",");
      sb.append(position.board[e7]).append(",");
      sb.append(position.board[f7]).append(",");
      sb.append(position.board[g7]).append(",");
      sb.append(position.board[h7]).append(",");
      sb.append(position.board[a8]).append(",");
      sb.append(position.board[b8]).append(",");
      sb.append(position.board[c8]).append(",");
      sb.append(position.board[d8]).append(",");
      sb.append(position.board[e8]).append(",");
      sb.append(position.board[f8]).append(",");
      sb.append(position.board[g8]).append(",");
      sb.append(position.board[h8]).append(",");

      // add active color feature
      sb.append(position.activeColor).append(",");

      // add full move number feature
      sb.append(position.getFullmoveNumber());

      System.out.println(sb.toString());
    }
  }
}

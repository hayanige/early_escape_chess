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

import static com.hayanige.chess.Notation.fromMove;

import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.IllegalNotationException;
import com.fluxchess.jcpi.protocols.ParseException;
import com.hayanige.chess.MoveList.MoveEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class PositionConverter {

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    String line;
    String[] tokens;
    Position position;
    while (scanner.hasNext()) {
      line = scanner.nextLine();
      tokens = line.trim().split("\\s", 2);
      position = parsePositionCommand(tokens);

      String bin;
      StringBuilder sb = new StringBuilder();

      // from bitboards
      for (int i = 0; i < Color.values.length; i++) {
        for (int j = 0; j < PieceType.values.length; j++) {
          bin = String.format("%64s", Long.toBinaryString(
              position.pieces[i][j].squares)).replace(' ', '0');
          for (int c = 0; c < bin.length(); c++) {
            sb.append(bin.charAt(c)).append(",");
          }
        }
      }

      // from active color
      sb.append(position.activeColor).append(",");

      // from castling rights
      bin = String.format("%4s", Long.toBinaryString(
          position.castlingRights)).replace(' ', '0');
      for (int c = 0; c < bin.length(); c++) {
        sb.append(bin.charAt(c)).append(",");
      }

      // from en passant square
      sb.append(position.enPassantSquare).append(",");

      // from halfmove clock
      sb.append(position.halfmoveClock).append(",");

      // from fullmove number
      sb.append(position.getFullmoveNumber()).append(",");

      sb.setLength(sb.length() - 1);
      System.out.println(sb);
    }
  }

  private static Position parsePositionCommand(String[] tokens)
      throws ParseException {
    assert tokens != null;

    if (tokens.length > 1) {
      List<String> list = getTokens(tokens[1]);
      assert !list.isEmpty();

      Iterator<String> iter = list.iterator();
      String token = iter.next();
      GenericBoard board = null;

      if (token.equalsIgnoreCase("startpos")) {
        board = new GenericBoard(GenericBoard.STANDARDSETUP);

        if (iter.hasNext()) {
          token = iter.next();
          if (!token.equalsIgnoreCase("moves")) {
            // Somethings really wrong here...
            throw new ParseException(
                "Error in position command: unknown keyword " + token
                    + " after startpos");
          } else if (!iter.hasNext()) {
            throw new ParseException(
                "Error in position command: missing moves");
          }
        }
      } else if (token.equalsIgnoreCase("fen")) {
        String fen = "";

        while (iter.hasNext()) {
          token = iter.next();
          if (token.equalsIgnoreCase("moves")) {
            if (!iter.hasNext()) {
              throw new ParseException(
                  "Error in position command: missing moves");
            }

            break;
          }

          fen += token + " ";
        }

        try {
          board = new GenericBoard(fen);
        } catch (IllegalNotationException e) {
          throw new ParseException(
              "Error in position command: illegal fen notation " + fen);
        }
      }

      assert board != null;

      List<GenericMove> moveList = new ArrayList<GenericMove>();

      try {
        while (iter.hasNext()) {
          token = iter.next();

          GenericMove move = new GenericMove(token);
          moveList.add(move);
        }

        Position currentPosition = Notation.toPosition(board);
        MoveGenerator moveGenerator = new MoveGenerator();

        for (GenericMove genericMove : moveList) {
          MoveList<MoveEntry> moves = moveGenerator.getLegalMoves(
              currentPosition, 1, currentPosition.isCheck());
          boolean found = false;
          for (int i = 0; i < moves.size; ++i) {
            int move = moves.entries[i].move;
            if (fromMove(move).equals(genericMove)) {
              currentPosition.makeMove(move);
              found = true;
              break;
            }
          }

          if (!found) {
            throw new IllegalArgumentException();
          }
        }

        return currentPosition;

      } catch (IllegalNotationException e) {
        throw new ParseException(
            "Error in position command: illegal move notation " + token);
      }
    } else {
      throw new ParseException(
          "Error in position command: no parameters specified");
    }
  }

  private static List<String> getTokens(String s) {
    List<String> tokens = new ArrayList<String>(
        Arrays.asList(s.trim().split("\\s")));

    for (Iterator<String> iter = tokens.iterator(); iter.hasNext(); ) {
      // Remove empty tokens
      if (iter.next().length() == 0) {
        iter.remove();
      }
    }

    return tokens;
  }
}

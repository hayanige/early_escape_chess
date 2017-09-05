/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Color.WHITE;
import static com.hayanige.chess.Notation.fromMove;
import static com.hayanige.chess.Notation.fromPosition;

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

public class FenAndWhiteViewScoreConverter {

  public static void main(String[] args) throws Exception {

    Scanner scanner = new Scanner(System.in);
    StringBuilder sb;
    String line;
    String[] tokens;
    Position position;
    List<String> list;
    Iterator<String> iter;

    while (scanner.hasNext()) {
      sb = new StringBuilder();

      // read position command
      line = scanner.nextLine();
      tokens = line.trim().split("\\s", 2);
      position = parsePositionCommand(tokens);
      sb.append(fromPosition(position)).append(",");

      // read info command
      line = scanner.nextLine();
      list = getTokens(line);
      iter = list.iterator();
      while (iter.hasNext()) {
        if (iter.next().equals("cp")) {
          int score = Integer.parseInt(iter.next());
          if (position.activeColor == WHITE) {
            sb.append(score);
          } else {
            sb.append(-score);
          }
          break;
        }
      }

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

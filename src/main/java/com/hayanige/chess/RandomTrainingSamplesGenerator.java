/*
 * Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import static com.hayanige.chess.Color.BLACK;
import static com.hayanige.chess.Square.a1;
import static com.hayanige.chess.Square.a2;
import static com.hayanige.chess.Square.a3;
import static com.hayanige.chess.Square.a4;
import static com.hayanige.chess.Square.a5;
import static com.hayanige.chess.Square.a6;
import static com.hayanige.chess.Square.a7;
import static com.hayanige.chess.Square.a8;
import static com.hayanige.chess.Square.b1;
import static com.hayanige.chess.Square.b2;
import static com.hayanige.chess.Square.b3;
import static com.hayanige.chess.Square.b4;
import static com.hayanige.chess.Square.b5;
import static com.hayanige.chess.Square.b6;
import static com.hayanige.chess.Square.b7;
import static com.hayanige.chess.Square.b8;
import static com.hayanige.chess.Square.c1;
import static com.hayanige.chess.Square.c2;
import static com.hayanige.chess.Square.c3;
import static com.hayanige.chess.Square.c4;
import static com.hayanige.chess.Square.c5;
import static com.hayanige.chess.Square.c6;
import static com.hayanige.chess.Square.c7;
import static com.hayanige.chess.Square.c8;
import static com.hayanige.chess.Square.d1;
import static com.hayanige.chess.Square.d2;
import static com.hayanige.chess.Square.d3;
import static com.hayanige.chess.Square.d4;
import static com.hayanige.chess.Square.d5;
import static com.hayanige.chess.Square.d6;
import static com.hayanige.chess.Square.d7;
import static com.hayanige.chess.Square.d8;
import static com.hayanige.chess.Square.e1;
import static com.hayanige.chess.Square.e2;
import static com.hayanige.chess.Square.e3;
import static com.hayanige.chess.Square.e4;
import static com.hayanige.chess.Square.e5;
import static com.hayanige.chess.Square.e6;
import static com.hayanige.chess.Square.e7;
import static com.hayanige.chess.Square.e8;
import static com.hayanige.chess.Square.f1;
import static com.hayanige.chess.Square.f2;
import static com.hayanige.chess.Square.f3;
import static com.hayanige.chess.Square.f4;
import static com.hayanige.chess.Square.f5;
import static com.hayanige.chess.Square.f6;
import static com.hayanige.chess.Square.f7;
import static com.hayanige.chess.Square.f8;
import static com.hayanige.chess.Square.g1;
import static com.hayanige.chess.Square.g2;
import static com.hayanige.chess.Square.g3;
import static com.hayanige.chess.Square.g4;
import static com.hayanige.chess.Square.g5;
import static com.hayanige.chess.Square.g6;
import static com.hayanige.chess.Square.g7;
import static com.hayanige.chess.Square.g8;
import static com.hayanige.chess.Square.h1;
import static com.hayanige.chess.Square.h2;
import static com.hayanige.chess.Square.h3;
import static com.hayanige.chess.Square.h4;
import static com.hayanige.chess.Square.h5;
import static com.hayanige.chess.Square.h6;
import static com.hayanige.chess.Square.h7;
import static com.hayanige.chess.Square.h8;

import com.hayanige.chess.MoveList.MoveEntry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class RandomTrainingSamplesGenerator {

  private static String engine = null;
  private static String depth = "5";
  private static String numSample = "100";
  private static String ignoreLargeScore = "3000";

  public static void main(String args[]) throws Exception {

    Options opts = createOption();
    CommandLine cli = new DefaultParser().parse(opts, args);

    if (cli.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("score generator from fen", opts);
      return;
    }

    if (cli.hasOption("e")) {
      engine = cli.getOptionValue("e");
    }

    if (cli.hasOption("d")) {
      depth = cli.getOptionValue("d");
    }

    if (cli.hasOption("n")) {
      numSample = cli.getOptionValue("n");
    }

    if (cli.hasOption("i")) {
      ignoreLargeScore = cli.getOptionValue("i");
    }

    if (engine == null) {
      throw new IllegalArgumentException("No engine specified.");
    }

    ProcessBuilder pb = new ProcessBuilder(engine);
    Process process = pb.start();

    OutputStream output = process.getOutputStream();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

    InputStream input = process.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

    writer.write("uci\n");
    writer.write("isready\n");
    writer.flush();

    for (; ; ) {
      String line = reader.readLine();
      if (line.equals("readyok")) {
        break;
      }
    }

    MoveGenerator generator = new MoveGenerator();

    Position position = Notation.toPosition(Notation.STANDARDPOSITION);

    int count = 0;
    Random rand = new Random();
    while (true) {

      // Check insufficient material, repetition and fifty move rule
      if (position.isRepetition()
          || position.hasInsufficientMaterial()
          || position.halfmoveClock >= 100) {
        position = Notation.toPosition(Notation.STANDARDPOSITION);
        continue;
      }

      MoveList<MoveEntry> moves = generator
          .getLegalMoves(position, 1, position.isCheck());

      // if there are not any legal moves, reset the position
      if (moves.size == 0) {
        position = Notation.toPosition(Notation.STANDARDPOSITION);
        continue;
      }

      int move = moves.entries[rand.nextInt(moves.size)].move;
      position.makeMove(move);

      // choose only 1 position in 10 positions
      if (rand.nextInt(10) != 0) {
        continue;
      }

      String fen = Notation.fromPosition(position);
      writer.write("position fen " + fen + "\n");
      writer.write("go " + "depth " + depth + "\n");
      writer.flush();

      String lineNew = null;
      String lineOld = null;
      List<String> list;
      Iterator<String> iter;

      while (true) {
        lineOld = lineNew;
        lineNew = reader.readLine();
        if (lineNew.matches("^bestmove.*")) {
          if (lineOld.matches("^info.*")) {
            list = getTokens(lineOld);
            iter = list.iterator();
            while (iter.hasNext()) {
              String token = iter.next();
              if (token.equals("cp")) {
                int score = Integer.parseInt(iter.next());
                if (Math.abs(score) > Integer.parseInt(ignoreLargeScore)) {
                  break;
                }
                if (position.activeColor == BLACK) {
                  score = -score;
                }
                String features = positionToFeatures(position);
                System.out.println(features + "," + score);
                count++;
                break;
              } else if (token.equals("mate")) {
                position = Notation.toPosition(Notation.STANDARDPOSITION);
                break;
              }
            }
            break;
          } else {
            throw new IOException("format error");
          }
        }
      }

      if (count >= Integer.parseInt(numSample)) {
        break;
      }
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

  private static String positionToFeatures(Position position) {
    StringBuilder sb = new StringBuilder();

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
    sb.append(position.activeColor);

    return sb.toString();
  }

  private static Options createOption() {
    Options result = new Options();

    result.addOption(Option.builder("e")
        .longOpt("engine")
        .desc("chess engine")
        .hasArg()
        .build()
    );

    result.addOption(Option.builder("d")
        .longOpt("depth")
        .desc("search depth")
        .hasArg()
        .build()
    );

    result.addOption(Option.builder("n")
        .longOpt("number")
        .desc("number of samples")
        .hasArg()
        .build()
    );

    result.addOption(Option.builder("i")
        .longOpt("ignore")
        .desc(
            "ignore samples if the absolute scores are larger than this number")
        .hasArg()
        .build()
    );

    result.addOption(Option.builder("h")
        .longOpt("help")
        .desc("help")
        .hasArg(false)
        .build()
    );

    return result;
  }
}

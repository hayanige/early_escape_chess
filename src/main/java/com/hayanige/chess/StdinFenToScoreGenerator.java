/*
 * Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

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
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class StdinFenToScoreGenerator {

  private static String engine = null;
  private static String depth = "5";

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

    Scanner sc = new Scanner(System.in);
    String fen;
    String lineNew = null;
    String lineOld = null;
    List<String> list;
    Iterator<String> iter;

    while (sc.hasNext()) {
      fen = sc.nextLine();
      if (fen.isEmpty()) {
        continue;
      }
      writer.write("position fen " + fen + "\n");
      writer.write("go " + "depth " + depth + "\n");
      writer.flush();

      for (; ; ) {
        lineOld = lineNew;
        lineNew = reader.readLine();
        if (lineNew.matches("^bestmove.*")) {
          if (lineOld.matches("^info.*")) {
            list = getTokens(lineOld);
            iter = list.iterator();
            while (iter.hasNext()) {
              String token = iter.next();
              if (token.equals("cp")) {
                System.out.println(fen + "," + Integer.parseInt(iter.next()));
                break;
              } else if (token.equals("mate")) {
                break;
              }
            }
            break;
          } else {
            throw new IOException("format error");
          }
        } else if (lineNew == null) {
          break;
        }
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

    result.addOption(Option.builder("h")
        .longOpt("help")
        .desc("help")
        .hasArg(false)
        .build()
    );

    return result;
  }
}

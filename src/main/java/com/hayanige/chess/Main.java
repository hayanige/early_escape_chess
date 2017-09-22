/*
 * Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

  private static String evalFunc = null;
  private static String model = null;

  public static void main(String[] args) throws IOException {
    Options opts = createOption();
    CommandLine cli;

    try {
      cli = new DefaultParser().parse(opts, args);
    } catch (ParseException e) {
      e.printStackTrace();
      throw new IOException("option error");
    }

    if (cli.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("Early Escape Chess Engine", opts);
      return;
    }

    if (cli.hasOption("e")) {
      evalFunc = cli.getOptionValue("e");
    } else {
      evalFunc = PulseEvaluation.class.getCanonicalName();
    }

    if (cli.hasOption("m")) {
      model = cli.getOptionValue("m");
    }

    try {
      Evaluation evaluation;
      String neuralEval = NeuralEvaluation.class.getCanonicalName();
      if (neuralEval.equals(evalFunc)) {
        if (model == null) {
          throw new IllegalArgumentException(
              "Please set a model if you want to use NeuralEvaluation");
        } else {
          evaluation = (Evaluation) Class.forName(evalFunc)
              .getDeclaredConstructor(String.class).newInstance(model);
        }
      } else {
        evaluation = (Evaluation) Class.forName(evalFunc).newInstance();
      }

      new EarlyEscape(evaluation).run();
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

  private static Options createOption() {
    Options result = new Options();

    result.addOption(Option.builder("e")
        .longOpt("evaluation")
        .desc("evaluation function name")
        .hasArg()
        .build()
    );

    result.addOption(Option.builder("m")
        .longOpt("model")
        .desc("model for neural evaluation function")
        .hasArg()
        .build()
    );

    return result;
  }
}

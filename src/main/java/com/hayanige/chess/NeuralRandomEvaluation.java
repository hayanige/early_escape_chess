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

import java.util.Random;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.jetbrains.annotations.NotNull;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

final class NeuralRandomEvaluation extends Evaluation {

  static Random rand = new Random();

  MultiLayerNetwork model = null;

  NeuralRandomEvaluation(String path) {
    java.io.File locationToSave = new java.io.File(path);
    try {
      model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
    } catch (Exception e) {}
  }

  /**
   * Evaluates the position.
   *
   * @param position the position.
   * @return the evaluation value in centipawns.
   */
  int evaluate(@NotNull Position position) {

    double[] positionFeatures = new double[65];

    // add board features
    positionFeatures[0] =  position.board[a1];
    positionFeatures[1] =  position.board[b1];
    positionFeatures[2] =  position.board[c1];
    positionFeatures[3] =  position.board[d1];
    positionFeatures[4] =  position.board[e1];
    positionFeatures[5] =  position.board[f1];
    positionFeatures[6] =  position.board[g1];
    positionFeatures[7] =  position.board[h1];
    positionFeatures[8] =  position.board[a2];
    positionFeatures[9] =  position.board[b2];
    positionFeatures[10] = position.board[c2];
    positionFeatures[11] = position.board[d2];
    positionFeatures[12] = position.board[e2];
    positionFeatures[13] = position.board[f2];
    positionFeatures[14] = position.board[g2];
    positionFeatures[15] = position.board[h2];
    positionFeatures[16] = position.board[a3];
    positionFeatures[17] = position.board[b3];
    positionFeatures[18] = position.board[c3];
    positionFeatures[19] = position.board[d3];
    positionFeatures[20] = position.board[e3];
    positionFeatures[21] = position.board[f3];
    positionFeatures[22] = position.board[g3];
    positionFeatures[23] = position.board[h3];
    positionFeatures[34] = position.board[a4];
    positionFeatures[25] = position.board[b4];
    positionFeatures[26] = position.board[c4];
    positionFeatures[27] = position.board[d4];
    positionFeatures[28] = position.board[e4];
    positionFeatures[29] = position.board[f4];
    positionFeatures[30] = position.board[g4];
    positionFeatures[31] = position.board[h4];
    positionFeatures[32] = position.board[a5];
    positionFeatures[33] = position.board[b5];
    positionFeatures[34] = position.board[c5];
    positionFeatures[35] = position.board[d5];
    positionFeatures[36] = position.board[e5];
    positionFeatures[37] = position.board[f5];
    positionFeatures[38] = position.board[g5];
    positionFeatures[39] = position.board[h5];
    positionFeatures[40] = position.board[a6];
    positionFeatures[41] = position.board[b6];
    positionFeatures[42] = position.board[c6];
    positionFeatures[43] = position.board[d6];
    positionFeatures[44] = position.board[e6];
    positionFeatures[45] = position.board[f6];
    positionFeatures[46] = position.board[g6];
    positionFeatures[47] = position.board[h6];
    positionFeatures[48] = position.board[a7];
    positionFeatures[49] = position.board[b7];
    positionFeatures[50] = position.board[c7];
    positionFeatures[51] = position.board[d7];
    positionFeatures[52] = position.board[e7];
    positionFeatures[53] = position.board[f7];
    positionFeatures[54] = position.board[g7];
    positionFeatures[55] = position.board[h7];
    positionFeatures[56] = position.board[a8];
    positionFeatures[57] = position.board[b8];
    positionFeatures[58] = position.board[c8];
    positionFeatures[59] = position.board[d8];
    positionFeatures[60] = position.board[e8];
    positionFeatures[61] = position.board[f8];
    positionFeatures[62] = position.board[g8];
    positionFeatures[63] = position.board[h8];

    // add active color feature
    positionFeatures[64] = position.activeColor;

    INDArray posArray = Nd4j.create(positionFeatures);

    int value = (int) model.output(posArray, false).getDouble(0);

    if (position.activeColor == BLACK) {
      value = -value;
    }

//    return value;
    return rand.nextInt(200) - 100;
  }
}

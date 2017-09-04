/*
 * Copyright (C) 2013-2016 Phokham Nonava
 * Modified work Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package com.hayanige.chess;

public class PerftExecutor {

  public static void main(String[] args) {
    try {
      new Perft().run();
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

}

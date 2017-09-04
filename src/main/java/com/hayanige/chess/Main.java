/*
 * Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

public class Main {

  public static void main(String[] args) {
    try {
      new EarlyEscape().run();
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }

}

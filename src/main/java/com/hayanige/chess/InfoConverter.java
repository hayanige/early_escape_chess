/*
 * Copyright 2017 hayanige
 *
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.hayanige.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class InfoConverter {

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    String line;
    String token;
    List<String> list;
    Iterator<String> iter;
    while (scanner.hasNext()) {
      line = scanner.nextLine();
      list = getTokens(line);
      iter = list.iterator();
      while (iter.hasNext()) {
        token = iter.next();
        if (token.equals("cp")) {
          System.out.println(iter.next());
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
}

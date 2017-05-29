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

import static com.hayanige.chess.Depth.MAX_PLY;
import static java.lang.Math.abs;

final class Value {

  static final int INFINITE = 200000;
  static final int CHECKMATE = 100000;
  static final int CHECKMATE_THRESHOLD = CHECKMATE - MAX_PLY;
  static final int DRAW = 0;

  static final int NOVALUE = 300000;

  private Value() {
  }

  static boolean isValid(int value) {
    int absvalue = abs(value);

    return absvalue <= CHECKMATE || absvalue == INFINITE;
  }

  static boolean isCheckmate(int value) {
    int absvalue = abs(value);

    return absvalue >= CHECKMATE_THRESHOLD && absvalue <= CHECKMATE;
  }
}

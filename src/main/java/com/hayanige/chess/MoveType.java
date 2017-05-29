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

final class MoveType {

  static final int MASK = 0x7;

  static final int NORMAL = 0;
  static final int PAWNDOUBLE = 1;
  static final int PAWNPROMOTION = 2;
  static final int ENPASSANT = 3;
  static final int CASTLING = 4;

  static final int NOMOVETYPE = 5;

  private MoveType() {
  }

  static boolean isValid(int type) {
    switch (type) {
      case NORMAL:
      case PAWNDOUBLE:
      case PAWNPROMOTION:
      case ENPASSANT:
      case CASTLING:
        return true;
      default:
        return false;
    }
  }
}

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

public class Rank {

  static final int r1 = 0;
  static final int r2 = 1;
  static final int r3 = 2;
  static final int r4 = 3;
  static final int r5 = 4;
  static final int r6 = 5;
  static final int r7 = 6;
  static final int r8 = 7;

  static final int NORANK = 8;

  static final int[] values = {
      r1, r2, r3, r4, r5, r6, r7, r8
  };

  private Rank() {
  }

  static boolean isValid(int rank) {
    switch (rank) {
      case r1:
      case r2:
      case r3:
      case r4:
      case r5:
      case r6:
      case r7:
      case r8:
        return true;
      default:
        return false;
    }
  }
}

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

import static org.junit.Assert.assertEquals;

import com.hayanige.chess.MoveList.MoveEntry;
import org.junit.Test;

public class TestMoveList {

  @Test
  public void test() {
    MoveList<MoveEntry> moveList = new MoveList<MoveEntry>(MoveEntry.class);

    assertEquals(moveList.size, 0);

    moveList.entries[moveList.size++].move = 1;
    assertEquals(moveList.size, 1);
  }
}

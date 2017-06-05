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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hayanige.chess.Move.NOMOVE;
import static com.hayanige.chess.Notation.fromMove;
import static com.hayanige.chess.Notation.fromPosition;
import static com.hayanige.chess.Value.CHECKMATE;
import static com.hayanige.chess.Value.CHECKMATE_THRESHOLD;
import static java.lang.Integer.signum;
import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.EngineAnalyzeCommand;
import com.fluxchess.jcpi.commands.EngineDebugCommand;
import com.fluxchess.jcpi.commands.EngineInitializeRequestCommand;
import com.fluxchess.jcpi.commands.EngineNewGameCommand;
import com.fluxchess.jcpi.commands.EnginePonderHitCommand;
import com.fluxchess.jcpi.commands.EngineReadyRequestCommand;
import com.fluxchess.jcpi.commands.EngineSetOptionCommand;
import com.fluxchess.jcpi.commands.EngineStartCalculatingCommand;
import com.fluxchess.jcpi.commands.EngineStopCalculatingCommand;
import com.fluxchess.jcpi.commands.ProtocolBestMoveCommand;
import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.fluxchess.jcpi.commands.ProtocolInitializeAnswerCommand;
import com.fluxchess.jcpi.commands.ProtocolReadyAnswerCommand;
import com.fluxchess.jcpi.models.GenericBoard;
import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.protocols.IProtocolHandler;
import com.hayanige.chess.MoveList.MoveEntry;
import com.hayanige.chess.MoveList.RootEntry;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class EarlyEscape extends AbstractEngine implements Protocol {

  final private Logger logger = LoggerFactory.getLogger(EarlyEscape.class);

  private Search search = new Search(this);
  private long startTime = 0;
  private long statusStartTime = 0;

  private Position currentPosition = Notation.toPosition(
      new GenericBoard(GenericBoard.STANDARDSETUP));

  public EarlyEscape() {
  }

  public EarlyEscape(BufferedReader input, PrintStream output) {
    super(input, output);
  }

  public EarlyEscape(IProtocolHandler handler) {
    super(handler);
  }

  protected void quit() {
    search.quit();
  }

  public void receive(EngineInitializeRequestCommand command) {
    search.stop();

    ProtocolInitializeAnswerCommand answerCommand
        = new ProtocolInitializeAnswerCommand(
        "EarlyEscape 0.0.1", "hayanige"
    );

    getProtocol().send(answerCommand);
  }

  public void receive(EngineSetOptionCommand command) {
  }

  public void receive(EngineDebugCommand command) {
  }

  public void receive(EngineReadyRequestCommand command) {
    checkNotNull(command);

    getProtocol().send(new ProtocolReadyAnswerCommand(command.token));
  }

  public void receive(EngineNewGameCommand command) {
    search.stop();

    currentPosition = Notation.toPosition(
        new GenericBoard(GenericBoard.STANDARDSETUP));
  }

  public void receive(EngineAnalyzeCommand command) {
    checkNotNull(command);

    search.stop();

    currentPosition = Notation.toPosition(command.board);

    MoveGenerator moveGenerator = new MoveGenerator();

    for (GenericMove genericMove : command.moves) {
      MoveList<MoveEntry> moves = moveGenerator.getLegalMoves(
          currentPosition, 1, currentPosition.isCheck());
      boolean found = false;
      for (int i = 0; i < moves.size; ++i) {
        int move = moves.entries[i].move;
        if (fromMove(move).equals(genericMove)) {
          currentPosition.makeMove(move);
          found = true;
          break;
        }
      }

      if (!found) {
        throw new IllegalArgumentException();
      }
    }

    logger.debug(fromPosition(currentPosition));
  }

  public void receive(EngineStartCalculatingCommand command) {
    checkNotNull(command);

    search.stop();

    if (command.getDepth() != null) {
      search.newDepthSearch(currentPosition, command.getDepth());
    } else if (command.getNodes() != null) {
      search.newNodesSearch(currentPosition, command.getNodes());
    } else if (command.getMoveTime() != null) {
      search.newTimeSearch(currentPosition, command.getMoveTime());
    } else if (command.getInfinite()) {
      search.newInfiniteSearch(currentPosition);
    } else {
      long whiteTimeLeft = 1;
      if (command.getClock(GenericColor.WHITE) != null) {
        whiteTimeLeft = command.getClock(GenericColor.WHITE);
      }

      long whiteTimeIncrement = 0;
      if (command.getClockIncrement(GenericColor.WHITE) != null) {
        whiteTimeIncrement = command.getClockIncrement(GenericColor.WHITE);
      }

      long blackTimeLeft = 1;
      if (command.getClock(GenericColor.BLACK) != null) {
        blackTimeLeft = command.getClock(GenericColor.BLACK);
      }

      long blackTimeIncrement = 0;
      if (command.getClockIncrement(GenericColor.BLACK) != null) {
        blackTimeIncrement = command.getClockIncrement(GenericColor.BLACK);
      }

      int searchMovesToGo = 40;
      if (command.getMovesToGo() != null) {
        searchMovesToGo = command.getMovesToGo();
      }

      if (command.getPonder()) {
        search.newPonderSearch(currentPosition, whiteTimeLeft,
            whiteTimeIncrement, blackTimeLeft, blackTimeIncrement,
            searchMovesToGo);
      } else {
        search.newClockSearch(currentPosition, whiteTimeLeft,
            whiteTimeIncrement, blackTimeLeft, blackTimeIncrement,
            searchMovesToGo);
      }
    }

    // Go...
    search.start();
    startTime = currentTimeMillis();
    statusStartTime = startTime;
  }

  public void receive(EnginePonderHitCommand command) {
    search.ponderHit();
  }

  public void receive(EngineStopCalculatingCommand command) {
    search.stop();
  }

  public void sendBestMove(int bestMove, int ponderMove) {
    GenericMove genericBestMove = null;
    GenericMove genericPonderMove = null;
    if (bestMove != NOMOVE) {
      genericBestMove = fromMove(bestMove);

      if (ponderMove != NOMOVE) {
        genericPonderMove = fromMove(ponderMove);
      }
    }

    getProtocol().send(
        new ProtocolBestMoveCommand(genericBestMove, genericPonderMove));
  }

  public void sendStatus(int currentDepth, int currentMaxDepth, long totalNodes,
      int currentMove, int currentMoveNumber) {
    if (currentTimeMillis() - statusStartTime >= 1000) {
      sendStatus(false, currentDepth, currentMaxDepth, totalNodes, currentMove,
          currentMoveNumber);
    }
  }

  public void sendStatus(boolean force, int currentDepth, int currentMaxDepth,
      long totalNodes, int currentMove, int currentMoveNumber) {
    long timeDelta = currentTimeMillis() - startTime;

    if (force || timeDelta >= 1000) {
      ProtocolInformationCommand command = new ProtocolInformationCommand();

      command.setDepth(currentDepth);
      command.setMaxDepth(currentMaxDepth);
      command.setNodes(totalNodes);
      command.setTime(timeDelta);
      command.setNps(timeDelta >= 1000 ? (totalNodes * 1000) / timeDelta : 0);
      if (currentMove != NOMOVE) {
        command.setCurrentMove(fromMove(currentMove));
        command.setCurrentMoveNumber(currentMoveNumber);
      }

      getProtocol().send(command);
      loggingSendInfo(command);

      statusStartTime = currentTimeMillis();
    }
  }

  public void sendMove(RootEntry entry, int currentDepth, int currentMaxDepth,
      long totalNodes) {
    long timeDelta = currentTimeMillis() - startTime;

    ProtocolInformationCommand command = new ProtocolInformationCommand();

    command.setDepth(currentDepth);
    command.setMaxDepth(currentMaxDepth);
    command.setNodes(totalNodes);
    command.setTime(timeDelta);
    command.setNps(timeDelta >= 1000 ? (totalNodes * 1000) / timeDelta : 0);
    if (abs(entry.value) >= CHECKMATE_THRESHOLD) {
      int mateDepth = CHECKMATE - abs(entry.value);
      command.setMate(signum(entry.value) * (mateDepth + 1) / 2);
    } else {
      command.setCentipawns(entry.value);
    }
    List<GenericMove> moveList = new ArrayList<>();
    for (int i = 0; i < entry.pv.size; ++i) {
      moveList.add(fromMove(entry.pv.moves[i]));
    }
    command.setMoveList(moveList);

    getProtocol().send(command);
    loggingSendInfo(command);

    statusStartTime = currentTimeMillis();
  }

  public void loggingSendInfo(ProtocolInformationCommand command) {
    String infoCommand = "info";

    if (command.getPvNumber() != null) {
      infoCommand += " multipv " + command.getPvNumber().toString();
    }
    if (command.getDepth() != null) {
      infoCommand += " depth " + command.getDepth().toString();

      if (command.getMaxDepth() != null) {
        infoCommand += " seldepth " + command.getMaxDepth().toString();
      }
    }
    if (command.getMate() != null) {
      infoCommand += " score mate " + command.getMate().toString();
    } else if (command.getCentipawns() != null) {
      infoCommand += " score cp " + command.getCentipawns().toString();
    }
    if (command.getValue() != null) {
      switch (command.getValue()) {
        case EXACT:
          break;
        case ALPHA:
          infoCommand += " upperbound";
          break;
        case BETA:
          infoCommand += " lowerbound";
          break;
        default:
          assert false : command.getValue();
      }
    }
    if (command.getMoveList() != null) {
      infoCommand += " pv";
      for (GenericMove move : command.getMoveList()) {
        infoCommand += " ";
        infoCommand += move.toString();
      }
    }
    if (command.getRefutationList() != null) {
      infoCommand += " refutation";
      for (GenericMove move : command.getRefutationList()) {
        infoCommand += " ";
        infoCommand += move.toString();
      }
    }
    if (command.getCurrentMove() != null) {
      infoCommand += " currmove " + command.getCurrentMove().toString();
    }
    if (command.getCurrentMoveNumber() != null) {
      infoCommand +=
          " currmovenumber " + command.getCurrentMoveNumber().toString();
    }
    if (command.getHash() != null) {
      infoCommand += " hashfull " + command.getHash().toString();
    }
    if (command.getNps() != null) {
      infoCommand += " nps " + command.getNps().toString();
    }
    if (command.getTime() != null) {
      infoCommand += " time " + command.getTime().toString();
    }
    if (command.getNodes() != null) {
      infoCommand += " nodes " + command.getNodes().toString();
    }
    if (command.getString() != null) {
      infoCommand += " string " + command.getString();
    }

    logger.debug(infoCommand);
  }
}

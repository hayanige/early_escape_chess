### 概要（Overview）

[Pulse](https://github.com/fluxroot/pulse/)というオープンソースのチェスエンジンを改造したチェスエンジンです。  
評価関数の改造が主な目的で、合わせていろいろなツールを開発しています。

This is a chess engine which modified [Pulse](https://github.com/fluxroot/pulse/). The main purpose of this software is improving the evaluate function and developing some util tools together.

### 使い方（How To Use）

* RandomTrainingSamplesGenerator  
教師データを生成するジェネレーター  
ランダムな局面に対してオプションで指定したチェスエンジンの評価値を出してくれる  
評価値はUCIプロトコルのスコア（Centipawn）であるが  
手番側から見た評価値ではなく常に白視点の評価値を生成する（黒番でも黒が優勢の時はマイナスになる）

```
$ java -cp early-escape-1.0-SNAPSHOT-jar-with-dependencies.jar com.hayanige.chess.RandomTrainingSamplesGenerator -e executable_chess_engine --help
usage: score generator from fen
 -d,--depth <arg>    search depth
 -e,--engine <arg>   chess engine
 -h,--help           help
 -i,--ignore <arg>   ignore samples if the absolute scores are larger than
                     this number
 -n,--number <arg>   number of samples

$ java -cp early-escape-1.0-SNAPSHOT-jar-with-dependencies.jar com.hayanige.chess.RandomTrainingSamplesGenerator -e executable_chess_engine -d 10 -i 1000 -n 1000 > training_data.csv
```

### LICENSE

[LICENSE](LICENSE)

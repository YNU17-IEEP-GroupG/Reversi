package jp.ac.ynu.pl2017.gg.reversi.ai;

import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shiita on 2017/05/13.
 */
public class Search {
    private Stone targetStone;
    // TODO: typeをフィールドに
    public Search(Stone targetStone) {
        this.targetStone = targetStone;
    }

    public Point beamSearch(Evaluation.EvaluationType type, Stone[][] board, int limit, int depth) {
        List<Status> evaluated = new ArrayList<>();
        List<Status> beam;

        // beamの初期化
        beam = calcNewStatus(new Status(0, new ArrayList<>(), targetStone.getReverse(), board), type);
        beam = updateBeam(beam, limit);
        while (!beam.isEmpty() && depth >= 0) {
            int size = beam.size();
            for (int j = 0; j < size; j++) {
                Status s = beam.get(0);
                beam.remove(0);
                if (s.stone == targetStone)
                    evaluated.add(s);
                List<Status> newS = calcNewStatus(s, type);
                beam.addAll(newS);
            }
            beam = updateBeam(beam, limit);
            depth--;
        }
        evaluated = evaluated.stream()
                .sorted(Comparator.comparing(Status::getValue, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return evaluated.get(0).path.get(0);
    }

    private List<Status> calcNewStatus(Status status, Evaluation.EvaluationType type) {
        Stone reverse = status.stone.getReverse();
        List<Status> list = new ArrayList<>();
        List<Point> hint = BoardHelper.makeHint(reverse, status.board);
        for (Point point : hint) {
            Stone[][] newBoard = BoardHelper.cloneBoard(status.board);
            BoardHelper.putStone(point.getRow(), point.getColumn(), reverse, newBoard);
            int value = evaluate(type, reverse, newBoard);
            List<Point> path = new ArrayList<>();
            for (Point p : status.path) path.add(p);
            path.add(point);
            list.add(new Status(value, path, reverse, newBoard));
        }
        return list;
    }

    private List<Status> updateBeam(List<Status> beam, int limit) {
        int l = Math.min(beam.size(), limit);
        if (l == 0) return new ArrayList<>();
        return beam.stream()
                .sorted(Comparator.comparing(Status::getValue, Comparator.reverseOrder()))
                .limit(l)
                .collect(Collectors.toList());
    }

    public Point untilEnd(Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        Triple bestTriple = new Triple(-1, 0, 0);
        Triple triple;
        List<Point> hint = BoardHelper.makeHint(stone, board);
        Point bestPoint = hint.get(0);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.getRow(), p.getColumn(), stone, board);
            triple = uEnd(type, stone.getReverse(), board);
            BoardHelper.undo(p.getRow(), p.getColumn(), stone, pList, board);
            if (triple.winPer > bestTriple.winPer) {
                bestTriple.winPer = triple.winPer;
                bestTriple.value = triple.value;
                bestPoint = p;
            }
            else if (triple.winPer == bestTriple.winPer && triple.value > bestTriple.value) {
                bestTriple.value = triple.value;
                bestPoint = p;
            }
        }
//        System.out.println("勝率" + (bestTriple.winPer * 100) +"%, 最低石差" + bestTriple.value + "個");
        return bestPoint;
    }

    private Triple uEnd(Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        List<Point> hint = BoardHelper.makeHint(stone, board);
        if (hint.isEmpty()) {
            List<Point> hint2 = BoardHelper.makeHint(stone.getReverse(), board);
            if (hint2.isEmpty()) {
                int value = evaluate(type, targetStone, board);
                return new Triple(value > 0 ? 1 : 0, 1, value);
            }
            else {
                return uEnd(type, stone.getReverse(), board);
            }
        }

        double winPer = 0;
        int count = 0;
        int value = stone == targetStone ? -(1 << 29) : (1 << 29);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.getRow(), p.getColumn(), stone, board);
            Triple triple = uEnd(type, stone.getReverse(), board);
            BoardHelper.undo(p.getRow(), p.getColumn(), stone, pList, board);

            // 目的の石の場合には最も良いものを選択
            if (stone == targetStone) {
                if (triple.winPer > winPer) {
                    winPer = triple.winPer;
                    value = triple.value;
                }
                else if (triple.winPer == winPer) {
                    value = Math.max(triple.value, value);
                }
            }
            // そうでなければ、全体の確率を考える
            else {
                winPer += triple.winPer * triple.count;
                value = Math.min(triple.value, value);
            }
            count += triple.count;
        }
        if (stone != targetStone) {
            winPer /= count;
        }
        return new Triple(winPer, count, value);
    }

    // depthは2の倍数で2以上ある必要がある
    public Point minMax(int depth, Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        if (depth % 2 == 1 || depth < 2) throw new RuntimeException();
        Point bestPoint = new Point(0,0);
        int alpha = -(1 << 30);
        int beta =    1 << 30;

        List<Point> hint = BoardHelper.makeHint(stone, board);
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.getRow(), p.getColumn(), stone, board);
            int value = alphaBeta(depth - 1, type, stone.getReverse(), board, alpha, beta);
            BoardHelper.undo(p.getRow(), p.getColumn(), stone, pList, board);
            if (value > alpha) {
                bestPoint = p;
                alpha = value;
            }
        }
        return bestPoint;
    }

    private int alphaBeta(int depth, Evaluation.EvaluationType type, Stone stone, Stone[][] board, int alpha, int beta) {
        if (depth == 0) {
            return evaluate(type, targetStone, board);
        }

        int value;
        if (stone == targetStone) alpha = -(1 << 29);
        else                      beta  =   1 << 29;

        List<Point> hint = BoardHelper.makeHint(stone, board);
        // 読みの中で勝敗が決まった場合
        if (hint.isEmpty()) {
            return stone == targetStone ? alpha : beta;
        }
        for (Point p : hint) {
            List<Point> pList = BoardHelper.putStone(p.getRow(), p.getColumn(), stone, board);
            value = alphaBeta(depth - 1, type, stone.getReverse(), board, alpha, beta);
            BoardHelper.undo(p.getRow(), p.getColumn(), stone, pList, board);
            if (stone == targetStone && value > alpha) {
                alpha = value;
                if (alpha > beta) return alpha;
            }
            else if (stone != targetStone && value < beta) {
                beta = value;
                if (alpha > beta) return beta;
            }
        }
        return stone == targetStone ? alpha : beta;
    }

    private int evaluate(Evaluation.EvaluationType type, Stone stone, Stone[][] board) {
        int value = 0;
        switch (type) {
            case SQUARE:
                value = Evaluation.squareEvaluation(stone, board);
                break;
            case COUNT:
                value = Evaluation.countEvaluation(stone, board);
                break;
        }
        return value;
    }

    private class Triple {
        public double winPer;
        public int count;
        public int value;
        public Triple(double winPer, int count, int value) {
            this.winPer = winPer; this.count = count; this.value = value;
        }
    }

    private class Status {
        public int value;
        public List<Point> path;
        public Stone stone;
        public Stone[][] board;
        public Status(int value, List<Point> path, Stone stone, Stone[][] board) {
            this.value = value;
            this.path = path;
            this.stone = stone;
            this.board = board;
        }
        public int getValue() {
            return value;
        }

        public void printPath() {
            path.stream().forEach(p -> {
                System.out.print("(" + p.getRow() + ", " + p.getColumn() + "), ");
            });
        }
    }
}


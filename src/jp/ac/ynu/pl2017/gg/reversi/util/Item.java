package jp.ac.ynu.pl2017.gg.reversi.util;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by shiita on 2017/05/18.
 */
public enum Item implements Serializable {
//    NONE { @Override public String toString() { return ""; } },
//    BAN { @Override public String toString() { return "盤面にランダムに石を置けないマスを3つ生成する"; } },
////    CONTROLER { @Override public String toString() { return "相手のターンを1度だけ自分が操作できるようになる"; } },
//    DROP { @Override public String toString() { return "相手の石を1つ選択して消す"; } },
//    GRAY { @Override public String toString() { return "相手の盤面の石を3ターンの間灰色にする"; } },
//    TRIPLE { @Override public String toString() { return "このターンに3つ分の石を配置することができるようになる"; } };
    NONE, BAN, DROP, GRAY, TRIPLE;

    // ※注意 enumのフィールドはシリアライズされないらしい
    private int[] pos = { -1, -1, -1, -1, -1, -1};
    private int[] coordinate = { -1, -1 };

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public int[] getPos() {
        return pos;
    }

    public int[] getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int[] coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "pos=" + Arrays.toString(pos) +
                ", coordinate=" + Arrays.toString(coordinate) +
                '}';
    }
}

package jp.ac.ynu.pl2017.gg.reversi.util;
/**
 * Created by shiita on 2017/04/29.
 */
public enum Direction {
    UP         { @Override public int getDR() { return -1; } @Override public int getDC() { return  0; } },
    UP_RIGHT   { @Override public int getDR() { return -1; } @Override public int getDC() { return  1; } },
    RIGHT      { @Override public int getDR() { return  0; } @Override public int getDC() { return  1; } },
    DOWN_RIGHT { @Override public int getDR() { return  1; } @Override public int getDC() { return  1; } },
    DOWN       { @Override public int getDR() { return  1; } @Override public int getDC() { return  0; } },
    DOWN_LEFT  { @Override public int getDR() { return  1; } @Override public int getDC() { return -1; } },
    LEFT       { @Override public int getDR() { return  0; } @Override public int getDC() { return -1; } },
    UP_LEFT    { @Override public int getDR() { return -1; } @Override public int getDC() { return -1; } };
    public abstract int getDR();
    public abstract int getDC();
}

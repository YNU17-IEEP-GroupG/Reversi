package jp.ac.ynu.pl2017.gg.reversi.ai;

/**
 * Created by shiita on 2017/05/10.
 */
public interface BaseAI {
    void think();
    void randomThink();
    int getRow();
    int getColumn();
}

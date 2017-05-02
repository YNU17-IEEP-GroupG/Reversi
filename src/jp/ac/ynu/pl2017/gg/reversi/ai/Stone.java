package jp.ac.ynu.pl2017.gg.reversi.ai;
import javax.swing.*;

import jp.ac.ynu.pl2017.gg.reversi.gui.Othello;

/**
 * Created by shiita on 2017/04/29.
 */
public enum Stone {
    Empty { @Override public ImageIcon getImageIcon() { return Othello.emptyIcon; }
            @Override public Stone getReverse() { return Empty; } },
    Black { @Override public ImageIcon getImageIcon() { return Othello.blackIcon; }
            @Override public Stone getReverse() { return White; } },
    White { @Override public ImageIcon getImageIcon() { return Othello.whiteIcon; }
            @Override public Stone getReverse() { return Black; } };
    public abstract ImageIcon getImageIcon();
    public abstract Stone getReverse();
}

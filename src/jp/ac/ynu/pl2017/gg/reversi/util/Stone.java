package jp.ac.ynu.pl2017.gg.reversi.util;
import javax.swing.*;

import jp.ac.ynu.pl2017.gg.reversi.gui.Othello;

/**
 * Created by shiita on 2017/04/29.
 */
public enum Stone {
    Empty { @Override public ImageIcon getImageIcon() { return Othello.emptyIcon; }
            @Override public ImageIcon getTripleImageIcon() { return Othello.emptyIcon; }
            @Override public Stone     getReverse() { return Empty; } },

    Black { @Override public ImageIcon getImageIcon() { return Othello.blackIcon; }
            @Override public ImageIcon getTripleImageIcon() { return Othello.tripleBlackIcon; }
            @Override public Stone     getReverse() { return White; } },

    White { @Override public ImageIcon getImageIcon() { return Othello.whiteIcon; }
            @Override public ImageIcon getTripleImageIcon() { return Othello.tripleWhiteIcon; }
            @Override public Stone     getReverse() { return Black; } },

    Ban   { @Override public ImageIcon getImageIcon() { return Othello.cannotPutIcon; }
            @Override public ImageIcon getTripleImageIcon() { return Othello.cannotPutIcon; }
            @Override public Stone     getReverse() { return Ban; } };

    public abstract ImageIcon getImageIcon();
    public abstract ImageIcon getTripleImageIcon();
    public abstract Stone getReverse();
}

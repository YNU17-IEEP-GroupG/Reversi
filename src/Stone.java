import javax.swing.*;

/**
 * Created by shiita on 2017/04/29.
 */
public enum Stone {
    Empty { @Override public ImageIcon getImageIcon() { return Othello.emptyIcon; } },
    Black { @Override public ImageIcon getImageIcon() { return Othello.blackIcon; } },
    White { @Override public ImageIcon getImageIcon() { return Othello.whiteIcon; } };
    public abstract ImageIcon getImageIcon();
}

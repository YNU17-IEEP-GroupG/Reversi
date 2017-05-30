package jp.ac.ynu.pl2017.gg.reversi.gui;

import static jp.ac.ynu.pl2017.gg.reversi.util.Stone.*;
import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.junit.Before;
import org.junit.Test;

import jp.ac.ynu.pl2017.gg.reversi.ai.BaseAI;
import jp.ac.ynu.pl2017.gg.reversi.util.BoardHelper;
import jp.ac.ynu.pl2017.gg.reversi.util.Item;
import jp.ac.ynu.pl2017.gg.reversi.util.Point;
import jp.ac.ynu.pl2017.gg.reversi.util.Stone;

public class OthelloTest {

	private Othello othello;
	private Stone[][] defaultBoard = {
			{Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
			{Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
			{Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
			{Empty, Empty, Empty, Black, White, Empty, Empty, Empty},
			{Empty, Empty, Empty, White, Black, Empty, Empty, Empty},
			{Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
			{Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
			{Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty}};

	@Before
	public void setUp() {
		othello = new Othello(new PlayCallback() {

			@Override
			public void onTurnChange(boolean isMyTurn, int[] pCountStones) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void onOpponentUseItem() {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void onGameOver(int result) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void onGameAborted() {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void onGainItem(boolean playerTurn) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void enableItem() {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void disableItem() {
				// TODO 自動生成されたメソッド・スタブ

			}
		}, BaseAI.class, 0, true);
	}

	@Test
	public void testOthelloBoard() throws Exception {
        Class c = othello.getClass();
        Field fld = c.getDeclaredField("board");
        fld.setAccessible(true);

		assertArrayEquals(defaultBoard, (Stone[][])(fld.get(othello)));
	}

	@Test
	public void testOthelloItemPoints() throws Exception {
        Class c = othello.getClass();
        Field fld = c.getDeclaredField("itemPoints");
        fld.setAccessible(true);

        assertEquals(Othello.ITEM_COUNT, ((List<Point>)(fld.get(othello))).size());
	}

	@Test
	public void testActionPerformed() throws Exception {
		// sourceがnullだと例外を吐くので適当に指定
		ActionEvent event = new ActionEvent(new Object(), 10000, "2,4");
		Stone[][] newBoard = BoardHelper.cloneBoard(defaultBoard);
		newBoard[2][4] = Black;
		newBoard[3][4] = Black;

        Class c = othello.getClass();
        Field fld1 = c.getDeclaredField("buttonBoard");
        fld1.setAccessible(true);
        ((JButton[][])(fld1.get(othello)))[2][4].doClick();

        Field fld2 = c.getDeclaredField("board");
        fld2.setAccessible(true);
		assertArrayEquals(newBoard, (Stone[][])(fld2.get(othello)));
	}

	@Test
	public void testMakeHint() {
		List<Point> expected = new ArrayList<>();
		expected.add(new Point(2, 4));
		expected.add(new Point(3, 5));
		expected.add(new Point(4, 2));
		expected.add(new Point(5, 3));

		List<Point> actual = othello.makeHint(Black);

		assertEquals(expected, actual);
	}

	@Test
	public void testUseItemTriple() throws Exception {
		// アイテム使用後に石を置く
		othello.useItem(Item.TRIPLE);
		Class c = othello.getClass();
        Field fld1 = c.getDeclaredField("buttonBoard");
        fld1.setAccessible(true);
        ((JButton[][])(fld1.get(othello)))[2][4].doClick();

        Field fld2 = c.getDeclaredField("triplePoints");
        fld2.setAccessible(true);
        int actual = ((List<Point>)(fld2.get(othello))).size();

		assertEquals(1, actual);
	}

	@Test
	public void testReflectBan() throws Exception {
		List<Point> banPoints = new ArrayList<>();
		banPoints.add(new Point(0, 0));
		banPoints.add(new Point(1, 3));

		othello.reflectBan(banPoints);
        Class c = othello.getClass();
        Field fld = c.getDeclaredField("board");
        fld.setAccessible(true);
        Stone[][] board = (Stone[][])(fld.get(othello));

        assertEquals(Ban, board[0][0]);
		assertEquals(Ban, board[1][3]);
	}

	@Test
	public void testReflectDrop() throws Exception {
		Point dropPoint = new Point(3, 5);

		othello.reflectDrop(dropPoint);
        Class c = othello.getClass();
        Field fld = c.getDeclaredField("board");
        fld.setAccessible(true);
        Stone[][] board = (Stone[][])(fld.get(othello));

        assertEquals(Empty, board[dropPoint.getRow()][dropPoint.getColumn()]);
	}

	@Test
	public void testReflectGray() throws Exception {
		othello.reflectGray();

        Class c = othello.getClass();
        Field fld = c.getDeclaredField("grayTurn");
        fld.setAccessible(true);

        assertTrue((Integer)(fld.get(othello)) > 0);
	}

	@Test
	public void testReflectTriple() throws Exception {
		Point triplePoint = new Point(2, 4);

		othello.reflectTriple();
        Class c = othello.getClass();
        Field fld1 = c.getDeclaredField("buttonBoard");
        fld1.setAccessible(true);
        ((JButton[][])(fld1.get(othello)))[triplePoint.getRow()][triplePoint.getColumn()].doClick();
        Field fld2 = c.getDeclaredField("triplePoints");
        fld2.setAccessible(true);
        Point actual = ((List<Point>)(fld2.get(othello))).get(0);

        assertEquals(triplePoint, actual);
	}
}

package jp.ac.ynu.pl2017.gg.reversi.test;

import jp.ac.ynu.pl2017.gg.reversi.util.Offline;
import jp.ac.ynu.pl2017.gg.reversi.util.User;

public class UserDataDriver {
	
	public static void main(String[] args) {
		User lUser = new User();
		System.out.println("UserDriver Start");
		lUser.setUserName("9s");
		lUser.setOnlineWin(5);
		lUser.setOnlineLose(3);
		lUser.setBackground(3);
		lUser.setIcon(1);
		lUser.setItem(15);
		
		// オフライン戦績ダミー
		Offline lOffline = new Offline();
		lOffline.setEasyWin(7);
		lOffline.setEasyLose(0);
		lOffline.setNormalWin(4);
		lOffline.setNormalLose(2);
		lOffline.setHardWin(2);
		lOffline.setHardLose(3);
		
		// テスト済み
		lUser.setOfflines(new Offline[]{lOffline, new Offline(), new Offline(), new Offline()});
		
		System.out.printf("名前:%s\nオンライン勝敗:{%d/%d}\nアイコン設定:%d\n背景設定:%d\nアイテム使用回数:%d\n", 
				lUser.getUserName(),
				lUser.getOnlineWin(), lUser.getOnlineLose(),
				lUser.getIcon(),
				lUser.getBackground(),
				lUser.getItem());
		System.out.printf("オフライン戦績α:{{%d,%d},{%d,%d},{%d,%d}}\n",
				lOffline.getWLLists()[0][0], lOffline.getWLLists()[0][1],
				lOffline.getWLLists()[1][0], lOffline.getWLLists()[1][1],
				lOffline.getWLLists()[2][0], lOffline.getWLLists()[2][1]);
	}

}

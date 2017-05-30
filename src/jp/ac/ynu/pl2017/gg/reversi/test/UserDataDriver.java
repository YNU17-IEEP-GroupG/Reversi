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
		
		System.out.printf("getUserName():%s\n"
				+ "getOnlineWin()/Lose():{%d/%d}\n"
				+ "getIcon():%d\n"
				+ "getBackground():%d\n"
				+ "getItem():%d\n", 
				lUser.getUserName(),
				lUser.getOnlineWin(), lUser.getOnlineLose(),
				lUser.getIcon(),
				lUser.getBackground(),
				lUser.getItem());
		System.out.printf("getOfflines()[0]:{{%d,%d},{%d,%d},{%d,%d}}\n",
				lUser.getOfflines()[0].getWLLists()[0][0], lUser.getOfflines()[0].getWLLists()[0][1],
				lUser.getOfflines()[0].getWLLists()[1][0], lUser.getOfflines()[0].getWLLists()[1][1],
				lUser.getOfflines()[0].getWLLists()[2][0], lUser.getOfflines()[0].getWLLists()[2][1]);
	}

}

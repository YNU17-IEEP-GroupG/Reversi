package jp.ac.ynu.pl2017.gg.reversi.test;

import jp.ac.ynu.pl2017.gg.reversi.util.Offline;

public class OfflineDataDriver {
	
	public static void main(String[] args) {
		System.out.println("Offline Data Driver Start");
		
		Offline lOffline = new Offline();
		lOffline.setEasyWin(7);
		lOffline.setEasyLose(0);
		lOffline.setNormalWin(4);
		lOffline.setNormalLose(2);
		lOffline.setHardWin(2);
		lOffline.setHardLose(3);
		
		System.out.printf("[弱い]勝敗:%d/%d, [普通]勝敗:%d/%d, [強い]勝敗:%d/%d\n",
				lOffline.getEasyWin(), lOffline.getEasyLose(),
				lOffline.getNormalWin(), lOffline.getNormalLose(),
				lOffline.getHardWin(), lOffline.getHardLose());
		System.out.printf("取得用配列:{{%d,%d},{%d,%d},{%d,%d}}",
				lOffline.getWLLists()[0][0], lOffline.getWLLists()[0][1],
				lOffline.getWLLists()[1][0], lOffline.getWLLists()[1][1],
				lOffline.getWLLists()[2][0], lOffline.getWLLists()[2][1]);
	}

}

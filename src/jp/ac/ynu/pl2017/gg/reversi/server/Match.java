package jp.ac.ynu.pl2017.gg.reversi.server;

public class Match {
	private int room;
	private String myName;
	private String enemyName;
	
	Match(int r,String m,String e){
		room = r;
		myName = m;
		enemyName = e;
	}
	
	public int getRoom(){
		return room;
	}
	
	public String getMyName(){
		return myName;
	}
	
	public String getEnemyName(){
		return enemyName;
	}
}

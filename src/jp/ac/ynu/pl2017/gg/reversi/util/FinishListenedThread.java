package jp.ac.ynu.pl2017.gg.reversi.util;

public abstract class FinishListenedThread extends Thread {
	
	public interface ThreadFinishListener {
		public void onThreadFinish(Object pCallbackParam);
	}
	
	private ThreadFinishListener listener;
	
	public FinishListenedThread(ThreadFinishListener pListener) {
		listener = pListener;
	}
	
	@Override
	public final void run() {
		listener.onThreadFinish(doRun());
	}
	
	public abstract Object doRun();

}

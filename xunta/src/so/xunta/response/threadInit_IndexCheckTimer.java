package so.xunta.response;

public class threadInit_IndexCheckTimer implements Runnable {
	//static long lastTimeofSearcher = System.currentTimeMillis();
	static long intervalIndexCheck = 5000;
	
	public void run(){
		Thread.yield();
		SearchMethods.IndexCheckTimer(); 
	}
}

package simple.run;

public class Timer {
	private static long timeAtLastCall = 0;
	private static int framesPassed = 0;
	
	public static int getFramesPassed() { return framesPassed; }
	
	// Directly delays program for exact amount of time
	public static void delay(int delayMillis) {
		try {
			Thread.sleep(delayMillis);
			framesPassed += 1;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	// Indirect delay which takes into account its last call. Evens out delaying for computers of different speed
	public static void correctedDelay(int delayMillis) {
		try {
			Thread.sleep(Math.max(delayMillis - (System.nanoTime()-timeAtLastCall)/1000000, 0));
			timeAtLastCall = System.nanoTime();
			framesPassed += 1;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

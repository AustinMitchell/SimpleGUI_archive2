package simple.run;

/** Static class that allows you to delay the program **/
public class Timer {
	private static long timeAtLastCall = 0;
	
	public static long latestTimePollMillis() { return timeAtLastCall/1000000L; }
		
	/** Delays the program for the exact amount of milliseconds given **/
	public static void delay(int delayMillis) {
		try {
			Thread.sleep(delayMillis);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/** Delays the program for the amount of milliseconds given, adjusted by the last call to this function.
	 * e.g.: If you call this function, do something for 20 milliseconds and then call correctedDelay(30), it will delay for 10 milliseconds
	 * Allows computers of different speeds to run a program in roughly the same pace; Guarantees programs will take at least delayMillis for the frme to pass **/
	public static void correctedDelay(int delayMillis) {
		try {
			Thread.sleep(Math.max(delayMillis - (System.nanoTime()-timeAtLastCall)/1000000, 0));
			timeAtLastCall = System.nanoTime();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

Simple to use, loop-based graphics library for java. Based on swing, it combines the idea of GUI application software like Swing with easy to use graphics like Processing into one java library.

Here is a basic program:

```java
public class Main extends SimpleGUIApp {
	public static void main(String[]args) { Main.start(new Main(), "Test Program"); } // Boilerplate code to start program
	public Main() { super(1025, 1025, 30); } // More boilerplate, arguments are screen dimensions and frames per second desired

	public void setup {
		// All initialization should be done here
	}
	public void loop {
		// This method will loop
	}
}
```
package testers;
import simple.run.*;

public class EmptyApp extends SimpleGUIApp {
    public static void main(String[]args) { EmptyApp.start(new EmptyApp(), "Test Program"); } // Boilerplate code to start program
    public EmptyApp() { super(1025, 1025, 30); } // More boilerplate, arguments are screen dimensions and frames per second desired

    public void setup() {
        // All initialization should be done here
    }
    public void loop() {
        // This method will loop until program ends (with a function call or by closing the window)
    }
}

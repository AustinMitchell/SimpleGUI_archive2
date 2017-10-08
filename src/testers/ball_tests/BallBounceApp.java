package testers.ball_tests;

import java.awt.Color;

import simple.gui.Draw;
import simple.run.SimpleGUIApp;

public class BallBounceApp extends SimpleGUIApp {
    public static void main (String[] args) { start(new BallBounceApp(), "Test"); }
    public BallBounceApp() { super(500, 500, 60); }
    
    int x, y, dx, dy;
    Color blue, darkblue;

    public void setup() {
        // initial ball position
        x = 200;
        y = 100;
        
        // Randomized directions
        dx = (int)(Math.random()*20) - 10;
        dy = (int)(Math.random()*20) - 10;
        
        // Storing colors so we don't have to recompute the value every iteration
        blue = new Color(0, 0, 255);
        darkblue = new Color(0, 0, 180);
    }
    public void loop() {
        // Adjust ball position
        x += dx;
        y += dy;
        
        // Make sure it doesn't go out of screen, and reverse the direction if it does
        if (x > getWidth()-20 || x < 20) {
            dx *= -1;
        }
        if (y > getHeight()-20 || y < 20) {
            dy *= -1;
        }
        
        // Set the fill color for drawing
        Draw.setFill(blue);
        // Set the stroke color for drawing
        Draw.setStroke(darkblue);
        // Draw the oval. Fill is the inside, stroke is the outer bound of the ball
        Draw.ovalCentered(x, y, 20, 20);
        /* oval() and ovalCentered() do different things: 
            oval() takes in the top-left corner and x and y diameters
            ovalCentered() takes in the center and the x and y radii */
        
        // Every frame update out view. All draw commands are put onto a buffer until switched.
        updateView();
    }
}

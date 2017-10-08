package testers.ball_tests;

import java.awt.Color;

import simple.gui.*;
import simple.run.*;
import simple.misc.Vector;

public class BallInputApp extends SimpleGUIApp {
    public static void main (String[] args) { start(new BallInputApp(), "Test"); }
    public BallInputApp() { super(1000, 500, 60); }
    
    int speed;
    Vector pos, dir, acc; // Position, Direction, Acceleration
    Color blue, darkblue;
    double accelerationRatio;

    public void setup() {
        // Set initial position, direction and acceleration values
        pos = new Vector(200, 200);
        dir = new Vector(0, 0);
        acc = new Vector(0, 0);
        // in each frame this value represents the percentage of speed the direction vector keeps. The other percent is given by
        // the acc vector.
        accelerationRatio = 0.95;
        
        // Set acceleration value
        speed = 20;
        
        // Set color values so we don't need to recompute them
        blue = new Color(0, 0, 255);
        darkblue = new Color(0, 0, 180);
    }
    public void loop() {
        // Check if either the key 'w' or 's' is pressed, but not both
        if (!(Input.keyDown('w') ^ Input.keyDown('s'))) {
            // If both or neither are set, stop accelerating y position
            acc.y = 0;
        } else {
            // Otherwise accelerate either up or down. Y axis is reversed on computer screens, 0 is the top of the screen
            if (Input.keyDown('w')) {
                acc.y = -speed;
            } else if (Input.keyDown('s')) {
                acc.y = speed;
            }
        }
        // Check if either the key 'a' or 'd' is pressed, but not both
        if (!(Input.keyDown('a') ^ Input.keyDown('d'))) {
            // If both or neither are set, stop accelerating x position
            acc.x = 0;
        } else {
            // Otherwise accelerate either left or right
            if (Input.keyDown('a')) {
                acc.x = -speed;
            } else if (Input.keyDown('d')) {
                acc.x = speed;
            }
        }
        
        // Adjust the dir by a certain portion depending on the acceleration ratio.
        dir = dir.mult(accelerationRatio).add(acc.mult(1-accelerationRatio));
        // Add the direction to the position.
        pos = pos.add(dir);
        
        // Make sure The ball doesn't go out of bounds. Otherwise....
        if (pos.x > getWidth()-20 || pos.x < 20) {
            // Set the x position
            pos.x = Math.min(Math.max(20, pos.x), getWidth()-20);
            // reverse the x direction
            dir = dir.mult(-1, 1);
        }
        // Make sure The ball doesn't go out of bounds. Otherwise....
        if (pos.y > getHeight()-20 || pos.y < 20) {
            // Set the y position
            pos.y = Math.min(Math.max(20, pos.y), getHeight()-20);
            // reverse the y direction
            dir = dir.mult(1, -1);
        }
        
        // Set fill color
        Draw.setFill(blue);
        // Set stroke color
        Draw.setStroke(darkblue);
        // Draw an oval using the fill for the inside and stroke for the border
        Draw.ovalCentered(pos.x(), pos.y(), 20, 20);
        
        // Take the buffered image and put it on the screen
        updateView();
    }
}

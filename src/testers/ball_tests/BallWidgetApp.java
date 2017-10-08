package testers.ball_tests;

import java.awt.Color;
import java.awt.Font;

import simple.gui.scrollbox.*;
import simple.gui.*;
import simple.run.*;
import simple.misc.*;

public class BallWidgetApp extends SimpleGUIApp {
    public static void main (String[] args) { start(new BallWidgetApp(), "Test"); }
    public BallWidgetApp() { super(1000, 500, 60); }
    
    Button exitButton;
    Label sliderLabel;
    Slider speedSlider;
    ScrollListBox<Button> colorSelect;
    
    int speed;
    Vector pos, dir, acc; // Position, Direction, Acceleration
    Color ballColor;
    double accelerationRatio;

    public void setup() {
        // Set the initial position, direction and acceleration values for the ball
        pos = new Vector(200, 200);
        dir = new Vector(0, 0);
        acc = new Vector(0, 0);
        // in each frame this value represents the percentage of speed the direction vector keeps. The other percent is given by
        // the acc vector.
        accelerationRatio = 0.95;
        
        speed = 10;
        
        // Creating a button widget.
        // Setting various color parameters
        // Setting font as well
        exitButton = new Button(940, 10, 50, 20, "Exit");
        exitButton.setFillColor(Color.RED);
        exitButton.setBorderColor(Color.WHITE);
        exitButton.setTextColor(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.PLAIN, 10));
        
        // Creating a scrolllist widget
        // This kind of scrolllist has a widget at each spot. This widget will be used to choose the color of the ball
        colorSelect = new ScrollListBox<Button>(650, 10, 250, 300, 5);
        colorSelect.setWidgetColorsDefaultToScrollBox(true);
        colorSelect.setFillColor(new Color(220, 220, 220));
        Color[] colors = {Color.white, Color.gray, Color.black, Color.blue, Color.red, Color.green, Color.pink, Color.cyan, Color.magenta, Color.orange, Color.yellow};
        // Setting all the widgets in the scroll box. Each one is a button of a different color
        for (Color c: colors) {
            Button b = new Button(null, null);
            b.setFillColor(c);
            colorSelect.addWidget(b);
        }
        
        // Creating a new label. Just exists to display text.
        sliderLabel = new Label(650, 370, 310, 30, "Current speed: " + speed);
        sliderLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        
        // Creating a slider widget. This slider will store the acceleration value on keypresses, and can be adjusted from 2 to 30.
        speedSlider = new Slider(650, 420, 310, 40, 2, 30, false, true);
        speedSlider.setFillColor(Color.black);
        speedSlider.setBorderColor(Color.white);
        speedSlider.setValue(speed);
        
        // Storing initial ball color
        ballColor = new Color(0, 0, 255);
    }
    public void loop() {
        // Call update functions. Usually updating is good to do before drawing.
        updateBall();
        updateWidgets();
        
        // Drawing ball
        Draw.setFill(ballColor);
        Draw.setStroke(Color.black);
        Draw.ovalCentered(pos.x(), pos.y(), 20, 20);
        
        // Drawing a panel that widgets will be drawn on. Purely aesthetics
        Draw.setFill(Color.white);
        Draw.setStroke(null);
        Draw.rect(600, 0, 400, getHeight());
        
        // Draw all the widgets to their relative positions
        exitButton.draw();
        colorSelect.draw();
        speedSlider.draw();
        sliderLabel.draw();
        
        // Update screen using the drawing backbuffer
        updateView();
    }
    void updateBall() {
        // Check if either the w or s keys are pressed, and set the speed
        if (!(Input.keyDown('w') ^ Input.keyDown('s'))) {
            acc.y = 0;
        } else {
            if (Input.keyDown('w')) {
                acc.y = -speed;
            } else if (Input.keyDown('s')) {
                acc.y = speed;
            }
        }
        if (!(Input.keyDown('a') ^ Input.keyDown('d'))) {
            acc.x = 0;
        } else {
            if (Input.keyDown('a')) {
                acc.x = -speed;
            } else if (Input.keyDown('d')) {
                acc.x = speed;
            }
        }
        
        // Update ball position and direction vectors
        dir = dir.mult(accelerationRatio).add(acc.mult(1-accelerationRatio));
        pos = pos.add(dir);
        
        // Keep ball in bounds
        if (pos.x > 600-20 || pos.x < 20) {
            pos.x = Math.min(Math.max(20, pos.x), 600-20);
            dir = dir.mult(-1, 1);
        }
        if (pos.y > getHeight()-20 || pos.y < 20) {
            pos.y = Math.min(Math.max(20, pos.y), getHeight()-20);
            dir = dir.mult(1, -1);
        }
    }
    void updateWidgets() {
        // update widgets. This will change appearance, update their state relative to the mouse, and other things depending on the widget
        // Ex. the scrollbox will update all the widgets it stores.
        exitButton.update();
        colorSelect.update();
        speedSlider.update();
        sliderLabel.update();
        
        // Exit if the exit button state is clicked
        if (exitButton.isClicked()) {
            quit();
        }
        
        // If the slider has changed, update the speed with the current slider value
        if (speedSlider.valueHasChanged()) {
            speed = speedSlider.getValue();
            // update label text with proper speed value
            sliderLabel.setText("Current Speed: " + speed);
        }
        // Check the widgets of the scrolllist if they've been clicked. If one has, then update the ball color
        for (int i=0; i<colorSelect.getNumWidgets(); i++) {
            Button b = colorSelect.getWidget(i);
            if (b.isClicked()) {
                ballColor = b.getFillColor();
            }
        }
    }
}

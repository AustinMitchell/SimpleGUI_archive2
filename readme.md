Simple to use, loop-based graphics library for java. Based on swing, it combines the idea of GUI application software like Swing with easy to use graphics like Processing 2 into one java library.

The example programs may be found under src/testers.

#### src/testers/EmptyApp.java
```java

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
```

The main() and Main() should both only have one line in them, these are to initialize the window (small things can be configured, which will be added into the Javadocs). For the program, use setup to initialize and loop to run whatever you want

### Basic Drawing

It comes with some drawing capabilities. The 'Draw' class is part of the simple.gui package, which contains functions for drawing text, images and shapes onto the screen or images.

Here is a basic drawing example. This will start with an initial x and y position, as well as random directions. When the ball hits a wall, it will bounce off of it by reversing the corresponding direction:

#### src/testers/ball_tests/BallBounceApp.java
```java

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
```

This is the basic idea for the program. You state all your variables at the top, initialize in the setup, and update/draw in the loop. Note that setFill() and setStroke() don't need to be called on every draw command, but since the members of Draw are all static any calls to Draw commands can overwrite eachother.

### User Input / Vector class

We also have keyboard and mouse input. Here's a version of the ball game which uses WASD for movement. It will also introduce another class added to SimpleGUI, Vectors:

#### src/testers/ball_tests/BallInputApp.java
```java

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
```

This is one of the few facets of the Input class: In this program, keyDown(char c) will determine whether c is being pressed down or not. Alternatively, you can also use key codes in java.awt.event.KeyEvent for non character keys, such as backspace, shift, etc. Input can also be used for mouse, getting the last character typed, and more.

Vector is essentially just a pair of integers, x and y, with a variety of functions around them (such as adding vectors, multiplying by scalars, etc.)

### Widgets

One of the core features of this libraries is the widgeting system. There is an array of built in widgets, such as buttons, sliders and scrollboxes, and you can make your own as well. Here is some basic functionality that combines a few types of widgets together:

#### src/testers/ball_tests/BallWidgetApp.java
```java

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
```

Essentially all this adds is the ability to exit with a button on screen, change the ball's color and change the maximum speed. All widgets have an update() and draw() function, and various other functions to change the state of the widget (position, size, color, etc.)

Here's an overview of widgets used in this program:

**Button:** A square that changes color slightly when you hover over and click it. All widgets can be clicked and have the isClicked(), isClicking() and isHovering() functions, but Button in particular is intended to display differently depending on the mouse state relative to the button, as well as an image overlaying the button and text if you choose. In this case, if the button is clicked it will trigger the quit() function which is the same as exiting the program.

**Slider:** You give it a range and an orientation (you can tell it if it is reversed and if it's horizontal, as those things change the display and how it determines the value), and it will keep track of the relative position on the slider you clicked at. For instance, in this example, it has a range from 2 to 30, and if you click right in the middle it will change to about 16 or 17. It has functions to tell you if the value has been recently changed, which is used to fix the speed of th ball here. You can use the mouse to set the value, use your mouse wheel, or call a function which will manually change the value from code.

**ScrollListBox<WidgetType>:** This is a bit more complex. There's a ScrollDialogBox which displays lines of text that you can scroll through. This one though has a list of widgets that it uses for display. It can only use objects that subclass from widget. For instance, in our program we have a scrollbox of Buttons, and so the scrollbox treats all the items as buttons without us having to cast it. We first make a list of colours and add new buttons to the scrollbox with a different fill color. In our case we go through the list of widgets and check to see if any have been clicked. If they have, then we set the color of the ball to the fill color that was clicked. Note that for this widget, it automatically sets the size and position of buttons for us relative to the containing scrollbox. Like the slider, the scrolllist can be navigated using the mouse wheel. It also has a slider and a pair of buttons to scroll through the list.

**Label:** The label simply displays text. It is bounded to a specified box, and can be set to display whatever string you want. You can also set the text alignment at the center of the box or one of 8 directions, depending on the desired appearance.

### Hex Arrays and Hex Widgets

As of October 2017, support has been added for working with hex coordinates and hexagon widgets. The inspiration for the core of the design can be found here: (Hexagonal Grids)[http://www.redblobgames.com/grids/hexagons/]

There are two classes that are intended to be used with hex arrays: *HexWidget* and *HexButton*. *HexWidget* provides a scaffold for making your own hex widgets, and does all the calculating legwork for you already. This class is abstract, and is meant to be used to produce custom hexwidgets and create an umbrella for making hex widgets generic (a useful tool depending on the case). It also custom defines mouse interaction with the widget so you don't need to figure that out yourself. Only pointy-top and flat-top orientations are currently supported. There is a concrete widget *HexButton*, which works much like a square button, but without the ability to use images on the button currently.

This is an example that combines the use of HexArrays and HexWidgets together:

```java

package testers;

import java.awt.Color;

import simple.run.*;
import simple.gui.*;
import simple.misc.*;
import simple.misc.hex.*;

public class HexTest extends SimpleGUIApp {
    public static void main(String[]args) { start(new HexTest(), "Hex Tester"); }
    public HexTest() {super(800, 800, 60);}

    // Sets hex orientation, pointy-top or flat-top
    final static HexWidget.HexType hexType = HexWidget.HexType.POINT_TOP;
    // This will be used in the hex array to switch between 2D and 3D hex coordinates
    final static HexData.CoordinateConverter coord = (hexType == HexWidget.HexType.FLAT_TOP) ?
                                                               HexData.FLAT_TOP_COORD :
                                                               HexData.POINT_TOP_COORD;
    // determines offset of the grid for 2D coordinates.
    final static int even = 1;
    // Width of grid if using a 2D array system
    final static int width = 8;
    // Height of grid if using a 2D array system
    final static int height = 8;
    // Radius of the drawn hexagons. Used for the hex widgets themselves
    final static int hex_draw_radius = 20;
    // If using a radial hex array, determines the radius in terms of number of hexes from the center
    final static int hex_grid_radius = 10;
    // used for color display for each hex widget
    final static int grid_color_offset = 3;
    // Used to draw the background line grid
    final static int gridline_radius = hex_draw_radius;
    
    // This point will be the center of the hex at (0, 0), or (0, 0, 0) in 3D
    final static int offsetx = 400;
    final static int offsety = 400;
    
    // When you click on a hex the 2D and 3D coordinates will display on the label
    Label hexLabel;
    // Hex array collection object
    HexArray<HexButton> hexArray;
    // Will be used to determine where to place each hexagon. Uses the 3D coordinates to determine position
    Vector[] scaledAxes = {
            null,
            null,
            null
    };
       
    @Override
    public void setup() {
        // Using the radial hex constructor. When you iterate through this array, it will also iterate in radial form
        hexArray = new HexArray<HexButton>(hex_grid_radius, even, coord);
        
        hexLabel = new Label(100, 10, 200, 50);
        // The containing box for the label is normally not drawn unless specifically requested
        hexLabel.setBoxIsVisible(true);
        
        // Sets the 3 coordinate directions depending whether its a pointy-top or flat-top hexagon.
        // Scales each vector by the size of the hexagon radius
        scaledAxes[0] = new Vector(hexArray.getCoordConv().getAxisVectors()[0]).mult(hex_draw_radius);
        scaledAxes[1] = new Vector(hexArray.getCoordConv().getAxisVectors()[1]).mult(hex_draw_radius);
        scaledAxes[2] = new Vector(hexArray.getCoordConv().getAxisVectors()[2]).mult(hex_draw_radius);
        
        // Iterate through all the hexes. 
        for (HexData<HexButton> hex: hexArray) {
            // Get the 3D coordinate for the current hex
            Tuple cube = hex.getCubeIndex();
            // This vector will determine the visual offset for each hex when drawn
            Vector shift = new Vector(0, 0);
            for (int axis=0; axis<3; axis++) {
                // Takes each component of the cube coordinates and multiplies it by the same base vector component
                // Adds it to the shift
                shift = shift.add(scaledAxes[axis].mult(cube.get(axis)));
            }
            
            // Create a new hexbutton with this data
            HexButton newHex;
            newHex = new HexButton(offsetx + (int)shift.x(), offsety + (int)shift.y(), hex_draw_radius, hexType);
            
            // Colors each hex. High component 0 is red, 1 is green and 2 is blue. Makes a kind of color wheel.
            float dist_ratio = (255f/(hex_grid_radius+grid_color_offset));
            int r = (int)(Math.max(cube.get(0)+grid_color_offset, 0) * dist_ratio);
            int g = (int)(Math.max(cube.get(1)+grid_color_offset, 0) * dist_ratio);
            int b = (int)(Math.max(cube.get(2)+grid_color_offset, 0) * dist_ratio);
            newHex.setFillColor(new Color(r, g, b));
            // By default every entry in the array is null. This sets the value to the new hexbutton.
            hex.setData(newHex);
        }
    }

    @Override
    public void loop() {
        // Draw the gridlines
        for (int i=0; i*gridline_radius < Math.max(getWidth(), getHeight()); i++) {
            Draw.setStroke(Color.BLACK);
            Draw.line(0, i*gridline_radius, getWidth(), i*gridline_radius);
            Draw.line(i*gridline_radius, 0, i*gridline_radius, getHeight());
        }
        
        // update and draw the hexes
        for (HexData<HexButton> hb: hexArray) {
            hb.getData().update();
            hb.getData().draw();
            
            // If a hex is clicked, update the label with the coordinates of that hexagon
            if (hb.getData().isClicked()) {
                hexLabel.setText(hb.getBaseIndex() + " | " + hb.getCubeIndex());
            }
        }
        
        hexLabel.draw();
        
        updateView();
    }

}
```

The hex array stores references to parameterized *HexData* objects, which store a piece of data (a *HexButton*, in this case), as well as the corresponding 2D and 3D coordinates. The 2D coordinate is useful normally for construction. The 3D coordinate is much more important as we can use this to determine where hexes are relative to eachother, draw them, get distances, create paths, walk directions, etc. Just like a 2D array can be navigated in 4 directions using 2 coordinates, a *HexArray* can be navigated in 6 directions using pairs from 3 coordinates. A link with more information can be found above the code example.

This program creates a radial hex array, which is an array where the (0, 0, 0) coordinate is at the center and the rule is that given radius *r* and coordinate (*x, y, z*): `(abs(x)+abs(y)+abs(z)/2 <= r`

The higher the hex is in a particular coordinate, the higher its corresponding color value is. In this case *x* maps to red, *y* maps to green, and *z* maps to blue. First we figure out what the hexes centerpoint is going to be in the screen given its cube coordinate, we color it, then place it in the array. Since the array by default stores a bunch of *HexData* objects with data set to null, this step is important. Finally, we draw a linegrid in the background, then draw the hexes, and check if a hex has been clicked. If it has, update the label with the coordinate data of that particular *HexButton*.
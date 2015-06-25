Simple to use, loop-based graphics library for java. Based on swing, it combines the idea of GUI application software like Swing with easy to use graphics like Processing 2 into one java library.

Here is a basic program:

```java
import simple.run*;

public class Main extends SimpleGUIApp {
	public static void main(String[]args) { Main.start(new Main(), "Test Program"); } // Boilerplate code to start program
	public Main() { super(1025, 1025, 30); } // More boilerplate, arguments are screen dimensions and frames per second desired

	public void setup {
		// All initialization should be done here
	}
	public void loop {
		// This method will loop until program ends (with a function call or by closing the window)
	}
}
```

The main() and Main() should both only have one line in them, these are to initialize the window (small things can be configured, which will be added into the Javadocs). For the program, use setup to initialize and loop to run whatever you want

It comes with some drawing capabilities. The 'draw' variable is part of SimpleGUIApp, which is essentially another way to call the static DrawModule class.

For example: This program simulates a ball bouncing around the screen:

```java
import java.awt.Color;
import simple.run.*;

public class Test extends SimpleGUIApp {
	public static void main (String[] args) { start(new Test(), "Test"); }
	public Test() { super(500, 500, 60); }
	
	int x, y, dx, dy;
	Color blue, darkblue;

	public void setup() {
		x = 200;
		y = 100;
		dx = (int)(Math.random()*20) - 10;
		dy = (int)(Math.random()*20) - 10;
		
		blue = new Color(0, 0, 255);
		darkblue = new Color(0, 0, 180);
	}
	public void loop() {
		x += dx;
		y += dy;
		
		if (x > getWidth()-20 || x < 20) {
			dx *= -1;
		}
		if (y > getHeight()-20 || y < 20) {
			dy *= -1;
		}
		
		draw.setFill(blue);
		draw.setStroke(darkblue);
		draw.ovalCentered(x, y, 20, 20);
		/* oval() and ovalCentered() do different things: 
			oval() takes in the top-left corner and x and y diameters
			ovalCentered() takes in the center and the x and y radii */
		
		updateView();
	}
}
```

This is the basic idea for the program. You state all your variables at the top, initialize in the setup, and update/draw in the loop. Note that setFill() and setStroke() don't necessarily need to be called every single time, but the variables inside DrawModule are all static, so any call to those functions will affect it anywhere else they're used.

We also have input. Here's a version of the ball game which uses WASD for movement. It will also introduce another class added to SimpleGUI, Vectors:

```java
import java.awt.Color;

import simple.gui.*;
import simple.run.*;

public class Test extends SimpleGUIApp {
	public static void main (String[] args) { start(new Test(), "Test"); }
	public Test() { super(1000, 500, 60); }
	
	int speed;
	Vector pos, dir, acc; // Position, Direction, Acceleration
	Color blue, darkblue;

	public void setup() {
		pos = new Vector(200, 200);
		dir = new Vector(0, 0);
		acc = new Vector(0, 0);
		
		speed = 10;
		
		blue = new Color(0, 0, 255);
		darkblue = new Color(0, 0, 180);
	}
	public void loop() {
		if (!(input.keyDown('w') ^ input.keyDown('s'))) {
			acc.y = 0;
		} else {
			if (input.keyDown('w')) {
				acc.y = -speed;
			} else if (input.keyDown('s')) {
				acc.y = speed;
			}
		}
		if (!(input.keyDown('a') ^ input.keyDown('d'))) {
			acc.x = 0;
		} else {
			if (input.keyDown('a')) {
				acc.x = -speed;
			} else if (input.keyDown('d')) {
				acc.x = speed;
			}
		}
		
		dir = dir.mult(0.9).add(acc.mult(0.1));
		pos = pos.add(dir);
		
		if (pos.x > getWidth()-20 || pos.x < 20) {
			pos.x = Math.min(Math.max(20, pos.x), getWidth()-20);
		}
		if (pos.y > getHeight()-20 || pos.y < 20) {
			pos.y = Math.min(Math.max(20, pos.y), getHeight()-20);
		}
		
		draw.setFill(blue);
		draw.setStroke(darkblue);
		draw.ovalCentered(pos.x(), pos.y(), 20, 20);
		
		updateView();
	}
}
```

This is one of the few facets of the Input class: In this program, keyDown(char c) will determine whether c is being pressed down or not. Alternatively, you can also use key codes in java.awt.event.KeyEvent for non character keys, such as backspace, shift, etc. Input can also be used for mouse, getting the last character typed, and more.

Vector is essentially just a pair od ints, x and y, with a variety of functions around them (such as adding vectors, multiplying by scalars, etc.)

The final thing I'll showcase here is the Widgets. There's a fine array of built in widgets, such as buttons, sliders and scrollboxes, and you can make your own as well. Here is some basic functionality:

```java
import java.awt.Color;
import java.awt.Font;

import simple.gui.*;
import simple.run.*;

public class Test extends SimpleGUIApp {
	public static void main (String[] args) { start(new Test(), "Test"); }
	public Test() { super(1000, 500, 60); }
	
	Button exitButton;
	Label sliderLabel;
	Slider speedSlider;
	ScrollListBox<Button> colorSelect;
	
	int speed;
	Vector pos, dir, acc; // Position, Direction, Acceleration
	Color ballColor;

	public void setup() {
		pos = new Vector(200, 200);
		dir = new Vector(0, 0);
		acc = new Vector(0, 0);
		
		speed = 10;
		
		exitButton = new Button(940, 10, 50, 20, "Exit");
		exitButton.setFillColor(Color.RED);
		exitButton.setBorderColor(Color.WHITE);
		exitButton.setTextColor(Color.WHITE);
		exitButton.setFont(new Font("Arial", Font.PLAIN, 10));
		
		colorSelect = new ScrollListBox<Button>(650, 10, 250, 300, 5);
		colorSelect.setWidgetColorsDefaultToScrollBox(true);
		colorSelect.setFillColor(new Color(220, 220, 220));
		Color[] colors = {Color.white, Color.gray, Color.black, Color.blue, Color.red, Color.green, Color.pink, Color.cyan, Color.magenta, Color.orange, Color.yellow};
		for (Color c: colors) {
			Button b = new Button(null, null);
			b.setFillColor(c);
			colorSelect.addWidget(b);
		}
		
		sliderLabel = new Label(650, 370, 310, 30, "Current speed: " + speed);
		sliderLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		sliderLabel.setIsCentered(true);
		
		speedSlider = new Slider(650, 420, 310, 40, 2, 30, false, true);
		speedSlider.setFillColor(Color.black);
		speedSlider.setBorderColor(Color.white);
		speedSlider.setValue(speed);
		
		ballColor = new Color(0, 0, 255);
	}
	public void loop() {
		updateBall();
		updateWidgets();
		
		draw.setFill(ballColor);
		draw.setStroke(Color.black);
		draw.ovalCentered(pos.x(), pos.y(), 20, 20);
		
		draw.setFill(Color.white);
		draw.setStroke(null);
		draw.rect(600, 0, 400, getHeight());
		
		exitButton.draw();
		colorSelect.draw();
		speedSlider.draw();
		sliderLabel.draw();
		
		updateView();
	}
	void updateBall() {
		if (!(input.keyDown('w') ^ input.keyDown('s'))) {
			acc.y = 0;
		} else {
			if (input.keyDown('w')) {
				acc.y = -speed;
			} else if (input.keyDown('s')) {
				acc.y = speed;
			}
		}
		if (!(input.keyDown('a') ^ input.keyDown('d'))) {
			acc.x = 0;
		} else {
			if (input.keyDown('a')) {
				acc.x = -speed;
			} else if (input.keyDown('d')) {
				acc.x = speed;
			}
		}
		
		dir = dir.mult(0.9).add(acc.mult(0.1));
		pos = pos.add(dir);
		
		if (pos.x > 600-20 || pos.x < 20) {
			pos.x = Math.min(Math.max(20, pos.x), 600-20);
		}
		if (pos.y > getHeight()-20 || pos.y < 20) {
			pos.y = Math.min(Math.max(20, pos.y), getHeight()-20);
		}
	}
	void updateWidgets() {
		exitButton.update();
		colorSelect.update();
		speedSlider.update();
		sliderLabel.update();
		
		if (exitButton.isClicked()) {
			quit();
		}
		if (speedSlider.valueHasChanged()) {
			speed = speedSlider.getValue();
			sliderLabel.setText("Current Speed: " + speed);
		}
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

This is not all the widgets made to use, but these here are a few key ones:

Button: Basic widget. It's a square that changes color slightly when you hover over and click it. All widgets can be clicked and have the isClicked(), isClicking() and isHovering() functions, but Button in particular displays it, as well as an image overlaying the button and text if you choose. In this case, if the button is clicked it will trigger the quit() function which is the same as exiting the program.

Slider: You give it a range and an orientation (you can tell it if it is reversed and if it's horizontal, as those things change the display and how it determines the value), and it will keep track of the relative position on the slider you clicked at. For instance, in this example, it has a range from 2 to 30, and if you click right in the middle it will change to about 16 or 17. It has functions to tell you if the value has been recently changed, which is used to fix the speed of th ball here.

ScrollListBox<WidgetType>: This is a bit more complex. There's a ScrollDialogBox which displays lines of text that you can scroll through. This one though has a list of widgets that it uses for display. It can only use objects that are of type widget. For instance, in our program we have a scrollbox of Buttons, and so the scrollbox treats all the items as buttons without us having to cast it. We first make a list of colours and add new buttons to the scrollbox with a different fill color. In our case we go through the list of widgets and check to see if any have been clicked. If they have, then we set the color of the ball to the fill color that was clicked. Note that for this widget, it automatically sets the size and position of buttons for us.

Label: The label simply displays text. It is bounded to a specified box, and can be set to display the lines centered.


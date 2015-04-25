package simple.run;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

import simple.gui.*;

public class Test extends SimpleGUIApp{
	public static void main(String[] args) { Test.start(new Test(500, 500, 30), "Test Program"); }
	public Test(int width, int height, int fps) { super(width, height, fps); }
	
	Button button;
	
	@Override
	public void setup() {
		button = new Button(100, 300, 100, 50, "Click me!", null);
	}

	@Override
	public void loop() {
		button.update();
		if (button.isClicked()) {
			draw.setStroke(new Color(0, 0, 0));
			draw.text("You clicked it!", new Font("Arial", Font.BOLD, 20), 20, 30);
		}
		
		button.draw();
		
		draw.setColors(new Color(100, 100, 255, 128), null);
		draw.ovalCentered(input.mouseX(), input.mouseY(), 50, 50);
		
		updateView();
	}

}

package simple.run;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

import simple.gui.*;

public class Test extends SimpleGUIApp{
	public static void main(String[] args) { Test.start(new Test(500, 500, 30), "Test Program"); }
	public Test(int width, int height, int fps) { super(width, height, fps); }

	String text;
	Button button;
	Vector v;
	TextBox textBox;
	
	@Override
	public void setup() {
		text = "Hi";
		v = new Vector(100, 0, true);
		button = new Button(30, 400, 150, 70, "Click me!", null);
		button.setFillColor(new Color(220, 220, 30));
		button.setBorderColor(new Color(180, 90, 0));
		button.setTextColor(new Color(0, 0, 0));
		
		textBox = new TextBox(350, 50, 120, 70);
		textBox.setFont(new Font("Arial", Font.PLAIN, 12));
	}

	@Override
	public void loop() {
		button.update();
		textBox.update();
		
		if (input.keyDown('a') || input.keyDown(KeyEvent.VK_LEFT) || button.isClicking()) {
			draw.setFill(Color.black);
			draw.oval(50, 50, 20, 20);
		}
		if (input.getChar() <=127 && input.getChar() >= 32) {
			text += input.getChar();
		}
		if (input.getChar() == KeyEvent.VK_BACK_SPACE && text.length() > 0) {
			text = text.substring(0, text.length()-1);
		}
		draw.setStroke(Color.white);
		draw.text(text, 50, 200);
		
		button.draw();
		textBox.draw();
		
		draw.setStroke(new Color(100, 200, 255), 4);
		draw.line(400, 400, 400+v.getX(), 400+v.getY());
		v = v.rotate(Math.PI/90);
		
		draw.setStroke(new Color (255, 50, 255), 1);
		draw.setFill(new Color (255, 50, 255));
		draw.oval(input.mouseX()-5, input.mouseY()-5, 10, 10);
		draw.line(input.mouseOldX(), input.mouseOldY(), input.mouseX(), input.mouseY());
		updateView();
	}

}

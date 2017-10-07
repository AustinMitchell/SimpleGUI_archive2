package simple.gui;

import java.awt.event.KeyEvent;

import simple.run.Input;

// creates a floating text box. Only works if clicked(active)
public class TextBox extends TextArea {
	public TextBox() {
		this(0, 0, 10, 10);
	}
	public TextBox(String text_) {
		this(0, 0, 10, 10, text_);
	}
	public TextBox(int x_, int y_, int w_, int h_) {
		this(x_, y_, w_, h_, "");
	}
	public TextBox(int x_, int y_, int w_, int h_, String text_) {
		super(x_, y_, w_, h_, text_);
	}
	
	@Override
	public void update() {
		if (!enabled || !visible) 
			return;
		
		updateClickingState();
		if (isClicked()) {
			active = true;
		} else if (!containsMouse() && Input.mousePressed()) {
			active = false;
		}
		
		handleInput();
	}
	
	protected void handleInput() {
		if (Input.getChar() != 0 && editable) {
			if (active) {
				if (Input.getChar() == KeyEvent.VK_BACK_SPACE) {
					removeChar();
				} else if (Input.getChar() == KeyEvent.VK_ENTER) {
					textDisplay.add("");
				} else if (Input.getChar() >= 32 && Input.getChar() <= 127) {
					addChar(Input.getChar());
				}
			}
		}
	}
	
	@Override
	public void draw() {
		if (!visible)
			return;
		
		if (boxIsVisible) {
			drawBox();
		}
		drawText();
	}	
}

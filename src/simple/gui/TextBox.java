package simple.gui;

import java.awt.event.KeyEvent;

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
		} else if (!containsMouse() && input.mousePressed()) {
			active = false;
		}
		if (input.getChar() != 0 && editable) {
			if (active) {
				if (input.getChar() == KeyEvent.VK_BACK_SPACE) {
					removeChar();
				} else if (input.getChar() == KeyEvent.VK_ENTER) {
					textDisplay.add("");
				} else if (input.getChar() >= 32 && input.getChar() <= 127) {
					addChar(input.getChar());
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

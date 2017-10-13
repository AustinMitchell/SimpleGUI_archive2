package simple.gui.textarea;

import java.awt.event.KeyEvent;

import simple.run.Input;

// creates a floating text box. Only works if clicked(active)
public class TextBox extends TextArea {
	public TextBox() {
		this(0, 0, 10, 10);
	}
	public TextBox(String text) {
		this(0, 0, 10, 10, text);
	}
	public TextBox(int x, int y, int w, int h) {
		this(x, y, w, h, "");
	}
	public TextBox(int x, int y, int w, int h, String text) {
		super(x, y, w, h, text);
	}
	
	@Override
	protected void updateWidget() {
		if (clicked()) {
			_active = true;
		} else if (!containsMouse() && Input.mousePressed()) {
			_active = false;
		}
		
		handleInput();
	}
	
	protected void handleInput() {
		if (Input.getChar() != 0 && _editable) {
			if (_active) {
				if (Input.getChar() == KeyEvent.VK_BACK_SPACE) {
					removeChar();
				} else if (Input.getChar() == KeyEvent.VK_ENTER) {
					_textDisplay.add("");
				} else if (Input.getChar() >= 32 && Input.getChar() <= 127) {
					addChar(Input.getChar());
				}
			}
		}
	}
	
	@Override
	protected void drawWidget() {
		if (_boxVisible) {
			drawBox();
		}
		drawText();
	}	
}

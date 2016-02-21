package simple.gui;

import java.awt.event.KeyEvent;

public class TextEntryBox extends TextBox {
	private String enteredText;
	private boolean textIsEntered;
	
	public String getEnteredText() { return enteredText; }
	public boolean hasTextEntered() { return textIsEntered; }
	
	public TextEntryBox() {
		this(0, 0, 10, 10);
	}
	public TextEntryBox(int x_, int y_, int w_, int h_) {
		super(x_, y_, w_, h_);		
		
		enteredText = "";
		textIsEntered = false;
	}
	
	@Override
	protected void handleInput() {
		if (input.getChar() != 0) {
			if (active) {
				if (input.getChar() == KeyEvent.VK_BACK_SPACE) {
					removeChar();
				} else if (input.getChar() == KeyEvent.VK_ENTER) {
					textIsEntered = true;
					enteredText = text;
					clear();
				} else if (input.getChar() >= 32 && input.getChar() <= 127) {
					addChar(input.getChar());
				}
			}
		}
	}
}

package simple.gui.textarea;

import java.awt.event.KeyEvent;

import simple.run.Input;

public class TextEntryBox extends TextBox {
	private String _enteredText;
	private boolean _textIsEntered;
	
	public String enteredText() { return _enteredText; }
	public boolean hasTextEntered() { return _textIsEntered; }
	
	@Override
	public void setEnabled(boolean enabled) { 
	    super.setEnabled(enabled);
	    _textIsEntered = _textIsEntered && enabled;
	}
	
	public TextEntryBox() {
		this(0, 0, 10, 10);
	}
	public TextEntryBox(String text) {
		this(0, 0, 10, 10, text);
	}
	public TextEntryBox(int x, int y, int w, int h) {
		this(x, y, w, h, "");
	}
	public TextEntryBox(int x, int y, int w, int h, String text) {
		super(x, y, w, h, text);		
		
		_enteredText = "";
		_textIsEntered = false;
	}
	
	@Override
	protected void handleInput() {
		if (_textIsEntered) {
			_textIsEntered = false;
			_enteredText = "";
			clear();
		}
		if (Input.getChar() != 0) {
			if (_active) {
				if (Input.getChar() == KeyEvent.VK_BACK_SPACE) {
					removeChar();
				} else if (Input.getChar() == KeyEvent.VK_ENTER) {
					_textIsEntered = true;
					_enteredText = _text;
				} else if (Input.getChar() >= 32 && Input.getChar() <= 127) {
					addChar(Input.getChar());
				}
			}
		}
	}
}

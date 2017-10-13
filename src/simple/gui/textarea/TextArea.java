package simple.gui.textarea;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.LinkedList;

import simple.gui.Draw;
import simple.gui.Image;
import simple.gui.Widget;

public abstract class TextArea extends Widget{

	public static enum Alignment {
		NORTHWEST, NORTH, NORTHEAST, WEST, CENTER, EAST, SOUTHWEST, SOUTH, SOUTHEAST;
		public static boolean isTopAligned(Alignment alignment)    { return alignment==NORTH || alignment==NORTHWEST || alignment==NORTHEAST; }
		public static boolean isBottomAligned(Alignment alignment) { return alignment==SOUTH || alignment==SOUTHWEST || alignment==SOUTHEAST; }
		public static boolean isLeftAligned(Alignment alignment)   { return alignment==WEST  || alignment==NORTHWEST || alignment==SOUTHWEST; }
		public static boolean isRightAligned(Alignment alignment)  { return alignment==EAST  || alignment==NORTHEAST || alignment==SOUTHEAST; }
		
		public static boolean centeredVertical(Alignment alignment)  { return alignment==NORTH || alignment==CENTER    || alignment==SOUTH; }
		public static boolean centeredHorizontal(Alignment alignment)  { return alignment==WEST  || alignment==CENTER    || alignment==EAST; }
	}
	
	
	protected LinkedList<String> _textDisplay;
	protected String _text;
	protected FontMetrics _fm;
	protected int _numLinesToDisplay;
	protected boolean _active, _editable;
	protected Alignment _alignment;
	protected boolean _boxVisible;
	
	public boolean editable() { return _editable; } 
	public boolean active() { return _active; }
	public int numLines() { return _numLinesToDisplay; }	
	public String text() { return _text; }
	public boolean isEmpty() { return _text.equals(""); }
	public Alignment alignment() { return _alignment; }
	
	public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        _active = _active && enabled;
    }
	public void setEditable(boolean editable) { _editable = editable; }
	public void setActive(boolean active) { _active = active; }
	public void setText(String text) { 
		clear();
		for (int i=0; i<text.length(); i++) {
			addChar(text.charAt(i));
		}
	}
	public void setAlignment(Alignment alignment) { _alignment = alignment; }
	public void setBoxVisible(boolean boxVisible) { _boxVisible = boxVisible; }
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		_numLinesToDisplay = Math.max(1, (_h-4)/(_fm.getMaxAscent()+2));
		setText(_text);
	}
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		_fm = Draw.getFontMetrics(_font);
		_numLinesToDisplay = Math.max(1, (_h-4)/(_fm.getMaxAscent()+2));
		setText(_text);
	}
	
	public TextArea() {
		this(0, 0, 10, 10);
	}
	public TextArea(String text) {
		this(0, 0, 10, 10, text);
	}
	public TextArea(int x, int y, int w, int h) {
		this(x, y, w, h, "");
	}
	public TextArea(int x, int y, int w, int h, String text) {
		super(x, y, w, h);
				
		_boxVisible = true;
		_editable = true;
		_active = false;
		_textDisplay = new LinkedList<String>();
		_fm = Draw.getFontMetrics(_font);
		setText(text);
		// We want space of at least 2 pixels above the text and below the text to the border, and 2 pixels between lines
		_numLinesToDisplay = Math.max(1, (_h-4)/(_fm.getMaxAscent()+2));
		
		_alignment = Alignment.NORTHWEST;
	}
	
	protected void addChar(char c) {
		_text += c;
		
		String textDisplayLine = _textDisplay.getLast();
		textDisplayLine += c;
		
		if (_fm.stringWidth(textDisplayLine) > _w-4) {
			int breakPoint = textDisplayLine.length()-2;
			for (int i=breakPoint; i>0; i--) {
				if (Character.isWhitespace(textDisplayLine.charAt(i))) {
					breakPoint = i;
					break;
				}
			}
			String currentLineText = textDisplayLine.substring(0, breakPoint+1);
			String nextLineText = textDisplayLine.substring(breakPoint+1);
			
			_textDisplay.removeLast();
			_textDisplay.addLast(currentLineText);
			_textDisplay.addLast(nextLineText);
		} else {
			_textDisplay.removeLast();
			_textDisplay.addLast(textDisplayLine);
		}
	}
	protected void removeChar() {
		if (_text.length() > 0) {
			_text = _text.substring(0, _text.length()-1);
		}
		
		String textDisplayLine = _textDisplay.getLast();
		if (textDisplayLine.equals("")) {
			if (_textDisplay.size() > 1) {
				_textDisplay.removeLast();
			}
		} else {
			textDisplayLine = textDisplayLine.substring(0, textDisplayLine.length()-1);
			_textDisplay.removeLast();
			_textDisplay.addLast(textDisplayLine);
		}
	}
	
	public void clear() {
		_text = "";
		_textDisplay = new LinkedList<String>();
		_textDisplay.add("");
	}
	
	protected void drawBox() {
	    Draw.setColors(_textAreaColor, _borderColor);
	    Draw.rect(_x, _y, _w, _h);
	}
	protected void drawText() {
		int lineHeight = _fm.getMaxAscent()+2;
		int currentNumDisplayLines = Math.min(_numLinesToDisplay, _textDisplay.size());
		
		
		Draw.setFont(_font);
		Draw.setStroke(_textColor);
		
		
		int baseY = 0;
		if (Alignment.isTopAligned(_alignment)) {
			baseY = _y+2;
		} else if(Alignment.centeredHorizontal(_alignment)) {
			baseY = _y+2+_h/2 - (lineHeight/2)*currentNumDisplayLines;
		} else {
			baseY = _y+_h-2 - lineHeight*currentNumDisplayLines;
		}
		
		if (Alignment.centeredVertical(_alignment)) {
			baseY += _fm.getMaxAscent()/2;
		}
		
		for (int i=0; i<currentNumDisplayLines; i++) {
			String currentText = _textDisplay.get(i);
			if (i != currentNumDisplayLines-1) { currentText = currentText.trim(); }
			
			int currentY = baseY + lineHeight*i;
			if (Alignment.isLeftAligned(_alignment)) {
			    Draw.text(currentText, _x+2, currentY);
			} else if (Alignment.centeredVertical(_alignment)) {
			    Draw.textCentered(currentText, _x+_w/2, currentY);
			} else {
			    Draw.textRight(currentText, _x+_w-2, currentY);
			}
		}
		if (_textDisplay.size() <= _numLinesToDisplay && _editable && _active) {
			if ((System.nanoTime() / 500000000) % 2 == 0) {
				String currentText = _textDisplay.getLast();
				int currentLine = _textDisplay.size()-1;
				int currentY = baseY + currentLine*lineHeight;
				if (Alignment.isLeftAligned(_alignment)) {
				    Draw.line(_x+2 + _fm.stringWidth(currentText)+1, currentY, _x+2 + _fm.stringWidth(currentText)+1, currentY+lineHeight);
				} else if (Alignment.centeredVertical(_alignment)) {
				    Draw.line(_x+_w/2 + _fm.stringWidth(currentText)/2 + 1, currentY - _fm.getMaxAscent()/2, _x+_w/2 + _fm.stringWidth(currentText)/2 + 1, currentY+lineHeight - _fm.getMaxAscent()/2);
				} else {
				    Draw.line(_x+_w-1, currentY, _x+_w-1, currentY+lineHeight);
				}
			}
		}
	}
}

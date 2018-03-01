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
		public static String toString(Alignment alignment) {
		    switch(alignment) {
            case CENTER:
                return "Center";
            case EAST:
                return "East";
            case NORTH:
                return "North";
            case NORTHEAST:
                return "Northeast";
            case NORTHWEST:
                return "Northwest";
            case SOUTH:
                return "South";
            case SOUTHEAST:
                return "Southeast";
            case SOUTHWEST:
                return "Southwest";
            case WEST:
                return "West";
		    }
            return "INVALID";
		}
	}
	
	
	protected LinkedList<String> _textDisplay;
	protected String _text;
	protected Image _textRender;
	protected FontMetrics _fm;
	protected int _maxRenderLines, _currentRenderLines, _baseTextY, _lineHeight;
	protected boolean _active, _editable;
	protected Alignment _alignment;
	protected boolean _boxVisible;
	
	public boolean editable() { return _editable; } 
	public boolean active() { return _active; }
	public int maxRenderLines() { return _maxRenderLines; }
	public int currentRenderLines() { return _currentRenderLines; }   
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
		addString(text);
		generateLineMetrics();
	}
	public void setAlignment(Alignment alignment) { 
	    _alignment = alignment;
        generateBaseMetrics();
	}
	public void setBoxVisible(boolean boxVisible) { _boxVisible = boxVisible; }
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		setText(_text);
		generateLineMetrics();
	}
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		_fm = Draw.getFontMetrics(_font);
		setText(_text);
		generateLineMetrics();
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
		_text = "";
		
		setFont(_font);
		setText(text);
		setAlignment(Alignment.NORTHWEST);
	}
	
	public void addString(String s) {
        for (int i=0; i<s.length(); i++) {
            addChar(s.charAt(i), false);
        }
        generateLineMetrics();
	}
	
	protected void addChar(char c, boolean updateMetrics) {
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
        if (updateMetrics) {
            generateLineMetrics();
        }
	}
	protected void addChar(char c) {
		addChar(c, true);
	}
	protected void removeChar() {
		removeChar(true);
	}
	protected void removeChar(boolean updateMetrics) {
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
        if (updateMetrics) {
            generateLineMetrics();
        }
	}
	
	public void clear() {
		_text = "";
		_textDisplay = new LinkedList<String>();
		_textDisplay.add("");
		generateLineMetrics();
	}
	
	protected void generateLineMetrics() {
        _lineHeight = _fm.getMaxAscent()+2;
        _maxRenderLines = Math.max(1, (_h-4)/(_lineHeight));
        _currentRenderLines = Math.min(_maxRenderLines, _textDisplay.size());
        generateBaseMetrics();
	}
	protected void generateBaseMetrics() {
	    if (Alignment.isTopAligned(_alignment)) {
            _baseTextY = 2;
        } else if(Alignment.centeredHorizontal(_alignment)) {
            _baseTextY = 2+_h/2 - (_lineHeight/2)*_currentRenderLines;
        } else {
            _baseTextY = _h-2 - _lineHeight*_currentRenderLines;
        }
	    
	    if (Alignment.centeredVertical(_alignment)) {
	        _baseTextY += (_lineHeight/2 - 2);
	    }
        renderTextImage();
	}
	
	protected void drawBox() {
	    Draw.setColors(_textAreaColor, _borderColor);
	    Draw.rect(_x, _y, _w, _h);
	}
	protected void drawCursor() {
	    if (_currentRenderLines <= _maxRenderLines && _enabled && _editable && _active) {
            if ((System.nanoTime() / 500000000) % 2 == 0) {
                String currentText = _textDisplay.getLast();
                int currentLine = _textDisplay.size()-1;
                int currentY = _baseTextY + currentLine*_lineHeight;
                if (Alignment.isLeftAligned(_alignment)) {
                    Draw.line(2 + _fm.stringWidth(currentText)+1, currentY, 2 + _fm.stringWidth(currentText)+1, currentY+_lineHeight);
                } else if (Alignment.centeredVertical(_alignment)) {
                    Draw.line(_w/2 + _fm.stringWidth(currentText)/2 + 1, currentY - _fm.getMaxAscent()/2, _w/2 + _fm.stringWidth(currentText)/2 + 1, currentY+_lineHeight - _fm.getMaxAscent()/2);
                } else {
                    Draw.line(_w-1, currentY, _w-1, currentY+_lineHeight);
                }
            }
        }
	}
	protected void renderTextImage() {
		_textRender = new Image(_w, _h);
		Draw.setFont(_textRender, _font);
		Draw.setStroke(_textColor);
		
		for (int i=0; i<_currentRenderLines; i++) {
			String currentText = _textDisplay.get(i);
			if (i != _currentRenderLines-1) { currentText = currentText.trim(); }
			
			int currentY = _baseTextY + _lineHeight*i;
			if (Alignment.isLeftAligned(_alignment)) {
			    Draw.text(_textRender, currentText, 2, currentY);
			} else if (Alignment.centeredVertical(_alignment)) {
			    Draw.textCentered(_textRender, currentText, _w/2, currentY);
			} else {
			    Draw.textRight(_textRender, currentText, _w-2, currentY);
			}
		}
	}
}

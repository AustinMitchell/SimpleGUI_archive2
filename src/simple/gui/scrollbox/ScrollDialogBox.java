package simple.gui.scrollbox;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;

import simple.gui.Draw;
import simple.run.Input;

public class ScrollDialogBox extends ScrollBox {
	
	/** Raw line data. Each index in lines is made from a call to addLine. **/
	protected ArrayList<String> _lines; 
	/** Represents the lines list but split up into lines whose width does not exceed the space given for text within the scrollBox. **/
	protected ArrayList<String> _lineDisplay;	
	
	/** The first line in lineDisplay to start drawing text from. **/
	protected int _firstIndex;
	/** Maximum number of lines to draw in the scrollBox **/
	protected int _numLinesToDisplay;
	
	protected FontMetrics _fm;
	
	/** Returns raw line data (data that gets converted into displayable lines). **/
	public ArrayList<String> lineRawData() { return _lines; }
	/** Returns display line data (converted from raw line data). **/
	public ArrayList<String> lineDisplay() { return _lineDisplay; }

	/** Sets the scrollBox's font, as well as reformats the line display **/
	public void setTextFont(Font newFont) {
		super.setFont(newFont);
		reformatDisplay();
	}
	/** Sets the scrollBox's width and height, as well as reformats the line display **/
	public void setSize(int w, int h) {
		super.setSize(w, h);
		reformatDisplay();
	}
	
	public void setFont(Font font) {
	    super.setFont(font);
	    _fm = Draw.getFontMetrics(font);
	}
	
	/** Creates a new scrollBox with default size and position. **/
	public ScrollDialogBox() {
		this(0, 0, 10, 10);
	}
	/** Creates a new scrollBox with given position and size. **/
	public ScrollDialogBox(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		_lines = new ArrayList<String>();
		_lineDisplay = new ArrayList<String>();
		_firstIndex = -1;
		_fm = Draw.getFontMetrics(_font);
		_numLinesToDisplay = (_h-4)/(_fm.getMaxAscent()+2);
	}
	
	/** Clears the display and adds every line from the raw line data. **/
	private void reformatDisplay() {
		_numLinesToDisplay = (_h-4)/(_fm.getMaxAscent()+2);
		
		ArrayList<String> tempLines = new ArrayList<String>();
		for(String s: _lines) 
			tempLines.add(s);
		
		clear();
		for(String s: tempLines) 
			addLine(s);
	}
	
	/** Adds a line to the raw line data, and splits the line into lines of the correct width. **/
	public void addLine(String newLine) {
		_lines.add(newLine);
		
		String currentText = newLine;
		int lineWidth = _w-4-_BAR_WIDTH;
		
		first: while(true) {
			// checks if the partitioned string will fit in the line
			if (_fm.stringWidth(currentText) <= lineWidth) {
				_lineDisplay.add(currentText);
				break first;
			// Searches for a point in the partitioned string that will fit in the box's bounds. Takes formation of words into account
			} else {
				boolean found = false;
				for (int i=currentText.length()-1; i>=0; i--) {
					if (_fm.stringWidth(currentText.substring(0, i+1)) <= lineWidth) {
						found = true;
						int lastIndex = i;
						for (int j=lastIndex; j>0; j--) {
							if (currentText.charAt(j) == ' ') {
								if (!(j == 1 && currentText.charAt(0) == ' ')) {
									lastIndex = j;
								}
								break;
							}
						}
						_lineDisplay.add(currentText.substring(0, lastIndex+1));
						currentText = "  " + currentText.substring(lastIndex+1);
						break;
					}
				}
				if (!found) {
					break first;
				}
			}
		}
		if (_lineDisplay.size() == 1 && _firstIndex == -1) {
			_firstIndex = 0;
		} else if (_lineDisplay.size() > 1) {
			_firstIndex = Math.max(_lineDisplay.size()-_numLinesToDisplay, 0);
			_scrollBar.setValue(_firstIndex);
		}
		_scrollBar.setRange(0, _lineDisplay.size()-1);
	}
	
	public void addRepeatedTextLine(String pattern) {
		String result = "";
		String temp;
		
		int lineWidth = _w-4-_BAR_WIDTH;
		
		while(true) {
			temp = result + pattern;
			if (_fm.stringWidth(temp) > lineWidth) {
				break;
			}
			result = temp;
		}
		addLine(result);
	}
	
	/** Clears the raw line data and the line display data. **/
	public void clear() {
		_lines = new ArrayList<String>();
		_lineDisplay = new ArrayList<String>();
	}
	
	/** Updates the scrollBox and scroll widgets, and sets how those widgets interact with the scrollbox. **/
	protected void updateWidget() {
		updateScrollWidgets(); 
		
		if ((_scrollUp.clicked() || (Input.mouseWheelUp()&&hovering())) && _firstIndex > 0) {
			_firstIndex -= 1;
			_scrollBar.setValue(_firstIndex);
		} else if ((_scrollDown.clicked() || (Input.mouseWheelDown()&&hovering())) && _firstIndex < _lineDisplay.size()-1) {
			_firstIndex += 1;
			_scrollBar.setValue(_firstIndex);
		}
		
		if (_scrollBar.value() != _firstIndex) {
			_firstIndex = _scrollBar.value();
		}
	}
	
	/** Draws the scrollBox and scroll widgets. **/
	protected void drawWidget() {
		drawScrollWidgets();
		
		Draw.setColors(_fillColor, _borderColor);
		Draw.rect(_x, _y, _w-_BAR_WIDTH, _h);
		
		int lineHeight = _fm.getMaxAscent()+2;
		
		Draw.setFont(_font);
		Draw.setStroke(_textColor);
		
		if (_firstIndex != -1) {
			for (int i=_firstIndex; i<_firstIndex+_numLinesToDisplay && i<_lineDisplay.size(); i++) {
			    Draw.text(_lineDisplay.get(i), _x+2, _y+2+lineHeight*(i-_firstIndex));
			}
		}
	}
}

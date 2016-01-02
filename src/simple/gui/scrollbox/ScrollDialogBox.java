package simple.gui.scrollbox;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;

public class ScrollDialogBox extends ScrollBox {
	
	/** Raw line data. Each index in lines is made from a call to addLine. **/
	protected ArrayList<String> lines; 
	/** Represents the lines list but split up into lines whose width does not exceed the space given for text within the scrollBox. **/
	protected ArrayList<String> lineDisplay;	
	
	/** The first line in lineDisplay to start drawing text from. **/
	protected int firstIndex;
	/** Maximum number of lines to draw in the scrollBox **/
	protected int numLinesToDisplay;
	
	/** Returns raw line data (data that gets converted into displayable lines). **/
	public ArrayList<String> getLineData() { return lines; }
	/** Returns display line data (converted from raw line data). **/
	public ArrayList<String> getLineDisplay() { return lineDisplay; }

	/** Sets the scrollBox's font, as well as reformats the line display **/
	public void setTextFont(Font newFont) {
		super.setFont(newFont);
		reformatDisplay();
	}
	/** Sets the scrollBox's width and height, as well as reformats the line display **/
	public void setSize(int newWidth, int newHeight) {
		super.setSize(newWidth, newHeight);
		reformatDisplay();
	}
	/** Sets the scrollBox's width, as well as reformats the line display **/
	public void setWidth(int newWidth) { setSize(newWidth, h); }
	/** Sets the scrollBox's height, as well as reformats the line display **/
	public void setHeight(int newHeight) { setSize(w, newHeight); }
	
	/** Creates a new scrollBox with default size and position. **/
	public ScrollDialogBox() {
		this(0, 0, 10, 10);
	}
	/** Creates a new scrollBox with given position and size. **/
	public ScrollDialogBox(int x_, int y_, int w_, int h_) {
		super(x_, y_, w_, h_);
		
		lines = new ArrayList<String>();
		lineDisplay = new ArrayList<String>();
		firstIndex = -1;
		FontMetrics fm = draw.getFontMetrics(font);
		numLinesToDisplay = (h-4)/(fm.getMaxAscent()+2);
	}
	
	/** Clears the display and adds every line from the raw line data. **/
	private void reformatDisplay() {
		FontMetrics fm = draw.getFontMetrics(font);
		numLinesToDisplay = (h-4)/(fm.getMaxAscent()+2);
		
		clearDisplay();
		for(String s: lines) 
			addLine(s);
	}
	
	/** Adds a line to the raw line data, and splits the line into lines of the correct width. **/
	public void addLine(String newLine) {
		lines.add(newLine);
		
		FontMetrics fm = draw.getFontMetrics(font);		
		String currentText = newLine;
		int lineWidth = w-4-BAR_WIDTH;
		
		first: while(true) {
			// checks if the partitioned string will fit in the line
			if (fm.stringWidth(currentText) <= lineWidth) {
				lineDisplay.add(currentText);
				break first;
			// Searches for a point in the partitioned string that will fit in the box's bounds. Takes formation of words into account
			} else {
				for (int i=currentText.length()-1; i>=0; i--) {
					if (fm.stringWidth(currentText.substring(0, i+1)) <= lineWidth) {
						int lastIndex = i;
						for (int j=lastIndex; j>0; j--) {
							if (currentText.charAt(j) == ' ') {
								if (!(j == 1 && currentText.charAt(0) == ' ')) {
									lastIndex = j;
								}
								break;
							}
						}
						lineDisplay.add(currentText.substring(0, lastIndex+1));
						currentText = "  " + currentText.substring(lastIndex+1);
						break;
					}
				}
			}
		}
		if (lineDisplay.size() == 1 && firstIndex == -1) {
			firstIndex = 0;
		} else if (lineDisplay.size() > 1) {
			firstIndex = Math.max(lineDisplay.size()-numLinesToDisplay, 0);
			scrollBar.setValue(firstIndex);
		}
		scrollBar.setRange(0, lineDisplay.size()-1);
	}
	
	/** Clears the raw line data and the line display data. **/
	public void clear() {
		lines = new ArrayList<String>();
		lineDisplay = new ArrayList<String>();
	}
	private void clearDisplay() {
		lineDisplay = new ArrayList<String>();
	}
	
	/** Updates the scrollBox and scroll widgets, and sets how those widgets interact with the scrollbox. **/
	public void update() {
		if (!enabled || !visible) 
			return;
		
		updateScrollWidgets(); 
		
		if (scrollUp.isClicked() && firstIndex > 0) {
			firstIndex -= 1;
			scrollBar.setValue(firstIndex);
		} else if (scrollDown.isClicked() && firstIndex < lineDisplay.size()-1) {
			firstIndex += 1;
			scrollBar.setValue(firstIndex);
		}
		
		if (scrollBar.getValue() != firstIndex) {
			firstIndex = scrollBar.getValue();
		}
	}
	
	/** Draws the scrollBox and scroll widgets. **/
	public void draw() {
		if (!visible) 
			return;
		
		drawScrollWidgets();
		
		draw.setColors(fillColor, borderColor);
		draw.rect(x, y, w-BAR_WIDTH, h);
		
		FontMetrics fm = draw.getFontMetrics(font);
		int lineHeight = fm.getMaxAscent()+2;
		
		draw.setFont(font);
		draw.setStroke(textColor);
		
		if (firstIndex != -1) {
			for (int i=firstIndex; i<firstIndex+numLinesToDisplay && i<lineDisplay.size(); i++) {
				draw.text(lineDisplay.get(i), x+2, y+2+lineHeight*(i+1-firstIndex));
			}
		}
	}
}

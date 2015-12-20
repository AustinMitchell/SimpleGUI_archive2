package simple.gui;

import java.awt.Font;
import java.awt.FontMetrics;

/** A floating text box that is uneditable. **/
public class Label extends Widget {
	/** Constant to show that no key is being pressed. **/
	public final static int KEY_NOT_PRESSED = -1;
	
	/** Raw text to be rendered and displayed. **/
	protected String text;
	/** Maximum number of lines to display text. Any text that would take the label beyond this line bound would be cut off and lost. 
	 * <P>This value is calculated by taking the label's height, divide by the MaxAscent of the font plus two, all floored. **/
	protected int numLinesToDisplay;
	/** Sets whether the label itself is drawable. If not, only the text will be displayed. This value is false by default. **/
	protected boolean boxIsDrawable;
	/** Sets whether the text is centered around the (x, y) coordinate or if that denotes the corner. **/
	protected boolean isCentered;
	
	/** Returns the maximum number of lines of text displayed. **/
	public int getNumLines() { return numLinesToDisplay; }	
	/** Returns the labels's text String. **/
	public String getText() { return text; }
		
	/** Sets the label's text String. **/
	public void setText(String newText) { text = newText; }
	/** Sets whether or not the label background itself is drawn. False means only floating text is displayed. **/
	public void setBoxIsDrawable(boolean drawable) { boxIsDrawable = drawable; }
	/** Sets whether the text is centered around the (x, y) coordinate or if that denotes the corner. **/
	public void setIsCentered(boolean centered) { isCentered = centered; }
	
	/** Sets the label's size and recalculates the number of lines to display. **/
	public void setSize(int newWidth, int newHeight) {
		super.setSize(newWidth, newHeight);
		FontMetrics fm = draw.getFontMetrics(font);
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	/** Sets the label's width and recalculates the number of lines to display. **/
	public void setWidth(int newWidth) {
		setSize(newWidth, h);
	}
	/** Sets the label's height and recalculates the number of lines to display. **/
	public void setHeight(int newHeight) {
		setSize(w, newHeight);
	}
	
	/** Sets the label's font and recalculates the number of lines to display. **/
	public void setTextFont(Font newFont) {
		super.setFont(newFont);
		FontMetrics fm = draw.getFontMetrics(font);
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	
	/** Creates a label with all default/zero values. **/
	public Label() {
		this(0, 0, 10, 10, "");
	}
	/** Creates a label with default position and size, with intial text. **/
	public Label(String text_) {
		this(0, 0, 10, 10, text_);
	}
	/** Creates a label with defined position and size with no text. **/
	public Label(int x_, int y_, int w_, int h_) {
		this(x_, y_, w_, h_, "");
	}
	/** Creates a label with defined position and size, as well as initial text. **/
	public Label(int x_, int y_, int w_, int h_, String text_) {
		super(x_, y_, w_, h_);
				
		boxIsDrawable = false;
		text = text_;
		FontMetrics fm = draw.getFontMetrics(font);
		// We want space of at least 2 pixels above the text and below the text to the border, and 2 pixels between lines
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	
	/** Adds one character to the stored text. **/
	public void addChar(char c) {
		text += c;
	}

	/** Updates label's state relative to the mouse. **/
	public void update() {
		if (!enabled || !visible) 
			return;
		updateClickingState();
	}
	
	/** Draws the label. If boxIsDrawable is true, it will draw a box with this widget's color values, but it will use textAreaColor for the fill instead of fillColor. **/
	//TODO: Shitty draw algorithm. Should store the split up string instead of splitting every time
	public void draw() {
		if (!visible)
			return;
		
		if (boxIsDrawable) {
			draw.setColors(textAreaColor, borderColor);
			draw.rect(x, y, w, h);
		}
		
		FontMetrics fm = draw.getFontMetrics(font);
		int lineHeight = fm.getMaxAscent()+2;
		
		draw.setFont(font);
		draw.setStroke(textColor);
		int currentLine = 1;
		String currentText = text;
		
		first: while(true) {
			// Cuts off any characters that would take it out of the box's bounds
			if (currentLine > numLinesToDisplay) {
				text = text.substring(0, text.length()-currentText.length());
				break first;
			// checks if the partitioned string will fit in the line
			} else if (fm.stringWidth(currentText) <= w-4) {
				if (isCentered) {
					draw.textCentered(currentText, x+w/2, y+2 + lineHeight*(currentLine-1));
				} else {
					draw.text(currentText, x+2, y+2 + lineHeight*(currentLine-1));
				}
				break first;
			// Searches for a point in the partitioned string that will fit in the box's bounds. Takes formation of words into account
			} else {
				for (int i=currentText.length()-1; i>=0; i--) {
					if (fm.stringWidth(currentText.substring(0, i+1)) <= w-4) {
						int lastIndex = i;
						if (currentLine < numLinesToDisplay) {
							for (int j=lastIndex; j>=0; j--) {
								if (currentText.charAt(j) == ' ') {
									lastIndex = j;
									break;
								}
							}
						}
						if (isCentered) {
							draw.text(currentText.substring(0, lastIndex+1), x+w/2, y+2 + lineHeight*(currentLine-1));
						} else {
							draw.text(currentText.substring(0, lastIndex+1), x+2, y+2 + lineHeight*(currentLine-1));
						}
						currentText = currentText.substring(lastIndex+1);
						currentLine += 1;
						break;
					}
				}
			}
		}
	}	
}

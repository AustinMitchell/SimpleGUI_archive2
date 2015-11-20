package simple.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;

// creates a floating text box. Only works if clicked(active)
public class TextBox extends Widget {
	public static final int KEY_NOT_PRESSED = -1;
	
	protected String text;
	protected int numLinesToDisplay;
	protected boolean active, editable;
	
	public boolean isEditable() { return editable; } 
	public boolean isActive() { return active; }
	public int getNumLines() { return numLinesToDisplay; }	
	public String getText() { return text; }
	public boolean isEmpty() { return text.equals(""); }
	
	public void setEditable(boolean newEditable) { editable = newEditable; }
	public void setText(String newText) { text = newText; }
	public void setActive(boolean newActive) { active = newActive; }
	
	@Override
	public void setSize(int w_, int h_) {
		super.setSize(w_, h_);
		FontMetrics fm = draw.getFontMetrics(font);
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	@Override
	public void setWidth(int w_) {
		setSize(w_, h);
	}
	@Override
	public void setHeight(int h_) {
		setSize(w, h_);
	}
	@Override
	public void setFont(Font f_) {
		super.setFont(f_);
		FontMetrics fm = draw.getFontMetrics(font);
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	
	public TextBox() {
		this(0, 0, 10, 10);
	}
	public TextBox(String text_) {
		this(0, 0, 10, 10, text_);
	}
	public TextBox(int x_, int y_, int w_, int h_) {
		super(x_, y_, w_, h_);
		
		editable = true;
		active = false;
		text = "";
		FontMetrics fm = draw.getFontMetrics(font);
		// We want space of at least 2 pixels above the text and below the text to the border, and 2 pixels between lines
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	public TextBox(int x_, int y_, int w_, int h_, String text_) {
		super(x_, y_, w_, h_);
				
		editable = true;
		active = false;
		text = text_;
		FontMetrics fm = draw.getFontMetrics(font);
		// We want space of at least 2 pixels above the text and below the text to the border, and 2 pixels between lines
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	
	public void addChar(char c) {
		text += c;
	}
	public void clear() {
		text = "";
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
					if (text.length() > 0) {
						text = text.substring(0, text.length()-1);
					}
				} else if (input.getChar() >= 32 && input.getChar() <= 127) {
					text += input.getChar();
				}
			}
		}
	}
	
	@Override
	//TODO: Shitty draw algorithm. Should store the split up string instead of splitting every time
	public void draw() {
		if (!visible)
			return;
		
		FontMetrics fm = draw.getFontMetrics(font);
		int lineHeight = fm.getMaxAscent()+2;
		
		draw.setColors(textAreaColor, borderColor);
		draw.rect(x, y, w, h);
		
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
				draw.text(currentText, x+2, y+2 + lineHeight*(currentLine-1));
				if (active && (System.nanoTime() / 500000000) % 2 == 0) {
					draw.line(x+2 + fm.stringWidth(currentText)+1, y+5 + lineHeight*(currentLine-1), x+2 + fm.stringWidth(currentText)+1, y+2 + lineHeight*(currentLine-1));
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
						draw.text(currentText.substring(0, lastIndex+1), x+2, y+2 + lineHeight*(currentLine-1));
						currentText = currentText.substring(lastIndex+1);
						currentLine += 1;
						break;
					}
				}
			}
		}
	}	
}

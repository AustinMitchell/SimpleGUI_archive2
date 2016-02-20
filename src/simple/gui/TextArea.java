package simple.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.LinkedList;

public abstract class TextArea extends Widget{

	public static enum Alignment {
		NORTHWEST, NORTH, NORTHEAST, WEST, CENTER, EAST, SOUTHWEST, SOUTH, SOUTHEAST;
		public static boolean isTopAligned(Alignment alignment)    { return alignment==NORTH || alignment==NORTHWEST || alignment==NORTHEAST; }
		public static boolean isBottomAligned(Alignment alignment) { return alignment==SOUTH || alignment==SOUTHWEST || alignment==SOUTHEAST; }
		public static boolean isLeftAligned(Alignment alignment)   { return alignment==WEST  || alignment==NORTHWEST || alignment==SOUTHWEST; }
		public static boolean isRightAligned(Alignment alignment)  { return alignment==EAST  || alignment==NORTHEAST || alignment==SOUTHEAST; }
		
		public static boolean notLeftOrRight(Alignment alignment)  { return alignment==NORTH || alignment==CENTER    || alignment==SOUTH; }
		public static boolean notTopOrBottom(Alignment alignment)  { return alignment==WEST  || alignment==CENTER    || alignment==EAST; }
	}
	
	
	protected LinkedList<String> textDisplay;
	protected String text;
	protected FontMetrics fm;
	protected int numLinesToDisplay;
	protected boolean active, editable;
	protected Alignment alignment;
	protected boolean boxIsVisible;
	
	public boolean isEditable() { return editable; } 
	public boolean isActive() { return active; }
	public int getNumLines() { return numLinesToDisplay; }	
	public String getText() { return text; }
	public boolean isEmpty() { return text.equals(""); }
	public Alignment getAlignment() { return alignment; }
	
	public void setEditable(boolean newEditable) { editable = newEditable; }
	public void setActive(boolean newActive) { active = newActive; }
	public void setText(String newText) { 
		clear();
		for (int i=0; i<newText.length(); i++) {
			addChar(newText.charAt(i));
		}
	}
	public void setAlignment(Alignment alignment) { this.alignment = alignment; }
	public void setBoxIsVisible(boolean boxIsVisible) { this.boxIsVisible = boxIsVisible; }
	
	@Override
	public void setSize(int w_, int h_) {
		super.setSize(w_, h_);
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
		fm = draw.getFontMetrics(font);
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
	}
	
	public TextArea() {
		this(0, 0, 10, 10);
	}
	public TextArea(String text_) {
		this(0, 0, 10, 10, text_);
	}
	public TextArea(int x_, int y_, int w_, int h_) {
		this(x_, y_, w_, h_, "");
	}
	public TextArea(int x_, int y_, int w_, int h_, String text_) {
		super(x_, y_, w_, h_);
				
		boxIsVisible = true;
		editable = true;
		active = false;
		textDisplay = new LinkedList<String>();
		fm = draw.getFontMetrics(font);
		setText(text_);
		// We want space of at least 2 pixels above the text and below the text to the border, and 2 pixels between lines
		numLinesToDisplay = Math.max(1, (h-4)/(fm.getMaxAscent()+2));
		
		alignment = Alignment.NORTHWEST;
	}
	
	protected void addChar(char c) {
		text += c;
		
		String textDisplayLine = textDisplay.getLast();
		textDisplayLine += c;
		
		if (fm.stringWidth(textDisplayLine) > w-4) {
			int breakPoint = textDisplayLine.length()-2;
			for (int i=breakPoint; i>0; i--) {
				if (Character.isWhitespace(textDisplayLine.charAt(i))) {
					breakPoint = i;
					break;
				}
			}
			String currentLineText = textDisplayLine.substring(0, breakPoint+1);
			String nextLineText = textDisplayLine.substring(breakPoint+1);
			
			textDisplay.removeLast();
			textDisplay.addLast(currentLineText);
			textDisplay.addLast(nextLineText);
		} else {
			textDisplay.removeLast();
			textDisplay.addLast(textDisplayLine);
		}
	}
	protected void removeChar() {
		if (text.length() > 0) {
			text = text.substring(0, text.length()-1);
		}
		
		String textDisplayLine = textDisplay.getLast();
		if (textDisplayLine.equals("")) {
			if (textDisplay.size() > 1) {
				textDisplay.removeLast();
			}
		} else {
			textDisplayLine = textDisplayLine.substring(0, textDisplayLine.length()-1);
			textDisplay.removeLast();
			textDisplay.addLast(textDisplayLine);
		}
	}
	
	public void clear() {
		text = "";
		textDisplay = new LinkedList<String>();
		textDisplay.add("");
	}
	
	protected void drawBox() {
		draw.setColors(textAreaColor, borderColor);
		draw.rect(x, y, w, h);
	}
	protected void drawText() {
		int lineHeight = fm.getMaxAscent()+2;
		int currentNumDisplayLines = Math.min(numLinesToDisplay, textDisplay.size());
		
		
		draw.setFont(font);
		draw.setStroke(textColor);
		
		
		int baseY = 0;
		if (Alignment.isTopAligned(alignment)) {
			baseY = y+2;
		} else if(Alignment.notTopOrBottom(alignment)) {
			baseY = y+2+h/2 - (lineHeight/2)*currentNumDisplayLines;
		} else {
			baseY = y+h-2 - lineHeight*currentNumDisplayLines;
		}
		
		if (Alignment.notLeftOrRight(alignment)) {
			baseY += fm.getMaxAscent()/2;
		}
		
		for (int i=0; i<currentNumDisplayLines; i++) {
			String currentText = textDisplay.get(i);
			if (i != currentNumDisplayLines-1) { currentText = currentText.trim(); }
			
			int currentY = baseY + lineHeight*i;
			if (Alignment.isLeftAligned(alignment)) {
				draw.text(currentText, x+2, currentY);
			} else if (Alignment.notLeftOrRight(alignment)) {
				draw.textCentered(currentText, x+w/2, currentY);
			} else {
				draw.textRight(currentText, x+w-2, currentY);
			}
		}
		if (textDisplay.size() <= numLinesToDisplay && editable && active) {
			if ((System.nanoTime() / 500000000) % 2 == 0) {
				String currentText = textDisplay.getLast();
				int currentLine = textDisplay.size()-1;
				int currentY = baseY + currentLine*lineHeight;
				if (Alignment.isLeftAligned(alignment)) {
					draw.line(x+2 + fm.stringWidth(currentText)+1, currentY, x+2 + fm.stringWidth(currentText)+1, currentY+lineHeight);
				} else if (Alignment.notLeftOrRight(alignment)) {
					draw.line(x+w/2 + fm.stringWidth(currentText)/2 + 1, currentY - fm.getMaxAscent()/2, x+w/2 + fm.stringWidth(currentText)/2 + 1, currentY+lineHeight - fm.getMaxAscent()/2);
				} else {
					draw.line(x+w-1, currentY, x+w-1, currentY+lineHeight);
				}
			}
		}
	}
	
	public abstract void update();
	public abstract void draw();
}

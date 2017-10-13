package simple.gui.scrollbox;

import java.awt.Color;

import simple.gui.Button;
import simple.gui.CustomDraw;
import simple.gui.Draw;
import simple.gui.Slider;
import simple.gui.Widget;

/** Provides the basic framework for other ScrollBox types. Essentially handles the background of the widget and the up/down buttons, and the side slider. 
 * <P>Like the Widget class, it is abstract as it is the framework for scrollboxes, not a functional scrollbox. However, as a programmer you must define
 * how the buttons and slider will interact with the class. **/
public abstract class ScrollBox extends Widget {
	/** Width of sidebar for all new ScrollBoxes. The default value of 20 is typically a good size in all cases, but this may be changed. **/
	protected static int _BAR_WIDTH = 20;
	
	/** Returns the current bar width for all new ScrollBoxes. **/
	public static int barWidth() { return _BAR_WIDTH; }
	/** Sets the new bar width for all future ScrollBoxes. **/
	public static void setBarWidth(int newBarWidth) { _BAR_WIDTH = newBarWidth; }
	
	/** Slider object to navigate through the ScrollBox. **/
	protected Slider _scrollBar;
	/** Up/Down buttons to navigate through the ScrollBox. **/
	protected Button _scrollUp, _scrollDown;
	protected boolean _widgetColorsDefaultToScrollBox;
	
	public void setWidgetColorsDefaultToScrollBox(boolean b) { _widgetColorsDefaultToScrollBox = b; }
	
	/** Returns the Button object for scrolling up. **/
	public Button scrollUpButton() { return _scrollUp; }
	/** Returns the Button object for scrolling down. **/
	public Button scrollDownButton() { return _scrollDown; }
	/** Returns the Slider object for scrolling. **/
	public Slider scrollBar() { return _scrollBar; }
	
	/** Sets the widget's fillColor variable **/
	public void setFillColor(Color fillColor) { 
		super.setFillColor(fillColor);
		if (_widgetColorsDefaultToScrollBox) {
			_scrollUp.setFillColor(fillColor);
			_scrollDown.setFillColor(fillColor);
			_scrollBar.setFillColor(fillColor);
		}
	}
	/** Sets the widget's borderColor variable **/
	public void setBorderColor(Color borderColor) { 
		super.setBorderColor(borderColor); 
		if (_widgetColorsDefaultToScrollBox) {
			_scrollUp.setBorderColor(borderColor);
			_scrollDown.setBorderColor(borderColor);
			_scrollBar.setBorderColor(borderColor);
		}
	}
	/** Sets the widget's textAreaColor variable **/
	public void setTextAreaColor(Color textAreaColor) { 
		super.setTextAreaColor(textAreaColor); 
		if (_widgetColorsDefaultToScrollBox) {
			_scrollUp.setTextAreaColor(textAreaColor);
			_scrollDown.setTextAreaColor(textAreaColor);
			_scrollBar.setTextAreaColor(textAreaColor);
		}
	}
	/** Sets the widget's textColor variable **/
	public void setTextColor(Color textColor) { 
		super.setTextColor(textColor); 
		if (_widgetColorsDefaultToScrollBox) {
			_scrollUp.setTextColor(textColor);
			_scrollDown.setTextColor(textColor);
			_scrollBar.setTextColor(textColor);
		}
	}
	/** Sets all the widget's color variables **/
	public void setWidgetColors(Color fillColor, Color borderColor, Color textAreaColor, Color textColor) {
		setFillColor(fillColor);
		setBorderColor(borderColor);
		setTextAreaColor(textAreaColor);
		setTextColor(textColor);
	}
	
	/** Sets the size for the scrollBox, and resizes/repositions the scroll widgets. **/
	public void setSize(int newWidth, int newHeight) {
		super.setSize(newWidth, newHeight);
		setScrollWidgetPosition();
	}
	
	/** Sets the new position of the scrollBox, as well as moves the scroll widgets. **/
	public void setLocation(int newX, int newY) {
		super.setLocation(newX, newY);
		setScrollWidgetPosition();
	}
	
	/** Creates a default ScrollBox object. **/
	public ScrollBox() {
		this(0, 0, 10, 10);
	}
	/** Creates a ScrollBox object with defined location and size. **/
	public ScrollBox(int x, int y, int w, int h) {
		super(x, y, w, h);
		_scrollUp = new Button(_x+_w-_BAR_WIDTH, _y, _BAR_WIDTH, _BAR_WIDTH, "", null);
		_scrollDown = new Button(_x+_w-_BAR_WIDTH, _y+_h-_BAR_WIDTH, _BAR_WIDTH, _BAR_WIDTH, "", null);
		_scrollBar = new Slider(_x+_w-_BAR_WIDTH, _y+_BAR_WIDTH, _BAR_WIDTH, _h-_BAR_WIDTH*2, 0, 0, false, false);
		// Scrolling is upside-down in this case, scroll down is incrementing.
		_scrollBar.setMouseScrollIncrement(-1);
		// We check for scrolling in this widget so checking again in the slider may cause problems
		_scrollBar.setMouseCanScroll(false);
		
		_widgetColorsDefaultToScrollBox = true;
		
		_scrollUp.setCustomDrawAfter(new CustomDraw() {
			public void draw(Widget w) {
				int[] x = new int[3]; 
				int[] y = new int[3];
				
				x[0] = w.x()+4;
				x[1] = w.x()+(w.w()/2);
				x[2] = w.x()+w.w()-4;
				y[0] = w.y()+w.h()-4;
				y[1] = w.y()+4;
				y[2] = w.y()+w.h()-4;
				
				Draw.setFill(null);
				Draw.setStroke(w.borderColor());
				Draw.polygon(x, y, 3);
			}});
		_scrollDown.setCustomDrawAfter(new CustomDraw() {
			public void draw(Widget w) {
				int[] x = new int[3]; 
				int[] y = new int[3];
				
				x[0] = w.x()+4;
				x[1] = w.x()+(w.w()/2);
				x[2] = w.x()+w.w()-4;
				y[0] = w.y()+4;
				y[1] = w.y()+w.h()-4;
				y[2] = w.y()+4;
				
				Draw.setFill(null);
				Draw.setStroke(w.borderColor());
				Draw.polygon(x, y, 3);
			}});
	}
	
	/** Resets the position of the scroll widgets. 
	 * Note: takes into account what BAR_WIDTH is at the time of this call, regardless of what it was before. **/
	protected void setScrollWidgetPosition() {
		_scrollUp.setLocation(_x+_w-_BAR_WIDTH, _y);
		_scrollUp.setSize(_BAR_WIDTH, _BAR_WIDTH);
		_scrollDown.setLocation(_x+_w-_BAR_WIDTH, _y+_h-_BAR_WIDTH);
		_scrollDown.setSize(_BAR_WIDTH, _BAR_WIDTH);
		_scrollBar.setLocation(_x+_w-_BAR_WIDTH, _y+_BAR_WIDTH);
		_scrollBar.setSize(_BAR_WIDTH, _h-_BAR_WIDTH*2);
	}
	
	/** Calls Update() on all the scroll widgets. **/
	protected void updateScrollWidgets() {
		_scrollUp.update();
		_scrollDown.update();
		_scrollBar.update();
	}
	
	/** Calls Draw() on all the scroll widgets. **/
	protected void drawScrollWidgets() {
		_scrollUp.draw();
		_scrollDown.draw();
		_scrollBar.draw();
	}
}

package simple.gui;

import java.awt.*;

import simple.run.Input;

/**
 * Generic Widget class. All widgets inherit from this class.
 * <P>The idea behind widgets is to create interfaceable components to perform tasks. These could be buttons, scrollboxes, textboxes, or possibly panels to contain other widgets.
 * Many generic widgets are defined here, but the goal behind this class was to create a system to easily allow you to make your own components.
 * <P>Note that if you decide to make your own Widget object, they have access to a DrawObject named draw. Drawing should be done with this object.
 * <P>Widget is an abstract class, as it's meant to be the framework for other widgets. 
 * @author Austin Mitchell
 *
 */
public abstract class Widget {	
	private static Color _DEFAULT_FILLCOLOR = Color.WHITE;
	private static Color _DEFAULT_BORDERCOLOR = Color.BLACK;
	private static Color _DEFAULT_TEXTAREACOLOR = Color.WHITE;
	private static Color _DEFAULT_TEXTCOLOR = Color.BLACK;
	private static Font  _DEFAULT_FONT = new Font("Consolas", Font.PLAIN, 12);
	
	/** Returns the default fillColor for all widgets. **/
	public static Color getDefaultFillColor() { return _DEFAULT_FILLCOLOR; }
	/** Returns the default borderColor for all widgets. **/
	public static Color getDefaultBorderColor() { return _DEFAULT_BORDERCOLOR; }
	/** Returns the default textAreaColor for all widgets. **/
	public static Color getDefaultTextAreaColor() { return _DEFAULT_TEXTAREACOLOR; }
	/** Returns the default textColor for all widgets. **/
	public static Color getDefaultTextColor() { return _DEFAULT_TEXTCOLOR; }
	/** Returns the default textFont for all widgets. **/
	public static Font getDefaultFont() { return _DEFAULT_FONT; }
	
	/** Sets the default fillColor for all widgets. **/
	public static void setDefaultFillColor(Color newFillColor) { _DEFAULT_FILLCOLOR = newFillColor; }
	/** Sets the default borderColor for all widgets. **/
	public static void setDefaultBorderColor(Color newBorderColor) { _DEFAULT_BORDERCOLOR = newBorderColor; }
	/** Sets the default textAreaColor for all widgets. **/
	public static void setDefaultTextAreaColor(Color newTextAreaColor) { _DEFAULT_TEXTAREACOLOR = newTextAreaColor; }
	/** Sets the default textColor for all widgets. **/
	public static void setDefaultTextColor(Color newTextColor) { _DEFAULT_TEXTCOLOR = newTextColor; }
	/** Sets the default color variables. **/
	public static void setDefaultWidgetColors(Color newFillColor, Color newBorderColor, Color newTextAreaColor, Color newTextColor) { 
		_DEFAULT_FILLCOLOR = newFillColor; 
		_DEFAULT_BORDERCOLOR = newBorderColor; 
		_DEFAULT_TEXTAREACOLOR = newTextAreaColor;
		_DEFAULT_TEXTCOLOR = newTextColor; 
	}
	/** Sets the default textFont for all widgets. **/
	public static void setDefaultFont(Font f_) { _DEFAULT_FONT = f_; }
	
	/** Method to determine whether two widgets intersect within a bounding box by default. Behaviour may be changed within individual widget classes with the non-static method. 
	 * Note that as it stands, given two widgets a and b, Widget.intersectsWith(a, b) and Widget.intersectsWith(b, a) may give different results, as w1 does not take into account w2's behaviour for intersectsWith(w1).
	 * @param w1		Widget object whose intersectsWith(Widget w) will be called. 
	 * @param w2		Widget object whose only relevant pieces of information in this method are its x, y, w, and h values. **/
	public static boolean intersectsWith(Widget w1, Widget w2) {
		return w1.intersectsWith(w2);
	}
	
	/** Bottom-left (visually top-left) coordinate of the widget. **/
	protected int _x, _y; 
	/** Width (w) and height (h) of the widget. **/
	protected int _w, _h;
	/** Widget's states relative to the mouse. **/
	protected boolean _hovering, _clicking, _clicked, _hasEntered;
	/** Widget's state of allowed interaction (e.g. Mouse, Keyboard). **/
	protected boolean _enabled; 
	/** Widget's state of visibility. Typically just affects whether the Draw() method does anything or not. **/
	protected boolean _visible;
	/** Another state of allowed interaction specifically with the mouse. Note the difference between isBlocked and enabled, apparent when you draw a disabled button vs. a blocked button. **/
	protected boolean _blocked;
	
	/** Widget's current color for filling miscellaneous space occupied with nothing else, and other defined areas. **/
	protected Color _fillColor;
	/** Widget's current bordering color for the widget's bounding box and text area, and other defined areas. **/
	protected Color _borderColor;
	/** Widget's current color for the area which will ahve text rendered upon, and other defined areas.. **/
	protected Color _textAreaColor;
	/** Widget's current color for rendering text, and other defined areas. **/
	protected Color _textColor;
	/** Widget's current font for rendering text. **/
	protected Font _font;
	
	/** Allows the button to implement a specified method for drawing. Will be done after drawing the image. **/
	protected CustomDraw _customDrawBefore;
	protected CustomDraw _customDrawAfter;
	protected CustomDraw _widgetControlledDraw;
	
	/** Returns the widget's x variable. **/
	public int x() { return _x; }
	/** Returns the widget's y variable. **/
	public int y() { return _y; }
	/** Returns the widget's w variable. **/
	public int w() { return _w; }
	/** Returns the widget's h variable. **/
	public int h() { return _h; }
	/** Returns the widget's enabled variable. **/
	public boolean enabled() { return _enabled; }
	/** Returns the widget's visible variable. **/
	public boolean visible() { return _visible; }
	/** Returns the widget's blocked variable. **/
	public boolean blocked() { return _blocked; }
	
	/** Returns the widget's fillColor variable **/
	public Color fillColor() { return _fillColor; }
	/** Returns the widget's borderColor variable **/
	public Color borderColor() { return _borderColor; }
	/** Returns the widget's textAreaColor variable **/
	public Color textAreaColor() { return _textAreaColor; }
	/** Returns the widget's textColor variable **/
	public Color textColor() { return _textColor; }
	/** Returns the widget's textFont variable **/
	public Font font() { return _font; }

	/** Sets the widget's x variable **/
	public void setX(int x) { setLocation(x, _y); }
	/** Sets the widget's y variable **/
	public void setY(int y) { setLocation(_x, y); }
	/** Adds to the widget's x variable **/
	public void addX(int ix) { setX(_x+ix); }
	/** Adds to the widget's y variable **/
	public void addY(int iy) { setY(_y+iy); }
	/** Sets the widget's x and y variables **/
	public void setLocation(int x, int y) { _x = x; _y = y; }
	/** Sets the widget's w variable **/
	public void setWidth(int w) { setSize(w, _h); }
	/** Sets the widget's h variable **/
	public void setHeight(int h) { setSize(_w, h); }
	/** Sets the widget's w and h variables **/
	public void setSize(int w, int h) { _w = w; _h = h; }
	/** Sets the widget's enabled variable, and sets false for all mouse interaction variables **/
	public void setEnabled(boolean enabled) { 
		this._enabled = enabled; 
		if (!enabled) {
			this._hovering = false;
			this._clicking = false;
			this._clicked = false;
		}
	}
	/** Sets the widget's visble variable, and sets false for all mouse interaction variables **/
	public void setVisible(boolean visible) { 
		this._visible = visible;
		if (!visible) {
			this._hovering = false;
			this._clicking = false;
			this._clicked = false;
		}
	}
	/** Blocks the widget from mouse interaction for the next time update() is called. */
	public void blockWidget() { 
		_blocked = true;
		_hovering = false;
		_clicking = false;
		_clicked = false;
	}
	
	/** Sets the widget's fillColor variable **/
	public void setFillColor(Color fillColor) { _fillColor = fillColor;}
	/** Sets the widget's borderColor variable **/
	public void setBorderColor(Color borderColor) { _borderColor = borderColor; }
	/** Sets the widget's textAreaColor variable **/
	public void setTextAreaColor(Color textAreaColor) { _textAreaColor = textAreaColor; }
	/** Sets the widget's textColor variable **/
	public void setTextColor(Color textColor) { _textColor = textColor; }
	/** Sets all the widget's color variables **/
	public void setWidgetColors(Color fillColor, Color borderColor, Color textAreaColor, Color textColor) {
		setFillColor(fillColor);
		setBorderColor(borderColor);
		setTextAreaColor(textAreaColor);
		setTextColor(textColor);
	}
	/** Sets the widget's textFont variable **/
	public void setFont(Font font) { _font = font; }
	
	/** Sets the button's CustomDraw object. **/
	public void setCustomDrawBefore(CustomDraw customDrawBefore) { _customDrawBefore = customDrawBefore; }
	public void setCustomDrawAfter(CustomDraw customDrawAfter)   { _customDrawAfter  = customDrawAfter; }
	public void setWidgetControlledDraw(CustomDraw widgetControlledDraw) { _widgetControlledDraw = widgetControlledDraw; }

	/** Creates a widget with default dimensions. In some cases, such as a scrollListBox or certain panels, the dimensions are specified by the object rather than directly by the user. **/
	public Widget() { 
		this(0, 0, 10, 10);
	}
	/** Creates a widgets with specified dimensions. **/
	public Widget(int x, int y, int w, int h) { 
		if (_w < 0 || _h < 0) { throw new IndexOutOfBoundsException("Size parameters must be non-negative"); }
		_x=x; 
		_y=y; 
		_w=w; 
		_h=h; 
		
		_fillColor = _DEFAULT_FILLCOLOR;
		_borderColor = _DEFAULT_BORDERCOLOR;
		_textAreaColor = _DEFAULT_TEXTAREACOLOR;
		_textColor = _DEFAULT_TEXTCOLOR;
		_font = _DEFAULT_FONT;
		
		_enabled = true;
		_visible = true;
		_hovering = false;
		_clicking = false;
		_clicked = false;
		_hasEntered = false;
		_blocked = false;
	}
	
	/** Returns whether or not this widget intersects with another widget. Overwriting encouraged if alternate behavior is needed. **/
	public boolean intersectsWith(Widget other) {
		return intersectsWith(other.x(), other.y(), other.w(), other.h());
	}
	/** Returns whether or not this widget intersects with the specified bounding box. Overwriting encouraged if alternate behaviour is needed. **/
	public boolean intersectsWith(int x, int y, int w, int h) {
		return (this._x+this._w > x && x+w > this._x && this._y+this._h > y && y+h > this._y);
	}
	
	/** Returns whether the mouse's x and y coordinates are contained within the widgets bounds. Overwriting encourages if alternate behaviour is needed. **/
	public boolean containsMouse() {
		if (Input.mouseX() < _x+_w && Input.mouseX() > _x && Input.mouseY() < _y+_h && Input.mouseY() > _y) {
			return true && _visible;
		} else {
			return false;
		}
	}
	/** Returns the widget's hovering variable. 
	 * <P> Hovering is defined as the mouse being contained by the widget, but with the mouse unpressed.**/
	public boolean hovering() {
		return _hovering;
	}
	/** Returns the widget's clicking variable.
	 * <P> Clicking is defined as the mouse being contained by the widget, with the mouse currently pressing down the button. **/
	public boolean clicking() {
		return _clicking;
	}
	/** Returns the widget's clicked variable.
	 * <P> Clicked is defined as the mouse being contained by the widget, with the mouse unpressed, but was clicking in the direct previous frame. **/
	public boolean clicked() {
		return _clicked;
	}
	
	/** Updates the widget's state relative to the mouse. This must be physically called in your widget's local Update() function for any mouse activity
	 * to register. **/
	protected void updateClickingState() {
		if (_clicked) 
			_clicked = false;
		if (_blocked) {
			_blocked = false;
			_hovering = false;
			_clicking = false;
			_clicked = false;
		} else {
			if (containsMouse() && Input.mousePressed() && (_hovering||_clicking)) {
				_clicking = true && _enabled && _visible;
				_hovering = false;
			} else if (containsMouse() && _clicking && !Input.mousePressed()) {
				_clicked = true && _enabled && _visible;
				_clicking = false;
			} else {
				_clicking = false;
				_clicked = false;
				if (containsMouse() && !Input.mousePressed()) {
					_hovering = true && _visible;
				} else {
					_hovering = false;
				}
			}
		}
	}
	
	public final void update() {
	    updateClickingState();
	    if (_enabled && _visible) {
	        updateWidget();
	    }
	}
	public final void draw() {
        drawCustom(_customDrawBefore);
        if (_visible) {
            drawWidget();
        }
        drawCustom(_customDrawAfter);
    }
	
	/** Method which must be implemented by your widget. The status of the widget and it's info should be modified and regulated here every frame. **/
	protected abstract void updateWidget();
	/** Method which must be implemented by your widget. Any draw functions for your widget should be called here, and drawn every frame relative to the widget's status. **/
	protected abstract void drawWidget();  
	
	public void drawCustom(CustomDraw cd) {
	    if (cd != null) {
	        cd.draw(this);
	    }
	}
	
	/** Basic toString() function that returns the widget's class name, x and y coordinate, and width and height**/
	public String toString() {
		return this.getClass().getName() + " at position (" + _x + "; " + _y + ") of size " + _w + "; " + _h + ").";
	}
}

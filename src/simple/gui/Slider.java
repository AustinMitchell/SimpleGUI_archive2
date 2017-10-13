package simple.gui;

import simple.run.Input;

/** Silder widget class. Creates a slider object with minimum and maximum integer values. Value of the slider depends on where the mouse
 * was last clicking on the slider. Value starts at the minimum, and can be adjusted manually. **/
public class Slider extends Widget {
    /** Takes two ranges and a value, and converts the value from one range to an equivalent value in another range */
    protected static int scaleValue (float value, float lowa, float higha, float lowb, float highb) {
        return (int) (lowb + Math.round (((value-lowa) / (higha-lowa)) * (highb-lowb)));
    }
    
	protected int _value, _low, _high;
	protected boolean _isHorizontal, _isReversed;
	protected int _oldValue;
	protected boolean _valueChanged;
	protected boolean _mouseCanScroll;
	protected int _mouseScrollIncrement;
	protected boolean _holdingMouse;
	
	/** Returns the current value of the slider **/
	public int value() { return _value; }
	/** Returns the minimum value of the slider **/
	public int low() { return _low; }
	/** Returns the maximum value of the slider **/
	public int high() { return _high; }
	
	public boolean mouseCanScroll() { return _mouseCanScroll; }
	public int mouseScrollIncrement() { return _mouseScrollIncrement; }
	
	/** Returns whether or not the value of the slider has changed since last frame **/
	public boolean valueChanged() { return _valueChanged; }
	
	/** Sets the current value of the slider. Value is bounded within [low, high] **/
	public void setValue(int value) { _value = Math.max(Math.min(value, _high), _low); }
	/** Sets the new range for the slider. If newHigh is greater than newLow, they will switch. Value is bounded by [newLow, newHigh] **/
	public void setRange(int low, int high) { 
		_low = Math.min(low, high);
		_high = Math.max(low, high);
		setValue(_value);
	}
	
	/** Enables or disables ability to scroll with mouse wheel */
	public void setMouseCanScroll(boolean mouseCanScroll) { _mouseCanScroll = mouseCanScroll; }
	/** Sets mouse wheel scroll value increment */
	public void setMouseScrollIncrement(int mouseScrollIncrement) { _mouseScrollIncrement = mouseScrollIncrement; }
	
	/** Creates a new slider object with no given dimensions 
	 * @param low         Sets lower boundary for slider value
	 * @param high        Sets upper boundary for slider value 
	 * @param isRev		  Sets what end of the slider is high and what is low. 
	 * @param isHoriz	  Sets what the orientation of the slider is. **/
	public Slider (int low, int high, boolean isRev, boolean isHoriz) {
		this(0, 0, 10, 10, low, high, isRev, isHoriz);
	}
	/** Creates a new slider object with the given dimensions           
	 * @param low         Sets lower boundary for slider value
     * @param high        Sets upper boundary for slider value 
	 * @param isRev		  Sets what end of the slider is high and what is low. 
	 * @param isHoriz     Sets what the orientation of the slider is. **/
	public Slider (int x, int y, int w, int h, int low, int high, boolean isRev, boolean isHoriz) {
		super(x, y, w, h);

		_oldValue = low;
		_value = low;
		_low = low;
		_high = high;
		
		_enabled = true;
		_visible = true;

		_isReversed = isRev;
		_isHorizontal = isHoriz;
		
		_mouseCanScroll = true;
		_mouseScrollIncrement = 1;
		
		_holdingMouse = false;
	}

	@Override
	protected void updateWidget() {
		_valueChanged = (_oldValue != _value);
		_oldValue = _value;
		
		if (clicking()) {
		    _holdingMouse = true;
		} else if (_holdingMouse && !Input.mousePressed()) {
		    _holdingMouse = false;
		}
		    
		
	    if (_mouseCanScroll && hovering()) {
		    if (Input.mouseWheelUp()) {
		        _value = Math.max(_low, Math.min(_high, _value+_mouseScrollIncrement));
		    } else if (Input.mouseWheelDown()) {
		        _value = Math.max(_low, Math.min(_high, _value-_mouseScrollIncrement));
            }
	    }
		    
	    /* Normally if mouse goes out of range while the mouse button is pressed down, the clicking state is disabled.
	    We get around this by creating a custom mouse control */
	    if (_holdingMouse) {
			if (_isHorizontal) {
				_value = scaleValue(Math.max(Math.min(Input.mouseX(), _x+_w-10), _x+10), _x+10, _x+_w-10, _low, _high);
			} else {
				_value = scaleValue(Math.max(Math.min(Input.mouseY(), _y+_h-10), _y+10), _y+10, _y+_h-10, _low, _high);
			}
			
			if (_isReversed) {
			    /* Inverts the value between low and high. The initial value is purely based on mouse position going from
			    low x to high x, or low y to high y */
				_value = _low + _high - _value;
			}
	    }
	}

	@Override
	protected void drawWidget() {
		Draw.setColors(_fillColor, _borderColor);
		Draw.rect(_x, _y, _w, _h);

		Draw.setFill(_borderColor);
		if (!_isHorizontal) {
		    Draw.line(_x + _w/2, _y+10, _x + _w/2, _y+_h-10);
			if (_isReversed) {
			    Draw.rect(_x + _w/2 - 5, scaleValue(_low + _high - _value, _low, _high, _y, _y+_h - 20) + 7, 10, 6);
			} else {
			    Draw.rect(_x + _w/2 - 5, scaleValue(_value, _low, _high, _y, _y+_h - 20) + 7, 10, 6);
			}
		} else { 
		    Draw.line(_x+10, _y + _h/2, _x+_w-10, _y + _h/2);
			if (_isReversed) {
			    Draw.rect(scaleValue(_low + _high - _value, _low, _high, _x, _x+_w - 20) + 7, _y + _h/2 - 5, 6, 10);
			} else {
			    Draw.rect(scaleValue(_value, _low, _high, _x, _x+_w - 20) + 7, _y + _h/2 - 5, 6, 10);
			}
		}
	}
}


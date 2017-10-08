package simple.gui;

import simple.run.Input;

/** Silder widget class. Creates a slider object with minimum and maximum integer values. Value of the slider depends on where the mouse
 * was last clicking on the slider. Value starts at the minimum, and can be adjusted manually. **/
public class Slider extends Widget {
	protected int value, low, high;
	protected boolean isHorizontal, isReversed;
	protected int oldValue;
	protected boolean valueChanged;
	protected boolean mouseCanScroll;
	protected int mouseScrollIncrement;
	
	/** Returns the current value of the slider **/
	public int getValue() { return value; }
	/** Returns the minimum value of the slider **/
	public int getLow() { return low; }
	/** Returns the maximum value of the slider **/
	public int getHigh() { return high; }
	
	public boolean canMouseScroll() { return mouseCanScroll; }
	public int getMouseScrollIncrement() { return mouseScrollIncrement; }
	
	/** Returns whether or not the value of the slider has changed since last frame **/
	public boolean valueHasChanged() { return valueChanged; }
	
	/** Sets the current value of the slider. Value is bounded within [low, high] **/
	public void setValue(int newValue) { value = Math.max(Math.min(newValue, high), low); }
	/** Sets the new range for the slider. If newHigh is greater than newLow, they will switch. Value is bounded by [newLow, newHigh] **/
	public void setRange(int newLow, int newHigh) { 
		low = Math.min(newLow, newHigh);
		high = Math.max(newLow, newHigh);
		setValue(value);
	}
	
	public void setMouseCanScroll(boolean mouseCanScroll) { this.mouseCanScroll = mouseCanScroll; }
	public void setMouseScrollIncrement(int mouseScrollIncrement) { this.mouseScrollIncrement = mouseScrollIncrement; }
	
	/** Creates a new slider object with no given dimensions 
	 * @param isRev_		Sets what end of the slider is high and what is low. 
	 * @param isHoriz_		Sets what the orientation of the slider is. **/
	public Slider (int low_, int high_, boolean isRev_, boolean isHoriz_) {
		this(0, 0, 10, 10, low_, high_, isRev_, isHoriz_);
	}
	/** Creates a new slider object with the given dimensions 
	 * @param isRev_		Sets what end of the slider is high and what is low. 
	 * @param isHoriz_		Sets what the orientation of the slider is. **/
	public Slider (int x_, int y_, int w_, int h_, int low_, int high_, boolean isRev_, boolean isHoriz_) {
		super(x_, y_, w_, h_);

		oldValue = low_;
		value = low_;
		low = low_;
		high = high_;
		
		enabled = true;
		visible = true;

		isReversed = isRev_;
		isHorizontal = isHoriz_;
		
		mouseCanScroll = true;
		mouseScrollIncrement = 1;
	}

	// Cool function that takes two ranges and a value, and converts the value from one range to an equivalent value in another range
	private int scaleValue (float value, float lowa, float higha, float lowb, float highb) {
		return (int) (lowb + Math.round (((value-lowa) / (higha-lowa)) * (highb-lowb)));
	}

	@Override
	public void update() {
		valueChanged = (oldValue != value);
		oldValue = value;
		
		if (!enabled || !visible)
			return;
			
		updateClickingState();
	    if (mouseCanScroll && isHovering()) {
		    if (Input.mouseWheelUp()) {
		        value = Math.max(low, Math.min(high, value+mouseScrollIncrement));
		    } else if (Input.mouseWheelDown()) {
		        value = Math.max(low, Math.min(high, value-mouseScrollIncrement));
            }
	    }
		    
	    if (isClicking()) {
			if (isHorizontal) {
				value = scaleValue(Math.max(Math.min(Input.mouseX(), x+w-10), x+10), x+10, x+w-10, low, high);
			} else {
				value = scaleValue(Math.max(Math.min(Input.mouseY(), y+h-10), y+10), y+10, y+h-10, low, high);
			}
			
			if (isReversed) {
				value = low + high - value;
			}
	    }
	}

	@Override
	public void draw() {
		if (!visible)
			return;
		
		Draw.setColors(fillColor, borderColor);
		Draw.rect(x, y, w, h);

		Draw.setFill(borderColor);
		if (!isHorizontal) {
		    Draw.line(x + w/2, y+10, x + w/2, y+h-10);
			if (isReversed) {
			    Draw.rect(x + w/2 - 5, scaleValue(low + high - value, low, high, y, y+h - 20) + 7, 10, 6);
			} else {
			    Draw.rect(x + w/2 - 5, scaleValue(value, low, high, y, y+h - 20) + 7, 10, 6);
			}
		} else { 
		    Draw.line(x+10, y + h/2, x+w-10, y + h/2);
			if (isReversed) {
			    Draw.rect(scaleValue(low + high - value, low, high, x, x+w - 20) + 7, y + h/2 - 5, 6, 10);
			} else {
			    Draw.rect(scaleValue(value, low, high, x, x+w - 20) + 7, y + h/2 - 5, 6, 10);
			}
		}
	}
}


package simple.gui;

//Creates a slider with a given range, associates value according to where the mouse is on the slider.
public class Slider extends Widget {
	private int value, low, high;
	private boolean isHorizontal, isReversed;
	private int oldValue;
	private boolean valueChanged;
	
	public int getValue() { return value; }
	public int getLow() { return low; }
	public int getHigh() { return high; }
	
	public boolean valueHasChanged() { return valueChanged; }
	
	
	public void setValue(int newValue) { value = Math.max(Math.min(newValue, high), low); }
	public void setRange(int newLow, int newHigh) { 
		low = newLow;
		high = newHigh;
		setValue(value);
	}
	
	public Slider (int low_, int high_, boolean isRev_, boolean isHoriz_) {
		this(0, 0, 10, 10, low_, high_, isRev_, isHoriz_);
	}
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
		if (containsMouse() && input.mousePressed()) {
			if (isHorizontal) {
				value = scaleValue(Math.max(Math.min(input.mouseX(), x+w-10), x+10), x+10, x+w-10, low, high);
			} else {
				value = scaleValue(Math.max(Math.min(input.mouseY(), y+h-10), y+10), y+10, y+h-10, low, high);
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
		
		draw.setColors(fillColor, borderColor);
		draw.rect(x, y, w, h);

		draw.setFill(borderColor);
		if (!isHorizontal) {
			draw.line(x + w/2, y+10, x + w/2, y+h-10);
			if (isReversed) {
				draw.rect(x + w/2 - 5, scaleValue(low + high - value, low, high, y, y+h - 20) + 7, 10, 6);
			} else {
				draw.rect(x + w/2 - 5, scaleValue(value, low, high, y, y+h - 20) + 7, 10, 6);
			}
		} else { 
			draw.line(x+10, y + h/2, x+w-10, y + h/2);
			if (isReversed) {
				draw.rect(scaleValue(low + high - value, low, high, x, x+w - 20) + 7, y + h/2 - 5, 6, 10);
			} else {
				draw.rect(scaleValue(value, low, high, x, x+w - 20) + 7, y + h/2 - 5, 6, 10);
			}
		}
	}
}


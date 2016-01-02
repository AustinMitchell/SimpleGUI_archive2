package simple.gui;

import java.awt.*;

/** Widget object. Creates a clickable button, intended to be tied to an event. Changes colour according to current widget status.
 * 
 * <P> TODO: create functionality for method attachment. **/
public class Button extends Widget {
	/** Colour fullness displayed. A value of 1 means the rgb values will be displayed at 100%. A value of 0.5 means rgb values 
	 * of 50%. **/
	protected float clrRatio;
	/** Text displayed at the center of the button. **/
	protected String text;
	/** Image displayed on the button's background. **/
	protected ImageBox imageBox;

	/** Returns the button's text variable. **/
	public String getText() { return text; }
	/** Returns the button's Image object of the imageBox variable. **/
	public Image getImage() { return imageBox.getImage(); }
	
	/** Sets the button's text variable. **/
	public void setText(String text) { this.text = text; }
	/** Sets the button's Image object of the imageBox variable**/
	public void setImage(Image image) { imageBox.setImage(image); }
	
	/** Sets the button's x and y coordinates, as well as shifts the imageBox. **/
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		imageBox.setLocation(x, y);
	}
	/** Sets the button's x coordinate, as well as shifts the imageBox. **/
	public void setX(int x) { setLocation(x, this.y); }
	/** Sets the button's y coordinate, as well as shifts the imageBox. **/
	public void setY(int y) { setLocation(this.x, y); }
	
	/** Sets the button's width and height, as well as resizes the imageBox. **/
	public void setSize(int w, int h) {
		super.setSize(w, h);
		imageBox.setSize(w, h);
	}
	/** Sets the button's width, as well as resizes the imageBox. **/
	public void setWidth(int w) { setSize(w, this.h); }
	/** Sets the button's height, as well as resizes the imageBox. **/
	public void setHeight(int h) { setSize(this.w, h); }
	
	public Button() {
		this(0, 0, 10, 10, "", null);
	}
	/** Creates a button with unspecified position and size, for the case where it's irrelevant. **/
	public Button(String text, Image image) {
		this(0, 0, 10, 10, text, image);
	}
	/** Creates a button with position, size and a title **/
	public Button(int x, int y, int w, int h, String text) {
		this(x, y, w, h, text, null);
	}
	public Button(int x, int y, int w, int h) {
		this(x, y, w, h, null, null);
	}
	/** Create's a Button object. **/
	public Button(int x, int y, int w, int h, String text, Image image) {
		super(x, y, w, h);
		this.text = (text==null)?"":text;
		this.imageBox = new ImageBox(x, y, w, h, image);
		
		this.clicking = false;
		this.enabled = true;

		this.clrRatio = 0.84f;
	}
	
	/** Updates the widget's status. **/
	public void update() {
		if (!enabled || !visible) 
			return;
		updateClickingState();
	}
	
	/** Draws the button to the SimpleGUIApp's graphics buffer. **/
	public void draw () {
		if (!visible) 
			return;
		
		if (!enabled) {
			clrRatio = 0.8f;
		} else if (blocked) {
			clrRatio = 1f;
		} else if (isClicking()) {
			clrRatio = 0.9f;
		} else if (isHovering()) {
			clrRatio = 0.94f;
		} else { 
			clrRatio = 1f;
		}

		draw.setFill(DrawModule.scaleColor(fillColor, clrRatio));
		draw.setStroke(DrawModule.scaleColor(borderColor, clrRatio));
		draw.rect(x, y, w, h);
		
		imageBox.draw();

		if (customDrawObject != null) {
			customDrawObject.draw(this);
		}
		
		if (text != "") {
			FontMetrics fm = draw.getFontMetrics(font);
			draw.setFont(font);
			draw.setStroke(DrawModule.scaleColor(textColor, clrRatio));
			draw.textCentered(text, x+w/2, y+h/2);
		}
	}
}

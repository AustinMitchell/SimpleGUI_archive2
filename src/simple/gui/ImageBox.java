package simple.gui;

public class ImageBox extends Widget {
	/** Image object to draw. **/
	protected Image image;
	/** State to determine whether to draw the image with the center at (x, y) or the corner at (x, y). **/
	protected boolean drawCentered;
	/** Angle to draw the image at. Works in terms of radians. Note that if this value is not 0, it will always draw the image rotated and centered even if drawCentered is set to false. **/
	protected double angle;
	
	/** Returns the imageBox's Image object. **/
	public Image getImage() { return image; }
	/** Returns the imageBox's angle. **/
	public double getAngle() { return angle; }
	
	/** Sets the imageBox's Image object. Calls the image's resize() function to scale it to the size of the ImageBox. **/
	public void setImage(Image image) { this.image = image.resize(w, h); }
	/** Sets whether the image to to be drawn centered to the (x, y) position or at the corner. **/
	public void setDrawCentered(boolean drawCentered) { this.drawCentered = drawCentered; }
	/** Sets the angle to draw the imageBox. **/
	public void setAngle(double angle) { this.angle = angle; }
	
	/** Sets the imageBox's size, and resizes the Image object as well. **/
	public void setSize(int w, int h) {
		super.setSize(w, h);
		if (image != null)
			image = image.resize(this.w, this.h);
	}
	/** Sets the imageBox's width, and resizes the Image object as well. **/
	public void setWidth(int w) { setSize(w, this.h); }
	/** Sets the imageBox's height, and resizes the Image object as well. **/
	public void setHeight(int h) { setSize(this.w, h); }

	/** Constructor which only takes an Image object. Initial position will be at (0, 0), and the size will be the size of the Image object. **/
	public ImageBox(Image image_) {
		this(0, 0, image_);
	}
	/** This constructor is the same as ImageBox(Image image_), but it also sets the initial position. **/
	public ImageBox(int x_, int y_, Image image_) {
		setLocation(x_, y_);
		drawCentered = false;
		if (image_ != null) { 
			image = image_;
			setSize(image.getWidth(), image.getHeight());
		} else {
			setSize(1, 1);
		}
	}
	/** Constructor which sets the imageBox's position and size, and then scales the Image object's size to match the size of the imageBox. **/
	public ImageBox(int x_, int y_, int w_, int h_, Image image_) {
		super(x_, y_, w_, h_);
		drawCentered = false;
		if (image_ != null) 
			image = image_.resize(w, h);
	}
	
	/** Updates the state of the imageBox relative to the mouse. **/
	public void update() {
		if (!enabled || !visible) 
			return;
		updateClickingState();
	}
	
	/** Draws the imageBox. If drawCentered is true, it will use the (x, y) position of the imageBox as the center point rather than the corner.
	 * <P>Note that it is up to the programmer to understand that in drawCentered mode, the width and height variables still refer to the overall
	 * size of the image, rather than the radii. The radius in terms of x or y will respectively be w/2 and h/2. **/
	public void draw() {
		if (!visible) {
			return;
		}
		if (image != null) {
			if (angle != 0) {
				draw.imageRotated(this.image, x, y, angle);
			} else if (drawCentered) {
				draw.imageCentered(this.image, x, y);
			} else {
				draw.image(this.image, x, y);
			}
		}
	}
}

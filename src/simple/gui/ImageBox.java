package simple.gui;

public class ImageBox extends Widget {
	/** Image object to draw. **/
	protected Image image, baseImage;
	/** State to determine whether to draw the image with the center at (x, y) or the corner at (x, y). **/
	protected boolean drawCentered;
	/** Angle to draw the image at. Works in terms of radians. Note that if this value is not 0, it will always draw the image rotated and centered even if drawCentered is set to false. **/
	protected double angle;
	
	protected Image.Orientation orientation;
	
	/** Returns the imageBox's Image object. **/
	public Image getImage() { return image; }
	/** Returns the imageBox's angle. **/
	public double getAngle() { return angle; }
	
	public Image.Orientation getOrientation() { return orientation; }
	
	/** Sets the imageBox's Image object. Calls the image's resize() function to scale it to the size of the ImageBox. **/
	public void setImage(Image image) { 
		if (image == null) {
			baseImage = null;
			this.image = null;
		} else {
			Image rotatedImage = image.reorient(orientation);
			this.baseImage = rotatedImage; 
			this.image = rotatedImage.resize(w, h); 
		}
	}
	/** Sets whether the image to to be drawn centered to the (x, y) position or at the corner. **/
	public void setDrawCentered(boolean drawCentered) { this.drawCentered = drawCentered; }
	/** Sets the angle to draw the imageBox. **/
	public void setAngle(double angle) { this.angle = angle; }
	
	/** Sets the imageBox's size, and resizes the Image object as well. **/
	public void setSize(int w, int h) {
		super.setSize(w, h);
		if (baseImage != null) {
			image = baseImage.resize(this.w, this.h);
		}
	}
	/** Sets the imageBox's width, and resizes the Image object as well. **/
	public void setWidth(int w) { setSize(w, this.h); }
	/** Sets the imageBox's height, and resizes the Image object as well. **/
	public void setHeight(int h) { setSize(this.w, h); }
	
	public void setScaledWidth(int w) { 
		if (baseImage == null) {
			double ratio =  w/(double)this.w;
	    	setSize(w, (int)(this.h*ratio));
		} else {
			image = null;
		    Image tmp = baseImage.resizeScaledWidth(w);
			setSize(tmp.getWidth(), tmp.getHeight());
			image = tmp;
		}
	}
	public void setScaledHeight(int h) { 
		if (baseImage == null) {
			double ratio =  h/(double)this.h;
	    	setSize((int)(this.w*ratio), h);
		} else {
			image = null;
		    Image tmp = baseImage.resizeScaledHeight(h);
			setSize(tmp.getWidth(), tmp.getHeight());
			image = tmp;
		}
	}
	
	public void setOrientation(Image.Orientation orientation) {
		if (this.orientation != orientation) {
			this.orientation = orientation;
			baseImage = baseImage.reorient(orientation);
		    Image tmp = image.reorient(orientation);
		    image = null;
			setSize(tmp.getWidth(), tmp.getHeight());
			image = tmp;
		}
	}

	public ImageBox() {
		this(0, 0, null, Image.Orientation.UP);
	}
	
	/** Constructor which only takes an Image object. Initial position will be at (0, 0), and the size will be the size of the Image object. **/
	public ImageBox(Image image) {
		this(0, 0, image);
	}
	public ImageBox(Image image, Image.Orientation startingOrientation) {
		this(0, 0, image, startingOrientation);
	}
	/** This constructor is the same as ImageBox(Image image_), but it also sets the initial position. **/
	public ImageBox(int x, int y, Image image) {
		this(x, y, image, (image==null) ? (Image.Orientation.UP) : (image.getOrientation()));
	}
	public ImageBox(int x, int y, Image image, Image.Orientation startingOrientation) {
		super();
		setLocation(x, y);
		orientation = startingOrientation;
		drawCentered = false;
		if (image != null) {
			baseImage = new Image(image);
			this.image = baseImage;
			setSize(image.getWidth(), image.getHeight());
		} else {
			setSize(1, 1);
		}
	}
	/** Constructor which sets the imageBox's position and size, and then scales the Image object's size to match the size of the imageBox. **/
	public ImageBox(int x, int y, int w, int h, Image image) {
		this(x, y, w, h, image, 
				(image==null) ? (Image.Orientation.UP) : (image.getOrientation()));
	}
	
	public ImageBox(int x, int y, int w, int h, Image image, Image.Orientation startingOrientation) {
		super(x, y, w, h);
		orientation = startingOrientation;
		drawCentered = false;
		if (image != null) {
			baseImage = new Image(image);
			baseImage.setOrientation(startingOrientation);
			this.image = baseImage.resize(w, h);
		}
	}
	
	/** Updates the state of the imageBox relative to the mouse. **/
	public void update() {
		if (!enabled || !visible) {
			return;
		}
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

package simple.gui;

public class ImageBox extends Widget {
	/** Image object to draw. **/
	protected Image _image, _baseImage;
	/** State to determine whether to draw the image with the center at (x, y) or the corner at (x, y). **/
	protected boolean _drawCentered;
	/** Angle to draw the image at. Works in terms of radians. Note that if this value is not 0, it will always draw the image rotated and centered even if drawCentered is set to false. **/
	protected double _angle;
	
	protected Image.Orientation _orientation;
	
	/** Returns the imageBox's Image object. **/
	public Image image() { return _image; }
	/** Returns the imageBox's backing Image object. Used for when resizing so you don't lose quality. **/
    public Image baseImage() { return _baseImage; }
	/** Returns the imageBox's angle. **/
	public double angle() { return _angle; }
	/** Returns the images orientation */
	public Image.Orientation orientation() { return _orientation; }
	
	/** Sets the imageBox's Image object. Calls the image's resize() function to scale it to the size of the ImageBox. **/
	public void setImage(Image image) { 
		if (image == null) {
			_baseImage = null;
			this._image = null;
		} else {
			Image rotatedImage = image.reorient(_orientation);
			this._baseImage = rotatedImage; 
			this._image = rotatedImage.resize(_w, _h); 
		}
	}
	/** Sets whether the image to to be drawn centered to the (x, y) position or at the corner. **/
	public void setDrawCentered(boolean drawCentered) { _drawCentered = drawCentered; }
	/** Sets the angle to draw the imageBox. **/
	public void setAngle(double angle) { _angle = angle; }
	
	/** Sets the imageBox's size, and resizes the Image object as well. **/
	public void setSize(int w, int h) {
		super.setSize(w, h);
		if (_baseImage != null) {
			_image = _baseImage.resize(_w, _h);
		}
	}
	
	/** Sets the width while maintaining width-to-height ratio */
	public void setScaledWidth(int w) { 
		if (_baseImage == null) {
			double ratio =  w/(double)this._w;
	    	setSize(w, (int)(this._h*ratio));
		} else {
			_image = null;
		    Image tmp = _baseImage.resizeScaledWidth(w);
			setSize(tmp.w(), tmp.h());
			_image = tmp;
		}
	}
	/** Sets the height while maintaining the width-to-height ratio */
	public void setScaledHeight(int h) { 
		if (_baseImage == null) {
			double ratio =  h/(double)this._h;
	    	setSize((int)(this._w*ratio), h);
		} else {
			_image = null;
		    Image tmp = _baseImage.resizeScaledHeight(h);
			setSize(tmp.w(), tmp.h());
			_image = tmp;
		}
	}
	
	/** Sets the image to a new orientation */
	public void setOrientation(Image.Orientation orientation) {
		if (this._orientation != orientation) {
			this._orientation = orientation;
			_baseImage = _baseImage.reorient(orientation);
		    Image tmp = _image.reorient(orientation);
		    _image = null;
			setSize(tmp.w(), tmp.h());
			_image = tmp;
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
		this(x, y, image, (image==null) ? (Image.Orientation.UP) : (image.orientation()));
	}
	public ImageBox(int x, int y, Image image, Image.Orientation startingOrientation) {
		super();
		setLocation(x, y);
		_orientation = startingOrientation;
		_drawCentered = false;
		if (image != null) {
			_baseImage = new Image(image);
			this._image = _baseImage;
			setSize(image.w(), image.h());
		} else {
			setSize(1, 1);
		}
	}
	/** Constructor which sets the imageBox's position and size, and then scales the Image object's size to match the size of the imageBox. **/
	public ImageBox(int x, int y, int w, int h, Image image) {
		this(x, y, w, h, image, 
				(image==null) ? (Image.Orientation.UP) : (image.orientation()));
	}
	
	public ImageBox(int x, int y, int w, int h, Image image, Image.Orientation startingOrientation) {
		super(x, y, w, h);
		_orientation = startingOrientation;
		_drawCentered = false;
		if (image != null) {
			_baseImage = new Image(image);
			_baseImage.setOrientation(startingOrientation);
			this._image = _baseImage.resize(w, h);
		}
	}
	
	/** Sets the base image to the current image. Important for manipulating pixel arrays, as the backing image will not be changed, only the 
	 * current one. **/
	public void resetBaseImage() {
		_baseImage = new Image(_image);
	}
	
	protected void updateWidget() {}
	
	/** Draws the imageBox. If drawCentered is true, it will use the (x, y) position of the imageBox as the center point rather than the corner.
	 * <P>Note that it is up to the programmer to understand that in drawCentered mode, the width and height variables still refer to the overall
	 * size of the image, rather than the radii. The radius in terms of x or y will respectively be w/2 and h/2. **/
	protected void drawWidget() {

		if (_image != null) {
			if (_angle != 0) {
				Draw.imageRotated(this._image, _x, _y, _angle);
			} else if (_drawCentered) {
			    Draw.imageCentered(this._image, _x, _y);
			} else {
			    Draw.image(this._image, _x, _y);
			}
		}
	}
}

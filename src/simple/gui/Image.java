package simple.gui;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/** Object that works as an overhead for the BufferedImage object. Has its own limited functionality, but if you wish to work directly 
 * with the BufferedImage, you can get this from the Image object. Ths object's funcionality is currently limited as it's meant to have
 * only simple and easy to understand usage. **/
public final class Image {
    public static class ResLoader {
        public static InputStream load(String path) {
            InputStream input = ResLoader.class.getResourceAsStream(path);
            if (input == null) {
                input = ResLoader.class.getResourceAsStream("/"+path);
            }
            return input;
        }
    }
    
	public static enum Orientation {
		UP, RIGHT, DOWN, LEFT;
		public static String toString(Orientation o) {
			switch(o) {
				case UP:
					return "UP";
				case RIGHT:
					return "RIGHT";
				case DOWN:
					return "DOWN";
				case LEFT:
					return "LEFT";
				default:
					return "";
			}
		}
		public static int toInt(Orientation o) {
			switch(o) {
				case UP:
					return 0;
				case RIGHT:
					return 1;
				case DOWN:
					return 2;
				case LEFT:
					return 3;
				default:
					return 0;
			}
		}
		public static Orientation fromInt(int i) {
			// same as i%4 but faster
			switch(i&3) {
			case 0:
				return UP;
			case 1:
				return RIGHT;
			case 2:
				return DOWN;
			case 3:
				return LEFT;
			default:
				return UP;
			}
		}
			
		public static int[][] getRotationMatrix(Orientation o1, Orientation o2) {
			int[][] matrix = new int[2][2];
			switch(Math.floorMod(o2.ordinal() - o1.ordinal(), 4)) {
				case 0:
					matrix[0][0]=1; matrix[0][1]=0;
					matrix[1][0]=0; matrix[1][1]=1;
					break;
				case 1:
					matrix[0][0]=0; matrix[0][1]=-1;
					matrix[1][0]=1; matrix[1][1]=0;
					break;
				case 2:
					matrix[0][0]=-1; matrix[0][1]=0;
					matrix[1][0]=0; matrix[1][1]=-1;
					break;
				case 3:
					matrix[0][0]=0; matrix[0][1]=1;
					matrix[1][0]=-1; matrix[1][1]=0;
					break;
			}
			return matrix;
		}
	}
	
    private BufferedImage _image;            
    private String _filename;                   
    private int _w, _h;          
    private Orientation _orientation;

    /** Returns the width of the image. **/
    public int w() { return _w; }
    /** Returns the height of the image. **/
    public int h() { return _h; }
    /** Returns orientation of the image **/
    public Orientation orientation() { return _orientation; }
    /** Returns the BufferedImage object from the image. **/
    public BufferedImage bufferedImage() { return _image; }
    public Graphics2D graphics2D() { return _image.createGraphics(); }
    /** Returns the filename of the image. If it is an image created without a filename, then a default filename is used:
     *the String (image width) + "-by-" + (image height). **/
    public String fileName() { return _filename; }
    
    /** Set the orientation of the image. This does not affect the image, but it will affect the output of reorient() **/
    public void setOrientation(Orientation orientation) { _orientation = orientation; }
    
    /** Sets the image's filename (name stored in the object, not the actual filename on the computer). **/
    public void setFileName(String fileName) { _filename = fileName; }
    
    /** Creates an empty image with a default filename of the given width and height. **/
    public Image(int w, int h) {
        if (_w < 0) throw new IllegalArgumentException("width must be non-negative");
        if (_h < 0) throw new IllegalArgumentException("height must be non-negative");
        _w = w;
        _h = h;
        _image = new BufferedImage(_w, _h, BufferedImage.TYPE_INT_ARGB);
        // set to TYPE_INT_ARGB to support transparency
        _filename = _w + "-by-" + _h;
        _orientation = Orientation.UP;
    }

    /** Creates an image using the same filename and color data from another image. **/
    public Image(Image imageToCopy) {
        _w = imageToCopy.w();
        _h = imageToCopy.h();
        _image = new BufferedImage(_w, _h, BufferedImage.TYPE_INT_ARGB);
        _filename = imageToCopy.fileName();
        setPixels(imageToCopy.getPixelsNoCopy());
        _orientation = imageToCopy.orientation();
    }
    
    /** Creates and image with a default filename using the color data from a BufferedImage. **/
    public Image(BufferedImage imageToCopy) {
        _w = imageToCopy.getWidth();
        _h = imageToCopy.getHeight();
        _image = new BufferedImage(_w, _h, BufferedImage.TYPE_INT_ARGB);
        _filename = _w + "-by-" + _h;
        setPixels(((DataBufferInt)imageToCopy.getRaster().getDataBuffer()).getData());
        _orientation = Orientation.UP;
    }
    
    public Image(InputStream is) {
    	BufferedImage imageToCopy;
		try {
			BufferedImage temp = ImageIO.read(is);
	    	imageToCopy = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    	imageToCopy.createGraphics().drawImage(temp, 0, 0, null);
			
			_w = imageToCopy.getWidth();
			_h = imageToCopy.getHeight();
			_image = new BufferedImage(_w, _h, BufferedImage.TYPE_INT_ARGB);
			_filename = _w + "-by-" + _h;
			setPixels(((DataBufferInt)imageToCopy.getRaster().getDataBuffer()).getData());
			_orientation = Orientation.UP;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Could not open file stream: " + is);
		}
    }

    /** Creates a new image from a given filename. First searches through working directory, then looks at the directoy of 
     * the .class file. **/
    public Image(String filename) {
        _filename = filename;
        try {
            // try to read from file in working directory
            File file = new File(_filename);
            if (file.isFile()) {
            	BufferedImage temp = ImageIO.read(file);
    	    	_image = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
    	    	_image.createGraphics().drawImage(temp, 0, 0, null);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(_filename);
                if (url == null) { url = new URL(_filename); }
                _image = ImageIO.read(url);
            }
            _w  = _image.getWidth();
            _h = _image.getHeight();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + _filename);
        }
        _orientation = Orientation.UP;
    }

    /** Creates file from a File object. **/
    public Image(File file) {
        try { 
        	BufferedImage temp = ImageIO.read(file);
	    	_image = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    	_image.createGraphics().drawImage(temp, 0, 0, null);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (_image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
        _w  = _image.getWidth();
        _h = _image.getHeight();
        _filename = file.getName();
        _orientation = Orientation.UP;
    }
    
    /** Returns a copy of an image object resized to new bounds. Scales pixels by deciding which pixel has the best claim (which pixel is closest
     * to the empty pixel upon resize). Does not perform any anti-aliasing. WARNING: If you shrink an image and set that image to it, the
     * old data is permanently lost.
     * @param image			Image object to resize. 
     * @param newWidth		New width to scale image to.
     * @param newHeight		New height to scale image to. **/
    public static Image resize(Image image, int newWidth, int newHeight) {
    	return image.resize(newWidth, newHeight);
    }
    public static Image resizeScaledWidth(Image image, int newWidth) {
    	return image.resizeScaledWidth(newWidth);
    }
    public static Image resizeScaledHeight(Image image, int newHeight) {
    	return image.resizeScaledHeight(newHeight);
    }
    /** Returns a copy of the image resized to new bounds. Scales pixels by deciding which pixel has the best claim (which pixel is closest
     * to the empty pixel upon resize). Does not perform any anti-aliasing. WARNING: If you skrink the image and set this image to it, the
     * old data is permanently lost.
     * @param w		New width to scale image to.
     * @param h		New height to scale image to. **/
    public Image resize(int w, int h) {
    	Image newImage = new Image(w, h);
    	newImage.setOrientation(_orientation);
    	float newToOldScaleX = (float)_w/w;
    	float newToOldScaleY = (float)_h/h;
    	
    	int[] newImagePixels = newImage.getPixelsNoCopy();
    	int[] oldImagePixels = this.getPixelsNoCopy();
		for (int x=0; x<w; x++) {
			for (int y=0; y<h; y++) {
				newImagePixels[x + y*w] = oldImagePixels[(int)(x*newToOldScaleX) + _w*(int)(y*newToOldScaleY)];
    		}
		}
		return newImage;
    }
    public Image resizeScaledWidth(int w) {
    	double ratio =  w/(double)_w;
    	return resize(w, (int)(_h*ratio));
    }
    public Image resizeScaledHeight(int h) {
    	double ratio =  h/(double)_h;
    	return resize((int)(_w*ratio), h);
    }
    
    /** Returns a new image set to the new orientation given. **/
    public static Image reorient(Image image, Orientation orientation) {
    	return image.reorient(orientation);
    }
    /** Returns a new image set to the new orientation given. **/
    public Image reorient(Orientation orientation) {
    	int[][] matrix = Orientation.getRotationMatrix(_orientation, orientation);
    	int newWidth = Math.abs(_w*matrix[0][0] + _h*matrix[0][1]);
    	int newHeight = Math.abs(_w*matrix[1][0] + _h*matrix[1][1]);
    	Image newImage = new Image(newWidth, newHeight);
    	newImage.setOrientation(orientation);
    	
    	int[] newImagePixels = newImage.getPixelsNoCopy();
    	int[] oldImagePixels = this.getPixelsNoCopy();
    	int newx=0, newy=0;    	
    	for (int x=0; x<_w; x++) {
    		for (int y=0; y<_h; y++) {
    			newx = x*matrix[0][0] + y*matrix[0][1];
    			if (newx < 0) {
    				newx += newWidth;
    			}
    	    	newy = x*matrix[1][0] + y*matrix[1][1];
    	    	if (newy < 0) {
    				newy += newHeight;
    			}
    	    	
    	    	newImagePixels[newx + newy*newWidth] = oldImagePixels[x + _w*y];
    		}
    	}
    	return newImage;
    }
    
    /** Returns an image rotated 90 degrees clockwise by rotationsRight many times, and sets new orientation. A negative number means left turns. **/
    public static Image rotate(Image image, int rotationsRight) {
    	return image.rotate(rotationsRight);
    }
    /** Returns an image rotated 90 degrees clockwise by rotationsRight many times, and sets new orientation. A negative number means left turns. **/
    public Image rotate(int rotationsRight) {
    	return reorient(Orientation.fromInt(Orientation.toInt(_orientation)+rotationsRight));
    }
    
    /** Returns the color data of the image at the pixel (w, h) as an integer. Note that the point (0, 0) is the origin of the image, not the screen. **/
    public int get(int x, int y) {
        if (x < 0 || x >= _w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (_w-1) + ", recieved " + x);
        if (y < 0 || y >= _h) throw new IndexOutOfBoundsException("y must be between 0 and " + (_h-1) + ", recieved " + y);
        return _image.getRGB(x, y);
    }
    /** Returns the color data of the image at the pixel (w, h) as a Color object. Note that the point (0, 0) is the origin of the image, not the screen. **/
    public Color getColor(int x, int y) {
        if (x < 0 || x >= _w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (_w-1) + ", recieved " + x);
        if (y < 0 || y >= _h) throw new IndexOutOfBoundsException("y must be between 0 and " + (_h-1) + ", recieved " + y);
        return new Color(_image.getRGB(x, y), true);
    }
    /** Returns the color data of all the pixels as an array of integers. Note that this creates a copy of the original pixels. **/
    public int[] getPixels() {
    	int[] pixelData = new int[_w*_h];
    	System.arraycopy(((DataBufferInt)_image.getRaster().getDataBuffer()).getData(), 0, pixelData, 0, _w*_h);
    	return pixelData;
    }
    
    /** Returns the color data of all the pixels as an array of integers. Returns the base array. Changes to the returned array WILL modify the image data.
     * Important note: If Image is used in context of an ImageBox and not just a standalone image, you need to take into account that the backing image 
     * used to preserve image quality needs to be updated on changes made to the pixel array. <P>
     * Note that this is essentially the fastest way to change pixel data directly. **/
    public int[] getPixelsNoCopy() {
    	return ((DataBufferInt)_image.getRaster().getDataBuffer()).getData();
    }
    
    /** Sets the color data of the image at the pixel (w, h) using an integer. Note that th point (0, 0) is the origin of the image, not the screen. **/
    public void set(int x, int y, int color) {
        if (x < 0 || x >= _w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (_w-1) + ", recieved " + x);
        if (y < 0 || y >= _h) throw new IndexOutOfBoundsException("y must be between 0 and " + (_h-1) + ", recieved " + y);
        _image.setRGB(x, y, color);
    }
    /** Sets the color data of the image at the pixel (w, h) using a Color object. Note that th point (0, 0) is the origin of the image, not the screen. **/
    public void setColor(int x, int y, Color color) {
        if (x < 0 || x >= _w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (_w-1) + ", recieved " + x);
        if (y < 0 || y >= _h) throw new IndexOutOfBoundsException("y must be between 0 and " + (_h-1) + ", recieved " + y);
        if (color == null) throw new NullPointerException("can't set Color to null");
        _image.setRGB(x, y, color.getRGB());
    }
    /** Sets all the color data of the image from an array of integers. **/
    public void setPixels(int[] pixelData) {
    	System.arraycopy(pixelData, 0, ((DataBufferInt)_image.getRaster().getDataBuffer()).getData(), 0, _w*_h);
    }

    /** Draws an image to a Graphics2D object with the origin at point x, y on the screen. 
     * 	Don't call this yourself. Use the DrawModule image() method. **/
    public void draw(Graphics2D g, int x, int y) {
    	g.drawImage(_image, x, y, null);
    }
    /** Draws an image to a Graphics2D object with the center of the image at point x, y on the screen. 
     * 	Don't call this yourself. Use the DrawModule imageCentered() method. **/
    public void drawCentered(Graphics2D g, int x, int y) {
    	g.drawImage(_image, x-(int)_image.getWidth()/2, y-(int)_image.getHeight()/2, null);
    }
    /** Draws and image rotated by an angle to a Graphics2D object with the center of the image at point x, y on the screen. 
     * 	Don't call this yourself. Use the DrawModule imageRotated() method. **/
    public void drawRotated(Graphics2D g, int x, int y, double angle) {
    	g.rotate(angle, x, y);
    	g.drawImage(_image, x-(int)_image.getWidth()/2, y-(int)_image.getHeight()/2, null);
    	g.rotate(-angle, x, y);
    }

    /** Returns whether an object equals this Image object. They must both be Image objects, and their color data must match exactly. **/
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Image that = (Image) obj;
        if (this.w()  != that.w())  return false;
        if (this.h() != that.h()) return false;
        
        int[] thatImagePixels = that.getPixelsNoCopy();
    	int[] thisImagePixels = this.getPixelsNoCopy();
    	int size = _w*_h;
    	for (int i = 0; i < size; i++)
    		if (thisImagePixels[i] != thatImagePixels[i]) return false;
        return true;
    }
}
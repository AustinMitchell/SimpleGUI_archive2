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
	
    private BufferedImage image;            
    private String filename;                   
    private int w, h;          
    private Orientation orientation;

    /** Returns the width of the image. **/
    public int getWidth() { return w; }
    /** Returns the height of the image. **/
    public int getHeight() { return h; }
    /** Returns orientation of the image **/
    public Orientation getOrientation() { return orientation; }
    /** Returns the BufferedImage object from the image. **/
    public BufferedImage getBufferedImage() { return image; }
    public Graphics2D getGraphics() { return image.createGraphics(); }
    /** Returns the filename of the image. If it is an image created without a filename, then a default filename is used:
     *the String (image width) + "-by-" + (image height). **/
    public String getFileName() { return filename; }
    
    /** Set the orientation of the image. This does not affect the image, but it will affect the output of reorient() **/
    public void setOrientation(Orientation orientation) { this.orientation = orientation; }
    
    /** Sets the image's filename (name stored in the object, not the actual filename on the computer). **/
    public void setFileName(String newFileName) { filename = newFileName; }
    
    /** Creates an empty image with a default filename of the given width and height. **/
    public Image(int w_, int h_) {
        if (w < 0) throw new IllegalArgumentException("width must be non-negative");
        if (h < 0) throw new IllegalArgumentException("height must be non-negative");
        w = w_;
        h = h_;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = w + "-by-" + h;
        orientation = Orientation.UP;
    }

    /** Creates an image using the same filename and color data from another image. **/
    public Image(Image imageToCopy) {
        w = imageToCopy.getWidth();
        h = imageToCopy.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        filename = imageToCopy.getFileName();
        setPixels(imageToCopy.getPixelsNoCopy());
        orientation = imageToCopy.getOrientation();
    }
    
    /** Creates and image with a default filename using the color data from a BufferedImage. **/
    public Image(BufferedImage imageToCopy) {
        w = imageToCopy.getWidth();
        h = imageToCopy.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        filename = w + "-by-" + h;
        setPixels(((DataBufferInt)image.getRaster().getDataBuffer()).getData());
        orientation = Orientation.UP;
    }
    
    public Image(InputStream is) {
    	BufferedImage imageToCopy;
		try {
			imageToCopy = ImageIO.read(is);
	    	
			w = imageToCopy.getWidth();
			h = imageToCopy.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			filename = w + "-by-" + h;
			setPixels(((DataBufferInt)image.getRaster().getDataBuffer()).getData());
			orientation = Orientation.UP;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Could not open file stream: " + is);
		}
    }

    /** Creates a new image from a given filename. First searches through working directory, then looks at the directoy of 
     * the .class file. **/
    public Image(String filename_) {
        filename = filename_;
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                image = ImageIO.read(file);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(filename);
                if (url == null) { url = new URL(filename); }
                image = ImageIO.read(url);
            }
            w  = image.getWidth();
            h = image.getHeight();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + filename);
        }
        orientation = Orientation.UP;
    }

    /** Creates file from a File object. **/
    public Image(File file) {
        try { image = ImageIO.read(file); }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
        w  = image.getWidth();
        h = image.getHeight();
        filename = file.getName();
        orientation = Orientation.UP;
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
     * @param newWidth		New width to scale image to.
     * @param newHeight		New height to scale image to. **/
    public Image resize(int newWidth, int newHeight) {
    	Image newImage = new Image(newWidth, newHeight);
    	newImage.setOrientation(orientation);
    	float newToOldScaleX = (float)w/newWidth;
    	float newToOldScaleY = (float)h/newHeight;
    	
    	int[] newImagePixels = newImage.getPixelsNoCopy();
    	int[] oldImagePixels = this.getPixelsNoCopy();
		for (int x=0; x<newWidth; x++) {
			for (int y=0; y<newHeight; y++) {
				newImagePixels[x + y*newWidth] = oldImagePixels[Math.round(x*newToOldScaleX) + w*Math.round(y*newToOldScaleY)];
    		}
		}
		return newImage;
    }
    public Image resizeScaledWidth(int newWidth) {
    	double ratio =  newWidth/(double)w;
    	return resize(newWidth, (int)(h*ratio));
    }
    public Image resizeScaledHeight(int newHeight) {
    	double ratio =  newHeight/(double)h;
    	return resize((int)(w*ratio), newHeight);
    }
    
    public static Image reorient(Image image, Orientation newOrientation) {
    	return image.reorient(newOrientation);
    }
    public Image reorient(Orientation newOrientation) {
    	int[][] matrix = Orientation.getRotationMatrix(this.orientation, newOrientation);
    	int newWidth = Math.abs(w*matrix[0][0] + h*matrix[0][1]);
    	int newHeight = Math.abs(w*matrix[1][0] + h*matrix[1][1]);
    	Image newImage = new Image(newWidth, newHeight);
    	newImage.setOrientation(newOrientation);
    	
    	int[] newImagePixels = newImage.getPixelsNoCopy();
    	int[] oldImagePixels = this.getPixelsNoCopy();
    	int newx=0, newy=0;    	
    	for (int x=0; x<w; x++) {
    		for (int y=0; y<h; y++) {
    			newx = x*matrix[0][0] + y*matrix[0][1];
    			if (newx < 0) {
    				newx += newWidth;
    			}
    	    	newy = x*matrix[1][0] + y*matrix[1][1];
    	    	if (newy < 0) {
    				newy += newHeight;
    			}
    	    	
    	    	newImagePixels[newx + newy*newWidth] = oldImagePixels[x + w*y];
    		}
    	}
    	return newImage;
    }
    
    /** Returns the color data of the image at the pixel (w, h) as an integer. Note that the point (0, 0) is the origin of the image, not the screen. **/
    public int get(int x, int y) {
        if (x < 0 || x >= w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (w-1) + ", recieved " + x);
        if (y < 0 || y >= h) throw new IndexOutOfBoundsException("y must be between 0 and " + (h-1) + ", recieved " + y);
        return image.getRGB(x, y);
    }
    /** Returns the color data of the image at the pixel (w, h) as a Color object. Note that the point (0, 0) is the origin of the image, not the screen. **/
    public Color getColor(int x, int y) {
        if (x < 0 || x >= w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (w-1) + ", recieved " + x);
        if (y < 0 || y >= h) throw new IndexOutOfBoundsException("y must be between 0 and " + (h-1) + ", recieved " + y);
        return new Color(image.getRGB(x, y), true);
    }
    /** Returns the color data of all the pixels as an array of integers. **/
    public int[] getPixels() {
    	int[] pixelData = new int[w*h];
    	System.arraycopy(((DataBufferInt)image.getRaster().getDataBuffer()).getData(), 0, pixelData, 0, w*h);
    	return pixelData;
    }
    
    /** Returns the color data of all the pixels as an array of integers. Returns the base array. Changes to the returned array WILL modify the image data. **/
    public int[] getPixelsNoCopy() {
    	return ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    }
    
    /** Sets the color data of the image at the pixel (w, h) using an integer. Note that th point (0, 0) is the origin of the image, not the screen. **/
    public void set(int x, int y, int color) {
        if (x < 0 || x >= w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (w-1) + ", recieved " + x);
        if (y < 0 || y >= h) throw new IndexOutOfBoundsException("y must be between 0 and " + (h-1) + ", recieved " + y);
        image.setRGB(x, y, color);
    }
    /** Sets the color data of the image at the pixel (w, h) using a Color object. Note that th point (0, 0) is the origin of the image, not the screen. **/
    public void setColor(int x, int y, Color color) {
        if (x < 0 || x >= w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (w-1) + ", recieved " + x);
        if (y < 0 || y >= h) throw new IndexOutOfBoundsException("y must be between 0 and " + (h-1) + ", recieved " + y);
        if (color == null) throw new NullPointerException("can't set Color to null");
        image.setRGB(x, y, color.getRGB());
    }
    /** Sets all the color data of the image from an array of integers. **/
    public void setPixels(int[] pixelData) {
    	System.arraycopy(pixelData, 0, ((DataBufferInt)image.getRaster().getDataBuffer()).getData(), 0, w*h);
    }

    /** Draws an image to a Graphics2D object with the origin at point x, y on the screen. 
     * 	Don't call this yourself. Use the DrawModule image() method. **/
    public void draw(Graphics2D g, int x, int y) {
    	g.drawImage(image, x, y, null);
    }
    /** Draws an image to a Graphics2D object with the center of the image at point x, y on the screen. 
     * 	Don't call this yourself. Use the DrawModule imageCentered() method. **/
    public void drawCentered(Graphics2D g, int x, int y) {
    	g.drawImage(image, x-(int)image.getWidth()/2, y-(int)image.getHeight()/2, null);
    }
    /** Draws and image rotated by an angle to a Graphics2D object with the center of the image at point x, y on the screen. 
     * 	Don't call this yourself. Use the DrawModule imageRotated() method. **/
    public void drawRotated(Graphics2D g, int x, int y, double angle) {
    	g.rotate(angle, x, y);
    	g.drawImage(image, x-(int)image.getWidth()/2, y-(int)image.getHeight()/2, null);
    	g.rotate(-angle, x, y);
    }

    /** Returns whether an object equals this Image object. They must both be Image objects, and their color data must match exactly. **/
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Image that = (Image) obj;
        if (this.getWidth()  != that.getWidth())  return false;
        if (this.getHeight() != that.getHeight()) return false;
        
        int[] thatImagePixels = that.getPixelsNoCopy();
    	int[] thisImagePixels = this.getPixelsNoCopy();
    	int size = w*h;
    	for (int i = 0; i < size; i++)
    		if (thisImagePixels[i] != thatImagePixels[i]) return false;
        return true;
    }
}
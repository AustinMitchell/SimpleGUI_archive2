package simple.gui;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/** Object that works as an overhead for the BufferedImage object. Has its own limited functionality, but if you wish to work directly 
 * with the BufferedImage, you can get this from the Image object. Ths object's funcionality is currently limited as it's meant to have
 * only simple and easy to understand usage. **/
public final class Image {
    private BufferedImage image;            
    private String filename;                   
    private int w, h;          

    /** Returns the width of the image. **/
    public int getWidth() { return w; }
    /** Returns the height of the image. **/
    public int getHeight() { return h; }
    /** Returns the BufferedImage object from the image. **/
    public BufferedImage getBufferedImage() { return image; }
    /** Returns the filename of the image. If it is an image created without a filename, then a default filename is used:
     *the String (image width) + "-by-" + (image height). **/
    public String getFileName() { return filename; }
    
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
    }

    /** Creates an image using the same filename and color data from another image. **/
    public Image(Image imageToCopy) {
        w = imageToCopy.getWidth();
        h = imageToCopy.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        filename = imageToCopy.getFileName();
        for (int col = 0; col < w; col++)
            for (int row = 0; row < h; row++)
                this.set(col, row, imageToCopy.get(col, row));
    }
    
    /** Creates and image with a default filename using the color data from a BufferedImage. **/
    public Image(BufferedImage imageToCopy) {
        w = imageToCopy.getWidth();
        h = imageToCopy.getHeight();
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        filename = w + "-by-" + h;
        for (int col = 0; col < w; col++)
            for (int row = 0; row < h; row++)
                image.setRGB(col, row, imageToCopy.getRGB(col, row));
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
        w  = image.getWidth(null);
        h = image.getHeight(null);
        filename = file.getName();
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
    /** Returns a copy of the image resized to new bounds. Scales pixels by deciding which pixel has the best claim (which pixel is closest
     * to the empty pixel upon resize). Does not perform any anti-aliasing. WARNING: If you skrink the image and set this image to it, the
     * old data is permanently lost.
     * @param newWidth		New width to scale image to.
     * @param newHeight		New height to scale image to. **/
    public Image resize(int newWidth, int newHeight) {
    	if (newWidth == w && newHeight == h) {
    		return new Image(this);
    	}
    	Image newImage = new Image(newWidth, newHeight);
    	float newToOldScaleX = (float)w/newWidth;
    	float newToOldScaleY = (float)h/newHeight;
		for (int x=0; x<newWidth; x++) {
			for (int y=0; y<newHeight; y++) {
    			newImage.set(x, y, this.get(Math.min(w-1, Math.round(x*newToOldScaleX)), Math.min(h-1, Math.round(y*newToOldScaleY))));
    		}
		}
		return newImage;
    }
    
    /** Returns the color data of the image at the pixel (w, h). Note that the point (0, 0) is the origin of the image, not the screen. **/
    public Color get(int x, int y) {
        if (x < 0 || x >= w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (w-1) + ", recieved " + x);
        if (y < 0 || y >= h) throw new IndexOutOfBoundsException("y must be between 0 and " + (h-1) + ", recieved " + y);
        return new Color(image.getRGB(x, y), true);
    }
    /** Returns the color data of all the pixels as an array. Returns column first. **/
    public Color[] getPixels() {
    	Color[] pixelData = new Color[w*h];
    	for (int x=0; x<w; x++)
    		for (int y=0; y<h; y++)
    			pixelData[x*w + y] = this.get(x, y);
    	
    	return pixelData;
    }

    /** Sets the color data of the image at the pixel (w, h). Note that th point (0, 0) is the origin of the image, not the screen. **/
    public void set(int x, int y, Color color) {
        if (x < 0 || x >= w)  throw new IndexOutOfBoundsException("x must be between 0 and " + (w-1) + ", recieved " + x);
        if (y < 0 || y >= h) throw new IndexOutOfBoundsException("y must be between 0 and " + (h-1) + ", recieved " + y);
        if (color == null) throw new NullPointerException("can't set Color to null");
        image.setRGB(x, y, color.getRGB());
    }
    /** Sets all the color data of the image from an aray. Sets column first. **/
    public void setPixels(Color[] pixelData) {
    	if (w*h != pixelData.length) throw new IndexOutOfBoundsException("number of pixels in pixelData(" + pixelData.length +") must equal number of pixels in Image Object(" + (w*h) + ").");
    	for (int x=0; x<w; x++)
    		for (int y=0; y<h; y++)
    			this.set(x, y, pixelData[x*w + y]);	
    }

    /** Draws an image to a Graphics2D object with the origin at point x, y on the screen. **/
    public void Draw(Graphics2D g, int x, int y) {
    	g.drawImage(image, x, y, null);
    }
    /** Draws an image to a Graphics2D object with the center of the image at point x, y on the screen. **/
    public void DrawCentered(Graphics2D g, int x, int y) {
    	g.drawImage(image, x-(int)image.getWidth()/2, y-(int)image.getHeight()/2, null);
    }
    /** Draws and image rotated by an angle to a Graphics2D object with the center of the image at point x, y on the screen. **/
    public void DrawRotated(Graphics2D g, int x, int y, double angle) {
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
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                if (!this.get(x, y).equals(that.get(x, y))) return false;
        return true;
    }
}
package simple.gui;

import simple.run.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/** Class with methods for drawing to a Graphics2D object and manipulating colors. 
 * <P>Note that if any of this classes color fields are null, when that color is used it will simply skip the operation rather than casting
 * an error. **/
public class Draw {	
	private static SimpleGUIApp _app;
	
	private static Color _fill = Color.BLACK;
	private static Color _stroke = Color.BLACK;
	
	private static Graphics2D _g;
	private static BufferedImage _bimage;
	private static Image _image;
		
	/** Called within SimpleGUIApp to initialize the DrawModule. Don't call this yourself unless you know what you're doing. 
	 * @param app      Application to associate with*/
	public static void initialize(SimpleGUIApp app) {
		Draw._app = app;
		_bimage = new BufferedImage(app.windowWidth(), app.windowHeight(), BufferedImage.TYPE_INT_ARGB);
		_g = (Graphics2D) _bimage.getGraphics();
		_image = new Image(_bimage, false);
	}
	/** Called within SimpleGUIApp. Don't call this yourself unless you know what you're doing. **/
	public static void setGraphics() {
		BufferedImage newImage = new BufferedImage(_app.getWidth(), _app.getHeight(), BufferedImage.TYPE_INT_ARGB);
		_g = (Graphics2D) newImage.getGraphics();
		_g.drawImage(_bimage, 0, 0, null);
		_bimage = newImage;
	}
	
	public static int windowWidth() { return _app.windowWidth(); }
	public static int windowHeight() { return _app.windowHeight(); }
	
	public static BufferedImage getBufferedImage() { return _bimage; }
	public static Image getImage() { return _image; }
	
	/** Returns the DrawObject's stored Graphics2D object. **/
	public static Graphics2D getGraphics() { return _g; }
	/** Returns a FontMetrics object from the stored Graphics2D object's current font. **/
	public static FontMetrics getFontMetrics() { return _g.getFontMetrics(); }
	/** Returns a FontMetrics object from the stored Graphics2D object's current font. **/
	private static FontMetrics getFontMetrics(Graphics2D g2D) { return g2D.getFontMetrics(); }
	/** Returns a FontMetrics object from the given font through the stored Graphics2D object. **/
	public static FontMetrics getFontMetrics(Font font) { return _g.getFontMetrics(font); }
			
	/** Sets the class's local fill field. **/
	public static void setFill(Color fill) { Draw._fill = fill; }
	/** Sets the class's local stroke field. Stroke is used for the borders of shapes as well as for rendering text. **/
	public static void setStroke(Color stroke) { setStroke(stroke, 1); }
	/** Sets the class's local stroke field. Stroke is used for the borders of shapes as well as for rendering text. **/
	public static void setStroke(Color stroke, int thickness) { Draw._stroke = stroke; _g.setStroke(new BasicStroke(thickness)); }
	/** Sets the class's local stroke field. Stroke is used for the borders of shapes as well as for rendering text. **/
    public static void setStrokeRound(Color stroke, int thickness) { Draw._stroke = stroke; _g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); }
    /** Sets the class's local stroke field. Stroke is used for the borders of shapes as well as for rendering text. **/
    public static void setStroke(Image image, Color stroke, int thickness) { Draw._stroke = stroke; image.graphics2D().setStroke(new BasicStroke(thickness)); }
    /** Sets the class's local stroke field. Stroke is used for the borders of shapes as well as for rendering text. **/
    public static void setStrokeRound(Image image, Color stroke, int thickness) { Draw._stroke = stroke; image.graphics2D().setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); }
    /** Sets the class's local color fields. **/
	public static void setColors(Color fill, Color stroke) {
		setFill(fill);
		setStroke(stroke);
	}
	public static void setColors(Color fill, Color stroke, int thickness) {
		setFill(fill);
		setStroke(stroke, thickness);
	}
	
	/** Draws a polygon defined by a series of points. The outline is specified by stroke, the fill by fill. 
	 * @param x			series of x coordinates of the polygon. 
	 * @param y			series of y coordinates of the polygon.
	 * @param numPoints	Number of points to use. **/
	public static void polygon(int[] x, int[] y, int numPoints) {
		polygon(_g, x, y, numPoints);
	}
	public static Image polygon(Image image, int[] x, int[] y, int numPoints) {
		polygon(image.graphics2D(), x, y, numPoints);
		return image;
	}
	private static void polygon(Graphics2D g2D, int[] x, int[] y, int numPoints) {
		if (_fill != null) {
			g2D.setColor(_fill);
			g2D.fillPolygon(x, y, numPoints);
		}
		if (_stroke != null) {
			g2D.setColor(_stroke);
			g2D.drawPolygon(x, y, numPoints);
		}
	}
	/** Draws a rectangle. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the bottom left (visually top left) corner. 
	 * @param y			y coordinate of the bottom left (visually top left) corner. 
	 * @param w			width of the retangle. 
	 * @param h			height of the rectangle. **/
	public static void rect(int x, int y, int w, int h) {
		rect(_g, x, y, w, h);
	}
	/** Draws a rectangle onto an image. The outline is specified by stroke, the fill by fill. 
	 * @param image     Destination image to draw onto
     * @param x         x coordinate of the bottom left (visually top left) corner. 
     * @param y         y coordinate of the bottom left (visually top left) corner. 
     * @param w         width of the retangle. 
     * @param h         height of the rectangle. **/
	public static Image rect(Image image, int x, int y, int w, int h) {
		rect(image.graphics2D(), x, y, w, h);
		return image;
	}
	private static void rect(Graphics2D g2D, int x, int y, int w, int h) {
		if (_fill != null) {
			g2D.setColor(_fill);
			g2D.fillRect(x, y, w, h);
		}
		if (_stroke != null) {
			g2D.setColor(_stroke);
			g2D.drawRect(x, y, w, h);
		}
	}
	/** Draws an arc. The outline is specified by stroke, the fill by fill. 
     * @param x             x coordinate of the top left of the bounding box. 
     * @param y             y coordinate of the top left of the bounding box. 
     * @param w             x diameter of the oval. 
     * @param h             y diameter of the oval. 
     * @param startAngle    Angle to begin the arc at
     * @param endAngle      Degrees to span arc*/
    public static void arc(int x, int y, int w, int h, int startAngle, int endAngle) {
        arc(_g, x, y, w, h, startAngle, endAngle);
    }
    /** Draws an arc onto an image. The outline is specified by stroke, the fill by fill. 
     * @param x             x coordinate of the top left of the bounding box. 
     * @param y             y coordinate of the top left of the bounding box. 
     * @param w             x diameter of the oval. 
     * @param h             y diameter of the oval. 
     * @param startAngle    Angle to begin the arc at
     * @param endAngle      Degrees to span arc*/
    public static Image arc(Image image, int x, int y, int w, int h, int startAngle, int endAngle) {
        arc(image.graphics2D(), x, y, w, h, startAngle, endAngle);
        return image;
    }
    private static void arc(Graphics2D g2D, int x, int y, int w, int h, int startAngle, int endAngle) {
        if (_fill != null) {
            g2D.setColor(_fill);
            g2D.fillArc(x, y, w, h, startAngle, endAngle);
        }
        if (_stroke != null) {
            g2D.setColor(_stroke);
            g2D.drawArc(x, y, w, h, startAngle, endAngle);
        }
    }
	
	/** Draws an oval. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the top left of the bounding box. 
	 * @param y			y coordinate of the top left of the bounding box. 
	 * @param w			x diameter of the oval. 
	 * @param h			y diameter of the oval. **/
	public static void oval(int x, int y, int w, int h) {
		oval(_g, x, y, w, h);
	}
	/** Draws an oval onto an image. The outline is specified by stroke, the fill by fill. 
	 * @param image     Destination image to draw to
     * @param x         x coordinate of the top left of the bounding box. 
     * @param y         y coordinate of the top left of the bounding box. 
     * @param w         x diameter of the oval. 
     * @param h         y diameter of the oval. **/
	public static Image oval(Image image, int x, int y, int w, int h) {
		oval(image.graphics2D(), x, y, w, h);
		return image;
	}
	private static void oval(Graphics2D g2D, int x, int y, int w, int h) {
		if (_fill != null) {
			g2D.setColor(_fill);
			g2D.fillOval(x, y, w, h);
		}
		if (_stroke != null) {
			g2D.setColor(_stroke);
			g2D.drawOval(x, y, w, h);
		}
	}
	/** Draws an oval centered at (x,y). The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the center. 
	 * @param y			y coordinate of the center. 
	 * @param w			x radius of the oval. 
	 * @param h			y radius of the oval. **/
	public static void ovalCentered(int x, int y, int w, int h) {
		ovalCentered(_g, x, y, w, h);
	}
	/** Draws an oval to an image centered at (x,y). The outline is specified by stroke, the fill by fill. 
	 * @param image     Destination image to draw onto
     * @param x         x coordinate of the center. 
     * @param y         y coordinate of the center. 
     * @param w         x radius of the oval. 
     * @param h         y radius of the oval. **/
	public static Image ovalCentered(Image image, int x, int y, int w, int h) {
		ovalCentered(image.graphics2D(), x, y, w, h);
		return image;
	}
	private static void ovalCentered(Graphics2D g2D, int x, int y, int w, int h) {
		if (_fill != null) {
			g2D.setColor(_fill);
			g2D.fillOval(x-w, y-h, w*2, h*2);
		}
		if (_stroke != null) {
			g2D.setColor(_stroke);
			g2D.drawOval(x-w, y-h, w*2, h*2);
		}
	}
	/** Draws a triangle. The outline is specified by stroke, the fill by fill. 
	 * @param x1			x coordinate of the first point. 
	 * @param y1			y coordinate of the first point.
	 * @param x2			x coordinate of the second point. 
	 * @param y2			y coordinate of the second point. 
	 * @param x3			x coordinate of the third point. 
	 * @param y3			y coordinate of the third point.
	 * */
	public static void tri(int x1, int y1, int x2, int y2, int x3, int y3) {
		tri(_g, x1, y1, x2, y2, x3, y3);
	}
	/** Draws a triangle onto an image. The outline is specified by stroke, the fill by fill. 
	 * @param image         Destination image to draw onto
     * @param x1            x coordinate of the first point. 
     * @param y1            y coordinate of the first point.
     * @param x2            x coordinate of the second point. 
     * @param y2            y coordinate of the second point. 
     * @param x3            x coordinate of the third point. 
     * @param y3            y coordinate of the third point.
     * */
	public static Image tri(Image image, int x1, int y1, int x2, int y2, int x3, int y3) {
		tri(image.graphics2D(), x1, y1, x2, y2, x3, y3);
		return image;
	}
	private static void tri(Graphics2D g2D, int x1, int y1, int x2, int y2, int x3, int y3) {
		int[] x = {x1, x2, x3};
		int[] y = {y1, y2, y3};
		polygon(g2D, x, y, 3);
	}
	/** Draws a line between two points onto the stored graphics context. The color is specified by stroke. 
	 * @param x1		x coordinate of one end. 
	 * @param y1		y coordinate of one end. 
	 * @param x2		x coordinate of another end. 
	 * @param y2		y coordinate of another end. **/
	public static void line(int x1, int y1, int x2, int y2) {
		line(_g, x1, y1, x2, y2);
	}
	/** Draws a line between two points onto an image. The color is specified by stroke. 
	 * @param image     Destination image to draw onto
     * @param x1        x coordinate of one end. 
     * @param y1        y coordinate of one end. 
     * @param x2        x coordinate of another end. 
     * @param y2        y coordinate of another end. **/
	public static Image line(Image image, int x1, int y1, int x2, int y2) {
		line(image.graphics2D(), x1, y1, x2, y2);
		return image;
	}
	private static void line(Graphics2D g2D, int x1, int y1, int x2, int y2) {
		if (_stroke != null) {
			g2D.setColor(_stroke);
			g2D.drawLine(x1, y1, x2, y2);
		}
	}
	
	/** Sets the stored Graphics2D object's stored font. 
	 * @param font     New font to store **/
	public static void setFont(Font font) { setFont(_g, font); }
	/** Sets the font for the images graphics context. 
     * @param font     New font to store **/
	public static void setFont(Image image, Font font) { setFont(image.graphics2D(), font); }
	private static void setFont(Graphics2D g2D, Font font) { g2D.setFont(font); }
	/** Draws text on the screen with a given font, with the text starting at the point x, y. This changes the currently set font
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param font			Set's the currently used font for the graphics object. 
	 * @param x				x coordinate to start the drawing of the text.
	 * @param y				y coordinate to start the drawing of the text.**/
	public static void text(String textToDraw, Font font, int x, int y) {
		text(_g, textToDraw, font, x, y);
	}
	/** Draws text onto an image with a given font, with the text starting at the point x, y. This changes the currently set font
     * for the given graphics object. 
     * @param image         Destination image to draw to
     * @param textToDraw    Text to draw to the screen. 
     * @param font          Set's the currently used font for the graphics object. 
     * @param x             x coordinate to start the drawing of the text.
     * @param y             y coordinate to start the drawing of the text.**/
	public static void text(Image image, String textToDraw, Font font, int x, int y) {
		text(image.graphics2D(), textToDraw, font, x, y);
	}
	private static void text(Graphics2D g2D, String textToDraw, Font font, int x, int y) {
		setFont(g2D, font);
		text(g2D, textToDraw, x, y);
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text starting at the point x, y.
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate to start the drawing of the text.
	 * @param y				y coordinate to start the drawing of the text.**/
	public static void text(String textToDraw, int x, int y) {
		text(_g, textToDraw, x, y);
	}
	public static Image text(Image image, String textToDraw, int x, int y) {
		text(image.graphics2D(), textToDraw, x, y);
		return image;
	}
	private static void text(Graphics2D g2D, String textToDraw, int x, int y) {
		if (_stroke != null) {
			g2D.setColor(_stroke);
			FontMetrics fm = getFontMetrics(g2D);
			g2D.drawString(textToDraw, x, y + fm.getMaxAscent());
		}
	}
	
	/** Draws text on the screen aligned to the right with whatever font the graphics object is using, with the text starting at the point x, y
	 * onto the stored graphics context
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate to start the drawing of the text.
	 * @param y				y coordinate to start the drawing of the text.**/
	public static void textRight(String textToDraw, int x, int y) {
		textRight(_g, textToDraw, x, y);
	}
	/** Draws text on the screen aligned to the right onto an image, with the text starting at the point x, y.
     * @param image         Destination image to draw onto
     * @param textToDraw    Text to draw to the screen. 
     * @param x             x coordinate to start the drawing of the text.
     * @param y             y coordinate to start the drawing of the text.**/
	public static Image textRight(Image image, String textToDraw, int x, int y) {
		textRight(image.graphics2D(), textToDraw, x, y);
		return image;
	}
	private static void textRight(Graphics2D g2D, String textToDraw, int x, int y) {
		if (_stroke != null) {
			g2D.setColor(_stroke);
			FontMetrics fm = getFontMetrics(g2D);
			g2D.drawString(textToDraw, x-fm.stringWidth(textToDraw), y + fm.getMaxAscent());
		}
	}
	
	/** Draws text on the screen with whatever font the graphics object is using, with the text center at the point x, y
	 * onto the stored graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate of the text center.
	 * @param y				y coordinate of the text center.**/
	public static void textCentered(String textToDraw, Font font, int x, int y) {
		textCentered(_g, textToDraw, font, x, y);
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text center at the point x, y
     * onto an image. 
     * @param image         image to draw to
     * @param textToDraw    Text to draw to the screen. 
     * @param x             x coordinate of the text center.
     * @param y             y coordinate of the text center.**/
	public static Image textCentered(Image image, String textToDraw, Font font, int x, int y) {
		textCentered(image.graphics2D(), textToDraw, font, x, y);
		return image;
	}
	private static void textCentered(Graphics2D g2D, String textToDraw, Font font, int x, int y) {
		setFont(g2D, font);
		textCentered(g2D, textToDraw, x, y);
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text center at the point x, y.
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate of the text center.
	 * @param y				y coordinate of the text center.**/
	public static void textCentered(String textToDraw, int x, int y) {
		textCentered(_g, textToDraw, x, y);
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text center at the point x, y
     * onto the given image
     * @param image         image to draw onto
     * @param textToDraw    Text to draw to the screen. 
     * @param x             x coordinate of the text center.
     * @param y             y coordinate of the text center.**/
	public static Image textCentered(Image image, String textToDraw, int x, int y) {
		textCentered(image.graphics2D(), textToDraw, x, y);
		return image;
	}
	private static void textCentered(Graphics2D g2D, String textToDraw, int x, int y) {
		if (_stroke != null) {
			g2D.setColor(_stroke);
			FontMetrics fm = getFontMetrics(g2D);
			g2D.drawString(textToDraw, x - fm.stringWidth(textToDraw)/2, (int)(y + fm.getStringBounds(textToDraw, g2D).getHeight()/4.0));
		}
	}
	
	/** Calls an Image object's Draw function using the stored Graphics2D object. Refer to Image. 
	 * @param imageToDraw      source image to draw with
	 * @param x                x position for where to start drawing
	 * @param y                y position for where to start drawing **/
	public static void image(Image imageToDraw, int x, int y) { image(_g, imageToDraw, x, y); }
	/** Calls an Image object's DrawCentered function using the stored Graphics2D object. Refer to Image.
	 * 
	 * @param imageToDraw      source image to draw with
	 * @param x                center x position for where to draw the image
	 * @param y                center y position for where to draw the image
	 */
	public static void imageCentered(Image imageToDraw, int x, int y) { imageCentered(_g, imageToDraw, x, y); }
	/** Calls an Image object's DrawRotated function using the stored Graphics2D object. Refer to Image.
	 * 
	 * @param imageToDraw      source image to draw with
	 * @param x                center x position for where to draw the image
	 * @param y                center y position for where to draw the image
	 * @param angle            angle to rotate the image by when drawing
	 */
	public static void imageRotated(Image imageToDraw, int x, int y, double angle) { imageRotated(_g, imageToDraw, x, y, angle); }
	
	/** Calls an Image object's Draw function using the imageBuffer's Graphics2D object. Refer to Image.
	 * @param imageBuffer      destination image to draw to
	 * @param imageToDraw      source image to draw with
     * @param x                x position for where to start drawing
     * @param y                y position for where to start drawing */
	public static Image image(Image imageBuffer, Image imageToDraw, int x, int y) { 
		image(imageBuffer.graphics2D(), imageToDraw, x, y);
		return imageBuffer;
	}
	/** Calls an Image object's DrawCentered function using the imageBuffer's Graphics2D object. Refer to Image. 
	 * @param imageBuffer      destination image to draw to
	 * @param imageToDraw      source image to draw with
     * @param x                center x position for where to draw the image
     * @param y                center y position for where to draw the image */
	public static Image imageCentered(Image imageBuffer, Image imageToDraw, int x, int y) { 
		imageCentered(imageBuffer.graphics2D(), imageToDraw, x, y); 
		return imageBuffer;
	}
	/** Calls an Image object's DrawRotated function using the imageBuffer's Graphics2D object. Refer to Image. 
	 * @param imageBuffer      destination image to draw to
     * @param imageToDraw      source image to draw with
     * @param x                center x position for where to draw the image
     * @param y                center y position for where to draw the image 
     * @param angle            angle to rotate the image by when drawing*/
	public static Image imageRotated(Image imageBuffer, Image imageToDraw, int x, int y, double angle) { 
		imageRotated(imageBuffer.graphics2D(), imageToDraw, x, y, angle); 
		return imageBuffer;
	}
	
	private static void image(Graphics2D g2D, Image imageToDraw, int x, int y) { imageToDraw.draw(g2D, x, y); }
	private static void imageCentered(Graphics2D g2D, Image imageToDraw, int x, int y) { imageToDraw.drawCentered(g2D, x, y); }
	private static void imageRotated(Graphics2D g2D, Image imageToDraw, int x, int y, double angle) { imageToDraw.drawRotated(g2D, x, y, angle); }
	
	/** Multiplies each value in a Color object by a constant.
	 * @param c				Base color
	 * @param scale			Constant scalar value **/
	public static Color scaleColor(Color c, float scale) {
		if (c==null) return null;
		return new Color((int)(c.getRed()*scale), (int)(c.getGreen()*scale), (int)(c.getBlue()*scale));
	}
	/** Multiplies each value in a Color object by a constant. Each color is multiplied by a different constant.
	 * @param c				Base color
	 * @param rscale		Constant scalar value for red
	 * @param gscale		Constant scalar value for green
	 * @param bscale		Constant scalar value for blue**/
	public static Color scaleColor(Color c, float rscale, float gscale, float bscale) {
		if (c==null) return null;
		return new Color(c.getRed()*rscale, c.getGreen()*gscale, c.getBlue()*bscale);
	}
	/** Multiplies each value in a Color object by a constant. Each color is multiplied by a different constant.
     * @param c             Base color
     * @param rscale        Constant scalar value for red
     * @param gscale        Constant scalar value for green
     * @param bscale        Constant scalar value for blue
     * @param ascale        Constant scalar value for alpha**/
    public static Color scaleColor(Color c, float rscale, float gscale, float bscale, float ascale) {
        if (c==null) return null;
        return new Color(c.getRed()*rscale, c.getGreen()*gscale, c.getBlue()*bscale, c.getAlpha()*ascale);
    }
}

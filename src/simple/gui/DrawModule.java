package simple.gui;

import simple.run.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/** Class with methods for drawing to a Graphics2D object and manipulating colors. 
 * <P>Note that if any of this classes color fields are null, when that color is used it will simply skip the operation rather than casting
 * an error. **/
public class DrawModule {	
	private static SimpleGUIApp app;
	
	private static Color fill = Color.BLACK;
	private static Color stroke = Color.BLACK;
	
	private static Graphics2D g;
	private static BufferedImage image;
		
	/** Called within SimpleGUIApp to initialize the DrawModule. Don't call this yourself unless you know what you're doing. **/
	public static void initialize(SimpleGUIApp app) {
		DrawModule.app = app;
		image = new BufferedImage(app.getWidth(), app.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
	}
	/** Called within SimpleGUIApp. Don't call this yourself unless you know what you're doing. **/
	public static void setGraphics() {
		BufferedImage newImage = new BufferedImage(app.getWidth(), app.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) newImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		image = newImage;
	}
	
	public static BufferedImage getImage() { return image; }
	
	/** Returns the DrawObject's stored Graphics2D object. **/
	public static Graphics2D getGraphics() { return g; }
	/** Returns a FontMetrics object from the stored Graphics2D object's current font. **/
	public static FontMetrics getFontMetrics() { return g.getFontMetrics(); }
	/** Returns a FontMetrics object from the stored Graphics2D object's current font. **/
	private static FontMetrics getFontMetrics(Graphics2D g2D) { return g2D.getFontMetrics(); }
	/** Returns a FontMetrics object from the given font through the stored Graphics2D object. **/
	public static FontMetrics getFontMetrics(Font font) { return g.getFontMetrics(font); }
			
	/** Sets the class's local fill field. **/
	public static void setFill(Color fill) { DrawModule.fill = fill; }
	/** Sets the class's local stroke field. Stroke is used for the borders of shapes as well as for rendering text. **/
	public static void setStroke(Color stroke) { setStroke(stroke, 1); }
	/** Sets the class's local stroke field. Stroke is used for the borders of shapes as well as for rendering text. **/
	public static void setStroke(Color stroke, int thickness) { DrawModule.stroke = stroke; g.setStroke(new BasicStroke(thickness)); }
	/** Sets the class's local color fields. **/
	public static void setColors(Color fill, Color stroke) {
		DrawModule.fill = fill;
		DrawModule.stroke = stroke;
	}
	
	/** Draws a polygon defined by a series of points. The outline is specified by stroke, the fill by fill. 
	 * @param x			series of x coordinates of the polygon. 
	 * @param y			series of y coordinates of the polygon.
	 * @param numPoints	Number of points to use. **/
	public static void polygon(int[] x, int[] y, int numPoints) {
		polygon(g, x, y, numPoints);
	}
	public static void polygon(Image image, int[] x, int[] y, int numPoints) {
		polygon(image.getGraphics(), x, y, numPoints);
	}
	private static void polygon(Graphics2D g2D, int[] x, int[] y, int numPoints) {
		if (fill != null) {
			g2D.setColor(fill);
			g2D.fillPolygon(x, y, numPoints);
		}
		if (stroke != null) {
			g2D.setColor(stroke);
			g2D.drawPolygon(x, y, numPoints);
		}
	}
	/** Draws a rectangle. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the bottom left (visually top left) corner. 
	 * @param y			y coordinate of the bottom left (visually top left) corner. 
	 * @param w			width of the retangle. 
	 * @param h			height of the rectangle. **/
	public static void rect(int x, int y, int w, int h) {
		rect(g, x, y, w, h);
	}
	public static void rect(Image image, int x, int y, int w, int h) {
		rect(image.getGraphics(), x, y, w, h);
	}
	private static void rect(Graphics2D g2D, int x, int y, int w, int h) {
		if (fill != null) {
			g2D.setColor(fill);
			g2D.fillRect(x, y, w, h);
		}
		if (stroke != null) {
			g2D.setColor(stroke);
			g2D.drawRect(x, y, w, h);
		}
	}
	/** Draws an oval. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the top left of the bounding box. 
	 * @param y			y coordinate of the top left of the bounding box. 
	 * @param w			x diameter of the oval. 
	 * @param h			y diameter of the oval. **/
	public static void oval(int x, int y, int w, int h) {
		oval(g, x, y, w, h);
	}
	public static void oval(Image image, int x, int y, int w, int h) {
		oval(image.getGraphics(), x, y, w, h);
	}
	private static void oval(Graphics2D g2D, int x, int y, int w, int h) {
		if (fill != null) {
			g2D.setColor(fill);
			g2D.fillOval(x, y, w, h);
		}
		if (stroke != null) {
			g2D.setColor(stroke);
			g2D.drawOval(x, y, w, h);
		}
	}
	/** Draws an oval. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the center. 
	 * @param y			y coordinate of the center. 
	 * @param w			x radius of the oval. 
	 * @param h			y radius of the oval. **/
	public static void ovalCentered(int x, int y, int w, int h) {
		ovalCentered(g, x, y, w, h);
	}
	public static void ovalCentered(Image image, int x, int y, int w, int h) {
		ovalCentered(image.getGraphics(), x, y, w, h);
	}
	private static void ovalCentered(Graphics2D g2D, int x, int y, int w, int h) {
		if (fill != null) {
			g2D.setColor(fill);
			g2D.fillOval(x-w, y-h, w*2, h*2);
		}
		if (stroke != null) {
			g2D.setColor(stroke);
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
		tri(g, x1, y1, x2, y2, x3, y3);
	}
	public static void tri(Image image, int x1, int y1, int x2, int y2, int x3, int y3) {
		tri(image.getGraphics(), x1, y1, x2, y2, x3, y3);
	}
	private static void tri(Graphics2D g2D, int x1, int y1, int x2, int y2, int x3, int y3) {
		int[] x = {x1, x2, x3};
		int[] y = {y1, y2, y3};
		polygon(g2D, x, y, 3);
	}
	/** Draws a line between two points. The color is specified by stroke. 
	 * @param x1		x coordinate of one end. 
	 * @param y1		y coordinate of one end. 
	 * @param x2		x coordinate of another end. 
	 * @param y2		y coordinate of another end. **/
	public static void line(int x1, int y1, int x2, int y2) {
		line(g, x1, y1, x2, y2);
	}
	public static void line(Image image, int x1, int y1, int x2, int y2) {
		line(image.getGraphics(), x1, y1, x2, y2);
	}
	private static void line(Graphics2D g2D, int x1, int y1, int x2, int y2) {
		if (stroke != null) {
			g2D.setColor(stroke);
			g2D.drawLine(x1, y1, x2, y2);
		}
	}
	
	/** Sets the stored Graphics2D object's stored font. **/
	public static void setFont(Font font) { setFont(g, font); }
	public static void setFont(Image image, Font font) { setFont(image.getGraphics(), font); }
	public static void setFont(Graphics2D g2D, Font font) { g2D.setFont(font); }
	/** Draws text on the screen with a given font, with the text starting at the point x, y. This changes the currently set font
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param font			Set's the currently used font for the graphics object. 
	 * @param x				x coordinate to start the drawing of the text.
	 * @param y				y coordinate to start the drawing of the text.**/
	public static void text(String textToDraw, Font font, int x, int y) {
		text(g, textToDraw, font, x, y);
	}
	public static void text(Image image, String textToDraw, Font font, int x, int y) {
		text(image.getGraphics(), textToDraw, font, x, y);
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
		text(g, textToDraw, x, y);
	}
	public static void text(Image image, String textToDraw, int x, int y) {
		text(image.getGraphics(), textToDraw, x, y);
	}
	private static void text(Graphics2D g2D, String textToDraw, int x, int y) {
		if (stroke != null) {
			g2D.setColor(stroke);
			FontMetrics fm = getFontMetrics(g2D);
			g2D.drawString(textToDraw, x, y + fm.getMaxAscent());
		}
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text center at the point x, y.
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate of the text center.
	 * @param y				y coordinate of the text center.**/
	public static void textCentered(String textToDraw, Font font, int x, int y) {
		textCentered(g, textToDraw, font, x, y);
	}
	public static void textCentered(Image image, String textToDraw, Font font, int x, int y) {
		textCentered(image.getGraphics(), textToDraw, font, x, y);
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
		textCentered(g, textToDraw, x, y);
	}
	public static void textCentered(Image image, String textToDraw, int x, int y) {
		textCentered(image.getGraphics(), textToDraw, x, y);
	}
	private static void textCentered(Graphics2D g2D, String textToDraw, int x, int y) {
		if (stroke != null) {
			g2D.setColor(stroke);
			FontMetrics fm = getFontMetrics(g2D);
			g2D.drawString(textToDraw, x - fm.stringWidth(textToDraw)/2, y + fm.getMaxAscent()/2);
		}
	}
	
	/** Calls an Image object's Draw function using the stored Graphics2D object. Refer to Image. **/
	public static void image(Image imageToDraw, int x, int y) { image(g, imageToDraw, x, y); }
	/** Calls an Image object's DrawCentered function using the stored Graphics2D object. Refer to Image. **/
	public static void imageCentered(Image imageToDraw, int x, int y) { imageCentered(g, imageToDraw, x, y); }
	/** Calls an Image object's DrawRotated function using the stored Graphics2D object. Refer to Image. **/
	public static void imageRotated(Image imageToDraw, int x, int y, double angle) { imageRotated(g, imageToDraw, x, y, angle); }
	
	/** Calls an Image object's Draw function using the imageBuffer's Graphics2D object. Refer to Image. **/
	public static void image(Image imageBuffer, Image imageToDraw, int x, int y) { image(imageBuffer.getGraphics(), imageToDraw, x, y); }
	/** Calls an Image object's DrawCentered function using the imageBuffer's Graphics2D object. Refer to Image. **/
	public static void imageCentered(Image imageBuffer, Image imageToDraw, int x, int y) { imageCentered(imageBuffer.getGraphics(), imageToDraw, x, y); }
	/** Calls an Image object's DrawRotated function using the imageBuffer's Graphics2D object. Refer to Image. **/
	public static void imageRotated(Image imageBuffer, Image imageToDraw, int x, int y, double angle) { imageRotated(imageBuffer.getGraphics(), imageToDraw, x, y, angle); }
	
	/** Calls an Image object's Draw function using the given Graphics2D object. Refer to Image. **/
	private static void image(Graphics2D g2D, Image imageToDraw, int x, int y) { imageToDraw.draw(g2D, x, y); }
	/** Calls an Image object's DrawCentered function using the given Graphics2D object. Refer to Image. **/
	private static void imageCentered(Graphics2D g2D, Image imageToDraw, int x, int y) { imageToDraw.drawCentered(g2D, x, y); }
	/** Calls an Image object's DrawRotated function using the given Graphics2D object. Refer to Image. **/
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
}

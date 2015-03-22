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
	
	public static void initialize(SimpleGUIApp app) {
		DrawModule.app = app;
		image = new BufferedImage(app.getWidth(), app.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
	}
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
	/** Returns a FontMetrics object from the given font throught the stored Graphics2D object. **/
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
		if (fill != null) {
			g.setColor(fill);
			g.fillPolygon(x, y, numPoints);
		}
		if (stroke != null) {
			g.setColor(stroke);
			g.drawPolygon(x, y, numPoints);
		}
	}
	/** Draws a rectangle. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the bottom left (visually top left) corner. 
	 * @param y			y coordinate of the bottom left (visually top left) corner. 
	 * @param w			width of the retangle. 
	 * @param h			height of the rectangle. **/
	public static void rect(int x, int y, int w, int h) {
		if (fill != null) {
			g.setColor(fill);
			g.fillRect(x, y, w, h);
		}
		if (stroke != null) {
			g.setColor(stroke);
			g.drawRect(x, y, w, h);
		}
	}
	/** Draws an oval. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the center. 
	 * @param y			y coordinate of the center. 
	 * @param w			x radius of the oval. 
	 * @param h			y radius of the oval. **/
	public static void oval(int x, int y, int w, int h) {
		if (fill != null) {
			g.setColor(fill);
			g.fillOval(x, y, w, h);
		}
		if (stroke != null) {
			g.setColor(stroke);
			g.drawOval(x, y, w, h);
		}
	}
	/** Draws an oval. The outline is specified by stroke, the fill by fill. 
	 * @param x			x coordinate of the center. 
	 * @param y			y coordinate of the center. 
	 * @param w			x radius of the oval. 
	 * @param h			y radius of the oval. **/
	public static void ovalCentered(int x, int y, int w, int h) {
		if (fill != null) {
			g.setColor(fill);
			g.fillOval(x-w, y-h, w*2, h*2);
		}
		if (stroke != null) {
			g.setColor(stroke);
			g.drawOval(x-w, y-h, w*2, h*2);
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
		int[] x = {x1, x2, x3};
		int[] y = {y1, y2, y3};
		polygon(x, y, 3);
	}
	/** Draws a line between two points. The color is specified by stroke. 
	 * @param x1		x coordinate of one end. 
	 * @param y1		y coordinate of one end. 
	 * @param x2		x coordinate of another end. 
	 * @param y2		y coordinate of another end. **/
	public static void line(int x1, int y1, int x2, int y2) {
		if (stroke != null) {
			g.setColor(stroke);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	/** Sets the stored Graphics2D object's stored font. **/
	public static void setFont(Font font) { g.setFont(font); }
	/** Draws text on the screen with a given font, with the text starting at the point x, y. This changes the currently set font
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param font			Set's the currently used font for the graphics object. 
	 * @param x				x coordinate to start the drawing of the text.
	 * @param y				y coordinate to start the drawing of the text.**/
	public static void text(String textToDraw, Font font, int x, int y) {
		g.setFont(font);
		text(textToDraw, x, y);
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text starting at the point x, y.
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate to start the drawing of the text.
	 * @param y				y coordinate to start the drawing of the text.**/
	public static void text(String textToDraw, int x, int y) {
		if (stroke != null) {
			g.setColor(stroke);
			g.drawString(textToDraw, x, y);
		}
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text center at the point x, y.
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate of the text center.
	 * @param y				y coordinate of the text center.**/
	public static void textCentered(String textToDraw, Font font, int x, int y) {
		if (stroke != null) {
			g.setColor(stroke);
			FontMetrics fm = getFontMetrics(font);
			setFont(font);
			g.drawString(textToDraw, x - fm.stringWidth(textToDraw)/2, y + fm.getMaxAscent()/2);
		}
	}
	/** Draws text on the screen with whatever font the graphics object is using, with the text center at the point x, y.
	 * for the given graphics object. 
	 * @param textToDraw	Text to draw to the screen. 
	 * @param x				x coordinate of the text center.
	 * @param y				y coordinate of the text center.**/
	public static void textCentered(String textToDraw, int x, int y) {
		if (stroke != null) {
			g.setColor(stroke);
			FontMetrics fm = getFontMetrics();
			g.drawString(textToDraw, x - fm.stringWidth(textToDraw)/2, y + fm.getMaxAscent()/2);
		}
	}
	
	/** Calls an Image object's Draw function using the stored Graphics2D object. Refer to Image. **/
	public static void image(Image imageToDraw, int x, int y) { imageToDraw.Draw(g, x, y); }
	/** Calls an Image object's DrawCentered function using the stored Graphics2D object. Refer to Image. **/
	public static void imageCentered(Image imageToDraw, int x, int y) { imageToDraw.DrawCentered(g, x, y); }
	/** Calls an Image object's DrawRotated function using the stored Graphics2D object. Refer to Image. **/
	public static void imageRotated(Image imageToDraw, int x, int y, double angle) { imageToDraw.DrawRotated(g, x, y, angle); }
	
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

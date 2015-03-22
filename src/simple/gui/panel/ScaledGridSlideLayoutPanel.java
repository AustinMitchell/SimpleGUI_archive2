package simple.gui.panel;

import java.util.ArrayList;

import simple.gui.Widget;

public class ScaledGridSlideLayoutPanel extends Panel{
	protected ArrayList<ScaledGridLayoutPanel> slides;
	protected int currentSlideID = 0;
	protected int rows, cols;
	
	public int getCurrentSlideID() { return currentSlideID; }
	public ArrayList<ScaledGridLayoutPanel> getAllSlides() { return slides; }
	public ScaledGridLayoutPanel getSlide(int slideID) { return slides.get(slideID); }
	
	public void setLocation(int newX, int newY) {
		int diffx = newX - x;
		int diffy = newY - y;
		super.setLocation(newX, newY);
		for (ScaledGridLayoutPanel slide: slides) {
			slide.setLocation(slide.getX()+diffx, slide.getY()+diffy);
		}
	}
	public void setX(int newX) { setLocation(newX, y); }
	public void setY(int newY) { setLocation(x, newY); }
	
	public void setCurrentSlide(int slideID) { currentSlideID = slideID; }
	
	public ScaledGridSlideLayoutPanel(ScaledGridLayoutPanel panel, int rows_, int cols_) {
		this(panel, panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight(), rows_, cols_);
	}
	public ScaledGridSlideLayoutPanel(ScaledGridLayoutPanel panel, int x_, int y_, int w_, int h_, int rows_, int cols_) {
		super(x_, y_, w_, h_);
		rows = rows_;
		cols = cols_;
		slides = new ArrayList<ScaledGridLayoutPanel>();
		addSlide(panel);
	}
	public ScaledGridSlideLayoutPanel(int rows_, int cols_) {
		this(0, 0, 10, 10, rows_, cols_);
	}
	public ScaledGridSlideLayoutPanel(int x_, int y_, int w_, int h_, int rows_, int cols_) {
		super(x_, y_, w_, h_);
		rows = rows_;
		cols = cols_;
		slides = new ArrayList<ScaledGridLayoutPanel>();
		addSlide();
	}
	
	public void addWidget(Widget newWidget, int slideID, int startCol_, int startRow_, int colSpace_, int rowSpace_) {
		slides.get(slideID).addWidget(newWidget, startCol_, startRow_, colSpace_, rowSpace_);
	}
	public void addWidget(Widget newWidget, int startCol_, int startRow_, int colSpace_, int rowSpace_) {
		addWidget(newWidget, currentSlideID, startCol_, startRow_, colSpace_, rowSpace_);
	}
	public void addWidget(Widget newWidget, int slideID) {
		slides.get(slideID).addWidget(newWidget);
	}
	public void addWidget(Widget newWidget) {
		addWidget(newWidget, currentSlideID);
	}
	
	public void addSlide(ScaledGridLayoutPanel newSlide) {
		slides.add(newSlide);
	}
	public void addSlide() {
		addSlide(new ScaledGridLayoutPanel(x, y, w, h, rows, cols));
	}
	
	public void update() {
		if (!enabled || !visible) {
			return;
		}
		
		slides.get(currentSlideID).update();
	}
	
	public void draw() {
		if (!visible)
			return;
		
		if (panelVisible) {
			draw.setColors(fillColor, borderColor);
			draw.rect(x, y, w, h);
		}
		
		slides.get(currentSlideID).draw();
	}
}

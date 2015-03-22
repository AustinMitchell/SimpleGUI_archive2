package simple.gui.panel;

import java.util.ArrayList;

import simple.gui.Widget;

public class SlideLayoutPanel extends Panel {
	ArrayList<Panel> slides;
	int currentSlideID = 0;
	
	public int getCurrentSlideID() { return currentSlideID; }
	public ArrayList<Panel> getAllSlides() { return slides; }
	public Panel getSlide(int slideID) { return slides.get(slideID); }
	
	public void setCurrentSlide(int slideID) { currentSlideID = slideID; }
	
	@Override
	public void setLocation(int newX, int newY) {
		int diffx = newX - x;
		int diffy = newY - y;
		super.setLocation(newX, newY);
		for (Panel slide: slides) {
			slide.setLocation(slide.getX()+diffx, slide.getY()+diffy);
		}
	}
	@Override
	public void setX(int newX) { setLocation(newX, y); }
	@Override
	public void setY(int newY) { setLocation(x, newY); }
	
	public SlideLayoutPanel() {
		this(0, 0, 10, 10);
	}
	public SlideLayoutPanel(Panel firstPanel) {
		this(firstPanel.getX(), firstPanel.getY(), firstPanel.getWidth(), firstPanel.getHeight());
		addSlide(firstPanel);
	}
	public SlideLayoutPanel(int x_, int y_, int w_, int h_) {
		super(x_, y_, w_, h_);
		
		panelVisible = false;
		slides = new ArrayList<Panel>();
		slides.add(new Panel(x, y, w, h));
	}
	
	public void addWidget(Widget newWidget, int slideID) {
		slides.get(slideID).addWidget(newWidget);
		newWidget.setLocation(x+newWidget.getX(), y+newWidget.getY());
	}
	@Override
	public void addWidget(Widget newWidget) {
		addWidget(newWidget, currentSlideID);
	}
	
	public void addSlide(Panel newSlide) {
		slides.add(newSlide);
	}
	public void addSlide() {
		addSlide(new Panel(x, y, w, h));
	}
	
	@Override
	public void update() {
		if (!enabled || !visible) {
			return;
		}
		
		slides.get(currentSlideID).update();
	}
	
	@Override
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

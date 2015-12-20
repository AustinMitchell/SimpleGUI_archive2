package simple.gui;

// Widget that stores a list of its own widgetList of any kind. The panel may be drawn itself if the panelVisible is set to true
// Note that the visible field affects all widgetList in the panel, not just the panel itself
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Panel extends Widget {
	private static class Rect {
		int x1, y1, x2, y2;
		Rect(int x1_, int y1_, int x2_, int y2_) {
			x1 = x1_;
			y1 = y1_;
			x2 = x2_;
			y2 = y2_;
		}
	}
			
	public static final int WIDGETS_SCALE_TO_PANEL = 0;
	public static final int WIDGETS_INDEPENDENT_OF_PANEL = 1;
	
	protected int widgetHandle;
	protected boolean panelVisible;
	protected Set<Widget> widgetSet;
	protected Map<Widget, Rect> rectMap;
	
	protected Rect constraints;
	protected Rect newWidgetBounds;
	
	protected int gridWidth, gridHeight; 
	protected double gridBoxWidth, gridBoxHeight;
	
	public int getWidgetHandle() { return widgetHandle; }
	public boolean isPanelVisible() { return panelVisible; }
	public Set<Widget> getWidgets() { return widgetSet; }
	
	public void setNewWidgetBounds(int newc) { setNewWidgetBounds(newc, newc); }
	public void setNewWidgetBounds(int newx, int newy) { setNewWidgetBounds(newx, newy, newx, newy); }
	public void setNewWidgetBounds(int newx1, int newy1, int newx2, int newy2) {
		newWidgetBounds.x1 = newx1;
		newWidgetBounds.y1 = newy1;
		newWidgetBounds.x2 = newx2;
		newWidgetBounds.y2 = newy2;
	}
	
	public void setConstraintsClear() { setConstraints(0); }
	public void setConstraints(int newc) { setConstraints(newc, newc); }
	public void setConstraints(int newx, int newy) { setConstraints(newx, newy, newx, newy); }
	public void setConstraints(int newx1, int newy1, int newx2, int newy2) {
		constraints.x1 = newx1;
		constraints.y1 = newy1;
		constraints.x2 = newx2;
		constraints.y2 = newy2;
	}
	public void setPanelVisible(boolean newPanelVisible) { panelVisible = newPanelVisible; }
	public void setGridSize(int newGridWidth, int newGridHeight) {
		if (widgetHandle != WIDGETS_INDEPENDENT_OF_PANEL) {
			throw new RuntimeException("setGridSize() only available to Panels with widgetHandle of WIDGET_INDEPENDENT_OF_PANEL (which is 1), set in constructor");
		}
		gridWidth = newGridWidth;
		gridHeight = newGridHeight;
	}
	
	@Override
	public void setSize(int newWidth, int newHeight) {
		super.setSize(newWidth, newHeight);
		if (widgetHandle == WIDGETS_SCALE_TO_PANEL) {
			
		}
	}
	@Override
	public void setWidth(int newWidth) { setSize(newWidth, h); }
	@Override
	public void setHeight(int newHeight) { setSize(w, newHeight); }
	
	@Override
	public void setLocation(int newX, int newY) {
		int diffx = newX - x;
		int diffy = newY - y;
		super.setLocation(newX, newY);
		for (Widget w: widgetSet) {
			w.setLocation(w.getX()+diffx, w.getY()+diffy);
		}
	}
	@Override
	public void setX(int newX) { setLocation(newX, y); }
	@Override
	public void setY(int newY) { setLocation(x, newY); }
	
	public Panel() {
		this(WIDGETS_SCALE_TO_PANEL);
	}
	public Panel(int widgetHandle_) {
		this(0, 0, 10, 10, widgetHandle_);
	}
	public Panel(int x_, int y_, int w_, int h_) { 
		this(x_, y_, w_, h_, WIDGETS_INDEPENDENT_OF_PANEL);
	}
	public Panel(int x_, int y_, int w_, int h_, int widgetHandle_) {
		super(x_, y_, w_, h_);
		if (widgetHandle_ < 0 || widgetHandle_ > 1) {
			throw new RuntimeException("Constructor to Panel must take in a new widgetHandle of a valid number: \n\t\t\t0: Panel.WIDGETS_SCALE_TO_PANEL \n\t\t\t1: Panel.WIDGETS_INDEPENDANT_OF_PANEL"); 
		}
		
		panelVisible = false;
		widgetSet = new HashSet<Widget>();
		rectMap = new HashMap<Widget, Rect>();
		widgetHandle = widgetHandle_;
		
		constraints = new Rect(0, 0, 0, 0);
		newWidgetBounds = new Rect(0, 0, 1, 1);
		
		gridWidth = 100;
		gridHeight = 100;
		gridBoxWidth = w_/gridWidth;
		gridBoxHeight = w_/gridHeight;
	}
	
	// Adds a widget. The new origins for widgetList are the x and y coordinates of the panel. 
	// If the panel is at 50;50, adds a button at 0;10, the new coordinates for the button are 50;60
	public void addWidget(Widget newWidget) {
		widgetSet.add(newWidget);
		if (widgetHandle == WIDGETS_INDEPENDENT_OF_PANEL)
		newWidget.setLocation(x+newWidget.getX(), y+newWidget.getY());
	}
	public void addWidget(Widget newWidget, int x1, int y1, int x2, int y2) {
		widgetSet.add(newWidget);
		newWidget.setLocation(x+newWidget.getX(), y+newWidget.getY());
	}
	public void addWidgets(Widget[] newWidgetList) {
		if (widgetHandle == WIDGETS_SCALE_TO_PANEL) {
			throw new RuntimeException("Panel of widgetHandle type WIDGETS_SCALE_TO_PANEL must use addWidget() to add widgets; Cannot take in a collection.");
		}
		for (Widget w : newWidgetList) {
			addWidget(w);
		}
	}
	public void addWidgets(Collection<Widget> newWidgetList) {
		if (widgetHandle == WIDGETS_SCALE_TO_PANEL) {
			throw new RuntimeException("Panel of widgetHandle type WIDGETS_SCALE_TO_PANEL must use addWidget() to add widgets; Cannot take in a collection.");
		}
		for (Widget w : newWidgetList) {
			addWidget(w);
		}
	}
	
	public void removeWidget(Widget widgetToRemove) {
		widgetSet.remove(widgetToRemove);
		rectMap.remove(widgetToRemove);
	}
	public void removeWidgets(Widget[] widgetsToRemove) {
		for (Widget w : widgetsToRemove) {
			removeWidget(w);
		}
	}
	public void removeWidgets(Collection<Widget> widgetsToRemove) {
		for (Widget w : widgetsToRemove) {
			removeWidget(w);
		}
	}
	
	@Override
	public void update() {
		if (!enabled || !visible)
			return;
			
		updateClickingState();
		
		for (Widget w: widgetSet) {
			w.update();
		}
	}
	
	@Override
	public void draw() {
		if (!visible)
			return;
		
		if (panelVisible) {
			draw.setColors(fillColor, borderColor);
			draw.rect(x, y, w, h);
		}
		
		for (Widget w: widgetSet) {
			w.draw();
		}
	}
}

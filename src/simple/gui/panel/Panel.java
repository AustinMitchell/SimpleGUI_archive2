package simple.gui.panel;

// Widget that stores a list of its own widgetList of any kind. The panel may be drawn itself if the panelVisible is set to true
// Note that the visible field affects all widgetList in the panel, not just the panel itself
import java.util.ArrayList;

import simple.gui.Widget;

public class Panel extends Widget {
	protected boolean panelVisible;
	protected ArrayList<Widget> widgetList;
	
	public boolean isPanelVisible() { return panelVisible; }
	public Widget getWidget(int widgetID) { return widgetList.get(widgetID); } 
	public ArrayList<Widget> getWidgets() { return widgetList; }
	
	public void setPanelVisible(boolean newPanelVisible) { panelVisible = newPanelVisible; }
	
	@Override
	public void setLocation(int newX, int newY) {
		int diffx = newX - x;
		int diffy = newY - y;
		super.setLocation(newX, newY);
		for (Widget w: widgetList) {
			w.setLocation(w.getX()+diffx, w.getY()+diffy);
		}
	}
	@Override
	public void setX(int newX) { setLocation(newX, y); }
	@Override
	public void setY(int newY) { setLocation(x, newY); }
	
	public Panel() {
		this(0, 0, 10, 10);
	}
	public Panel(int x_, int y_, int w_, int h_) {
		super(x_, y_, w_, h_);
		
		panelVisible = false;
		widgetList = new ArrayList<Widget>();
	}
	
	// Adds a widget. The new origins for widgetList are the x and y coordinates of the panel. 
	// If the panel is at 50;50, adds a button at 0;10, the new coordinates for the button are 50;60
	public void addWidget(Widget newWidget) {
		widgetList.add(newWidget);
		newWidget.setLocation(x+newWidget.getX(), y+newWidget.getY());
	}
	public void addWidget(Widget[] newWidgetList) {
		for (Widget w : newWidgetList) {
			addWidget(w);
		}
	}
	public void addWidget(ArrayList<Widget> newWidgetList) {
		for (Widget w : newWidgetList) {
			addWidget(w);
		}
	}
	
	@Override
	public void update() {
		if (!enabled || !visible)
			return;
			
		updateClickingState();
		
		for (Widget w: widgetList) {
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
		
		for (Widget w: widgetList) {
			w.draw();
		}
	}
}

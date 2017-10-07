package simple.gui.panel;

import java.util.*;

import simple.gui.Draw;
import simple.gui.Widget;

public abstract class Panel extends Widget {
	public static class Dimensions {
		public float x, y, w, h;
		public void set(Dimensions other) { x = other.x; y = other.y; w = other.w; h = other.h; }
		public Dimensions(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}
	public static class Constraints {
		public float x1, y1, x2, y2;
		public void set(Constraints other) { x1 = other.x1; y1 = other.y1; x2 = other.x2; y2 = other.y2; }
		public Constraints(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2; 
			this.y2 = y2;
		}
	}
	
	protected static int DEFAULT_PRIORITY = 1;
	
	protected int     defaultPriority;
	protected boolean drawContainingPanel;
	
	protected TreeMap<Integer, List<Widget>> priorityMap;
	protected Map<Widget, Integer>          widgetMap;
	protected List<Widget>                  widgetList;
	
	protected Constraints constraints;
	
	public int defaultPriority() { return defaultPriority; }
	public boolean doesDrawContainingPanel() { return drawContainingPanel; }
	
	public boolean hasWidget(Widget widget) { return widgetMap.containsKey(widget); }
	public int size() { return widgetList.size(); }
	public int getWidgetPriority(Widget widget) { 
		if (!hasWidget(widget)) {
			throw new RuntimeException("Panel does not contain the given widget");
		}
		return widgetMap.get(widget);
	}
	public Widget getIndex(int widgetID) { 
		if (widgetID >= widgetList.size() || widgetID < 0) {
			throw new RuntimeException("Index out of bounds for stored Widget list. widgetID=" + widgetID);
		}
		return widgetList.get(widgetID); 
	}
	public List<Widget> getWidgetList() { return widgetList; }
	
	public void setWidgetPriority(Widget widget, int priority) {
		priorityMap.get(widgetMap.get(widget)).remove(widget);
		widgetMap.put(widget, priority);
		priorityMap.get(priority).add(widget);
	}
	public void setDefaultPriority(int defaultPriority) { this.defaultPriority = defaultPriority; }
	public void setDrawContainingPanel(boolean drawContainingPanel) { this.drawContainingPanel = drawContainingPanel; }
	
	public void setConstraints(int c) { setConstraints(c, c, c, c); }
	public void setConstraints(int x, int y) { setConstraints(x, y, x, y); }
	public void setConstraints(int x1, int y1, int x2, int y2) { constraints = new Constraints(x1, y1, x2, y2); }
	public void setConstraints(Constraints newConstraints) { constraints.set(newConstraints); }
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Widget w: widgetList) {
			w.setEnabled(enabled);
		}
	}
		
	@Override
	public void setLocation(int x, int y) {
		int dx = x - this.x;
		int dy = y - this.y;
		
		super.setLocation(x, y);
		
		for (Widget w: widgetList) {
			w.setLocation(w.getX()+dx, w.getY()+dy);
		}
	}
	@Override
	public void setX(int x) { setLocation(x, this.y); }
	@Override
	public void setY(int y) { setLocation(this.x, y); }
	
	public void setWidgetDimensions(Widget widget, int x, int y, int w, int h) { setWidgetDimensions(widget, new Dimensions(x, y, w, h)); }
	public abstract void setWidgetDimensions(Widget widget, Dimensions d);
	
	public Panel() {
		this(0, 0, 10, 10);
	}
	public Panel(int x, int y, int w, int h) {
		super(x, y, w, h);
		priorityMap = new TreeMap<>();
		widgetMap = new HashMap<>();
		widgetList = new ArrayList<>();
		
		defaultPriority = DEFAULT_PRIORITY;
		drawContainingPanel = false;
		constraints = new Constraints(0, 0, 0, 0);
	}
	
	public void addWidget(Widget newWidget, int x, int y, int w, int h) { addWidget(newWidget, new Dimensions(x, y, w, h), constraints); }
	public void addWidget(Widget newWidget, int x, int y, int w, int h, int priority) { addWidget(newWidget, new Dimensions(x, y, w, h), constraints, priority); }
	public void addWidget(Widget newWidget) { addWidget(newWidget, defaultPriority); }
	public void addWidget(Widget newWidget, Dimensions d) { addWidget(newWidget, d, defaultPriority); }
	public void addWidget(Widget newWidget, Dimensions d, int priority) { addWidget(newWidget, d, constraints, priority); }
	public void addWidget(Widget newWidget, Dimensions d, Constraints c) { addWidget(newWidget, d, c, defaultPriority); }
	
	public abstract void addWidget(Widget newWidget, int priority);
	public abstract void addWidget(Widget newWidget, Dimensions d, Constraints c, int priority);
	
	protected void addWidgetToCollection(Widget newWidget, int priority) {
		if (widgetMap.containsKey(newWidget)) {
			throw new RuntimeException("Each instance of Widget may only be referenced in (i.e. added to) each Panel one time.");
		}
		if (!priorityMap.containsKey(priority)) {
			priorityMap.put(priority, new ArrayList<Widget>());
		}
		priorityMap.get(priority).add(newWidget);
		widgetMap.put(newWidget, priority);
		widgetList.add(newWidget);
	}
	
	public boolean removeWidget(Widget widgetToRemove) {
		if (!widgetMap.containsKey(widgetToRemove)) {
			return false;
		}
		
		priorityMap.get(widgetMap.get(widgetToRemove)).remove(widgetToRemove);
		widgetMap.remove(widgetToRemove);
		widgetList.remove(widgetToRemove);
		
		return true;
	}
	public Widget removeIndex(int widgetID) {
		if (widgetID >= widgetList.size() || widgetID < 0) {
			throw new RuntimeException("Index out of bounds for stored Widget list. widgetID=" + widgetID);
		}
		
		Widget widgetToRemove = widgetList.remove(widgetID);
		priorityMap.get(widgetMap.get(widgetToRemove)).remove(widgetToRemove);
		widgetMap.remove(widgetToRemove);
		return widgetToRemove;
	}
	public void clear() {
		priorityMap = new TreeMap<>();
		widgetMap = new HashMap<>();
		widgetList = new ArrayList<>();
	}
	
	@Override
	public void update(){
		if (!enabled || !visible) 
			return;
		
		updateClickingState();
		
		boolean setFlag = false;
		boolean mouseInPriorityWidget = false; 
		for (int priority: priorityMap.keySet()) {
			for (Widget w: priorityMap.get(priority)) {
				if (mouseInPriorityWidget) {
					// Blocking a widget makes the widget think the mouse isn't in it.
					// This means if the mouse is hovering over two widgets, only the highest priority one will get mouse interaction.
					w.blockWidget();
				}
				w.update();
				if (w.containsMouse()) {
					setFlag = true;
				}
			}
			if (setFlag) {
				mouseInPriorityWidget = true;
			}
		}
	}
	@Override
	public void draw() {
		if (!visible) 
			return;
		
		if (drawContainingPanel) {
		    Draw.setFill(fillColor);
		    Draw.setStroke(borderColor);
		    Draw.rect(x, y, w, h);
		}
		
		if (customDrawObject != null) {
			customDrawObject.draw(this);
		}
		
		for (int priority: priorityMap.descendingKeySet()) {
			for (Widget w: priorityMap.get(priority)) {
				w.draw();
			}
		}
	}
}

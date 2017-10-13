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
	
	protected static int _DEFAULT_PRIORITY = 1;
	
	protected int     _defaultPriority;
	protected boolean _drawContainingPanel;
	
	protected TreeMap<Integer, List<Widget>> _priorityMap;
	protected Map<Widget, Integer> _widgetMap;
	protected List<Widget> _widgetList;
	
	protected Constraints _constraints;
	
	public int defaultPriority() { return _defaultPriority; }
	public boolean doesDrawContainingPanel() { return _drawContainingPanel; }
	
	public boolean hasWidget(Widget widget) { return _widgetMap.containsKey(widget); }
	public int size() { return _widgetList.size(); }
	public int getWidgetPriority(Widget widget) { 
		if (!hasWidget(widget)) {
			throw new RuntimeException("Panel does not contain the given widget");
		}
		return _widgetMap.get(widget);
	}
	public Widget getIndex(int widgetID) { 
		if (widgetID >= _widgetList.size() || widgetID < 0) {
			throw new RuntimeException("Index out of bounds for stored Widget list. widgetID=" + widgetID);
		}
		return _widgetList.get(widgetID); 
	}
	public List<Widget> widgetList() { return _widgetList; }
	
	public void setWidgetPriority(Widget widget, int priority) {
		_priorityMap.get(_widgetMap.get(widget)).remove(widget);
		_widgetMap.put(widget, priority);
		_priorityMap.get(priority).add(widget);
	}
	public void setDefaultPriority(int defaultPriority) { this._defaultPriority = defaultPriority; }
	public void setDrawContainingPanel(boolean drawContainingPanel) { this._drawContainingPanel = drawContainingPanel; }
	
	public void setConstraints(int c) { setConstraints(c, c, c, c); }
	public void setConstraints(int x, int y) { setConstraints(x, y, x, y); }
	public void setConstraints(int x1, int y1, int x2, int y2) { _constraints = new Constraints(x1, y1, x2, y2); }
	public void setConstraints(Constraints newConstraints) { _constraints.set(newConstraints); }
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Widget w: _widgetList) {
			w.setEnabled(enabled);
		}
	}
		
	@Override
	public void setLocation(int x, int y) {
		int dx = x - this._x;
		int dy = y - this._y;
		
		super.setLocation(x, y);
		
		for (Widget w: _widgetList) {
			w.setLocation(w.x()+dx, w.y()+dy);
		}
	}
	@Override
	public void setX(int x) { setLocation(x, this._y); }
	@Override
	public void setY(int y) { setLocation(this._x, y); }
	
	public void setWidgetDimensions(Widget widget, int x, int y, int w, int h) { setWidgetDimensions(widget, new Dimensions(x, y, w, h)); }
	public abstract void setWidgetDimensions(Widget widget, Dimensions d);
	
	public Panel() {
		this(0, 0, 10, 10);
	}
	public Panel(int x, int y, int w, int h) {
		super(x, y, w, h);
		_priorityMap = new TreeMap<>();
		_widgetMap = new HashMap<>();
		_widgetList = new ArrayList<>();
		
		_defaultPriority = _DEFAULT_PRIORITY;
		_drawContainingPanel = false;
		_constraints = new Constraints(0, 0, 0, 0);
	}
	
	public void addWidget(Widget newWidget, int x, int y, int w, int h) { addWidget(newWidget, new Dimensions(x, y, w, h), _constraints); }
	public void addWidget(Widget newWidget, int x, int y, int w, int h, int priority) { addWidget(newWidget, new Dimensions(x, y, w, h), _constraints, priority); }
	public void addWidget(Widget newWidget) { addWidget(newWidget, _defaultPriority); }
	public void addWidget(Widget newWidget, Dimensions d) { addWidget(newWidget, d, _defaultPriority); }
	public void addWidget(Widget newWidget, Dimensions d, int priority) { addWidget(newWidget, d, _constraints, priority); }
	public void addWidget(Widget newWidget, Dimensions d, Constraints c) { addWidget(newWidget, d, c, _defaultPriority); }
	
	public abstract void addWidget(Widget newWidget, int priority);
	public abstract void addWidget(Widget newWidget, Dimensions d, Constraints c, int priority);
	
	protected void addWidgetToCollection(Widget newWidget, int priority) {
		if (_widgetMap.containsKey(newWidget)) {
			throw new RuntimeException("Each instance of Widget may only be referenced in (i.e. added to) each Panel one time.");
		}
		if (!_priorityMap.containsKey(priority)) {
			_priorityMap.put(priority, new ArrayList<Widget>());
		}
		_priorityMap.get(priority).add(newWidget);
		_widgetMap.put(newWidget, priority);
		_widgetList.add(newWidget);
	}
	
	public boolean removeWidget(Widget widgetToRemove) {
		if (!_widgetMap.containsKey(widgetToRemove)) {
			return false;
		}
		
		_priorityMap.get(_widgetMap.get(widgetToRemove)).remove(widgetToRemove);
		_widgetMap.remove(widgetToRemove);
		_widgetList.remove(widgetToRemove);
		
		return true;
	}
	public Widget removeIndex(int widgetID) {
		if (widgetID >= _widgetList.size() || widgetID < 0) {
			throw new RuntimeException("Index out of bounds for stored Widget list. widgetID=" + widgetID);
		}
		
		Widget widgetToRemove = _widgetList.remove(widgetID);
		_priorityMap.get(_widgetMap.get(widgetToRemove)).remove(widgetToRemove);
		_widgetMap.remove(widgetToRemove);
		return widgetToRemove;
	}
	public void clear() {
		_priorityMap = new TreeMap<>();
		_widgetMap = new HashMap<>();
		_widgetList = new ArrayList<>();
	}
	
	@Override
	protected void updateWidget(){	
		boolean setFlag = false;
		boolean mouseInPriorityWidget = false; 
		for (int priority: _priorityMap.keySet()) {
			for (Widget w: _priorityMap.get(priority)) {
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
	protected void drawWidget() {
		
		if (_drawContainingPanel) {
		    Draw.setFill(_fillColor);
		    Draw.setStroke(_borderColor);
		    Draw.rect(_x, _y, _w, _h);
		}
		
		drawCustom(_widgetControlledDraw);
		
		for (int priority: _priorityMap.descendingKeySet()) {
			for (Widget w: _priorityMap.get(priority)) {
				w.draw();
			}
		}
	}
}

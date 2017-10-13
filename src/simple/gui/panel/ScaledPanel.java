package simple.gui.panel;

import simple.gui.Widget;

import java.util.*;

public class ScaledPanel extends Panel {
	protected static class Modifiers {
		public Dimensions dimensions;
		public Constraints constraints;
		public Modifiers(Dimensions dimensions, Constraints constraints) {
			this.dimensions = dimensions;
			this.constraints = constraints;
		}
	}
	
	protected static int _DEFAULT_ROWS_COLS = 100;
	
	protected Map<Widget, Modifiers> _widgetModifiersMap;
	int _rows, _cols;
	double _boxWidth, _boxHeight;

	public Dimensions getWidgetDimensions(Widget widget) { 
		if (!hasWidget(widget)) {
			throw new RuntimeException("Panel does not contain the given widget");
		}
		return _widgetModifiersMap.get(widget).dimensions;
	}
	
	public int rows() { return _rows; }
	public int cols() { return _cols; }
	
	public double boxWidth() { return _boxWidth; }
	public double boxHeight() { return _boxHeight; }
	
	@Override
	public void setWidgetDimensions(Widget widget, Dimensions d) {
		if (!hasWidget(widget)) {
			return;
		}
		
		Constraints c = _widgetModifiersMap.get(widget).constraints;
		
		widget.setX((int)(this._x + Math.ceil(_boxWidth*(d.x)) - c.x1));
		widget.setY((int)(this._y + Math.ceil(_boxHeight*(d.y)) - c.y1));
		widget.setWidth((int)(Math.ceil(_boxWidth*d.w) + c.x2));
		widget.setHeight((int)(Math.ceil(_boxHeight*d.h) + c.y2));
		
		_widgetModifiersMap.put(widget, new Modifiers(d, c));
	}
	
	@Override
	public void setSize(int w, int h) { 
		super.setSize(w, h); 
		
		_boxWidth  = w / (double)_cols;
		_boxHeight = h / (double)_rows;
		
		for (Widget widget: _widgetList) {
			Modifiers m = _widgetModifiersMap.get(widget);
			
			widget.setX((int)(this._x + Math.ceil(_boxWidth*(m.dimensions.x)) - m.constraints.x1));
			widget.setY((int)(this._y + Math.ceil(_boxHeight*(m.dimensions.y)) - m.constraints.y1));
			widget.setWidth((int)(Math.ceil(_boxWidth*m.dimensions.w) + m.constraints.x2));
			widget.setHeight((int)(Math.ceil(_boxHeight*m.dimensions.h) + m.constraints.y2));
		}
	}
	
	public ScaledPanel() {
		this(0, 0, 10, 10, _DEFAULT_ROWS_COLS, _DEFAULT_ROWS_COLS);
	}
	public ScaledPanel(int cols, int rows) {
		this(0, 0, 10, 10, cols, rows);
	}
	public ScaledPanel(double boxWidth, double boxHeight, int cols, int rows) {
		this(0, 0, boxWidth, boxHeight, cols, rows);
	}
	public ScaledPanel(int x, int y, int w, int h) {
		this(x, y, w, h, _DEFAULT_ROWS_COLS, _DEFAULT_ROWS_COLS);
	}
	public ScaledPanel(int x, int y, double boxWidth, double boxHeight, int cols, int rows) {
		super(x, y, (int)Math.ceil(boxWidth*cols), (int)Math.ceil(boxHeight*rows));
		
		_rows = rows;
		_cols = cols;
		
		_boxWidth  = boxWidth;
		_boxHeight = boxHeight;
		
		_widgetModifiersMap = new HashMap<Widget, Modifiers>();
	}
	public ScaledPanel(int x, int y, int w, int h, int cols, int rows) {
		super(x, y, w, h);
		
		_rows = rows;
		_cols = cols;
		
		_boxWidth  = w / (double)cols;
		_boxHeight = h / (double)rows;
		
		_widgetModifiersMap = new HashMap<Widget, Modifiers>();
	}

	@Override
	public void addWidget(Widget newWidget, int priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWidget(Widget newWidget, Dimensions d, Constraints c, int priority) {
		newWidget.setX((int)(this._x + Math.ceil(_boxWidth*(d.x)) - c.x1));
		newWidget.setY((int)(this._y + Math.ceil(_boxHeight*(d.y)) - c.y1));
		newWidget.setWidth((int)(Math.ceil(_boxWidth*d.w) + c.x2));
		newWidget.setHeight((int)(Math.ceil(_boxHeight*d.h) + c.y2));
						
		_widgetModifiersMap.put(newWidget, new Modifiers(d, c));
		
		addWidgetToCollection(newWidget, priority);
	}
	
	@Override
	public boolean removeWidget(Widget widgetToRemove) {
		boolean success = super.removeWidget(widgetToRemove);
		
		if (success)
			_widgetModifiersMap.remove(widgetToRemove);
		
		return success;
	}
	@Override
	public Widget removeIndex(int widgetID) {
		Widget widgetToRemove = super.removeIndex(widgetID);
		
		_widgetModifiersMap.remove(widgetToRemove);
		
		return widgetToRemove;
	}
}

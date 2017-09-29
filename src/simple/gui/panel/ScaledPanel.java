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
	
	protected static int DEFAULT_ROWS_COLS = 100;
	
	protected Map<Widget, Modifiers> widgetModifiersMap;
	int rows, cols;
	double boxWidth, boxHeight;

	public Dimensions getWidgetDimensions(Widget widget) { 
		if (!hasWidget(widget)) {
			throw new RuntimeException("Panel does not contain the given widget");
		}
		return widgetModifiersMap.get(widget).dimensions;
	}
	
	public int getRows() { return rows; }
	public int getCols() { return cols; }
	
	public double getBoxWidth() { return boxWidth; }
	public double getBoxHeight() { return boxHeight; }
	
	@Override
	public void setWidgetDimensions(Widget widget, Dimensions d) {
		if (!hasWidget(widget)) {
			return;
		}
		
		Constraints c = widgetModifiersMap.get(widget).constraints;
		
		widget.setX((int)(this.x + Math.ceil(boxWidth*(d.x)) - c.x1));
		widget.setY((int)(this.y + Math.ceil(boxHeight*(d.y)) - c.y1));
		widget.setWidth((int)(Math.ceil(boxWidth*d.w) + c.x2));
		widget.setHeight((int)(Math.ceil(boxHeight*d.h) + c.y2));
		
		widgetModifiersMap.put(widget, new Modifiers(d, c));
	}
	
	@Override
	public void setSize(int w, int h) { 
		super.setSize(w, h); 
		
		boxWidth  = w / (double)cols;
		boxHeight = h / (double)rows;
		
		for (Widget widget: widgetList) {
			Modifiers m = widgetModifiersMap.get(widget);
			
			widget.setX((int)(this.x + Math.ceil(boxWidth*(m.dimensions.x)) - m.constraints.x1));
			widget.setY((int)(this.y + Math.ceil(boxHeight*(m.dimensions.y)) - m.constraints.y1));
			widget.setWidth((int)(Math.ceil(boxWidth*m.dimensions.w) + m.constraints.x2));
			widget.setHeight((int)(Math.ceil(boxHeight*m.dimensions.h) + m.constraints.y2));
		}
	}
	@Override
	public void setWidth(int w) { setSize(w, this.h); }
	@Override
	public void setHeight(int h) { setSize(this.w, h); }
	
	public ScaledPanel() {
		this(0, 0, 10, 10, DEFAULT_ROWS_COLS, DEFAULT_ROWS_COLS);
	}
	public ScaledPanel(int cols, int rows) {
		this(0, 0, 10, 10, cols, rows);
	}
	public ScaledPanel(double boxWidth, double boxHeight, int cols, int rows) {
		this(0, 0, boxWidth, boxHeight, cols, rows);
	}
	public ScaledPanel(int x, int y, int w, int h) {
		this(x, y, w, h, DEFAULT_ROWS_COLS, DEFAULT_ROWS_COLS);
	}
	public ScaledPanel(int x, int y, double boxWidth, double boxHeight, int cols, int rows) {
		super(x, y, (int)Math.ceil(boxWidth*cols), (int)Math.ceil(boxHeight*rows));
		
		this.rows = rows;
		this.cols = cols;
		
		this.boxWidth  = boxWidth;
		this.boxHeight = boxHeight;
		
		widgetModifiersMap = new HashMap<Widget, Modifiers>();
	}
	public ScaledPanel(int x, int y, int w, int h, int cols, int rows) {
		super(x, y, w, h);
		
		this.rows = rows;
		this.cols = cols;
		
		boxWidth  = w / (double)cols;
		boxHeight = h / (double)rows;
		
		widgetModifiersMap = new HashMap<Widget, Modifiers>();
	}

	@Override
	public void addWidget(Widget newWidget, int priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWidget(Widget newWidget, Dimensions d, Constraints c, int priority) {
		newWidget.setX((int)(this.x + Math.ceil(boxWidth*(d.x)) - c.x1));
		newWidget.setY((int)(this.y + Math.ceil(boxHeight*(d.y)) - c.y1));
		newWidget.setWidth((int)(Math.ceil(boxWidth*d.w) + c.x2));
		newWidget.setHeight((int)(Math.ceil(boxHeight*d.h) + c.y2));
						
		widgetModifiersMap.put(newWidget, new Modifiers(d, c));
		
		addWidgetToCollection(newWidget, priority);
	}
	
	@Override
	public boolean removeWidget(Widget widgetToRemove) {
		boolean success = super.removeWidget(widgetToRemove);
		
		if (success)
			widgetModifiersMap.remove(widgetToRemove);
		
		return success;
	}
	@Override
	public Widget removeIndex(int widgetID) {
		Widget widgetToRemove = super.removeIndex(widgetID);
		
		widgetModifiersMap.remove(widgetToRemove);
		
		return widgetToRemove;
	}
}

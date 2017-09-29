package simple.gui.panel;

import java.util.HashMap;
import java.util.Map;

import simple.gui.Widget;

public class BasicPanel extends Panel {

	protected Map<Widget, Constraints> widgetConstraintsMap;
	
	@Override
	public void setWidgetDimensions(Widget widget, Dimensions d) {
		if (!hasWidget(widget)) {
			return;
		}
		
		Constraints c = widgetConstraintsMap.get(widget);
		
		widget.setX((int)(this.x + d.x - c.x1));
		widget.setY((int)(this.y + d.y - c.y1));
		widget.setWidth((int)(d.w + c.x2));
		widget.setHeight((int)(d.h + c.y2));
	}
	
	public BasicPanel() {
		this(0, 0, 10, 10);
	}
	public BasicPanel(int x, int y, int w, int h) {
		super(x, y, w, h);
		widgetConstraintsMap = new HashMap<Widget, Constraints>();
	}
	
	@Override
	public void addWidget(Widget newWidget, int priority) {
		addWidgetToCollection(newWidget, priority);
	}
	@Override
	public void addWidget(Widget newWidget, Dimensions d, Constraints c, int priority) {
		newWidget.setX((int)(this.x + d.x - c.x1));
		newWidget.setY((int)(this.y + d.y - c.y1));
		newWidget.setWidth((int)(d.w + c.x2));
		newWidget.setHeight((int)(d.h + c.y2));
				
		addWidget(newWidget, priority);
		widgetConstraintsMap.put(newWidget, c);
	}
}

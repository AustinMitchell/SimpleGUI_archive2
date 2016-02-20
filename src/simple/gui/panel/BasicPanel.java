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
		
		widget.setX(this.x + d.x - c.x1);
		widget.setY(this.y + d.y - c.y1);
		widget.setWidth(d.w + c.x2);
		widget.setHeight(d.h + c.y2);
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
		newWidget.setX(this.x + d.x - c.x1);
		newWidget.setY(this.y + d.y - c.y1);
		newWidget.setWidth(d.w + c.x2);
		newWidget.setHeight(d.h + c.y2);
				
		addWidget(newWidget, priority);
		widgetConstraintsMap.put(newWidget, c);
	}
}

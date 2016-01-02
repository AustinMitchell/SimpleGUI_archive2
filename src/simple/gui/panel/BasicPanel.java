package simple.gui.panel;

import simple.gui.Widget;

public class BasicPanel extends Panel {

	public BasicPanel() {
		this(0, 0, 10, 10);
	}
	public BasicPanel(int x, int y, int w, int h) {
		super(x, y, w, h);
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
	}
}

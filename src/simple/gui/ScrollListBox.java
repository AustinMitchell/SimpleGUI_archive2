package simple.gui;

import java.util.ArrayList;

// Creates a list of a given widget
public class ScrollListBox<WidgetType extends Widget> extends ScrollBox {
	private static final int BAR_WIDTH = 20;
	
	protected ArrayList<WidgetType> widgetList; 
	protected int numWidgetsToDisplay, firstIndex;
	protected float widgetWidth, widgetHeight;
	
	public WidgetType getWidget(int widgetID) { return widgetList.get(widgetID); }
	public int getNumWidgets() { return widgetList.size(); }
	
	public void setNumWidgetsToDisplay(int num) { 
		numWidgetsToDisplay = num;
		widgetHeight = (float)h/numWidgetsToDisplay;
		setWidgetPosition();
	}
	
	@Override
	public void setSize(int w_, int h_) {
		super.setSize(w_, h_);
		widgetWidth = w-BAR_WIDTH;
		widgetHeight = (float)h/numWidgetsToDisplay;
		setWidgetPosition();
	}
	@Override
	public void setWidth(int w_) {setSize(w_, h); }
	@Override
	public void setHeight(int h_) { setSize(w, h_); }
	
	@Override
	public void setLocation(int x_, int y_) {
		super.setLocation(x_, y_);
		setWidgetPosition();
	}
	@Override
	public void setX(int x_) { setLocation(x_, y); }
	@Override
	public void setY(int y_) { setLocation(x, y_); }
	
	
	public ScrollListBox(int numWidgetsToDisplay) {
		this(0, 0, 10, 10, numWidgetsToDisplay);
	}
	public ScrollListBox(int x_, int y_, int w_, int h_, int numWidgetsToDisplay_) {
		super(x_, y_, w_, h_);
		numWidgetsToDisplay = numWidgetsToDisplay_;
		
		widgetList = new ArrayList<WidgetType>();
		widgetWidth = w-BAR_WIDTH;
		widgetHeight = (float)h/numWidgetsToDisplay;
		firstIndex = 0;
	}
	
	public void setWidgetPosition() {
		for (int i=firstIndex; i<firstIndex+numWidgetsToDisplay && i<widgetList.size(); i++) {
			widgetList.get(i).setLocation(x, (int)Math.floor(y+widgetHeight*(i-firstIndex)));
			widgetList.get(i).setSize((int)Math.ceil(widgetWidth), (int)Math.ceil(widgetHeight));
		}
	}
	
	public void addWidget(WidgetType newWidget) {
		widgetList.add(newWidget);
		if (firstIndex == -1) {
			firstIndex = 0;
		} else {
			scrollBar.setRange(0, widgetList.size()-1);
		}
		setWidgetPosition();
	}
	
	public void removeWidget(int widgetID) {
		widgetList.remove(widgetID);
		if (widgetList.isEmpty()) {
			firstIndex = -1;
		} else if (firstIndex >= widgetList.size()-1) {
			firstIndex -= 1;
		}
		if (firstIndex != -1) {
			setWidgetPosition();
			scrollBar.setRange(0, widgetList.size()-1);
		}
	}
	
	public void clear() {
		widgetList = new ArrayList<WidgetType>();
		firstIndex = 0;
	}
	
	@Override
	public void update() {
		if (!enabled || !visible)
			return;
		
		updateScrollWidgets();
		
		if (scrollUp.isClicked() && firstIndex > 0) {
			firstIndex -= 1;
			scrollBar.setValue(firstIndex);
			setWidgetPosition();
		} else if (scrollDown.isClicked() && firstIndex < widgetList.size()-1) {
			firstIndex += 1;
			scrollBar.setValue(firstIndex);
			setWidgetPosition();
		}
		
		if (scrollBar.getValue() != firstIndex) {
			firstIndex = scrollBar.getValue();
			setWidgetPosition();
		}
		
		if (firstIndex != -1) {
			for (int i=firstIndex; i<firstIndex+numWidgetsToDisplay && i<widgetList.size(); i++) {
				widgetList.get(i).update();
			}
		}
	}
	
	@Override
	public void draw() {
		drawScrollWidgets();
		
		draw.setColors(fillColor, borderColor);
		draw.rect(x, y, w-BAR_WIDTH, h);
		
		if (firstIndex != -1) {
			for (int i=firstIndex; i<firstIndex+numWidgetsToDisplay && i<widgetList.size(); i++) {
				widgetList.get(i).draw();
			}
		}
	}
}

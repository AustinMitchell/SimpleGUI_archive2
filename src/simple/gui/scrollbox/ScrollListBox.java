package simple.gui.scrollbox;

import java.util.ArrayList;

import simple.gui.Draw;
import simple.gui.Widget;
import simple.run.Input;

// Creates a list of a given widget
public class ScrollListBox<WidgetType extends Widget> extends ScrollBox {	
	protected ArrayList<WidgetType> _widgetList; 
	protected int _numWidgetsToDisplay, _firstIndex;
	protected float _widgetWidth, _widgetHeight;
	
	public WidgetType widget(int widgetID) { return _widgetList.get(widgetID); }
	public int numWidgets() { return _widgetList.size(); }
	
	public void setNumWidgetsToDisplay(int num) { 
		_numWidgetsToDisplay = num;
		_widgetHeight = (float)_h/_numWidgetsToDisplay;
		setWidgetPosition();
	}
	
	@Override
	public void setSize(int w_, int h_) {
		super.setSize(w_, h_);
		_widgetWidth = _w-_BAR_WIDTH;
		_widgetHeight = (float)_h/_numWidgetsToDisplay;
		setWidgetPosition();
	}
	
	@Override
	public void setLocation(int x_, int y_) {
		super.setLocation(x_, y_);
		setWidgetPosition();
	}
	
	
	public ScrollListBox(int numWidgetsToDisplay) {
		this(0, 0, 10, 10, numWidgetsToDisplay);
	}
	public ScrollListBox(int x, int y, int w, int h, int numWidgetsToDisplay_) {
		super(x, y, w, h);
		_numWidgetsToDisplay = numWidgetsToDisplay_;
		
		_widgetList = new ArrayList<WidgetType>();
		_widgetWidth = _w-_BAR_WIDTH;
		_widgetHeight = (float)_h/_numWidgetsToDisplay;
		_firstIndex = 0;
	}
	
	public void setWidgetPosition() {
		for (int i=_firstIndex; i<_firstIndex+_numWidgetsToDisplay && i<_widgetList.size(); i++) {
			_widgetList.get(i).setLocation(_x, (int)Math.floor(_y+_widgetHeight*(i-_firstIndex)));
			_widgetList.get(i).setSize((int)Math.ceil(_widgetWidth), (int)Math.ceil(_widgetHeight));
		}
	}
	
	public void addWidget(WidgetType newWidget) {
		_widgetList.add(newWidget);
		if (_firstIndex == -1) {
			_firstIndex = 0;
		} else {
			_scrollBar.setRange(0, _widgetList.size()-1);
		}
		setWidgetPosition();
	}
	
	public void removeWidget(int widgetID) {
		_widgetList.remove(widgetID);
		if (_widgetList.isEmpty()) {
			_firstIndex = -1;
		} else if (_firstIndex >= _widgetList.size()-1) {
			_firstIndex -= 1;
		}
		if (_firstIndex != -1) {
			setWidgetPosition();
			_scrollBar.setRange(0, _widgetList.size()-1);
		}
	}
	
	public void clear() {
		_widgetList = new ArrayList<WidgetType>();
		_firstIndex = 0;
	}
	
	@Override
	protected void updateWidget() {		
		updateScrollWidgets();
		
		if ((_scrollUp.clicked() || (Input.mouseWheelUp()&&hovering())) && _firstIndex > 0) {
			_firstIndex -= 1;
			_scrollBar.setValue(_firstIndex);
			setWidgetPosition();
		} else if ((_scrollDown.clicked() || (Input.mouseWheelDown()&&hovering())) && _firstIndex < _widgetList.size()-1) {
			_firstIndex += 1;
			_scrollBar.setValue(_firstIndex);
			setWidgetPosition();
		}
		
		if (_scrollBar.value() != _firstIndex) {
			_firstIndex = _scrollBar.value();
			setWidgetPosition();
		}
		
		if (_firstIndex != -1) {
			for (int i=_firstIndex; i<_firstIndex+_numWidgetsToDisplay && i<_widgetList.size(); i++) {
				_widgetList.get(i).update();
			}
		}
	}
	
	@Override
	protected void drawWidget() {
		drawScrollWidgets();
		
		Draw.setColors(_fillColor, _borderColor);
		Draw.rect(_x, _y, _w-_BAR_WIDTH, _h);
		
		if (_firstIndex != -1) {
			for (int i=_firstIndex; i<_firstIndex+_numWidgetsToDisplay && i<_widgetList.size(); i++) {
				_widgetList.get(i).draw();
			}
		}
	}
}

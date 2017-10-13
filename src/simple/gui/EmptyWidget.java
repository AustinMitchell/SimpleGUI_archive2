package simple.gui;

/** Emtpy widget whose only purpose is to collect mouse info and display some custom draw stuff*/
public class EmptyWidget extends Widget {
	
	public EmptyWidget(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	public EmptyWidget() {
		super(0, 0, 10, 10);
	}
	
	@Override
	protected void updateWidget() {}

	@Override
	protected void drawWidget() {
	    if (_widgetControlledDraw != null) {
	        _widgetControlledDraw.draw(this);
	    }
	}

}

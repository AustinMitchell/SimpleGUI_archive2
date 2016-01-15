package simple.gui;

/** A floating text box that is uneditable. **/
public class Label extends TextArea {
	
	public void setEditable() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	public Label() {
		this(0, 0, 10, 10);
	}
	public Label(String text_) {
		this(0, 0, 10, 10, text_);
	}
	public Label(int x_, int y_, int w_, int h_) {
		this(x_, y_, w_, h_, "");
	}
	public Label(int x_, int y_, int w_, int h_, String text_) {
		super(x_, y_, w_, h_, text_);
		alignment = Alignment.CENTER;
		boxIsVisible = false;
		editable = false;
	}
	
	@Override
	public void update() {
		if (!enabled || !visible) 
			return;
		
		updateClickingState();
	}
	
	@Override
	public void draw() {
		if (!visible)
			return;
		
		if (boxIsVisible) {
			drawBox();
		}
		drawText();
	}	
}

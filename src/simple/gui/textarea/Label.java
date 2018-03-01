package simple.gui.textarea;

import simple.gui.Draw;
import simple.gui.textarea.TextArea.Alignment;

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
		_alignment = Alignment.CENTER;
		_boxVisible = false;
		_editable = false;
	}
	
	@Override
	protected void updateWidget() {}
	
	@Override
	protected void drawWidget() {
		if (_boxVisible) {
			drawBox();
		}
		Draw.image(_textRender, _x, _y);
	}	
}

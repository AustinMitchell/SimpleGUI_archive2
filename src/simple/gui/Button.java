package simple.gui;

import java.awt.*;

import simple.gui.textarea.Label;
import simple.gui.textarea.TextArea;

/** Widget object. Creates a clickable button, intended to be tied to an event. Changes colour according to current widget status.
 * 
 * <P> TODO: create functionality for method attachment. **/
public class Button extends Widget {
    
	/** Image displayed on the button's background. **/
	protected ImageBox _imageBox;
	
	protected Label _textLabel;
	
	protected Color _hoverColor, _clickColor, _disabledColor, _currentColor;
	
	public Color hoverColor() { return _hoverColor; }
    public Color clickColor() { return _clickColor; }
    public Color disabledColor() { return _disabledColor; }

	/** Returns the button's text variable. **/
	public String text() { return _textLabel.text(); }
	/** Returns the button's Image object of the imageBox variable. **/
	public Image image() { return _imageBox.image(); }
	
	/** By default will reset the other colors as well*/
    @Override
    public void setFillColor(Color fillColor) { 
        super.setFillColor(fillColor);
        _hoverColor = Draw.scaleColor(fillColor, 0.92f);
        _clickColor = Draw.scaleColor(fillColor, 0.86f);
        _disabledColor = Draw.scaleColor(fillColor, 0.8f);
    }
    /** Sets only the fill color, doesn't adjust the other colors */
    public void setOnlyFillColor(Color fillColor) { super.setFillColor(fillColor); }
    public void setHoverColor(Color hoverColor) { _hoverColor = hoverColor; }
    public void setClickColor(Color clickColor) { _clickColor = clickColor; }
    public void setDisabledColor(Color disabledColor) { _disabledColor = disabledColor; }
	
	/** Sets the button's text variable. **/
	public void setText(String text) { _textLabel.setText(text); }
	/** Sets the button's Image object of the imageBox variable**/
	public void setImage(Image image) { _imageBox.setImage(image); }
	
	/** Sets the button's x and y coordinates, as well as shifts the imageBox. **/
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		_imageBox.setLocation(x, y);
		_textLabel.setLocation(x, y);
	}
	
	/** Sets the button's width and height, as well as resizes the imageBox. **/
	public void setSize(int w, int h) {
		super.setSize(w, h);
		_imageBox.setSize(w, h);
		_textLabel.setSize(w, h);
	}
	
	@Override
    public void setEnabled(boolean enabled) { 
	    super.setEnabled(enabled); 
	    _imageBox.setEnabled(enabled);
	    _textLabel.setEnabled(enabled);
	    if (!enabled) { 
	        _currentColor = _disabledColor;
	    }
	}
    @Override
    public void blockWidget() { super.blockWidget(); _currentColor = _fillColor; }
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		_textLabel.setFont(font);
	}
	public void setAlignment(TextArea.Alignment alignment) {
		_textLabel.setAlignment(alignment);
	}
	@Override
	public void setTextColor(Color textColor) {
		super.setTextColor(textColor);
		_textLabel.setTextColor(textColor);
	}
	
	public Button() {
		this(0, 0, 10, 10, "", null);
	}
	public Button(String text) {
		this(0, 0, 10, 10, text, null);
	}
	/** Creates a button with unspecified position and size, for the case where it's irrelevant. **/
	public Button(String text, Image image) {
		this(0, 0, 10, 10, text, image);
	}
	/** Creates a button with position, size and a title **/
	public Button(int x, int y, int w, int h, String text) {
		this(x, y, w, h, text, null);
	}
	public Button(int x, int y, int w, int h) {
		this(x, y, w, h, null, null);
	}
	/** Create's a Button object. **/
	public Button(int x, int y, int w, int h, String text, Image image) {
		super(x, y, w, h);
		
		_hoverColor = Draw.scaleColor(_fillColor, 0.92f);
        _clickColor = Draw.scaleColor(_fillColor, 0.86f);
        _disabledColor = Draw.scaleColor(_fillColor, 0.8f);
		
		_textLabel = new Label(x, y, w, h, (text==null)?"":text);
		_textLabel.setAlignment(TextArea.Alignment.CENTER);
		
		_imageBox = new ImageBox(x, y, w, h, image);
	}
	
	/** Updates the widget's status. **/
	protected void updateWidget() {
	    if (!_enabled) {
            _currentColor = _disabledColor;
        } else if (_blocked) {
            _currentColor = _fillColor;
        } else if (clicking()) {
            _currentColor = _clickColor;
        } else if (hovering()) {
            _currentColor = _hoverColor;
        } else { 
            _currentColor = _fillColor;
        }
	    
		_imageBox.update();
		_textLabel.update();
	}
	
	/** Draws the button to the SimpleGUIApp's graphics buffer. **/
	protected void drawWidget () {
		Draw.setFill(_currentColor);
		Draw.setStroke(_borderColor);
		Draw.rect(_x, _y, _w, _h);
		
		_imageBox.draw();
		
		drawCustom(_widgetControlledDraw);
		
		_textLabel.draw();
	}
}

package simple.gui;

import java.awt.Color;
import java.awt.Font;

import simple.gui.textarea.Label;

public class HexButton extends HexWidget {
    protected Color _hoverColor, _clickColor, _disabledColor, _currentColor;
    protected Label _textLabel;

    public Color hoverColor() { return _hoverColor; }
    public Color clickColor() { return _clickColor; }
    public Color disabledColor() { return _disabledColor; }

    public String text() { return _textLabel.text(); }
    
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
    
    @Override
    public void setEnabled(boolean enabled) { 
        super.setEnabled(enabled); 
        _textLabel.setEnabled(enabled);
        if (!enabled) { 
            _currentColor = _disabledColor;
        }
    }
    @Override
    public void blockWidget() { super.blockWidget(); _currentColor = _fillColor; }
    
    public void setText(String text) { _textLabel.setText(text); }
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        _textLabel.setFont(font);
    }
    @Override
    public void setTextColor(Color textColor) {
        super.setTextColor(textColor);
        _textLabel.setTextColor(textColor);
    }
    
    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        _textLabel.setLocation(x, y);
    }
    @Override
    public void setSize(int w, int h) {
        super.setSize(w, h);
        if (_textLabel != null) {
            _textLabel.setSize(w, h);
        }
    }
    @Override
    public void setRadius(float radiusx, float radiusy) {
        super.setRadius(radiusx, radiusy);
        if (_textLabel != null) {
            _textLabel.setLocation(_x, _y);
            _textLabel.setSize(_w, _h);
        }
    }
    
    public HexButton(int centerx, int centery, float radiusx, float radiusy, HexType hexType) {
        super(centerx, centery, radiusx, radiusy, hexType);
        _hoverColor = Draw.scaleColor(_fillColor, 0.92f);
        _clickColor = Draw.scaleColor(_fillColor, 0.86f);
        _disabledColor = Draw.scaleColor(_fillColor, 0.8f);
        _textLabel = new Label(_x, _y, _w, _h);
    }
    
    public HexButton(int centerx, int centery, float radius, HexType hexType) {
        this(centerx, centery, radius, radius, hexType);
    }
    
    public HexButton(int x, int y, int w, int h, HexType hexType) {
        super(x, y, w, h, hexType);
        _hoverColor = Draw.scaleColor(_fillColor, 0.92f);
        _clickColor = Draw.scaleColor(_fillColor, 0.86f);
        _disabledColor = Draw.scaleColor(_fillColor, 0.8f);
        _textLabel = new Label(x, y, w, h);
    }

    @Override
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
        
        _textLabel.draw();
    }

    @Override
    protected void drawWidget() {
        Draw.setStroke(_borderColor);
        Draw.setFill(_currentColor);
        Draw.polygon(_hexPoints[0],_hexPoints[1], 6);
        _textLabel.draw();
    }
    
}

package simple.gui;

import java.awt.Color;
import java.awt.Font;

public class HexButton extends HexWidget {
    protected Color _hoverColor, _clickColor, _disabledColor, _currentColor;
    protected Label textLabel;

    public Color getHoverColor() { return _hoverColor; }
    public Color getClickColor() { return _clickColor; }
    public Color getDisabledColor() { return _disabledColor; }

    public String getText() { return textLabel.getText(); }
    
    /** By default will reset the other colors as well*/
    @Override
    public void setFillColor(Color fillColor) { 
        super.setFillColor(fillColor);
        _hoverColor = Draw.scaleColor(fillColor, 0.92f);
        _clickColor = Draw.scaleColor(fillColor, 0.86f);
        _disabledColor = Draw.scaleColor(fillColor, 0.8f);
    }
    public void setOnlyFillColor(Color fillColor) { super.setFillColor(fillColor); }
    public void setHoverColor(Color hoverColor) { _hoverColor = hoverColor; }
    public void setClickColor(Color clickColor) { _clickColor = clickColor; }
    public void setDisabledColor(Color disabledColor) { _disabledColor = disabledColor; }
    
    public void setText(String text) { textLabel.setText(text); }
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        textLabel.setFont(font);
    }
    @Override
    public void setTextColor(Color color) {
        super.setTextColor(color);
        textLabel.setTextColor(color);
    }
    
    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        textLabel.setLocation(x, y);
    }
    @Override
    public void setSize(int w, int h) {
        super.setSize(w, h);
        if (textLabel != null) {
            textLabel.setSize(w, h);
        }
    }
    @Override
    public void setRadius(float radiusx, float radiusy) {
        super.setRadius(radiusx, radiusy);
        if (textLabel != null) {
            textLabel.setLocation(x, y);
            textLabel.setSize(w, h);
        }
    }
    
    public HexButton(int centerx, int centery, float radiusx, float radiusy, HexType hexType) {
        super(centerx, centery, radiusx, radiusy, hexType);
        _hoverColor = Draw.scaleColor(fillColor, 0.92f);
        _clickColor = Draw.scaleColor(fillColor, 0.86f);
        _disabledColor = Draw.scaleColor(fillColor, 0.8f);
        textLabel = new Label(x, y, w, h);
    }
    
    public HexButton(int centerx, int centery, float radius, HexType hexType) {
        this(centerx, centery, radius, radius, hexType);
    }
    
    public HexButton(int x, int y, int w, int h, HexType hexType) {
        super(x, y, w, h, hexType);
        _hoverColor = Draw.scaleColor(fillColor, 0.92f);
        _clickColor = Draw.scaleColor(fillColor, 0.86f);
        _disabledColor = Draw.scaleColor(fillColor, 0.8f);
        textLabel = new Label(x, y, w, h);
    }

    @Override
    public void update() {
        updateClickingState();
        
        if (!enabled) {
            _currentColor = _disabledColor;
        } else if (blocked) {
            _currentColor = fillColor;
        } else if (isClicking()) {
            _currentColor = _clickColor;
        } else if (isHovering()) {
            _currentColor = _hoverColor;
        } else { 
            _currentColor = fillColor;
        }
        
        textLabel.update();
    }

    @Override
    public void draw() {
        if (!visible) return;
        
        Draw.setStroke(borderColor);
        Draw.setFill(_currentColor);
        Draw.polygon(_hexPoints[0],_hexPoints[1], 6);
        
        textLabel.draw();
    }
    
}

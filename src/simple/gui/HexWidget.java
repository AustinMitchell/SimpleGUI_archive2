package simple.gui;

public abstract class HexWidget extends Widget {
    public enum HexType { POINT_TOP, FLAT_TOP }
    
    protected int[][] _hexPoints;
    protected int _centerx, _centery;
    protected HexType _hexType;
    
    /** Moves the hex relative to the top left corner*/
    @Override
    public void setLocation(int x, int y) {
        int shiftx = x - this.x;
        int shifty = y - this.y;
        super.setLocation(x, y);
        for (int i=0; i<6; i++) {
            _hexPoints[0][i] += shiftx;
            _hexPoints[1][i] += shifty;
        }
        _centerx += shiftx;
        _centery += shifty;
    }
    /** Moves the hex relative to the center */
    public void setCenter(int cx, int cy) {
        int shiftx = cx - _centerx;
        int shifty = cy - _centery;
        setLocation(x+shiftx, y+shifty);
    }
    @Override
    public void setSize(int w, int h) {
        _centerx = this.x + w/2;
        _centery = this.y + h/2;
        
        float multiplier = (float)(1/Math.sin(Math.PI/3));
        switch(_hexType) {
        case FLAT_TOP:
            setRadius(w/2f, multiplier*h/2f);
            break;
        case POINT_TOP:
            setRadius(multiplier*w/2f, h/2f);
            break;
        }
    }
    public void setRadius(float radius) {
        setRadius(radius, radius);
    }
    public void setRadius(float radiusx, float radiusy) {
        double offset = (_hexType == HexType.FLAT_TOP) ? 0 : Math.PI/6;
        for (int i=0; i<6; i++) {
            _hexPoints[0][i] = (int)(radiusx*Math.cos(Math.PI*(i*2)/6 + offset)) + _centerx;
            _hexPoints[1][i] = (int)(radiusy*Math.sin(Math.PI*(i*2)/6 + offset)) + _centery;
        }
        
        switch(_hexType) {
        case FLAT_TOP:
            radiusy *= Math.sin(Math.PI/3);
            break;
        case POINT_TOP:
            radiusx *= Math.cos(Math.PI/6);
            break;
        }
        
        super.setLocation(_centerx - (int)radiusx, _centery - (int)radiusy);
        super.setSize((int)(radiusx*2), (int)(radiusy*2));
    }
    
    public HexWidget(int centerx, int centery, float radius, HexType hexType) {
        this(centerx, centery, radius, radius, hexType);
    }
    public HexWidget(int centerx, int centery, float radiusx, float radiusy, HexType hexType) {
        _hexPoints = new int[2][6];
        _hexType = hexType;
        _centerx = centerx;
        _centery = centery;
        setRadius(radiusx, radiusy);
    }
    public HexWidget(int x, int y, int w, int h, HexType hexType) {
        _hexPoints = new int[2][6];
        _hexType = hexType;
        this.x = x;
        this.y = y;
        setSize(w, h);
    }
    
    @Override
    public boolean containsMouse() {
        int absMouseX = Math.abs(input.mouseX() - _centerx);
        int absMouseY = Math.abs(input.mouseY() - _centery);
        
        if (absMouseX > w/2 || absMouseY > h/2) {
            return false;
        }
        
        switch(_hexType) {
        case FLAT_TOP:
            float yRatio = 1 - absMouseY / (h/2f);
            return absMouseX < _hexPoints[0][1]-_centerx + (_hexPoints[0][0]-_hexPoints[0][1])*yRatio;
        case POINT_TOP:
            float xRatio = absMouseX / (w/2f);
            return absMouseY < _hexPoints[1][1]-_centery + (_hexPoints[0][1]-_hexPoints[1][1])*xRatio;    
        }
        
        return false;
    }

}

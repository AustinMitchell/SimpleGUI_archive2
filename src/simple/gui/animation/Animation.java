package simple.gui.animation;

import java.util.List;

import simple.gui.Draw;
import simple.gui.Image;
import simple.run.Timer;

public class Animation implements Animatable {
    public static class Generator {
        private Image _drawSurface;
        private List<Image> _images;
        private int _offsetx, _offsety;
        private int _animationTime;
        private boolean _loop;
        
        public Generator(List<Image> images, int animationTime, boolean loop) {
            this(images, 0, 0, animationTime, loop);
        }
        public Generator(Image drawSurface, List<Image> images, int animationTime, boolean loop) {
            this(drawSurface, images, 0, 0, animationTime, loop);
        }
        public Generator(List<Image> images, int offsetx, int offsety, int animationTime, boolean loop) {
            this(null, images, offsetx, offsety, animationTime, loop);
        }
        public Generator(Image drawSurface, List<Image> images, int offsetx, int offsety, int animationTime, boolean loop) {
            _drawSurface = drawSurface;
            _images = images; 
            _offsetx = offsetx; 
            _offsety = offsety; 
            _animationTime = animationTime;
            _loop = loop;
        }
        
        public Animation generate(int x, int y) {
            return generate(_drawSurface, x, y, _offsetx, _offsety);
        }
        public Animation generate(Image drawSurface, int x, int y) {
            return generate(drawSurface, _offsetx, _offsety);
        }
        public Animation generate(int x, int y, int offsetx, int offsety) {
            return generate(_drawSurface, x, y, offsetx, offsety);
        }
        public Animation generate(Image drawSurface, int x, int y, int offsetx, int offsety) {
            return new Animation(drawSurface, _images, x+_offsetx, y+_offsety, _animationTime, _loop);
        }
        
    }
    
    protected Image       _drawSurface;
    protected List<Image> _images;
    protected int         _animationTime, _currentImage;
    protected int	      _x, _y, _offsetx, _offsety;
    protected boolean     _finished, _loop;
    protected float       _frameDelay;
    protected long        _initialTime;
	
	public boolean finished() { return _finished; }
	
	public void updateX(int x) { updatePosition(x, _y); }
    public void updateY(int y) { updatePosition(_x, y); }
    public void updatePosition(int x, int y) {
        _x = x+_offsetx;
        _y = y+_offsety;
    }
    public void endAmination() { _finished = true; }
	
	public Animation(Image drawSurface, List<Image> images, int offsetx, int offsety, int animationTime, boolean loop) {
	    this(drawSurface, images, 0, 0, offsetx, offsety, animationTime, loop);
	}
	public Animation(List<Image> images, int offsetx, int offsety, int animationTime, boolean loop) {
        this(null, images, 0, 0, offsetx, offsety, animationTime, loop);
    }
	public Animation(List<Image> images, int x, int y, int offsetx, int offsety, int animationTime, boolean loop) {
	    this(null, images, x, y, offsetx, offsety, animationTime, loop);
	}
	public Animation(Image drawSurface, List<Image> images, int x, int y, int offsetx, int offsety, int animationTime, boolean loop) {
	    _drawSurface   = drawSurface;
		_images        = images;
		_animationTime = animationTime;
		_loop          = loop;

		_x       = x+offsetx;
		_y       = y+offsety;
		_offsetx = offsetx;
		_offsety = offsety;
		
		_frameDelay = animationTime/(float)images.size();
		
		_finished      = false;
		_currentImage  = 0;
		_initialTime   = Timer.latestTimePollMillis();
	}
	
	@Override
	public void updateInternal(long currentTime) {  
	    if (_finished) {
            return;
        }
	    if (!_loop && currentTime-_initialTime >= _animationTime) {
            _finished = true;
            return;
        }
	    
	    _currentImage = (int)(((currentTime-_initialTime)%_animationTime)/_frameDelay);
	}
	
	public void draw() {
	    if (_finished) {
	        return;
	    }
	    
	    if (_drawSurface == null) {
	        Draw.image(_images.get(_currentImage), _x, _y);
	    } else {
	        Draw.image(_drawSurface, _images.get(_currentImage), _x, _y);
	    }
	}
}

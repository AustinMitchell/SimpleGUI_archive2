package simple.gui.animation;

import java.util.List;

import simple.gui.Draw;
import simple.gui.Image;
import simple.run.Timer;

public class Animation implements Animatable {
    public static class Generator {
        private Image _drawSurface;
        private Animator _animator;
        private int _offsetx, _offsety;
        private boolean _loop;
        
        public Generator(Animator animator, boolean loop) {
            this(animator, 0, 0, loop);
        }
        public Generator(Image drawSurface, Animator animator, boolean loop) {
            this(drawSurface, animator, 0, 0, loop);
        }
        public Generator(Animator animator, int offsetx, int offsety, boolean loop) {
            this(null, animator, offsetx, offsety, loop);
        }
        public Generator(Image drawSurface, Animator animator, int offsetx, int offsety, boolean loop) {
            _drawSurface = drawSurface;
            _animator = animator; 
            _offsetx = offsetx; 
            _offsety = offsety; 
            _loop = loop;
        }
        
        // Fuck you java give me default parameters.
        
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
            return generate(_animator, drawSurface, x, y, _offsetx,_offsety);
        }
        public Animation generate(Animator animator, int x, int y) {
            return generate(animator, _drawSurface, x, y, _offsetx, _offsety);
        }
        public Animation generate(Animator animator, Image drawSurface, int x, int y) {
            return generate(animator, drawSurface, _offsetx, _offsety);
        }
        public Animation generate(Animator animator, int x, int y, int offsetx, int offsety) {
            return generate(animator, _drawSurface, x, y, offsetx, offsety);
        }
        public Animation generate(Animator animator, Image drawSurface, int x, int y, int offsetx, int offsety) {
            return new Animation(drawSurface, animator, x+_offsetx, y+_offsety, _loop);
        }
        
    }
    public static interface Animator {
        // Returns the number of frames this animation should last
        public int numFrames();
        // Returns how long the animation should last
        public int animationTime();
        // Draws the offered frame
        public void drawFrame(Image drawSurface, int frame, int x, int y);
    }
    
    public static Animator createImageAnimator(final List<Image> images, final int animationTime) {
        return new Animator() {
                public int numFrames() { return images.size(); }
                public int animationTime() { return animationTime; }
                public void drawFrame(Image drawSurface, int frame, int x, int y) {
                    Draw.image(drawSurface, images.get(frame), x, y);
                }
            };
    }
    public static Animator createCenteredImageAnimator(final List<Image> images, final int animationTime) {
        return new Animator() {
                public int numFrames() { return images.size(); }
                public int animationTime() { return animationTime; }
                public void drawFrame(Image drawSurface, int frame, int x, int y) {
                    Draw.imageCentered(drawSurface, images.get(frame), x, y);
                }
            };
    }
    
    protected Image       _drawSurface;
    protected Animator    _animator;
    protected int         _animationTime, _currentFrame;
    protected int         _x, _y, _offsetx, _offsety;
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
    
    public Generator generator() {
        return new Generator(_drawSurface, _animator, _offsetx, _offsety,_loop);
    }
    
    public Animation(Image drawSurface, Animator animator, int offsetx, int offsety, boolean loop) {
        this(drawSurface, animator, 0, 0, offsetx, offsety, loop);
    }
    public Animation(Animator animator, int offsetx, int offsety, boolean loop) {
        this(null, animator, 0, 0, offsetx, offsety, loop);
    }
    public Animation(Animator animator, int x, int y, int offsetx, int offsety, boolean loop) {
        this(null, animator, x, y, offsetx, offsety, loop);
    }
    public Animation(Image drawSurface, Animator animator, int x, int y, int offsetx, int offsety, boolean loop) {
        _drawSurface   = drawSurface;
        _animator      = animator;
        _animationTime = animator.animationTime();
        _loop          = loop;

        _x       = x+offsetx;
        _y       = y+offsety;
        _offsetx = offsetx;
        _offsety = offsety;
        
        _frameDelay = _animationTime/(float)animator.numFrames();
        
        _finished      = false;
        _currentFrame  = 0;
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
        
        _currentFrame = (int)(((currentTime-_initialTime)%_animationTime)/_frameDelay);
    }
    
    public void draw() {
        if (_finished) {
            return;
        }
        
        if (_drawSurface == null) {
            _animator.drawFrame(Draw.getImage(), _currentFrame, _x, _y);
        } else {
            _animator.drawFrame(_drawSurface, _currentFrame, _x, _y);
        }
    }
}

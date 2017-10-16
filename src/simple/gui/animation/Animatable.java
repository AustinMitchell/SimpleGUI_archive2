package simple.gui.animation;

import simple.run.Timer;

public interface Animatable {
    public static class Handle {
        private Animatable _animatable;
        public Handle(Animatable animatable) {
            _animatable = animatable;
        }
        
        public void updateX(int x) { _animatable.updateX(x); }
        public void updateY(int y) { _animatable.updateY(y); }
        public void updatePosition(int x, int y) { _animatable.updatePosition(x, y); }
        public void endAnimation() { _animatable.endAmination(); }
    }
    
	public void updateX(int x);
	public void updateY(int y);
	public void updatePosition(int x, int y);
	public void endAmination();
	
	public boolean finished();
	
	public default Handle handle() { return new Handle(this); }
	
	default public void update() { updateInternal(Timer.latestTimePollMillis()); }
	public void updateInternal(long currentTime);
	
	public void draw();
}

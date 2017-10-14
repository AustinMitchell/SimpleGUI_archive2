package simple.gui.animation;

import simple.run.Timer;

public interface Animatable {
	public void updateX(int newx);
	public void updateY(int newy);
	public void updatePosition(int newx, int newy);
	public void endAmination();
	
	public boolean finished();
	
	default public void update() { updateInternal(Timer.latestTimePollMillis()); }
	public void updateInternal(long currentTime);
	
	public void draw();
}

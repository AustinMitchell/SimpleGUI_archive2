package simple.gui.animation;

import java.util.*;
import java.util.Map.Entry;

import simple.gui.Image;
import simple.run.Timer;

public class AnimationGroup implements Animatable {
    
    protected Map<Integer, ArrayList<Animatable>> _animationMap;
    protected int _x, _y;
    protected boolean _finished;
    
	@Override
	public boolean finished() {
		return _finished;
	}

	public void updateX(int x) { updatePosition(x, _y); }
    public void updateY(int y) { updatePosition(_x, y); }
    public void updatePosition(int x, int y) {
        for (Entry<Integer, ArrayList<Animatable>> entry: _animationMap.entrySet()) {
            for (Animatable a: _animationMap.get(entry.getKey())) {
                a.updatePosition(x, y);
            }
        }
        _x = x;
        _y = y;
    }
    public void endAmination() { _finished = true; }
	
	public void registerAnimation(Animatable newAnimation, int layer) {
	    if (!_animationMap.containsKey(layer)) {
	        _animationMap.put(layer, new ArrayList<Animatable>());
	    }
	    _animationMap.get(layer).add(newAnimation);
	    newAnimation.updatePosition(_x, _y);
	}
	
   public AnimationGroup(int x, int y) {        
        _x = x;
        _y = y;
        _finished = false;
        _animationMap = new TreeMap<Integer, ArrayList<Animatable>>();
   }
    

    @Override
    public void updateInternal(long currentTime) {
        if (_finished) {
            return;
        }
        for (Entry<Integer, ArrayList<Animatable>> entry: _animationMap.entrySet()) {
            Animatable a = null;
            for (Iterator<Animatable> listIter = _animationMap.get(entry.getKey()).iterator(); listIter.hasNext();) {
                a=listIter.next();
                a.updateInternal(currentTime);
                if (a.finished()) {
                    listIter.remove();
                }
            }
        }
    }
	
    @Override
    public void draw() {
        if (_finished) {
            return;
        }
        for (Entry<Integer, ArrayList<Animatable>> entry: _animationMap.entrySet()) {
            for (Animatable a: _animationMap.get(entry.getKey())) {
                a.draw();
            }
        }
    }
}

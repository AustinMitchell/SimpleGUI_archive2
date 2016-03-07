package simple.gui;

public class Canvas extends Widget {
	
	public Canvas(int x_, int y_, int w_, int h_) {
		super(x_, y_, w_, h_);
	}
	public Canvas() {
		super(0, 0, 10, 10);
	}
	
	@Override
	public void update() {
		if (!enabled || !visible) 
			return;
		updateClickingState();
	}

	@Override
	public void draw() {
		if (!visible) 
			return;
		if (customDrawObject != null) {
			customDrawObject.draw(this);
		}
	}

}

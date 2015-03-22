package simple.gui.panel;

import simple.gui.Widget;

// Same as a panel, but added widgetList are laid out in an order as a grid
public class GridLayoutPanel extends Panel{	
	public static final int HORIZONTAL_FIRST = 0;
	public static final int VERTICAL_FIRST = 1;
	
	private int placementMode;
	private int rows, cols, currentRow, currentCol;
	private float boxWidth, boxHeight;
	
	public int getRows() { return rows; }
	public int getCols() { return cols; }
	public int getTotalWidgetSpace() { return rows*cols; }
	public float getBoxWidth() { return boxWidth; } 
	public float getBoxHeight() { return boxHeight; }
	
	@Override
	public void setSize(int newWidth, int newHeight) {
		w = newWidth;
		h = newHeight;
		boxWidth = ((float)w)/cols;
		boxHeight = ((float)h)/rows;
		
		int currentRow = 0;
		int currentCol = 0;
		for (Widget w : widgetList) {
			w.setLocation(x+(int)(currentCol*Math.floor(boxWidth)), y+(int)(currentRow*Math.floor(boxHeight)));
			w.setSize((int)(Math.ceil(boxWidth)), (int)(Math.ceil(boxHeight)));
			if (placementMode == VERTICAL_FIRST) {
				currentCol += 1;
				if (currentCol == cols) {
					currentCol = 0;
					currentRow += 1;
					if (currentRow == rows) {
						currentCol = -1;
						currentRow = -1;
					}
				}
			} else {
				currentRow += 1;
				if (currentRow == rows) {
					currentRow = 0;
					currentCol += 1;
					if (currentCol == cols) {
						currentRow = -1;
						currentCol = -1;
					}
				}	
			}
		}
	}
	@Override
	public void setWidth(int newWidth) {
		setSize(newWidth, h);
	}
	@Override
	public void setHeight(int newHeight) {
		setSize(w, newHeight);
	}
	
	public GridLayoutPanel(int rows_, int cols_) {
		this(0, 0, 10, 10, rows_, cols_, VERTICAL_FIRST);
	}
	public GridLayoutPanel(int rows_, int cols_, int placementMode_) {
		this(0, 0, 10, 10, rows_, cols_, placementMode_);
	}
	public GridLayoutPanel(int x_, int y_, int w_, int h_, int rows_, int cols_) {
		this(x_, y_, w_, h_, rows_, cols_, VERTICAL_FIRST);
	}
	public GridLayoutPanel(int x_, int y_, int w_, int h_, int rows_, int cols_, int placementMode_) {
		super(x_, y_, w_, h_);
		rows = Math.max(1, rows_);
		cols = Math.max(1, cols_);
		placementMode = placementMode_;
		
		currentRow = 0;
		currentCol = 0;
		boxWidth = ((float)w)/cols;
		boxHeight = ((float)h)/rows;
	}
	
	@Override
	public void addWidget(Widget newWidget) {
		if (currentCol == -1 && currentRow == -1) {
			throw new IndexOutOfBoundsException("Attempted to add widget to full BoxLayoutPanel; This panel may only have " 
													+ rows*cols + " (" + rows + " rows x " + cols + " columns) widgets");
		}
		widgetList.add(newWidget);
		newWidget.setLocation(x+(int)(currentCol*Math.floor(boxWidth)), y+(int)(currentRow*Math.floor(boxHeight)));
		newWidget.setSize((int)(Math.ceil(boxWidth)), (int)(Math.ceil(boxHeight)));
		if (placementMode == VERTICAL_FIRST) {
			currentCol += 1;
			if (currentCol == cols) {
				currentCol = 0;
				currentRow += 1;
				if (currentRow == rows) {
					currentCol = -1;
					currentRow = -1;
				}
			}
		} else {
			currentRow += 1;
			if (currentRow == rows) {
				currentRow = 0;
				currentCol += 1;
				if (currentCol == cols) {
					currentRow = -1;
					currentCol = -1;
				}
			}	
		}
	}
}

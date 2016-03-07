package simple.gui.panel;

import java.util.*;

import simple.gui.Widget;

public class PanelCollection extends Widget {
	protected Map<Panel, Integer> panelMap;
	protected List<Panel> panelList;
	Panel currentPanel;
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		for (Panel p: panelList) {
			p.setLocation(x, y);
		}
	}
	@Override
	public void setX(int x) { setLocation(x, this.y); }
	@Override
	public void setY(int y) { setLocation(this.x, y); }
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		for (Panel p: panelList) {
			p.setSize(w, h);
		}
	}
	@Override
	public void setWidth(int w) { setSize(w, this.h); }
	@Override
	public void setHeight(int h) { setSize(this.w, h); }
	
	public void setCurrentPanel(int index) { 
		if (index < 0 || index >= panelList.size()) {
			throw new RuntimeException("PanelCollection setCurrentPanel(): Index out of bounds");
		}
		currentPanel = panelList.get(index); 
	}
	public void setCurrentPanel(Panel p) { 
		if (!panelMap.containsKey(p)) {
			throw new RuntimeException("PanelCollection setCurrentPanel(): Index out of bounds");
		}	
		currentPanel = p;
	}
	
	public List<Panel> getPanelList() { return panelList; }
	public Panel getPanel(int index) { 
		if (index < 0 || index >= panelList.size()) {
			throw new RuntimeException("PanelCollection removePanel(): Index out of bounds");
		}
		return panelList.get(index); 
	}
	
	public Panel getCurrentPanel() { 
		return currentPanel; 
	}
	public int getCurrentPanelIndex() { 
		if (currentPanel == null) {
			return -1;
		}
		return panelMap.get(currentPanel); 
	}
	public boolean containsPanel(Panel p) { return panelMap.containsKey(p); }
	public int size() { return panelList.size(); }
	
	public PanelCollection() {
		this(0, 0, 10, 10);
	}
	public PanelCollection(int x, int y, int w, int h) {
		super(x, y, w, h);
		panelMap = new HashMap<Panel, Integer>();
		panelList = new ArrayList<Panel>();
	}
	
	public void addPanel(Panel newPanel) {
		if (panelMap.containsKey(newPanel)) return;
		
		panelMap.put(newPanel, panelList.size());
		panelList.add(newPanel);
		
		newPanel.setLocation(x, y);
		newPanel.setSize(w, h);
	}
	
	public void removePanel(int index) {
		if (index < 0 || index >= panelList.size()) {
			throw new RuntimeException("PanelCollection removePanel(): Index out of bounds");
		}
		Panel removed = panelList.remove(index);
		panelMap.remove(removed);
		if (removed == currentPanel) {
			currentPanel = null;
		}
	}
	public void removePanel(Panel p) {
		if (!panelMap.containsKey(p)) {
			return;
		}
		int removed = panelMap.remove(p);
		panelList.remove(removed);
		if (p == currentPanel) {
			currentPanel = null;
		}
	}
	
	public void clear() {
		panelMap = new HashMap<Panel, Integer>();
		panelList = new ArrayList<Panel>();
	}
	
	@Override
	public void update() {
		if (!enabled || !visible) return;
		updateClickingState();
		
		if (currentPanel != null) {
			currentPanel.update();
		}
	}
	@Override 
	public void draw() {
		if (!visible) return;
		
		if (currentPanel != null) {
			currentPanel.draw();
		}
	}
}

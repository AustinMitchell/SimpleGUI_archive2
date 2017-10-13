package simple.gui.panel;

import java.util.*;

import simple.gui.Widget;

public class PanelCollection extends Widget {
	protected Map<Panel, Integer> _panelMap;
	protected List<Panel> _panelList;
	Panel _currentPanel;
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		for (Panel p: _panelList) {
			p.setLocation(x, y);
		}
	}
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		for (Panel p: _panelList) {
			p.setSize(w, h);
		}
	}
	
	public void setCurrentPanel(int index) { 
		if (index < 0 || index >= _panelList.size()) {
			throw new RuntimeException("PanelCollection setCurrentPanel(): Index out of bounds");
		}
		_currentPanel = _panelList.get(index); 
	}
	public void setCurrentPanel(Panel p) { 
		if (!_panelMap.containsKey(p)) {
			throw new RuntimeException("PanelCollection setCurrentPanel(): Index out of bounds");
		}	
		_currentPanel = p;
	}
	
	public List<Panel> panelList() { return _panelList; }
	public Panel getPanel(int index) { 
		if (index < 0 || index >= _panelList.size()) {
			throw new RuntimeException("PanelCollection removePanel(): Index out of bounds");
		}
		return _panelList.get(index); 
	}
	
	public Panel currentPanel() { 
		return _currentPanel; 
	}
	public int currentPanelIndex() { 
		if (_currentPanel == null) {
			return -1;
		}
		return _panelMap.get(_currentPanel); 
	}
	public boolean containsPanel(Panel p) { return _panelMap.containsKey(p); }
	public int size() { return _panelList.size(); }
	
	public PanelCollection() {
		this(0, 0, 10, 10);
	}
	public PanelCollection(int x, int y, int w, int h) {
		super(x, y, w, h);
		_panelMap = new HashMap<Panel, Integer>();
		_panelList = new ArrayList<Panel>();
	}
	
	public void addPanel(Panel newPanel) {
		if (_panelMap.containsKey(newPanel)) return;
		
		_panelMap.put(newPanel, _panelList.size());
		_panelList.add(newPanel);
		
		newPanel.setLocation(_x, _y);
		newPanel.setSize(_w, _h);
	}
	
	public void removePanel(int index) {
		if (index < 0 || index >= _panelList.size()) {
			throw new RuntimeException("PanelCollection removePanel(): Index out of bounds");
		}
		Panel removed = _panelList.remove(index);
		_panelMap.remove(removed);
		if (removed == _currentPanel) {
			_currentPanel = null;
		}
	}
	public void removePanel(Panel p) {
		if (!_panelMap.containsKey(p)) {
			return;
		}
		int removed = _panelMap.remove(p);
		_panelList.remove(removed);
		if (p == _currentPanel) {
			_currentPanel = null;
		}
	}
	
	public void clear() {
		_panelMap = new HashMap<Panel, Integer>();
		_panelList = new ArrayList<Panel>();
	}
	
	@Override
	protected void updateWidget() {
		if (_currentPanel != null) {
			_currentPanel.update();
		}
	}
	@Override 
	protected void drawWidget() {
		if (_currentPanel != null) {
			_currentPanel.draw();
		}
	}
}

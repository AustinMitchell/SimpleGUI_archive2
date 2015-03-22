package simple.run;

import javax.swing.*;

import simple.gui.DrawModule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class SimpleGUIApp extends JPanel implements Runnable, KeyListener {
	private static class GUIRunWindow extends JFrame{
		public GUIRunWindow(SimpleGUIApp programToRun, String title, boolean isUndecorated) {
			super(title);
			setContentPane(programToRun);
			setUndecorated(isUndecorated);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setResizable(false);
			pack();
			setVisible(true);
		}
		@SuppressWarnings("unused")
		public GUIRunWindow(SimpleGUIApp programToRun, String title) {
			super(title);
			setContentPane(programToRun);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setResizable(false);
			pack();
			setVisible(true);
		}
	}
	
	public static final int MAXWIDTH = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int MAXHEIGHT = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public static void start(SimpleGUIApp mainProgram, String name, boolean isUndecorated) {
		mainProgram.setFrame(new GUIRunWindow(mainProgram, name, isUndecorated)); 
	}
	public static void start(SimpleGUIApp mainProgram, String name) {
		mainProgram.setFrame(new GUIRunWindow(mainProgram, name, false)); 
	}
	
	private Color backgroundColor;
	private boolean running;
	private int width, height, fps, delayTime;
	private JFrame frame;
	private Thread thread;
		
	protected DrawModule draw;
	protected Input input;
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getFPS() { return fps; }
	public Color getBackgroundColor() { return backgroundColor; }
	public JFrame getJFrame() { return frame; }
	
	public void setBackgroundColor(Color c) { backgroundColor = c; }
	
	public void quit() { running = false; }
	public void minimize() { frame.setState(Frame.ICONIFIED); }
	
	private void setFrame(JFrame frame_) { frame = frame_; }
	
	public SimpleGUIApp(int width, int height, int fps) {
		super();
		this.backgroundColor = new Color(180, 180, 180);
		this.running = false;
		this.width = width;
		this.height = height;
		this.fps = fps;
		this.delayTime = fps;
		this.frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
	}
	
	private void initGraphics() {
		DrawModule.initialize(this);
		draw = new DrawModule();
		running = true;
	}
	
	public void run() {
		initGraphics();
		setup();
		while(running) {
			input.update();
			loop();
		}
		System.exit(0);
	}
	
	// Method to initialize main program
	public abstract void setup();
	// Program loop is setup here
	public abstract void loop();
	
	// Physically updates the screen and draws whatever is on the buffer
	protected void DrawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(DrawModule.getImage(), 0, 0, null);
		g2.dispose();
	}
	// Made to wipe the screen after drawing is complete
	protected void cls() {
		draw.setColors(backgroundColor, null);
		draw.rect(0, 0, width, height);
	}
	
	protected void updateView() {
		DrawToScreen();
		Timer.correctedDelay(delayTime);
		cls();
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addMouseListener(input.getListener());
			addMouseMotionListener(input.getListener());
			addMouseWheelListener(input.getListener());
			addKeyListener(input.getListener());
			thread.start();
		}
	}
}

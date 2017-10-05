package simple.run;

import javax.swing.*;

import simple.gui.DrawModule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;

public abstract class SimpleGUIApp extends JPanel implements Runnable {
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
	
	/** Maximum width the window can be for you screen size **/
	public static final int MAXWIDTH = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	/** Maximum height the window can be for your sceen size **/
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
		
	/** DrawModule object, for shorthand calls to DrawModule **/
	protected DrawModule draw;
	/** Input object, for shorthand calls to Input **/
	protected Input input;
	/** Timer object, for shorthand calls to Timer **/
	protected Timer time;
		
	/** Returns the width of the window frame **/
	public int getWidth() { return width; }
	/** Returns the height of the window frame **/
	public int getHeight() { return height; }
	/** Returns the width of the window frame **/
	public int windowWidth() { return width; }
	/** Returns the height of the window frame **/
	public int windowHeight() { return height; }
	/** Returns the target frames per second for the program **/
	public int getFPS() { return fps; }
	/** Returns the target delay time for the program given the FPS **/
	public int getDelay() { return delayTime; }
	/** Returns the current background color (color that each frame starts as) **/
	public Color getBackgroundColor() { return backgroundColor; }
	/** Returns the JFrame object for the window **/
	public JFrame getJFrame() { return frame; }
	private void setFrame(JFrame frame_) { frame = frame_; }
	
	/** Sets the background color to the given color **/
	public void setBackgroundColor(Color c) { backgroundColor = c; }
	
	/** Signals the program to terminate **/
	public void quit() { running = false; }
	/** Minimizes the window into the explorer bar **/
	public void minimize() { frame.setState(Frame.ICONIFIED); }
	
	/** Creates a new SimpleGUIApp with a given width, height and target frames per second **/
	public SimpleGUIApp(int width, int height, int fps) {
		super();
		this.backgroundColor = new Color(180, 180, 180);
		this.running = false;
		this.width = width;
		this.height = height;
		this.fps = fps;
		this.delayTime = 1000/fps;
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
	
	/** Method required for the Runnable Interface; Contains the program loop **/
	public void run() {
		initGraphics();
		setup();
		while(running) {
		    Input.update();
			loop();
		}
		System.exit(0);
	}
	
	/** Method called before the loop begins; Set up variables here, not in your contructor **/
	public abstract void setup();
	/** Method called each frame **/
	public abstract void loop();
	
	/** Draws whatever is on the DrawModule image buffer to the program window **/
	protected void DrawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(DrawModule.getImage(), 0, 0, null);
		g2.dispose();
	}
	/** Covers the DrawModule image buffer with the background color, effectively clearing it **/
	protected void cls() {
		DrawModule.setColors(backgroundColor, null);
		DrawModule.rect(0, 0, width, height);
	}
	/** Calls DrawToScreen(), Timer.correctedDelay() with your target FPS in mind and cls(). This is the most convenient way to update the screen **/
	protected void updateView() {
		DrawToScreen();
		Timer.correctedDelay(delayTime);
		cls();
	}
	
	/** Method required for the Runnable interface. **/
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addMouseListener(Input.getListener());
			addMouseMotionListener(Input.getListener());
			addMouseWheelListener(Input.getListener());
			addKeyListener(Input.getListener());
			thread.start();
		}
	}
}

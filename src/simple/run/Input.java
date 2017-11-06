package simple.run;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

import simple.misc.Vector;

/** This is a static class which allows for a simple interface with the mouse and keyboard. **/
public class Input {
	static class Listener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
		public void keyPressed(KeyEvent e) {
			keyChars[e.getKeyChar()] = true;
			keyCodes[e.getKeyCode()] = true;
		}

		public void keyReleased(KeyEvent e) {
			keyChars[e.getKeyChar()] = false;
			keyCodes[e.getKeyCode()] = false;
			codeReleasedBuffer.add(e.getKeyCode());
			charReleasedBuffer.add(e.getKeyChar());
		}

		public void keyTyped(KeyEvent e) {
			codeBuffer.add(e.getKeyCode());
			charBuffer.add(e.getKeyChar());
		}

		public void mousePressed(MouseEvent e) { mouseDown = true; }
		public void mouseReleased(MouseEvent e) { mouseDown = false; }
		public void mouseDragged(MouseEvent e) { newx = e.getX(); newy = e.getY(); }
		public void mouseMoved(MouseEvent e) { newx = e.getX(); newy = e.getY(); }

		public void mouseWheelMoved(MouseWheelEvent e) { bufferedNotches = e.getWheelRotation(); }
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	private static int x=0, y=0, newx=0, newy=0, oldx=0, oldy=0;
	private static boolean mouseDown = false;
	private static int bufferedNotches=0, mouseNotches=0;
	
	private static boolean[] keyCodes = new boolean[65536];
	private static boolean[] keyChars = new boolean[65536];
	
	private static Queue<Integer> codeBuffer = new LinkedList<Integer>();
	private static Queue<Character> charBuffer = new LinkedList<Character>();
	private static Queue<Integer> codeReleasedBuffer = new LinkedList<Integer>();
	private static Queue<Character> charReleasedBuffer = new LinkedList<Character>();
	private static char currentChar = 0;
	private static int currentCode = 0;
	private static char currentReleasedChar = 0;
	private static int currentReleasedCode = 0;

	/** Returns whether the mouse button is pressed down or not **/
	public static boolean mousePressed() { return mouseDown; }
	/** Returns the x coordinate of the mouse from the previous frame **/
	public static int mouseOldX() { return oldx; }
	/** Returns the y coordinate of the mouse from the previous frame **/
	public static int mouseOldY() { return oldy; }
	/** Returns the x coordinate of the mouse from the current frame **/
	public static int mouseX() { return x; }
	/** Returns the y coordinate of the mouse from the current frame **/
	public static int mouseY() { return y; }
	/** Returns the x coordinate of the mouse from the current frame **/
    public static int mouseShiftX() { return x-oldx; }
    /** Returns the y coordinate of the mouse from the current frame **/
    public static int mouseShiftY() { return y-oldy; }
    
    public static Vector mouse()      { return new Vector(x, y); }
    public static Vector mouseOld()   { return new Vector(oldx, oldy); }
    public static Vector mouseShift() { return new Vector(x-oldx, y-oldy); }
    
    public static int mouseWheelNotches()  { return mouseNotches; }
    public static boolean mouseWheelUp()   { return mouseNotches < 0; }
    public static boolean mouseWheelDown() { return mouseNotches > 0; }
	
	
	/** Returns whether the given key is pressed, using given keycode **/
	public static boolean keyDown(int key) { return (key>=0 && key<65536) ? (keyCodes[key]) : false; }
	/** Returns whether the given key is pressed, using given character**/
	public static boolean keyDown(char key) { return (key>=0 && key<65536) ? (keyChars[key]) : false; }
	/** Returns the keycode of the first key in the typing buffer **/
	public static int getCode() { return currentCode; }
	/** Returns the character of the first key in the typing buffer **/
	public static char getChar() { return currentChar; }
	/** Returns the keycode of the first key in the release buffer **/
	public static int releasedCode() { return currentReleasedCode; }
	/** Returns the character of the first key in the release buffer **/
	public static char releasedChar() { return currentReleasedChar; }
	
	/** Updates the internal state of Input. This is used internally in SimpleGUIApp, and should not be called anywhere else **/
	public static void update() {
		oldx = x;
		oldy = y;
		x = newx;
		y = newy;
		
		mouseNotches = bufferedNotches;
		bufferedNotches = 0;
		
		if (codeBuffer.peek() != null) {
			currentCode = codeBuffer.poll();
		} else {
			currentCode = 0;
		}
		
		if (charBuffer.peek() != null) {
			currentChar = charBuffer.poll();
		} else {
			currentChar = 0;
		}
		
		if (codeReleasedBuffer.peek() != null) {
			currentReleasedCode = codeReleasedBuffer.poll();
		} else {
			currentReleasedCode = 0;
		}
		
		if (charReleasedBuffer.peek() != null) {
			currentReleasedChar = charReleasedBuffer.poll();
		} else {
			currentReleasedChar = 0;
		}
	}
	
	private static Listener listener = new Listener();
	/** Returns an object that uses the KeyListener, MouseListener, MouseMotionListener and MouseWheelListener interfaces. Used internally for SimpleGUIApp**/
	public static Listener getListener() { return listener; }
}

package simple.run;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

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

		public void mouseWheelMoved(MouseWheelEvent e) {}
		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	private static int x, y, newx, newy, oldx, oldy = 0;
	private static boolean mouseDown = false;
	
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

	public static boolean mousePressed() { return mouseDown; }
	public static int mouseOldX() { return oldx; }
	public static int mouseOldY() { return oldy; }
	public static int mouseX() { return x; }
	public static int mouseY() { return y; }
	
	public static boolean keyDown(int key) { return (key>=0 && key<65536) ? (keyCodes[key]) : false; }
	public static boolean keyDown(char key) { return (key>=0 && key<65536) ? (keyChars[key]) : false; }
	public static int getCode() { return currentCode; }
	public static char getChar() { return currentChar; }
	public static int releasedCode() { return currentReleasedCode; }
	public static char releasedChar() { return currentReleasedChar; }
	
	public static void update() {
		oldx = x;
		oldy = y;
		x = newx;
		y = newy;
		
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
	public static Listener getListener() { return listener; }
}

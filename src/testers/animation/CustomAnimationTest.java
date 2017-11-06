package testers.animation;

import simple.run.*;
import simple.gui.*;
import simple.gui.animation.Animation;
import simple.gui.animation.Animation;
import simple.gui.animation.AnimationGroup;
import simple.gui.panel.*;

import java.awt.Color;
import java.util.*;

public class CustomAnimationTest extends SimpleGUIApp {
    public static void main(String[]args) { SimpleGUIApp.start(new CustomAnimationTest(), "Test Program"); } // Boilerplate code to start program
    public CustomAnimationTest() { super(1025, 1025, 144); } // More boilerplate, arguments are screen dimensions and frames per second desired

    class LineAnimation implements Animation.Animator {
        int _startx, _starty, _endx, _endy, _halfFrames, _thickness;
        Color _c;
        
        public LineAnimation(int startx, int starty, int endx, int endy, Color c, int thickness) {
            _startx = startx;
            _starty = starty;
            _endx = endx;
            _endy = endy;
            _c = c;
            _halfFrames = numFrames()/2;
            _thickness = thickness;
        }
        
        public int numFrames() { return 60; }
        public int animationTime() { return 1200; }
        public void drawFrame(Image drawSurface, int frame, int x, int y) {
            int adjustedFrame = (int)(-Math.abs(frame-_halfFrames)+_halfFrames);            
            //Color adjustedColor = Draw.scaleColor(_c, (float)adjustedFrame/(numFrames()/2));
            Color adjustedColor = _c;
            
            Draw.setStrokeRound(adjustedColor, (int)(_thickness*((float)adjustedFrame/_halfFrames)));
            Draw.line(_startx, _starty, _endx, _endy);
        }
    }
    
    // This group will collectively update all explosion animations
    AnimationGroup animations;
    
    // This is just a clickable surface to get a clicking event
    Panel screen;
    
    public void setup() {
        // Spans the whole window
        screen = new BasicPanel(0, 0, getWidth(), getHeight());

        // All registered animations will be offset relative to the point (0, 0).
        animations = new AnimationGroup(0, 0);
    }
    public void loop() {
        screen.update();
        // This will update all stored animations
        animations.update();

        /* If screen is clicked, create a new animation. Register it at layer 0. The layer is just ordering. 
         * Lower layers are drawn first. */
        if (screen.clicking()) {
            /* We made a custom animator, which will take old mouse and new mouse positions and draw a line
             * between them*/
            Animation.Animator lineAnimator1 = new LineAnimation(Input.mouseOldX(), Input.mouseOldY(), Input.mouseX(), Input.mouseY(), Color.RED, 50);
            Animation.Animator lineAnimator2 = new LineAnimation(Input.mouseOldX(), Input.mouseOldY(), Input.mouseX(), Input.mouseY(), Color.ORANGE, 30);
            Animation.Animator lineAnimator3 = new LineAnimation(Input.mouseOldX(), Input.mouseOldY(), Input.mouseX(), Input.mouseY(), Color.YELLOW, 10);
            /* Offset is 0, 0, since we programmed it in a way where the offset doesn't do anything. We also want no looping*/
            animations.registerAnimation(new Animation(lineAnimator1, 0, 0, true), 0);
            animations.registerAnimation(new Animation(lineAnimator2, 0, 0, true), 1);
            animations.registerAnimation(new Animation(lineAnimator3, 0, 0, true), 2);
        }
        
        // Draw everything
        screen.draw();
        animations.draw();
        updateView();
    }
}
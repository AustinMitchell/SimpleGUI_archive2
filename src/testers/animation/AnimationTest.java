package testers.animation;

import simple.run.*;
import simple.gui.*;
import simple.gui.animation.Animation;
import simple.gui.animation.AnimationGroup;
import simple.gui.panel.*;

import java.util.*;

public class AnimationTest extends SimpleGUIApp {
    public static void main(String[]args) { AnimationTest.start(new AnimationTest(), "Test Program"); } // Boilerplate code to start program
    public AnimationTest() { super(1025, 1025, 144); } // More boilerplate, arguments are screen dimensions and frames per second desired

    // This group will collectively update all explosion animations
    AnimationGroup explosions;
    /* Since all the explosions are pretty much the same in this case, we have a generator which
     * will store metadata about each new explosion animation */
    Animation.Generator explosionGenerator;
    
    // This is just a clickable surface to get a clicking event
    Panel screen;
    
    public void setup() {
        // Create list of images
        ArrayList<Image> imageList = new ArrayList<Image>();
        for (int i=5; i<=74; i++) {
            /* The resloader is a useful tool for creating an inputstream from something in your classpath. 
             * Absolutely necessary for runnable jar files. Ideally when running this program these image
             * files are on your classpath under the folder explosion.
             * Image can use an inputstream and turn it into a buffered image */
            imageList.add(new Image(Image.ResLoader.load("explosion/explosion"+i+".png")));
        }
        
        // Spans the whole window
        screen = new BasicPanel(0, 0, getWidth(), getHeight());
        /* Each animation will use imagelist, last 800 milliseconds and won't loop. Toggle the delay and 
         * loop value to see what happens. To center the image, offset is -50, -50 */
        explosionGenerator = new Animation.Generator(imageList, -50, -50, 800, false);
        // All registered animations will be offset relative to the point (0, 0).
        explosions = new AnimationGroup(0, 0);
    }
    public void loop() {
        screen.update();
        // This will update all stored animations
        explosions.update();

        /* If screen is clicked, create a new animation. Register it at layer 0. The layer is just ordering. 
         * Lower layers are drawn first. */
        if (screen.clicked()) {
            /* Note here that you could skip the generator and just make a new instance of Animation or some 
             * other Animatable object and toss it in. The generator is just for convenience.
             * Note the animation will be offset by (-60, -60) since we set this at the beginning. */
            explosions.registerAnimation(explosionGenerator.generate(Input.mouseX(), Input.mouseY()), 0);
        }
        
        // Draw everything
        screen.draw();
        explosions.draw();
        updateView();
    }
}
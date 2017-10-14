package testers.animation;

import simple.run.*;
import simple.gui.*;
import simple.gui.animation.Animation;
import simple.gui.animation.AnimationGroup;
import simple.gui.panel.*;

import java.util.*;

public class AnimationTest extends SimpleGUIApp {
    public static void main(String[]args) { AnimationTest.start(new AnimationTest(), "Test Program"); } // Boilerplate code to start program
    public AnimationTest() { super(1025, 1025, 30); } // More boilerplate, arguments are screen dimensions and frames per second desired

    AnimationGroup explosions;
    Animation.Generator explosionGenerator;
    
    Panel screen;
    
    public void setup() {
        ArrayList<Image> imageList = new ArrayList<Image>();
        for (int i=5; i<=74; i++) {
            imageList.add(new Image(Image.ResLoader.load("explosion/explosion"+i+".png")));
        }
        
        screen = new BasicPanel(0, 0, getWidth(), getHeight());
        explosionGenerator = new Animation.Generator(imageList, -25, -25, 1000, false);
        explosions = new AnimationGroup(0, 0);
    }
    public void loop() {
        screen.update();
        explosions.update();

        if (screen.clicked()) {
            explosions.registerAnimation(explosionGenerator.generate(null, Input.mouseX()-60, Input.mouseY()-60), 0);
        }
        
        
        screen.draw();
        explosions.draw();
        updateView();
    }
}
package testers.vector;

import simple.run.Input;
import simple.run.SimpleGUIApp;
import simple.misc.Vector;

import java.awt.Color;

import simple.gui.Draw;
import simple.gui.textarea.Label;
import simple.gui.textarea.TextArea.Alignment;

public class DistanceTest extends SimpleGUIApp {
    public static void main(String[] args) { start(new DistanceTest(), "Vector Distance Test");}
    public DistanceTest() { super(800, 800, 60); }
    
    Vector origin;
    Label mouseLabel, distLabel, appdistLabel;
    
    public void setup() {
        origin = new Vector(400, 400);
        mouseLabel =   new Label(0, 0, 800, 25);
        distLabel =    new Label(0, 0, 800, 50);
        appdistLabel = new Label(0, 0, 800, 75);
        
        mouseLabel.setAlignment(Alignment.WEST);
        distLabel.setAlignment(Alignment.WEST);
        appdistLabel.setAlignment(Alignment.WEST);
    }
    
    public void loop() {
        
        Vector mouse = Input.mouse();
        double distance = origin.dist(mouse);
        int appdist = origin.approxdist(mouse);
        
        Vector appdistVector = mouse.sub(origin).normalize(appdist).add(origin);
        
        mouseLabel.setText("Mouse position: " + mouse);
        distLabel.setText(String.format("Distance from origin " + origin + ":           %.3f", distance));
        appdistLabel.setText("Distance from origin " + origin + " (approx.): " + appdist);
        
        mouseLabel.draw();
        distLabel.draw();
        appdistLabel.draw();
        
        Draw.setStroke(Color.BLACK, 5);
        Draw.line(origin.x(), origin.y(), mouse.x(), mouse.y());
        Draw.setStroke(Color.RED);
        Draw.line(origin.x(), origin.y(), appdistVector.x(), appdistVector.y());
        
        updateView();
    }
}

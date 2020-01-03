package testers;

import java.awt.Color;
import java.util.Arrays;

import simple.run.*;
import simple.gui.*;
import simple.gui.textarea.Label;
import simple.gui.textarea.TextArea;
import simple.misc.*;
import simple.misc.hex.*;

public class HexTest extends SimpleGUIApp {
    public static void main(String[]args) { start(new HexTest(), "Hex Tester"); }
    public HexTest() {super(1200, 800, 60);}

    // Sets hex orientation, pointy-top or flat-top
    final static HexWidget.HexType hexType = HexWidget.HexType.POINT_TOP;

    // Radius of the drawn hexagons. Used for the hex widgets themselves
    final static int hex_draw_radius = 60;

    // If using a radial hex array, determines the radius in terms of number of hexes from the center
    final static int hex_grid_radius = 3;

    // used for color display for each hex widget
    final static int grid_color_offset = 3;

    // Used to draw the background line grid
    final static int gridline_radius = hex_draw_radius;
    
    // This point will be the center of the hex at (0, 0), or (0, 0, 0) in 3D
    final static int offsetx = 400;
    final static int offsety = 400;
    
    Label hexLabel; // When you click on a hex the 2D and 3D coordinates will display on the label
    HexArray<HexButton> hexArray; // Hex array collection object

    // Will be used to determine where to place each hexagon. Uses the 3D coordinates to determine position
    Vector[] scaledAxes = {
            null,
            null,
            null
    };
    
    Tuple[] edgeSet;      // When a hex is clicked, we will request for the set of edges to set the edge labels.
    Tuple[] cornerSet;    // When a hex is clicked, we will request for the set of corners to set the corner labels.
    Label[] hexEdges;     // Label for the coordinate of each corner around a selected hex
    Label[] hexCorners;   // Label for the coordinate of each edge around a selected hex
    Label   edgeLabel;    // Header label for edge labels
    Label   cornerLabel;  // header lable for corner labels
       
    @Override
    public void setup() {
        /* This generator is helpful for setting up initial data in your hex array. This serves as a factory
        for the hexarray to generate new items given a coordinate, and will only be used for instantiation. 
        Not necessary but allows for decoupling. */
        HexData.Generator<HexButton> dgen = new HexData.Generator<HexButton>() {

            @Override
            public HexButton generate(int hx, int hy, int hz) {
                return generate(new Tuple(hx, hy, hz));
            }

            @Override
            public HexButton generate(Tuple cube) {
                // This vector will determine the visual offset for each hex when drawn
                Vector shift = new Vector(0, 0);
                for (int axis=0; axis<3; axis++) {
                    // Takes each component of the cube coordinates and multiplies it by the same base vector component
                    // Adds it to the shift
                    shift = shift.add(scaledAxes[axis].mult(cube.entry(axis)));
                }
                
                // Create a new hexbutton with this data
                HexButton newHex;
                newHex = new HexButton(offsetx + (int)shift.x(), offsety + (int)shift.y(), hex_draw_radius, hexType);
                
                // Colors each hex. High component 0 is red, 1 is green and 2 is blue. Makes a kind of color wheel.
                float dist_ratio = (255f/(hex_grid_radius+grid_color_offset));
                int r = (int)(Math.max(cube.entry(0)+grid_color_offset, 0) * dist_ratio);
                int g = (int)(Math.max(cube.entry(1)+grid_color_offset, 0) * dist_ratio);
                int b = (int)(Math.max(cube.entry(2)+grid_color_offset, 0) * dist_ratio);
                newHex.setFillColor(new Color(r, g, b));
                return newHex;
            }
        };
        
        hexLabel = new Label(930, 10, 240, 50);
        // The containing box for the label is normally not drawn unless specifically requested by the following call:
        hexLabel.setBoxVisible(true);
        
        // Set header labels and initial corner/edge labels
        hexCorners  = new Label[6];
        hexEdges    = new Label[6];
        cornerLabel = new Label(950,  70, 200, 30, "Corners:");
        cornerLabel.setTextColor(Color.white);
        cornerLabel.setAlignment(TextArea.Alignment.WEST);
        edgeLabel =   new Label(950, 420, 200, 30, "Edges:");
        edgeLabel.setTextColor(Color.white);
        edgeLabel.setAlignment(TextArea.Alignment.WEST);
        
        for (int i=0; i<6; i++) {
            hexCorners[i] = new Label(900, 100+i*50, 300, 50);
            hexCorners[i].setTextColor(Color.WHITE);
            hexEdges[i] = new Label(900, 450+i*50, 300, 50);
            hexEdges[i].setTextColor(Color.WHITE);
        }
        
        // Sets the 3 coordinate directions depending whether its a pointy-top or flat-top hexagon.
        // Scales each vector by the size of the hexagon radius
        scaledAxes[0] = new Vector(HexWidget.getAxisVectors(hexType)[0]).mult(hex_draw_radius);
        scaledAxes[1] = new Vector(HexWidget.getAxisVectors(hexType)[1]).mult(hex_draw_radius);
        scaledAxes[2] = new Vector(HexWidget.getAxisVectors(hexType)[2]).mult(hex_draw_radius);

        // Using the radial tuple generator. When you iterate through this array, it will also iterate in radial form.
        // To set initial data, also pass our data generator from above.
        hexArray = new HexArray<HexButton>(Tuple.createRadialHexGenerator(hex_grid_radius), dgen);
    
    }

    @Override
    public void loop() {
        // Draw the gridlines
        for (int i=0; i*gridline_radius < Math.max(getWidth(), getHeight()); i++) {
            Draw.setStroke(Color.BLACK);
            Draw.line(0, i*gridline_radius, getWidth(), i*gridline_radius);
            Draw.line(i*gridline_radius, 0, i*gridline_radius, getHeight());
        }
        
        // update and draw the hexes
        for (HexData<HexButton> hb: hexArray) {
            hb.data().update();
            hb.data().draw();
            
            // If a hex is clicked, update the label with the coordinates of that hexagon
            if (hb.data().clicked()) {
                hexLabel.setText("Coordinates: " + hb.cubeIndex());
                cornerSet = hb.adjecentCorners();
                edgeSet = hb.adjecentEdges();
                for (int i=0; i<6; i++) {
                    hexCorners[i].setText(""+cornerSet[i]);
                    float[] properEdge = {edgeSet[i].entry(0)/2f, edgeSet[i].entry(1)/2f, edgeSet[i].entry(2)/2f};
                    hexEdges[i].setText(""+edgeSet[i] + " -> " + Arrays.toString(properEdge));
                }
            }
        }
        
        // Draws separating rectangle on the right
        Draw.setColors(new Color(50, 50, 50), null);
        Draw.rect(900, 0, 300, 800);
        
        hexLabel.draw();
        cornerLabel.draw();
        edgeLabel.draw();
        for (int i=0; i<6; i++) {
            hexCorners[i].update();
            hexEdges[i].update();
            hexCorners[i].draw();
            hexEdges[i].draw();
        }
        
        updateView();
    }

}

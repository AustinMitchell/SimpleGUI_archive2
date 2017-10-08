package testers;

import java.awt.Color;

import simple.run.*;
import simple.gui.*;
import simple.misc.*;
import simple.misc.hex.*;

public class HexTest extends SimpleGUIApp {
    public static void main(String[]args) { start(new HexTest(), "Hex Tester"); }
    public HexTest() {super(800, 800, 60);}

    // Sets hex orientation, pointy-top or flat-top
    final static HexWidget.HexType hexType = HexWidget.HexType.POINT_TOP;
    // This will be used in the hex array to switch between 2D and 3D hex coordinates
    final static HexData.CoordinateConverter coord = (hexType == HexWidget.HexType.FLAT_TOP) ?
                                                               HexData.FLAT_TOP_COORD :
                                                               HexData.POINT_TOP_COORD;
    // determines offset of the grid for 2D coordinates.
    final static int even = 1;
    // Width of grid if using a 2D array system
    final static int width = 8;
    // Height of grid if using a 2D array system
    final static int height = 8;
    // Radius of the drawn hexagons. Used for the hex widgets themselves
    final static int hex_draw_radius = 20;
    // If using a radial hex array, determines the radius in terms of number of hexes from the center
    final static int hex_grid_radius = 10;
    // used for color display for each hex widget
    final static int grid_color_offset = 3;
    // Used to draw the background line grid
    final static int gridline_radius = hex_draw_radius;
    
    // This point will be the center of the hex at (0, 0), or (0, 0, 0) in 3D
    final static int offsetx = 400;
    final static int offsety = 400;
    
    // When you click on a hex the 2D and 3D coordinates will display on the label
    Label hexLabel;
    // Hex array collection object
    HexArray<HexButton> hexArray;
    // Will be used to determine where to place each hexagon. Uses the 3D coordinates to determine position
    Vector[] scaledAxes = {
            null,
            null,
            null
    };
       
    @Override
    public void setup() {
        // Using the radial hex constructor. When you iterate through this array, it will also iterate in radial form
        hexArray = new HexArray<HexButton>(hex_grid_radius, even, coord);
        
        hexLabel = new Label(100, 10, 200, 50);
        // The containing box for the label is normally not drawn unless specifically requested
        hexLabel.setBoxIsVisible(true);
        
        // Sets the 3 coordinate directions depending whether its a pointy-top or flat-top hexagon.
        // Scales each vector by the size of the hexagon radius
        scaledAxes[0] = new Vector(hexArray.getCoordConv().getAxisVectors()[0]).mult(hex_draw_radius);
        scaledAxes[1] = new Vector(hexArray.getCoordConv().getAxisVectors()[1]).mult(hex_draw_radius);
        scaledAxes[2] = new Vector(hexArray.getCoordConv().getAxisVectors()[2]).mult(hex_draw_radius);
        
        // Iterate through all the hexes. 
        for (HexData<HexButton> hex: hexArray) {
            // Get the 3D coordinate for the current hex
            Tuple cube = hex.getCubeIndex();
            // This vector will determine the visual offset for each hex when drawn
            Vector shift = new Vector(0, 0);
            for (int axis=0; axis<3; axis++) {
                // Takes each component of the cube coordinates and multiplies it by the same base vector component
                // Adds it to the shift
                shift = shift.add(scaledAxes[axis].mult(cube.get(axis)));
            }
            
            // Create a new hexbutton with this data
            HexButton newHex;
            newHex = new HexButton(offsetx + (int)shift.x(), offsety + (int)shift.y(), hex_draw_radius, hexType);
            
            // Colors each hex. High component 0 is red, 1 is green and 2 is blue. Makes a kind of color wheel.
            float dist_ratio = (255f/(hex_grid_radius+grid_color_offset));
            int r = (int)(Math.max(cube.get(0)+grid_color_offset, 0) * dist_ratio);
            int g = (int)(Math.max(cube.get(1)+grid_color_offset, 0) * dist_ratio);
            int b = (int)(Math.max(cube.get(2)+grid_color_offset, 0) * dist_ratio);
            newHex.setFillColor(new Color(r, g, b));
            // By default every entry in the array is null. This sets the value to the new hexbutton.
            hex.setData(newHex);
        }
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
            hb.getData().update();
            hb.getData().draw();
            
            // If a hex is clicked, update the label with the coordinates of that hexagon
            if (hb.getData().isClicked()) {
                hexLabel.setText(hb.getBaseIndex() + " | " + hb.getCubeIndex());
            }
        }
        
        hexLabel.draw();
        
        updateView();
    }

}
